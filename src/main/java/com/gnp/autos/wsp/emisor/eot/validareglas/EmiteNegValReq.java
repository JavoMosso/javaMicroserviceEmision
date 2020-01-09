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
 * The Class EmiteNegValReq.
 */
@Data
@XmlRootElement(name = "EmiteNegValReq")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EmiteNegValReq")
@ApiModel(value = "EmiteNegValReq")
public class EmiteNegValReq {
    /** The id cotizacion. */
    @XmlElement(name = "idCotizacion")
    @JsonProperty(value = "idCotizacion")
    @ApiModelProperty(value = "id de Cotizacion", position = 0, example = "CIANNE00000001")
    private String idCotizacion;
    
    /** The codigo promocion. */
    @XmlElement(name = "codigoPromocion")
    @JsonProperty(value = "codigoPromocion")
    @ApiModelProperty(value = "Codigo de Promocion", position = 1, example = "COP0000005")
    private String codigoPromocion;
    
    /** The ini vig. */
    @XmlElement(name = "iniVig")
    @JsonProperty(value = "iniVig")
    @ApiModelProperty(value = "Inicio de vigencia", position = 2, example = "2017-10-18")
    private String iniVig;
    
    /** The fin vig. */
    @XmlElement(name = "finVig")
    @JsonProperty(value = "finVig")
    @ApiModelProperty(value = "Fin de vigencia", position = 3, example = "2018-10-18")
    private String finVig;
    
    /** The fch cotizacion. */
    @XmlElement(name = "fchCotizacion")
    @JsonProperty(value = "fchCotizacion")
    @ApiModelProperty(value = "Fecha de cotizacion", position = 4, example = "2017-10-18")
    private String fchCotizacion;
    
    /** The fch expedicion. */
    @XmlElement(name = "fchExpedicion")
    @JsonProperty(value = "fchExpedicion")
    @ApiModelProperty(value = "Fecha de expedicion", position = 5, example = "2017-10-18")
    private String fchExpedicion;
    
    /** The forma pago. */
    @XmlElement(name = "formaPago")
    @JsonProperty(value = "formaPago")
    @ApiModelProperty(value = "Forma de Pago", position = 6, example = "A")
    private String formaPago;
    
    /** The canal cobro. */
    @XmlElement(name = "canalCobro")
    @JsonProperty(value = "canalCobro")
    @ApiModelProperty(value = "Canal de cobro", position = 7, example = "IN")
    private String canalCobro;
    
    /** The canal cobro sub. */
    @XmlElement(name = "canalCobroSub")
    @JsonProperty(value = "canalCobroSub")
    @ApiModelProperty(value = "Canal de cobro subsecuente", position = 8, example = "BC")
    private String canalCobroSub;
    
    /** The prima neta. */
    @XmlElement(name = "primaNeta")
    @JsonProperty(value = "primaNeta")
    @ApiModelProperty(value = "Prima deta de la poliza", position = 9, example = "4194.78")
    private String primaNeta = "";
    
    /** The id modelo negocio. */
    @XmlElement(name = "idModeloNegocio")
    @JsonProperty(value = "idModeloNegocio")
    @ApiModelProperty(value = "Id modelo de negocio", position = 10, example = "MON0000001")
    private String idModeloNegocio = "";
    
    /** The id negocio operable. */
    @XmlElement(name = "idNegocioOperable")
    @JsonProperty(value = "idNegocioOperable")
    @ApiModelProperty(value = "Id negocio operable", position = 11, example = "NOP0000002")
    private String idNegocioOperable = "";
    
    /** The id negocio comercial. */
    @XmlElement(name = "idNegocioComercial")
    @JsonProperty(value = "idNegocioComercial")
    @ApiModelProperty(value = "Id negocio comercial", position = 12, example = "NCO0000000")
    private String idNegocioComercial = "";
    
    /** The vehiculo. */
    @XmlElement(name = "vehiculo")
    @JsonProperty(value = "vehiculo")
    @ApiModelProperty(value = "Vehiculo", position = 13)
    private VehiculoValNeg vehiculo;
    
    /** The convenios. */
    @XmlElement(name = "convenios")
    @JsonProperty(value = "convenios")
    @ApiModelProperty(value = "Convenios", position = 15)
    private List<ConvenioValNeg> convenios;
    
    /** The personas. */
    @XmlElement(name = "personas")
    @JsonProperty(value = "personas")
    @ApiModelProperty(value = "Personas", position = 14)
    private List<PersonaValNeg> personas;
    
    /** The agentes. */
    @XmlElement(name = "agentes")
    @JsonProperty(value = "agentes")
    @ApiModelProperty(value = "Agentes", position = 15)
    private List<AgenteValNeg> agentes;
    
    /** The descuentos. */
    @XmlElement(name = "descuentos")
    @JsonProperty(value = "descuentos")
    @ApiModelProperty(value = "Descuentos", position = 16)
    private List<DescuentoValNeg> descuentos;
    
    /** The datos producto. */
    @XmlElement(name = "datosProducto")
    @JsonProperty(value = "datosProducto")
    @ApiModelProperty(value = "Datos del producto", position = 17)
    private DatosProductoValNeg datosProducto;
}