package com.gnp.autos.wsp.emisor.eot.rest.service;

import org.springframework.web.client.RestTemplate;
import com.gnp.autos.wsp.emisor.eot.error.ExecutionError;
import com.gnp.autos.wsp.errors.ClienteSafeConsumer;
import com.gnp.autos.wsp.negocio.catalogo.model.CatalogoReq;
import com.gnp.autos.wsp.negocio.catalogo.model.CatalogoResp;
import lombok.Data;

/**
 * The Class CatalogoClient.
 */
@Data
public class CatalogoClient {
    /** The Constant STRVACIO. */
    private static final String STRVACIO = "";
    
    /** The Constant catalogoClient. */
    private static final CatalogoClient CATALOGO_CLIENT = new CatalogoClient();
    
    /** The request cat. */
    private static CatalogoReq requestCat;
    
    /** The rest template. */
    private static RestTemplate restTemplate;
    
    /**
     * Gets the instancia.
     *
     * @param rest the rest
     * @return the instancia
     */
    public static CatalogoClient getInstancia(final RestTemplate rest) {
        CatalogoClient.requestCat = new CatalogoReq();
        CatalogoClient.requestCat.setUsuario(STRVACIO);
        CatalogoClient.requestCat.setPassword(STRVACIO);
        CatalogoClient.requestCat.setFecha(STRVACIO);
        CatalogoClient.requestCat.setIdUMO(STRVACIO);
        CatalogoClient.requestCat.setTipoCatalogo(STRVACIO);
        restTemplate = rest;
        return CATALOGO_CLIENT;
    }
    
    /**
     * Gets the instancia.
     *
     * @param objReqCat the obj req cat
     * @param rest the rest
     * @return the instancia
     */
    public static CatalogoClient getInstancia(final CatalogoReq objReqCat, final RestTemplate rest) {
        requestCat = objReqCat;
        restTemplate = rest;
        return CATALOGO_CLIENT;
    }
    
    /**
     * Obten catalogo.
     *
     * @param tipoCatalogo the tipo catalogo
     * @param paramUrl the param url
     * @return the catalogo resp
     */
    public CatalogoResp obtenCatalogo(final String tipoCatalogo, final String paramUrl) {
        try {
            requestCat.setTipoCatalogo(tipoCatalogo);
            CatalogoResp catResp;
            ClienteSafeConsumer<CatalogoReq> req = new ClienteSafeConsumer<>(restTemplate);
            catResp = req.uncheckedConsume(requestCat, paramUrl, CatalogoResp.class);
            return catResp;
        } catch (Exception ex) {
            throw new ExecutionError(0, "Error en consulta de catalogo.");
        }
    }
    
    /**
     * Obten catalogo.
     *
     * @param paramUrl the param url
     * @return the catalogo resp
     */
    public CatalogoResp obtenCatalogo(final String paramUrl) {
        try {
            CatalogoResp catResp;
            ClienteSafeConsumer<CatalogoReq> req = new ClienteSafeConsumer<>(restTemplate);
            catResp = req.uncheckedConsume(requestCat, paramUrl, CatalogoResp.class);
            return catResp;
        } catch (Exception ex) {
            throw new ExecutionError(0, "Error en consulta de catalogo.");
        }
    }
}