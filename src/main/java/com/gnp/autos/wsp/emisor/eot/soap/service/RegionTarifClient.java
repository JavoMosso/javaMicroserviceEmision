package com.gnp.autos.wsp.emisor.eot.soap.service;

import java.sql.Timestamp;
import java.util.Date;

import javax.xml.bind.JAXBElement;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.gnp.autos.wsp.emisor.eot.calcp.wsdl.ConsultaRegionTarificacionPorCp;
import com.gnp.autos.wsp.emisor.eot.calcp.wsdl.ConsultaRegionTarificacionPorCpResponse;
import com.gnp.autos.wsp.emisor.eot.calcp.wsdl.ObjectFactory;
import com.gnp.autos.wsp.errors.exceptions.WSPXmlExceptionWrapper;
import com.gnp.autos.wsp.errors.xml.ErrorXML;
import com.gnp.autos.wsp.negocio.log.TransaccionIntermedia;
import com.gnp.autos.wsp.negocio.log.model.ErrorLog;

/**
 * The Class RegionTarifClient.
 */
public class RegionTarifClient extends WebServiceGatewaySupport {
    /**
     * Gets the reg tarif.
     *
     * @param objReq the obj req
     * @param tid the tid
     * @param urlTInter the url T inter
     * @return the reg tarif
     */
    @SuppressWarnings("unchecked")
    public ConsultaRegionTarificacionPorCpResponse getRegTarif(final ConsultaRegionTarificacionPorCp objReq,
            final Integer tid, final String urlTInter) {
        ObjectFactory objF = new ObjectFactory();
        JAXBElement<ConsultaRegionTarificacionPorCp> objTarif = objF.createConsultaRegionTarificacionPorCp(objReq);
        JAXBElement<ConsultaRegionTarificacionPorCpResponse> response;
        Timestamp tsReq = new Timestamp(new Date().getTime());
        try {
            response = (JAXBElement<ConsultaRegionTarificacionPorCpResponse>) getWebServiceTemplate()
                    .marshalSendAndReceive(objTarif, new SoapActionCallback("urn:ConsultaRegionTarificacionPorCp"));
            TransaccionIntermedia.guardarTransaccion(urlTInter, tid, "RT", objTarif, response,
                    getWebServiceTemplate().getDefaultUri(), tsReq, getWebServiceTemplate().getMarshaller());
            return response.getValue();
        } catch (Exception ex) {
            ErrorLog errlog = new ErrorLog();
            errlog.setError(ex.getMessage());
            TransaccionIntermedia.guardarTransaccionError(urlTInter, tid, "RT", objTarif, errlog,
                    getWebServiceTemplate().getDefaultUri(), tsReq, getWebServiceTemplate().getMarshaller());

            ErrorXML wspxml = new ErrorXML();
            wspxml.setClave(1);
            wspxml.setError(ex.getMessage());
            wspxml.setOrigen("SERVICIO REGION TARIFICACION");
            wspxml.setNow(new Date());
            throw new WSPXmlExceptionWrapper(wspxml);
        }
    }
}