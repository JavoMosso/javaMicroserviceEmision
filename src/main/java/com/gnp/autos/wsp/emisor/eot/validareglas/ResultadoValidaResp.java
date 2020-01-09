package com.gnp.autos.wsp.emisor.eot.validareglas;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * The Class ResultadoValidaResp.
 */
@Data
@XmlRootElement(name = "ResultadoValidaResp")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResultadoValidaResp")
@ApiModel(value = "ResultadoValidaResp")
public class ResultadoValidaResp {
    /** The resultado valida. */
    @XmlElement(name = "RESULTADO_VALIDA")
    @JsonProperty(value = "resultadoValida")
    @ApiModelProperty(value = "ResultadoValida", position = 0)
    private String resultadoValida;
    
    /** The lista causas. */
    @XmlElement(name = "LISTA_CAUSAS")
    @JsonProperty(value = "listaCausas")
    @ApiModelProperty(value = "ListaCausas", position = 1)
    private List<String> listaCausas;
}