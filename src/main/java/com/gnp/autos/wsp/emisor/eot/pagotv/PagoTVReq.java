package com.gnp.autos.wsp.emisor.eot.pagotv;

import lombok.Data;

/**
 * The Class PagoTVReq.
 */
@Data
public class PagoTVReq {
    /** The id. */
    private String id = "";

    /** The usuario. */
    private String usuario = "";

    /** The pw. */
    private String pw = "";

    /** The num autorizacion. */
    private String numAutorizacion = "";

    /** The num poliza. */
    private String numPoliza = "";

    /** The num seguimiento. */
    private String numSeguimiento = "";

    /** The id cotizacion. */
    private String idCotizacion = "";
}