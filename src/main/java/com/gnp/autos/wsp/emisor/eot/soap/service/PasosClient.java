package com.gnp.autos.wsp.emisor.eot.soap.service;

import java.sql.Timestamp;
import java.util.Date;

import javax.xml.bind.JAXBElement;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.SoapFaultClientException;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.gnp.autos.wsp.emisor.eot.pasos.wsdl.ConsultaPasosConfigurador;
import com.gnp.autos.wsp.emisor.eot.pasos.wsdl.ConsultaPasosConfiguradorResponse;
import com.gnp.autos.wsp.emisor.eot.pasos.wsdl.ObjectFactory;
import com.gnp.autos.wsp.errors.exceptions.WSPXmlExceptionWrapper;
import com.gnp.autos.wsp.errors.xml.ErrorXML;
import com.gnp.autos.wsp.negocio.log.TransaccionIntermedia;
import com.gnp.autos.wsp.negocio.log.model.ErrorLog;

/**
 * The Class PasosClient.
 */
public class PasosClient extends WebServiceGatewaySupport {
    /**
     * Gets the pasos.
     *
     * @param objReq the obj req
     * @param tid the tid
     * @param urlTInter the url T inter
     * @return the pasos
     */
    @SuppressWarnings("unchecked")
    public ConsultaPasosConfiguradorResponse getPasos(final ConsultaPasosConfigurador objReq, final Integer tid,
            final String urlTInter) {
        ObjectFactory objF = new ObjectFactory();
        JAXBElement<ConsultaPasosConfigurador> objRules = objF.createConsultaPasosConfigurador(objReq);
        JAXBElement<ConsultaPasosConfiguradorResponse> response;
        Timestamp tsReq = new Timestamp(new Date().getTime());
        try {
            response = (JAXBElement<ConsultaPasosConfiguradorResponse>) getWebServiceTemplate()
                    .marshalSendAndReceive(objRules, new SoapActionCallback("urn:ConsultaPasosConfigurador"));
            TransaccionIntermedia.guardarTransaccion(urlTInter, tid, "VP", objRules, response,
                    getWebServiceTemplate().getDefaultUri(), tsReq, getWebServiceTemplate().getMarshaller());
            return response.getValue();
        } catch (SoapFaultClientException ex) {
            logger.info(ex);
            ErrorLog errlog = new ErrorLog();
            errlog.setError(ex.getMessage());
            TransaccionIntermedia.guardarTransaccionError(urlTInter, tid, "VP", objRules, errlog,
                    getWebServiceTemplate().getDefaultUri(), tsReq, getWebServiceTemplate().getMarshaller());

            ErrorXML wspxml = new ErrorXML();
            wspxml.setClave(1);
            wspxml.setError(ex.getMessage());
            wspxml.setOrigen("SERVICIO VALIDA PASOS");
            wspxml.setNow(new Date());
            throw new WSPXmlExceptionWrapper(wspxml);
        }
    }
}