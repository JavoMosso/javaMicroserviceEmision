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
 * The Class DatosSolicitudReq.
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "DATOS_SOLICITUD")
@ApiModel(value = "DATOS_SOLICITUD")
@SuppressWarnings("all")
public class DatosSolicitudReq {
    /** The sub ramo. */
    @XmlElement(name = "SUBRAMO")
    @JsonProperty(value = "SUBRAMO")
    @ApiModelProperty(value = "subramo", example = "01", position = 0)
    private String subRamo;
    
    /** The tipo vehiculo. */
    @XmlElement(name = "TIPO_VEHICULO")
    @JsonProperty(value = "TIPO_VEHICULO")
    @ApiModelProperty(value = "tipo vehiculo", example = "AUT", position = 1)
    private String tipoVehiculo;
    
    /** The cve tipouso. */
    @XmlElement(name = "CVE_TIPO_USO")
    @JsonProperty(value = "CVE_TIPO_USO")
    @ApiModelProperty(value = "clave tipo de uso", example = "23", position = 2)
    private String cveTipouso;
    
    /** The codigo promocion. */
    @XmlElement(name = "ID_CODIGO_PROMOCION")
    @JsonProperty(value = "ID_CODIGO_PROMOCION")
    @ApiModelProperty(value = "codigo de promocion", example = "23", position = 2)
    private String codigoPromocion;
}