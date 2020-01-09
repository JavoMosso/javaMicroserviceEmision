package com.gnp.autos.wsp.emisor.eot.cancelacion;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * The Class CancelacionReq.
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CANCELACION")
@ApiModel(value = "CANCELACION")
@SuppressWarnings("all")
public class CancelacionReq {
    /** The usuario. */
    @XmlElement(name = "USUARIO")
    @JsonProperty(value = "USUARIO")
    @ApiModelProperty(value = "usuario", example = "WEBNE218", position = 0)
    private String usuario;
    
    /** The password. */
    @XmlElement(name = "PASSWORD")
    @JsonProperty(value = "PASSWORD")
    @ApiModelProperty(value = "password", example = "374d6457", position = 1)
    private String password;
    
    /** The num poliza. */
    @XmlElement(name = "NUMERO_POLIZA")
    @JsonProperty(value = "NUMERO_POLIZA")
    @ApiModelProperty(value = "numero de poliza", example = "01234567891234", position = 2)
    private String numPoliza;
}