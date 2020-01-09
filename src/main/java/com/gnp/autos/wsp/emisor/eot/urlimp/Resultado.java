package com.gnp.autos.wsp.emisor.eot.urlimp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * The Class Resultado.
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "RESULTADO")
@ApiModel(value = "RESULTADO")
public class Resultado {
    /** The url documento. */
    @XmlElement(name = "URL_DOCUMENTO")
    @JsonProperty(value = "URL_DOCUMENTO")
    @ApiModelProperty(value = "UrlDocumento", position = 0)
    private String urlDocumento;
    
    /** The nombre documento. */
    @XmlElement(name = "NOMBRE_DOCUMENTO")
    @JsonProperty(value = "NOMBRE_DOCUMENTO")
    @ApiModelProperty(value = "NombreDocumento", position = 1)
    private String nombreDocumento;
    
    /** The extension archivo. */
    @XmlElement(name = "EXTENSION_ARCHIVO")
    @JsonProperty(value = "EXTENSION_NARCHIVO")
    @ApiModelProperty(value = "ExtensionDocumento", position = 2)
    private String extensionArchivo;
}