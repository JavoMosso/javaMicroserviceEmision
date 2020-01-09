package com.gnp.autos.wsp.emisor.eot.liquidacion;

import lombok.Data;

/**
 * The Class RegistrarCuentaFinancieraReq.
 */
@Data
public class RegistrarCuentaFinancieraReq {
    /** The id transaccion. */
    private String idTransaccion;
    
    /** The id participante. */
    private String idParticipante;
    
    /** The cod filiacion. */
    private String codFiliacion;
    
    /** The cve entidad financiera. */
    private String cveEntidadFinanciera;
    
    /** The cve moneda. */
    private String cveMoneda;
    
    /** The cve tipo dato bancario. */
    private String cveTipoDatoBancario;
    
    /** The cve tipo cuenta tarjeta. */
    private String cveTipoCuentaTarjeta;
    
    /** The num cuenta. */
    private String numCuenta;
    
    /** The fch vencimiento. */
    private String fchVencimiento;
    
    /** The ban cta principal. */
    private Boolean banCtaPrincipal;
    
    /** The dia cobro preferido. */
    private short diaCobroPreferido;
    
    /** The titular. */
    private String titular;
}
