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
 * The Class BuscaDocReq.
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "IMPRESION_POLIZA")
@ApiModel(value = "IMPRESION_POLIZA")
public class BuscaDocReq {
    /** The num poliza. */
    @XmlElement(name = "NUM_POLIZA")
    @JsonProperty(value = "NUM_POLIZA")
    @ApiModelProperty(value = "Numero de poliza", position = 1)
    private String numPoliza;
    
    /** The num version. */
    @XmlElement(name = "NUM_VERSION")
    @JsonProperty(value = "NUM_VERSION")
    @ApiModelProperty(value = "Numero de version", position = 2)
    private String numVersion;
    
    /** The extension archivo. */
    @XmlElement(name = "EXTENSION_ARCHIVO")
    @JsonProperty(value = "EXTENSION_ARCHIVO")
    @ApiModelProperty(value = "Extencion de archivo", position = 2 + 1)
    private String extensionArchivo;
}