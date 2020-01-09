package com.gnp.autos.wsp.emisor.eot.soap.service;

import java.sql.Timestamp;
import java.util.Date;

import javax.xml.bind.JAXBElement;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.SoapFaultClientException;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.gnp.autos.wsp.emisor.eot.TecCom.wsdl.ConsultarTallerGenericoRequest;
import com.gnp.autos.wsp.emisor.eot.TecCom.wsdl.ConsultarTallerGenericoResponse;
import com.gnp.autos.wsp.emisor.eot.TecCom.wsdl.ObjectFactory;
import com.gnp.autos.wsp.emisor.eot.error.ExecutionError;
import com.gnp.autos.wsp.negocio.log.TransaccionIntermedia;
import com.gnp.autos.wsp.negocio.log.model.ErrorLog;

/**
 * The Class ProdTecComClient.
 */
public class ProdTecComClient extends WebServiceGatewaySupport {
    /**
     * Gets the prod tec com.
     *
     * @param objProd the obj prod
     * @param urlTInter the url T inter
     * @param tid the tid
     * @return the prod tec com
     */
    @SuppressWarnings("unchecked")
    public ConsultarTallerGenericoResponse getProdTecCom(final ConsultarTallerGenericoRequest objProd,
            final String urlTInter, final Integer tid) {
        Timestamp tsReq = new Timestamp(new Date().getTime());
        ObjectFactory objF = new ObjectFactory();
        JAXBElement<ConsultarTallerGenericoRequest> prodReq = objF.createConsultarTallerGenericoRequest(objProd);
        JAXBElement<ConsultarTallerGenericoResponse> response = null;
        try {
            response = (JAXBElement<ConsultarTallerGenericoResponse>) getWebServiceTemplate().marshalSendAndReceive(
                    getWebServiceTemplate().getDefaultUri(), prodReq,
                    new SoapActionCallback("urn:ConsultarTallerGenerico"));
            TransaccionIntermedia.guardarTransaccion(urlTInter, tid, getWebServiceTemplate().getDefaultUri(), prodReq,
                    response, "TG", tsReq, getWebServiceTemplate().getMarshaller());
            return response.getValue();
        } catch (SoapFaultClientException sf) {
            logger.info(sf);
            ErrorLog errlog = new ErrorLog();
            errlog.setError(sf.getMessage() + "(" + tid.toString() + "");
            TransaccionIntermedia.guardarTransaccionError(urlTInter, tid, "TG", prodReq, errlog,
                    getWebServiceTemplate().getDefaultUri(), tsReq, getWebServiceTemplate().getMarshaller());
            throw new ExecutionError(2, sf.getMessage());
        }
    }
}