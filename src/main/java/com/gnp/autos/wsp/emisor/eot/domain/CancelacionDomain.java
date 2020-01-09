package com.gnp.autos.wsp.emisor.eot.domain;

import com.gnp.autos.wsp.emisor.eot.cancelacion.CancelacionReq;
import com.gnp.autos.wsp.emisor.eot.cancelacion.CancelacionResp;

/**
 * The Interface CancelacionDomain.
 */
@FunctionalInterface
public interface CancelacionDomain {
    
    /**
     * Cancelacion NTU.
     *
     * @param req the req
     * @return the cancelacion resp
     */
    CancelacionResp cancelacionNTU(CancelacionReq req);
}