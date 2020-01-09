package com.gnp.autos.wsp.emisor.eot.domain.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gnp.autos.wsp.emisor.eot.config.ConfWSP;
import com.gnp.autos.wsp.emisor.eot.domain.EmisionDomain;
import com.gnp.autos.wsp.emisor.eot.emision.req.EmisionReq;
import com.gnp.autos.wsp.emisor.eot.emision.wsdl.Cabecera;
import com.gnp.autos.wsp.emisor.eot.emision.wsdl.RegistrarEmisionRequest;
import com.gnp.autos.wsp.emisor.eot.emision.wsdl.RegistrarEmisionResponse;
import com.gnp.autos.wsp.emisor.eot.soap.service.EmisionEOTClient;
import com.gnp.autos.wsp.negocio.emision.model.req.TraductorEmitirReq;
import com.gnp.autos.wsp.negocio.neg.model.EmiteNegReq;

/**
 * The Class EmisionDomainImpl.
 */
@Service
public class EmisionDomainImpl implements EmisionDomain {
    /** The conf. */
    @Autowired
    private ConfWSP conf;
    
    /** The client. */
    @Autowired
    private EmisionEOTClient client;
    
    /**
     * Gets the emision.
     *
     * @param traductorReq the traductor req
     * @param tid the tid
     * @return the emision
     */
    @Override
    public RegistrarEmisionResponse getEmision(final TraductorEmitirReq traductorReq, final int tid) {
        EmiteNegReq objNegocio = traductorReq.getEmiteNeg();
        client.setDefaultUri(conf.getUrlEmsion());
        return client.getEmision(obtenerRequest(objNegocio), tid);
    }
    
    /**
     * Obtener request.
     *
     * @param emisionNeg the emision neg
     * @return the registrar emision request
     */
    protected RegistrarEmisionRequest obtenerRequest(final EmiteNegReq emisionNeg) {
        if (emisionNeg == null) {
            return null;
        }
        RegistrarEmisionRequest request = new EmisionReq().getEmision(emisionNeg);
        Cabecera cabecera = new Cabecera();
        cabecera.setIDTRANSACCION(emisionNeg.getIdCotizacion());
        cabecera.setCVETRANSACCION(conf.getCabCveTransaccion());
        cabecera.setIDACTOR(conf.getCabIdActor());
        cabecera.setIDROL(conf.getCabIdRol());
        cabecera.setIDPERFIL(conf.getCabIdPerfil());
        request.setCabecera(cabecera);
        return request;
    }
}