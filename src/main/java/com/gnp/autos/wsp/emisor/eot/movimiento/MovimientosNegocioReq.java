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
 * The Class MovimientosNegocioReq.
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "MOVIMIENTOS_NEGOCIO")
@ApiModel(value = "MOVIMIENTOS_NEGOCIO")
@SuppressWarnings("all")
public class MovimientosNegocioReq {
    /** The id negocio operable. */
    @XmlElement(name = "ID_NEGOCIO_OPERABLE")
    @JsonProperty(value = "ID_NEGOCIO_OPERABLE")
    @ApiModelProperty(value = "id negocio operable", example = "NOP0000016", position = 0)
    private String idNegocioOperable;
 
    /** The datos solicitud. */
    @XmlElement(name = "DATOS_SOLICITUD")
    @JsonProperty(value = "DATOS_SOLICITUD")
    private DatosSolicitudReq datosSolicitud;
}