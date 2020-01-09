package com.gnp.autos.wsp.emisor.eot.domain;

import com.gnp.autos.wsp.emisor.eot.emision.wsdl.RegistrarEmisionResponse;
import com.gnp.autos.wsp.negocio.emision.model.req.TraductorEmitirReq;

/**
 * The Interface EmisionDomain.
 */
@FunctionalInterface
public interface EmisionDomain {
    /**
     * Gets the emision.
     *
     * @param traductorReq the traductor req
     * @param tid the tid
     * @return the emision
     */
    RegistrarEmisionResponse getEmision(TraductorEmitirReq traductorReq, int tid);
}