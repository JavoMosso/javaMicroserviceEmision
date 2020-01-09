package com.gnp.autos.wsp.emisor.eot.validareglas;

import java.util.List;
import lombok.Data;

/**
 * The Class DatosProductoValNeg.
 */
@Data
public class DatosProductoValNeg {
    /** The idproducto. */
    private String idproducto;
    
    /** The coberturas. */
    private List<CoberturaValNeg> coberturas;
}