package com.gnp.autos.wsp.emisor.eot.soap.service;

import java.sql.Timestamp;
import java.util.Date;

import javax.xml.bind.JAXBElement;

import org.springframework.ws.client.WebServiceIOException;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.SoapFaultClientException;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.gnp.autos.wsp.emisor.eot.util.Utileria;
import com.gnp.autos.wsp.emisor.eot.wsdl.EmitirRequest;
import com.gnp.autos.wsp.emisor.eot.wsdl.EmitirResponse;
import com.gnp.autos.wsp.emisor.eot.wsdl.MBException;
import com.gnp.autos.wsp.emisor.eot.wsdl.ObjectFactory;
import com.gnp.autos.wsp.errors.exceptions.WSPXmlExceptionWrapper;
import com.gnp.autos.wsp.errors.xml.ErrorXML;
import com.gnp.autos.wsp.negocio.log.TransaccionIntermedia;
import com.gnp.autos.wsp.negocio.log.model.ErrorLog;

/**
 * The Class EmitirEOTClient.
 */
public class EmitirEOTClient extends WebServiceGatewaySupport {
    /**
     * Gets the emitir.
     *
     * @param objEmi the obj emi
     * @param tid the tid
     * @param urlTInter the url T inter
     * @return the emitir
     */
    public EmitirResponse getEmitir(final EmitirRequest objEmi, final Integer tid, final String urlTInter) {
        Timestamp tsReq = new Timestamp(new Date().getTime());
        ObjectFactory objF = new ObjectFactory();
        JAXBElement<EmitirRequest> emiteRequest = objF.createEmitirRequest(objEmi);
        try {
            @SuppressWarnings("unchecked")
            JAXBElement<EmitirResponse> responseJAX = (JAXBElement<EmitirResponse>) getWebServiceTemplate()
                    .marshalSendAndReceive(emiteRequest,
                            new SoapActionCallback("http://gnp.com.mx/mbr/au/sp/sce/contrato/ContratacionAutos"));
            TransaccionIntermedia.guardarTransaccion(urlTInter, tid, "CO", emiteRequest, responseJAX,
                    getWebServiceTemplate().getDefaultUri(), tsReq, getWebServiceTemplate().getMarshaller());
            return responseJAX.getValue();
        } catch (SoapFaultClientException sf) {
            logger.info(sf);
            MBException mbE = Utileria.errorInterp(sf, this);
            ErrorXML errXML = manejaError(mbE.getException() + "->" + mbE.getCompensation(), urlTInter, tid,
                    emiteRequest, tsReq);
            throw new WSPXmlExceptionWrapper(errXML);
        } catch (WebServiceIOException ex) {
            logger.info(ex);
            ErrorXML errXML = manejaError(ex.getMessage(), urlTInter, tid, emiteRequest, tsReq);
            throw new WSPXmlExceptionWrapper(errXML);
        }
    }

    /**
     * Maneja error.
     *
     * @param msgError the msg error
     * @param urlTInter the url T inter
     * @param tid the tid
     * @param emiteRequest the emite request
     * @param tsReq the ts req
     * @return the error XML
     */
    final ErrorXML manejaError(final String msgError, final String urlTInter, final Integer tid,
            final JAXBElement<EmitirRequest> emiteRequest, final Timestamp tsReq) {

        ErrorLog errlog = new ErrorLog();
        String msgErrorT = msgError;
        errlog.setError(msgErrorT);
        TransaccionIntermedia.guardarTransaccionError(urlTInter, tid, "CO", emiteRequest, errlog,
                getWebServiceTemplate().getDefaultUri(), tsReq, getWebServiceTemplate().getMarshaller());

        if (Utileria.existeValor(tid)) {
            msgErrorT += "(" + tid.toString() + ")";
        }

        ErrorXML wspxml = new ErrorXML();
        wspxml.setClave(1);
        wspxml.setError(msgErrorT);
        wspxml.setOrigen("SERVICIO CONTRATACION EOT");
        wspxml.setNow(new Date());
        return wspxml;

    }
}