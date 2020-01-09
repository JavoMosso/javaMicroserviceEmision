package com.gnp.autos.wsp.emisor.eot.movimiento;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * The Class CabeceraReq.
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Cabecera")
@ApiModel(value = "Cabecera")
@SuppressWarnings("all")
public class CabeceraReq {
    /** The id transaccion. */
    @XmlElement(name = "ID_TRANSACCION")
    @JsonProperty(value = "ID_TRANSACCION")
    @ApiModelProperty(value = "id transaccion", example = "1", position = 0)
    private String idTransaccion;

    /** The cve transaccion. */
    @XmlElement(name = "CVE_TRANSACCION")
    @JsonProperty(value = "CVE_TRANSACCION")
    @ApiModelProperty(value = "clave transaccion", example = "1", position = 1)
    private String cveTransaccion;
    
    /** The id actor. */
    @XmlElement(name = "ID_ACTOR")
    @JsonProperty(value = "ID_ACTOR")
    @ApiModelProperty(value = "id actor", example = "1", position = 2)
    private String idActor;
    
    /** The id rol. */
    @XmlElement(name = "ID_ROL")
    @JsonProperty(value = "ID_ROL")
    @ApiModelProperty(value = "id rol", example = "1", position = 2 + 1)
    private String idRol;
    
    /** The id perfil. */
    @XmlElement(name = "ID_PERFIL")
    @JsonProperty(value = "ID_PERFIL")
    @ApiModelProperty(value = "id perfil", example = "1", position = 2 + 2)
    private String idPerfil;
}
