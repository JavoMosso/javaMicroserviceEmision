package com.gnp.autos.wsp.emisor.eot.movimiento;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * The Class DataReq.
 */
@Data
@XmlRootElement(name = "Data")
@ApiModel(value = "Data")
public class DataReq {
    /** The movimientos neg. */
    @XmlElement(name = "MOVIMIENTOS_NEGOCIO")
    @JsonProperty(value = "MOVIMIENTOS_NEGOCIO")
    private MovimientosNegocioReq movimientosNeg;
}