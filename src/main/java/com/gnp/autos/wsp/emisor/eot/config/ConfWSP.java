package com.gnp.autos.wsp.emisor.eot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

// TODO: Auto-generated Javadoc
/**
 * The Class ConfWSP.
 */
@Data
@Component
public class ConfWSP {
    /** The url emision EOT. */
    @Value("${wsp_url_EmisionEOT}")
    private String urlEmisionEOT;
    
    /** The url catalogo. */
    @Value("${wsp_url_Catalogo}")
    private String urlCatalogo;
    
    /** The url liquidacion. */
    @Value("${wsp_url_Liquidacion}")
    private String urlLiquidacion;
    
    /** The url validas. */
    @Value("${wsp_url_Validas}")
    private String urlValidas;
    
    /** The url impresion. */
    @Value("${wsp_url_Impresion}")
    private String urlImpresion;
    
    /** The url persona. */
    @Value("${wsp_url_Persona}")
    private String urlPersona;
    
    /** The url movimiento EOT. */
    @Value("${wsp_url_Movimiento}")
    private String urlMovimientoEOT;
    
    /** The url consulta CP. */
    @Value("${wsp_url_ConsultaCP}")
    private String urlConsultaCP;
    
    /** The url cab transaccion. */
    @Value("${wsp_cab_cveTransaccion}")
    private String cabCveTransaccion;
    
    /** The url cab id actor. */
    @Value("${wsp_cab_idActor}")
    private String cabIdActor;
    
    /** The url cab id perfil. */
    @Value("${wsp_cab_idPerfil}")
    private String cabIdPerfil;
    
    /** The url cab id rol. */
    @Value("${wsp_cab_idRol}")
    private String cabIdRol;
    
    /** The url T inter. */
    @Value("${wsp_url_Transacciones}")
    private String urlTransacciones;
    
    /** The url mov eot. */
    @Value("${wsp_url_Movimiento}")
    private String urlMovEot;
    
    /** The url emsion. */
    @Value("${wsp_url_Emision}")
    private String urlEmsion;
    
    /** The url cotizacion. */
    @Value("${wsp_url_Cotizacion}")
    private String urlCotizacion;
    
    /** The url pasos. */
    @Value("${wsp_url_urlPasos}")
    private String urlPasos;
    
    /** The url prod tec com. */
    @Value("${wsp_url_prodTecCom}")
    private String urlProdTecCom;
    
    /** The url foliador. */
    @Value("${wsp_url_urlFoliador}")
    private String urlFoliador;
    
    /** The url cancelacion. */
    @Value("${wsp_url_Cancelacion}")
    private String urlCancelacion;
    
    /** The url consulta pol. */
    @Value("${wsp_url_ConsultaPol}")
    private String urlConsultaPol;
    
    /** The url WS pago TV. */
    @Value("${wsp_url_WSPagoTV}")
    private String urlWSPagoTV;
    
    /** The valida pago. */
    @Value("${wsp_valida_pago}")
    private String validaPago;
    
    /** The cod promo recotiza. */
    @Value("${wsp_codPromo_recotiza}")
    private String codPromoRecotiza;
    
    /** The cambia pago. */
    @Value("${wsp_cambia_pago}")
    private String cambiaPago;
    
    /** The liquida origen default. */
    @Value("${wsp_liquida_origen_default}")
    private String liquidaOrigenDefault;
}