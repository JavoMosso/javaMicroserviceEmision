package com.gnp.autos.wsp.emisor.eot.soap.service;

import java.sql.Timestamp;
import java.util.Date;

import javax.xml.bind.JAXBElement;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.SoapFaultClientException;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.gnp.autos.wsp.emisor.eot.error.ExecutionError;
import com.gnp.autos.wsp.emisor.eot.movimiento.wsdl.ConsultaNegocio;
import com.gnp.autos.wsp.emisor.eot.movimiento.wsdl.ConsultaNegocioResponse;
import com.gnp.autos.wsp.emisor.eot.movimiento.wsdl.ObjectFactory;
import com.gnp.autos.wsp.negocio.log.TransaccionIntermedia;
import com.gnp.autos.wsp.negocio.log.model.ErrorLog;

/**
 * The Class MovimientoEOTClient.
 */
public class MovimientoEOTClient extends WebServiceGatewaySupport {
    /**
     * Gets the consulta.
     *
     * @param objConsulta the obj consulta
     * @param urlTInter the url T inter
     * @param tid the tid
     * @return the consulta
     */
    @SuppressWarnings("unchecked")
    public ConsultaNegocioResponse getConsulta(final ConsultaNegocio objConsulta, final String urlTInter,
            final Integer tid) {
        Timestamp tsReq = new Timestamp(new Date().getTime());
        ObjectFactory objF = new ObjectFactory();
        JAXBElement<ConsultaNegocio> emisionRequest = objF.createConsultaNegocio(objConsulta);
        JAXBElement<ConsultaNegocioResponse> responseJAX = null;
        try {
            responseJAX = (JAXBElement<ConsultaNegocioResponse>) getWebServiceTemplate()
                    .marshalSendAndReceive(emisionRequest, new SoapActionCallback("urn:ConsultaNegocio"));
            TransaccionIntermedia.guardarTransaccion(urlTInter, tid, "MO", emisionRequest, responseJAX,
                    getWebServiceTemplate().getDefaultUri(), tsReq, getWebServiceTemplate().getMarshaller());
            return responseJAX.getValue();
        } catch (SoapFaultClientException sf) {
            logger.info(sf);
            ErrorLog errlog = new ErrorLog();
            errlog.setError(sf.getMessage() + "(" + tid.toString() + ")");
            TransaccionIntermedia.guardarTransaccionError(urlTInter, tid, "MO", emisionRequest, errlog,
                    getWebServiceTemplate().getDefaultUri(), tsReq, getWebServiceTemplate().getMarshaller());
            throw new ExecutionError(1, sf.getMessage());
        }
    }
}