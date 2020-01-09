package com.gnp.autos.wsp.emisor.eot.rest.service;

import java.util.Collections;

import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.gnp.autos.wsp.emisor.eot.error.ExecutionError;
import com.gnp.autos.wsp.emisor.eot.pagotv.PagoTVReq;
import com.gnp.autos.wsp.emisor.eot.pagotv.PagoTVResp;
import com.gnp.autos.wsp.emisor.eot.util.Constantes;
import com.gnp.autos.wsp.errors.ClienteSafeConsumer;
import com.gnp.autos.wsp.errors.exceptions.WSPSimpleException;

import lombok.Data;

/**
 * The Class ValidaPagoTVClient.
 */
@Data
public class ValidaPagoTVClient {
    /** The obj pago req. */
    private PagoTVReq objPagoReq;
    
    /** The rest template. */
    private RestTemplate restTemplate;
    
    /**
     * Instantiates a new valida pago TV client.
     *
     * @param idCotizacion the id cotizacion
     * @param numAutorizacion the num autorizacion
     * @param usuarioVp the usuario vp
     * @param passVp the pass vp
     * @param rest the rest
     */
    public ValidaPagoTVClient(final String idCotizacion, final String numAutorizacion, final String usuarioVp,
            final String passVp, final RestTemplate rest) {
        restTemplate = rest;
        objPagoReq = new PagoTVReq();
        objPagoReq.setNumAutorizacion(numAutorizacion);
        objPagoReq.setIdCotizacion(idCotizacion);
        objPagoReq.setUsuario(usuarioVp);
        objPagoReq.setPw(passVp);
    }
    
    /**
     * Gets the valida pagotv.
     *
     * @param paramUrl the param url
     * @param tid the tid
     * @return the valida pagotv
     */
    public PagoTVResp getValidaPagotv(final String paramUrl, final Integer tid) {
        String msgError = "ValidaPagoTV --";
        ClienteSafeConsumer<PagoTVReq> safe = new ClienteSafeConsumer<>(restTemplate);
        PagoTVResp lResp = null;
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
        try {
            if (tid != null) {
                lResp = safe.uncheckedConsume(getObjPagoReq(), paramUrl + "/getPago?tid=" + tid.toString(), 
                        PagoTVResp.class, MediaType.APPLICATION_JSON);
            } else {
                lResp = safe.uncheckedConsume(getObjPagoReq(), paramUrl + "/getPago", PagoTVResp.class,
                        MediaType.APPLICATION_JSON);
            }
        } catch (Exception ex) {
            Logger.getRootLogger().info(ex);
            msgError += "url:" + paramUrl + "; error: " + ex.getMessage();
            if (tid != null) {
                msgError += "(" + tid.toString() + ")";
            }
            throw new ExecutionError(Constantes.ERROR_3, msgError);
        }
        if (lResp != null) {
            return lResp;
        } else {
            throw new WSPSimpleException("", Constantes.ERROR_3, paramUrl);
        }
    }
}