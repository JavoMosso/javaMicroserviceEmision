package com.gnp.autos.wsp.emisor.eot.soap.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import javax.xml.bind.JAXBElement;
import javax.xml.transform.Source;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.SoapFaultDetailElement;
import org.springframework.ws.soap.client.SoapFaultClientException;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.gnp.autos.wsp.emisor.eot.config.ConfWSP;
import com.gnp.autos.wsp.emisor.eot.emision.wsdl.MBException;
import com.gnp.autos.wsp.emisor.eot.emision.wsdl.ObjectFactory;
import com.gnp.autos.wsp.emisor.eot.emision.wsdl.RegistrarEmisionRequest;
import com.gnp.autos.wsp.emisor.eot.emision.wsdl.RegistrarEmisionResponse;
import com.gnp.autos.wsp.emisor.eot.error.ExecutionError;
import com.gnp.autos.wsp.negocio.log.TransaccionIntermedia;
import com.gnp.autos.wsp.negocio.log.model.ErrorLog;

/**
 * The Class EmisionEOTClient.
 */
public class EmisionEOTClient extends WebServiceGatewaySupport {
    /** The conf. */
    @Autowired
    private ConfWSP conf;
    
    /**
     * Gets the emision.
     *
     * @param objEmi the obj emi
     * @param tid the tid
     * @return the emision
     */
    @SuppressWarnings("unchecked")
    public RegistrarEmisionResponse getEmision(final RegistrarEmisionRequest objEmi, final int tid) {
        Timestamp tsReq = new Timestamp(new Date().getTime());
        ObjectFactory objF = new ObjectFactory();
        JAXBElement<RegistrarEmisionRequest> emisionRequest = objF.createRegistrarEmisionRequest(objEmi);
        Jaxb2Marshaller mars = new Jaxb2Marshaller();
        mars.setContextPath("com.gnp.autos.wsp.emisor.eot.emision.wsdl");
        getWebServiceTemplate().setMarshaller(mars);
        getWebServiceTemplate().setUnmarshaller(mars);
        try {
            JAXBElement<RegistrarEmisionResponse> responseJAX = 
                    (JAXBElement<RegistrarEmisionResponse>) getWebServiceTemplate()
                    .marshalSendAndReceive(emisionRequest, new SoapActionCallback("urn:RegistrarEmision"));
            TransaccionIntermedia.guardarTransaccion(conf.getUrlTransacciones() + "/logintermedia", tid, "EE", 
                    emisionRequest, responseJAX,
                    conf.getUrlEmsion(), tsReq, getWebServiceTemplate().getMarshaller());
            return responseJAX.getValue();
        } catch (SoapFaultClientException sf) {
            Logger.getRootLogger().info(sf);
            MBException mbE = null;
            try {
                SoapFaultDetail details = sf.getSoapFault().getFaultDetail();
                SoapFaultDetailElement detailElementChild = details.getDetailEntries().next();
                Source detailSource = detailElementChild.getSource();
                JAXBElement<MBException> detail = (JAXBElement<MBException>) getWebServiceTemplate().getUnmarshaller()
                        .unmarshal(detailSource);
                mbE = detail.getValue();
                ErrorLog errlog = new ErrorLog();
                errlog.setError(mbE.getException());
                TransaccionIntermedia.guardarTransaccionError(conf.getUrlTransacciones() + "/logintermedia", tid, "EE",
                        emisionRequest, errlog,
                        conf.getUrlEmsion(), tsReq, getWebServiceTemplate().getMarshaller());
                throw new ExecutionError(1, mbE.getException());
            } catch (IOException e1) {
                logger.info(e1);
                throw new ExecutionError(0, e1.getCause());
            }
        }
    }
}