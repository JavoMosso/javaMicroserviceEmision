package com.gnp.autos.wsp.emisor.eot.validareglas;

import lombok.Data;

/**
 * The Class CoberturaValNeg.
 */
@Data
public class CoberturaValNeg {
    /** The clavecobertura. */
    private String clavecobertura;
    
    /** The mto suma asegurada. */
    private String mtoSumaAsegurada;
    
    /** The mto deducible. */
    private String mtoDeducible;
}