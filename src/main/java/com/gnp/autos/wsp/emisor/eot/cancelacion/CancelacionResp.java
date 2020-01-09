package com.gnp.autos.wsp.emisor.eot.cancelacion;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * The Class CancelacionResp.
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CANCELAR")
public class CancelacionResp {
    /** The num poliza. */
    @XmlElement(name = "NUMERO_POLIZA")
    @ApiModelProperty(value = "numero de poliza", example = "1", position = 0)
    private String numPoliza;
    
    /** The num version. */
    @XmlElement(name = "NUMERO_VERSION")
    @ApiModelProperty(value = "numero de version", example = "1", position = 1)
    private String numVersion;
    
    /** The est poliza. */
    @XmlElement(name = "ESTADO_POLIZA")
    @ApiModelProperty(value = "estado de la poliza", example = "1", position = 2)
    private String estPoliza;
}