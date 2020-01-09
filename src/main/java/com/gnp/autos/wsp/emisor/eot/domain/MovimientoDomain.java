package com.gnp.autos.wsp.emisor.eot.domain;

import com.gnp.autos.wsp.negocio.neg.model.EmiteNegReq;

/**
 * The Interface MovimientoDomain.
 */
@FunctionalInterface
public interface MovimientoDomain {
    /**
     * Gets the movimiento NC.
     *
     * @param objEmiNeg the obj emi neg
     * @param tid the tid
     * @return the movimiento NC
     */
    EmiteNegReq getMovimientoNC(EmiteNegReq objEmiNeg, Integer tid);
}