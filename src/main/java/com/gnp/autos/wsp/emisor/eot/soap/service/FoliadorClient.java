package com.gnp.autos.wsp.emisor.eot.soap.service;

import java.sql.Timestamp;
import java.util.Date;

import javax.xml.bind.JAXBElement;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.SoapFaultClientException;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.gnp.autos.wsp.emisor.eot.error.ExecutionError;
import com.gnp.autos.wsp.emisor.eot.foliador.wsdl.Foliador;
import com.gnp.autos.wsp.emisor.eot.foliador.wsdl.FoliadorResponse;
import com.gnp.autos.wsp.emisor.eot.foliador.wsdl.ObjectFactory;
import com.gnp.autos.wsp.negocio.log.TransaccionIntermedia;
import com.gnp.autos.wsp.negocio.log.model.ErrorLog;

/**
 * The Class FoliadorClient.
 */
public class FoliadorClient extends WebServiceGatewaySupport {
    /**
     * Gets the folio.
     *
     * @param objReq the obj req
     * @param tid the tid
     * @param urlTInter the url T inter
     * @return the folio
     */
    @SuppressWarnings("unchecked")
    public FoliadorResponse getFolio(final Foliador objReq, final Integer tid, final String urlTInter) {
        ObjectFactory objF = new ObjectFactory();
        JAXBElement<Foliador> objFolio = objF.createFoliador(objReq);
        JAXBElement<FoliadorResponse> response;
        Timestamp tsReq = new Timestamp(new Date().getTime());
        try {
            response = (JAXBElement<FoliadorResponse>) getWebServiceTemplate().marshalSendAndReceive(objFolio,
                    new SoapActionCallback("urn:Foliador"));
            TransaccionIntermedia.guardarTransaccion(urlTInter, tid, "FL", objFolio, response,
                    getWebServiceTemplate().getDefaultUri(), tsReq, getWebServiceTemplate().getMarshaller());
            return response.getValue();
        } catch (SoapFaultClientException ex) {
            ErrorLog errlog = new ErrorLog();
            errlog.setError(ex.getMessage());
            TransaccionIntermedia.guardarTransaccionError(urlTInter, tid, "FL", objFolio, errlog,
                    getWebServiceTemplate().getDefaultUri(), tsReq, getWebServiceTemplate().getMarshaller());
            throw new ExecutionError(0, ex);
        }
    }
}