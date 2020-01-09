package com.gnp.autos.wsp.emisor.eot.pagotv;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * The Class PagoTVResp.
 */
@Data
public class PagoTVResp {
    /** The id. */
    @JsonProperty(value = "Id")
    @ApiModelProperty(value = "Id", example = "1")
    private String id;

    /** The id cotizacion. */
    @JsonProperty(value = "IdCotizacion")
    @ApiModelProperty(value = "IdCotizacion", example = "1")
    private String idCotizacion;

    /** The num autorizacion. */
    @JsonProperty(value = "NumAutorizacion")
    @ApiModelProperty(value = "NumAutorizacion", example = "1")
    private String numAutorizacion;

    /** The num poliza. */
    @JsonProperty(value = "NumPoliza")
    @ApiModelProperty(value = "NumPoliza", example = "1")
    private String numPoliza;

    /** The num seguimiento. */
    @JsonProperty(value = "NumSeguimiento")
    @ApiModelProperty(value = "NumSeguimiento", example = "1")
    private String numSeguimiento;

    /** The errores. */
    @JsonProperty(value = "Errores")
    @ApiModelProperty(value = "Errores", example = "1")
    private String errores;
}