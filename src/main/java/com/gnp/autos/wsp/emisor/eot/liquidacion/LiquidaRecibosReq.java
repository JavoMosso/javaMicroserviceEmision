package com.gnp.autos.wsp.emisor.eot.liquidacion;

import lombok.Data;

/**
 * The Class LiquidaRecibosReq.
 */
@Data
public class LiquidaRecibosReq {
    /** The cve cond pago. */
    private String cveCondPago = "CL";
    
    /** The ind tipo emi. */
    private String indTipoEmi = "L";
    
    /** The id transaccion. */
    private String idTransaccion = "CIANNE171002022481";
    
    /** The cve transaccion. */
    private String cveTransaccion = "5";
    
    /** The id actor. */
    private String idActor = "TSUAUT";
    
    /** The id rol. */
    private String idRol = "";
    
    /** The id perfil. */
    private String idPerfil = "Empleado";
    
    /** The num poliza. */
    private String numPoliza = "00000374954592";
    
    /** The fecha vigencia inicial. */
    private String fechaVigenciaInicial = "2017-10-06";
    
    /** The fecha vigencia final. */
    private String fechaVigenciaFinal = "2018-10-06";
    
    /** The canal cobro. */
    private String canalCobro = "CL";
    
    /** The medio cobro. */
    private String medioCobro = "006";
    
    /** The num tarjeta. */
    private String numTarjeta = "5470464940536400";
    
    /** The origen. */
    private String origen = "VD";
    
    /** The referencia. */
    private String referencia = "XFD45345345";
    
    /** The reintento. */
    private boolean reintento;
    
    /** The cve tipo cuenta tarjeta. */
    private String cveTipoCuentaTarjeta = "VD";
    
    /** The num cobro. */
    private String numCobro = "0000110951";
    
    /** The usuario audit. */
    private String usuarioAudit = "TSUAUT";
}