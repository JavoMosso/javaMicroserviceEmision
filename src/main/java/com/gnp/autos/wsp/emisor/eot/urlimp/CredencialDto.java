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
 * The Class CredencialDto.
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "credencialDto")
@ApiModel(value = "credencialDto")
public class CredencialDto {
    /** The cve rol. */
    @XmlElement(name = "CVE_ROL")
    @JsonProperty(value = "CVE_ROL")
    @ApiModelProperty(value = "CveRol")
    private String cveRol;
    
    /** The userlogin. */
    @XmlElement(name = "USER_LOGIN")
    @JsonProperty(value = "USER_LOGIN")
    @ApiModelProperty(value = "UserLogin")
    private String userlogin;
}