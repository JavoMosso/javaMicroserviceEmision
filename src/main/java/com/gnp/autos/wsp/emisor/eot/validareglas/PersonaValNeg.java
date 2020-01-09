package com.gnp.autos.wsp.emisor.eot.validareglas;

import lombok.Data;

/**
 * The Class PersonaValNeg.
 */
@Data
public class PersonaValNeg {
    /** The tipo. */
    private String tipo;
    
    /** The tipo persona. */
    private String tipoPersona;
    
    /** The id participante. */
    private String idParticipante;
    
    /** The rfc. */
    private String rfc;
    
    /** The fec nacimiento. */
    private String fecNacimiento;
    
    /** The fec constitucion. */
    private String fecConstitucion;
    
    /** The domicilio. */
    private DomicilioValNeg domicilio;
    
    /** The ban beneficiario preferente. */
    private String banBeneficiarioPreferente;
}