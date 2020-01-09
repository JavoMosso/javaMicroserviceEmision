package com.gnp.autos.wsp.emisor.eot.domain;

import com.gnp.autos.wsp.emisor.eot.consulta.wsdl.ConsultarPolizaPorNumPolizaResponse;

/**
 * The Interface ConsultaDomain.
 */
@FunctionalInterface
public interface ConsultaDomain {
    /**
     * Consultar poliza.
     *
     * @param numPoliza the num poliza
     * @param idTransaccion the id transaccion
     * @param tid the tid
     * @return the consultar poliza por num poliza response
     */
    ConsultarPolizaPorNumPolizaResponse consultarPoliza(String numPoliza, String idTransaccion, Integer tid);
}