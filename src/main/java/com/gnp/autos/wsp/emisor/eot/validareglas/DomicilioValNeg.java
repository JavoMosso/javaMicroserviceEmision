package com.gnp.autos.wsp.emisor.eot.validareglas;

import lombok.Data;

/**
 * The Class DomicilioValNeg.
 */
@Data
public class DomicilioValNeg {
    /** The codigo postal. */
    private String codigoPostal;
    
    /** The estado. */
    private String estado;
    
    /** The municipio. */
    private String municipio;
    
    /** The pais. */
    private String pais;
}