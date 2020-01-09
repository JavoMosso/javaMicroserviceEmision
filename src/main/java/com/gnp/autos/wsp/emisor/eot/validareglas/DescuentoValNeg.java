package com.gnp.autos.wsp.emisor.eot.validareglas;

import lombok.Data;

/**
 * The Class DescuentoValNeg.
 */
@Data
public class DescuentoValNeg {
    /** The cve descuento. */
    private String cveDescuento;
    
    /** The descripcion. */
    private String descripcion;
    
    /** The unidad medida. */
    private String unidadMedida;
    
    /** The valor. */
    private String valor;
    
    /** The ban recargo. */
    private String banRecargo;
}