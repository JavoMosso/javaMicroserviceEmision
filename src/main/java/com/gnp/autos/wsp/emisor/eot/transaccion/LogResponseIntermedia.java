package com.gnp.autos.wsp.emisor.eot.transaccion;

import lombok.Data;

/**
 * The Class LogResponseIntermedia.
 */
@Data
public class LogResponseIntermedia {
    /** The id transaccion. */
    private String idTransaccion;
    
    /** The cve servicio intermedio. */
    private String cveServicioIntermedio;
    
    /** The request. */
    private String request;
    
    /** The response. */
    private String response;
}