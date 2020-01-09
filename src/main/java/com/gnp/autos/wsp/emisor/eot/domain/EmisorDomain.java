package com.gnp.autos.wsp.emisor.eot.domain;

import com.gnp.autos.wsp.negocio.emision.model.req.TraductorEmitirReq;
import com.gnp.autos.wsp.negocio.emision.model.resp.TraductorEmitirResp;

/**
 * The Interface EmisorDomain.
 */
@FunctionalInterface
public interface EmisorDomain {
    /**
     * Gets the emitir.
     *
     * @param cotReq the cot req
     * @param tid the tid
     * @return the emitir
     */
    TraductorEmitirResp getEmitir(TraductorEmitirReq cotReq, Integer tid);
}