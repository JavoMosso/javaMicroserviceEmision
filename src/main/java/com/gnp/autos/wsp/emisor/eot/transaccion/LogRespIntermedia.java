package com.gnp.autos.wsp.emisor.eot.transaccion;

import lombok.Data;

/**
 * The Class LogRespIntermedia.
 */
@Data
public class LogRespIntermedia {
    /** The folio WSP. */
    private Integer folioWSP;
    
    /** The request. */
    private String request;
    
    /** The response. */
    private String response;
    
    /** The id transaccion. */
    private String idTransaccion;
}