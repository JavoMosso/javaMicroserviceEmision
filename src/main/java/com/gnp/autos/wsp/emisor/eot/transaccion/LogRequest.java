package com.gnp.autos.wsp.emisor.eot.transaccion;

import java.sql.Timestamp;
import lombok.Data;

/**
 * The Class LogRequest.
 */
@Data
public class LogRequest {
    /** The request. */
    private String request;
    
    /** The response. */
    private String response;
    
    /** The tiempo peticion. */
    private Timestamp tiempoPeticion;
    
    /** The tiempo respuesta. */
    private Timestamp tiempoRespuesta;
    
    /** The is exitoso. */
    private boolean isExitoso;
    
    /** The cve modulo. */
    private String cveModulo;
    
    /** The id error. */
    private Integer idError;
    
    /** The cve sistema emisor. */
    private String cveSistemaEmisor;
    
    /** The cve sistema origen. */
    private String cveSistemaOrigen;
    
    /** The cve usuario. */
    private String cveUsuario;
    
    /** The importe prima total. */
    private Double importePrimaTotal;
    
    /** The importe prima neta. */
    private Double importePrimaNeta;
    
    /** The folio WSP. */
    private Integer folioWSP;
    
    /** The id transaccion. */
    private String idTransaccion;
    
    /** The cve operacion. */
    private String cveOperacion = "E";
}