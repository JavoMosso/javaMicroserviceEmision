package com.gnp.autos.wsp.emisor.eot.soap.service;

import java.sql.Timestamp;
import java.util.Date;

import javax.xml.bind.JAXBElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.SoapFaultClientException;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.gnp.autos.wsp.emisor.eot.cancelacion.wsdl.EmitirCancelacionRequest;
import com.gnp.autos.wsp.emisor.eot.cancelacion.wsdl.EmitirCancelacionResponse2;
import com.gnp.autos.wsp.emisor.eot.cancelacion.wsdl.MBException;
import com.gnp.autos.wsp.emisor.eot.cancelacion.wsdl.ObjectFactory;
import com.gnp.autos.wsp.emisor.eot.cancelacion.wsdl.TarificarCancelacionRequest;
import com.gnp.autos.wsp.emisor.eot.cancelacion.wsdl.TarificarCancelacionResponse2;
import com.gnp.autos.wsp.emisor.eot.config.ConfWSP;
import com.gnp.autos.wsp.emisor.eot.error.ExecutionError;
import com.gnp.autos.wsp.emisor.eot.util.Utileria;
import com.gnp.autos.wsp.negocio.log.TransaccionIntermedia;
import com.gnp.autos.wsp.negocio.log.model.ErrorLog;

/**
 * The Class CancelacionClient.
 */
public class CancelacionClient extends WebServiceGatewaySupport {
    /** The Constant SOAP_ACTION_TAR. */
    private static final String SOAP_ACTION_TAR = "http://www.example.org/MovimientosComunes/TarificarCancelacion";
    
    /** The Constant SOAP_ACTION_EMI. */
    private static final String SOAP_ACTION_EMI = "http://www.example.org/MovimientosComunes/EmitirCancelacion";
    
    /** The conf. */
    @Autowired
    private ConfWSP conf;
    
    /**
     * Cancelacion defaul.
     */
    protected void cancelacionDefaul() {
        this.setDefaultUri(conf.getUrlCancelacion());
    }
    
    /**
     * se manda a llamar el servicio de tarificacion.
     *
     * @param objReq the obj req
     * @param tid the tid
     * @return the tarificar
     */
    @SuppressWarnings("unchecked")
    public TarificarCancelacionResponse2 getTarificar(final TarificarCancelacionRequest objReq, final Integer tid) {
        cancelacionDefaul();
        ObjectFactory objF = new ObjectFactory();
        JAXBElement<TarificarCancelacionRequest> objtarificacion = objF.createTarificarCancelacionRequest(objReq);
        JAXBElement<TarificarCancelacionResponse2> response = null;
        Timestamp tsReq = new Timestamp(new Date().getTime());
        obtenJBMars();
        try {
            response = (JAXBElement<TarificarCancelacionResponse2>) getWebServiceTemplate()
                    .marshalSendAndReceive(objtarificacion, new SoapActionCallback(SOAP_ACTION_TAR));
            TransaccionIntermedia.guardarTransaccion(conf.getUrlTransacciones(), tid, "CT", objtarificacion, response,
                    getWebServiceTemplate().getDefaultUri(), tsReq, getWebServiceTemplate().getMarshaller());
            return response.getValue();
        } catch (SoapFaultClientException sf) {
            MBException mbE = Utileria.errorInterp(sf, this);
            ErrorLog errlog = new ErrorLog();
            errlog.setError(mbE.getException());
            TransaccionIntermedia.guardarTransaccionError(conf.getUrlTransacciones() + "/logintermedia", tid, "CT", 
                    objtarificacion, errlog,
                    conf.getUrlCancelacion(), tsReq, getWebServiceTemplate().getMarshaller());
            throw new ExecutionError(1, "Emitir tarificacion error : " + mbE.getCompensation());
        }
    }
    
    /**
     * Gets the cancelacion.
     *
     * @param contratoReq the contrato req
     * @param tid the tid
     * @return the cancelacion
     */
    @SuppressWarnings("unchecked")
    public EmitirCancelacionResponse2 getCancelacion(final EmitirCancelacionRequest contratoReq, final Integer tid) {
        cancelacionDefaul();
        ObjectFactory objF = new ObjectFactory();
        JAXBElement<EmitirCancelacionRequest> objCancelacion = objF.createEmitirCancelacionRequest(contratoReq);
        JAXBElement<EmitirCancelacionResponse2> response;
        Timestamp tsReq = new Timestamp(new Date().getTime());
        obtenJBMars();
        try {
            response = (JAXBElement<EmitirCancelacionResponse2>) getWebServiceTemplate()
                    .marshalSendAndReceive(objCancelacion, new SoapActionCallback(SOAP_ACTION_EMI));
            TransaccionIntermedia.guardarTransaccion(conf.getUrlTransacciones(), tid, "EC", objCancelacion, response,
                    getWebServiceTemplate().getDefaultUri(), tsReq, getWebServiceTemplate().getMarshaller());
            return response.getValue();
        } catch (SoapFaultClientException sf) {
            MBException mbE = Utileria.errorInterp(sf, this);
            ErrorLog errlog = new ErrorLog();
            errlog.setError(mbE.getException());
            TransaccionIntermedia.guardarTransaccionError(conf.getUrlTransacciones() + "/logintermedia", tid, "EC",
                    objCancelacion, errlog,
                    conf.getUrlCancelacion(), tsReq, getWebServiceTemplate().getMarshaller());
            throw new ExecutionError(1, "Emitir cancelacion error : " + mbE.getCompensation());
        }
    }
    
    /**
     * Obten JB mars.
     *
     * @return the jaxb 2 marshaller
     */
    private Jaxb2Marshaller obtenJBMars() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.gnp.autos.wsp.emisor.eot.cancelacion.wsdl");
        getWebServiceTemplate().setMarshaller(marshaller);
        getWebServiceTemplate().setUnmarshaller(marshaller);
        return marshaller;
    }
}