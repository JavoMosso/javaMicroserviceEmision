package com.gnp.autos.wsp.emisor.eot.rest.service;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import com.gnp.autos.wsp.emisor.eot.liquidacion.LiquidaRecibosReq;
import com.gnp.autos.wsp.emisor.eot.liquidacion.LiquidaRecibosResp;
import lombok.Data;

/**
 * The Class LiquidacionClient.
 */

@Data
public class LiquidacionClient {
    /**
     * Liquida poliza.
     *
     * @param objReq the obj req
     * @param paramUrl the param url
     * @param tid the tid
     */
    public void liquidaPoliza(final LiquidaRecibosReq objReq, final String paramUrl, final Integer tid) {
        try {
            AsyncRestTemplate objR = new AsyncRestTemplate();
            Logger.getRootLogger().info("liquida");
            ListenableFuture<ResponseEntity<LiquidaRecibosResp>> response;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<LiquidaRecibosReq> entityRequest = new HttpEntity<>(objReq, headers);
            if (tid != null) {
                response = objR.postForEntity(paramUrl + "/liquidaRecibos" + "?tid=" + tid.toString(), entityRequest,
                        LiquidaRecibosResp.class);
            } else {
                response = objR.postForEntity(paramUrl + "/liquidaRecibos", entityRequest, LiquidaRecibosResp.class);
            }
            Logger.getRootLogger().info("termina liquida");
            if (response.isDone()) {
                LiquidaRecibosResp lResp;
                lResp = response.get().getBody();
                Logger.getRootLogger().info(lResp);
            }
        } catch (Exception ex) {
            Logger.getRootLogger().info(ex);
        }
    }
}