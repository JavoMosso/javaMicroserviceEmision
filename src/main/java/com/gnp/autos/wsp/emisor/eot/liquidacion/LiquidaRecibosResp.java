package com.gnp.autos.wsp.emisor.eot.liquidacion;

import java.math.BigDecimal;
import lombok.Data;

/**
 * The Class LiquidaRecibosResp.
 */
@Data
public class LiquidaRecibosResp {
    /** The num recibo. */
    private String numRecibo;
    
    /** The fecha inicio recibo. */
    private String fechaInicioRecibo;
    
    /** The fecha vencimiento recibo. */
    private String fechaVencimientoRecibo;
    
    /** The cve estado recibo. */
    private String cveEstadoRecibo;
    
    /** The cve sub estado recibo. */
    private String cveSubEstadoRecibo;
    
    /** The monto total recibo. */
    private BigDecimal montoTotalRecibo;
    
    /** The cod intermediario. */
    private String codIntermediario;
    
    /** The cve moneda. */
    private String cveMoneda;
    
    /** The id fraccion recibo. */
    private int idFraccionRecibo;
    
    /** The fecha cancelacion recibo. */
    private String fechaCancelacionRecibo;
    
    /** The error liquidacion. */
    private String errorLiquidacion;
    
    /** The mensaje. */
    private String mensaje;
    
    /** The num confirmacion. */
    private String numConfirmacion;
    
    /** The num trabajo. */
    private String numTrabajo;
    
    /** The ban operacion exitosa. */
    private boolean banOperacionExitosa;
}