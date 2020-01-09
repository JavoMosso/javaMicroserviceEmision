package com.gnp.autos.wsp.emisor.eot.movimiento;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * The Class ConsultaNegocioReq.
 */
@Data
@XmlRootElement(name = "ConsultaNegocioRequest")
@ApiModel(value = "ConsultaNegocioRequest")
public class ConsultaNegocioReq {
    /** The cabecera. */
    @XmlElement(name = "Cabecera")
    @JsonProperty(value = "Cabecera")
    private CabeceraReq cabecera;
    
    /** The data. */
    @XmlElement(name = "Data")
    @JsonProperty(value = "Data")
    private DataReq data;
}