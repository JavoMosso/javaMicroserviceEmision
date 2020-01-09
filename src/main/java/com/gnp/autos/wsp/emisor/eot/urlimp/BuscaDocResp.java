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
 * The Class BuscaDocResp.
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BUSCADOCUMENTO")
@ApiModel(value = "BUSCADOCUMENTO")
public class BuscaDocResp {
    /** The resultado. */
    @XmlElement(name = "RESULTADO")
    @JsonProperty(value = "RESULTADO")
    @ApiModelProperty(value = "Resultado", position = 0)
    private Resultado resultado;
}