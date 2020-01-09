package com.gnp.autos.wsp.emisor.eot.domain;

import com.gnp.autos.wsp.negocio.neg.model.EmiteNegResp;
import com.gnp.autos.wsp.negocio.umoservicemodel.EmisionDatos;

/**
 * The Interface Domain.
 */
@FunctionalInterface
public interface Domain {
    /**
     * Gets the emitir.
     *
     * @param emDatos the em datos
     * @param tid the tid
     * @param prodTC the prod TC
     * @return the emitir
     */
    EmiteNegResp getEmitir(EmisionDatos emDatos, Integer tid, String prodTC);
}