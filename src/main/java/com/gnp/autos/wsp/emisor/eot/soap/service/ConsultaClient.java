package com.gnp.autos.wsp.emisor.eot.soap.service;

import java.sql.Timestamp;
import java.util.Date;

import javax.xml.bind.JAXBElement;
import javax.xml.transform.Source;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.SoapFaultDetailElement;
import org.springframework.ws.soap.client.SoapFaultClientException;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.gnp.autos.wsp.emisor.eot.config.ConfWSP;
import com.gnp.autos.wsp.emisor.eot.consulta.wsdl.ConsultarPolizaPorNumPolizaRequest;
import com.gnp.autos.wsp.emisor.eot.consulta.wsdl.ConsultarPolizaPorNumPolizaResponse;
import com.gnp.autos.wsp.emisor.eot.consulta.wsdl.MBException;
import com.gnp.autos.wsp.emisor.eot.consulta.wsdl.ObjectFactory;
import com.gnp.autos.wsp.emisor.eot.error.ExecutionError;
import com.gnp.autos.wsp.negocio.log.TransaccionIntermedia;
import com.gnp.autos.wsp.negocio.log.model.ErrorLog;

/**
 * The Class ConsultaClient.
 */
public class ConsultaClient extends WebServiceGatewaySupport {
    /** The conf. */
    @Autowired
    private ConfWSP conf;

    /**
     * Gets the poliza.
     *
     * @param objReq the obj req
     * @param tid the tid
     * @return the poliza
     */
    @SuppressWarnings("unchecked")
    public ConsultarPolizaPorNumPolizaResponse getPoliza(final ConsultarPolizaPorNumPolizaRequest objReq,
            final Integer tid) {
        setDefaultUri(conf.getUrlConsultaPol());
        ObjectFactory objF = new ObjectFactory();
        JAXBElement<ConsultarPolizaPorNumPolizaRequest> objConsultaReq = objF
                .createConsultarPolizaPorNumPolizaRequest(objReq);
        JAXBElement<ConsultarPolizaPorNumPolizaResponse> response;
        Timestamp tsReq = new Timestamp(new Date().getTime());
        try {
            response = (JAXBElement<ConsultarPolizaPorNumPolizaResponse>) getWebServiceTemplate()
                    .marshalSendAndReceive(objConsultaReq, new SoapActionCallback("urn:ConsultarPolizaPorNumPoliza"));
            TransaccionIntermedia.guardarTransaccion(conf.getUrlTransacciones(), tid, "OP", objConsultaReq, response,
                    getWebServiceTemplate().getDefaultUri(), tsReq, getWebServiceTemplate().getMarshaller());
            return response.getValue();
        } catch (SoapFaultClientException sf) {
            MBException mbE = errInterpretar(sf);
            ErrorLog errlog = new ErrorLog();
            errlog.setError(mbE.getException());
            TransaccionIntermedia.guardarTransaccionError(conf.getUrlTransacciones() + "/logintermedia", tid, "OP",
                    objConsultaReq, errlog, conf.getUrlCancelacion(), tsReq, getWebServiceTemplate().getMarshaller());
            throw new ExecutionError(1, "Emitir tarificacion error : " + mbE.getCompensation());
        }
    }

    /**
     * Err interpretar.
     *
     * @param sf the sf
     * @return the MB exception
     */
    private MBException errInterpretar(final SoapFaultClientException sf) {
        Logger.getRootLogger().info(sf);
        MBException mb = null;
        try {
            SoapFaultDetail det = sf.getSoapFault().getFaultDetail();
            SoapFaultDetailElement detElementChild = det.getDetailEntries().next();
            Source detailSource = detElementChild.getSource();
            @SuppressWarnings("unchecked")
            JAXBElement<MBException> d = (JAXBElement<MBException>) getWebServiceTemplate().getUnmarshaller()
                    .unmarshal(detailSource);
            mb = d.getValue();
            return mb;
        } catch (Exception e1x) {
            logger.info(e1x);
            throw new ExecutionError(1, sf.getMessage());
        }
    }
}