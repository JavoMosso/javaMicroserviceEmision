package com.gnp.autos.wsp.emisor.eot.rest.service;

import java.util.Collections;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.gnp.autos.wsp.emisor.eot.error.ExecutionError;
import com.gnp.autos.wsp.emisor.eot.liquidacion.RegistrarCuentaFinancieraReq;
import com.gnp.autos.wsp.emisor.eot.util.Constantes;

import lombok.Data;

/**
 * The Class CuentaFinancieraClient.
 */
@Data
public class CuentaFinancieraClient {
    /** The rest template. */
    private RestTemplate restTemplate;
    
    /**
     * Instantiates a new cuenta financiera client.
     *
     * @param rest the rest
     */
    public CuentaFinancieraClient(final RestTemplate rest) {
        restTemplate = rest;
    }
    
    /**
     * Gets the cuenta financiera service.
     *
     * @param cuentaFinancieraReq the cuenta financiera req
     * @param paramUrl the param url
     * @param tid the tid
     */
    public void getCuentaFinancieraService(final RegistrarCuentaFinancieraReq cuentaFinancieraReq,
            final String paramUrl, final Integer tid) {
        String msgError = "Servicio de cuenta Financiera-- ";
        try {
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
            HttpEntity<RegistrarCuentaFinancieraReq> requestEntity = new HttpEntity<>(cuentaFinancieraReq, 
                    requestHeaders);
            String url = paramUrl + "/cuentaFinanciera";
            if (tid != null) {
                url += "?tid=" + tid.toString();
            }
            restTemplate.postForEntity(url, requestEntity, Void.class);
        } catch (Exception ex) {
            Logger.getRootLogger().info(ex);
            msgError += "url:" + paramUrl + "; error: " + ex.getMessage();
            manejaErrorID(msgError, tid);
        }
    }
    
    /**
     * Maneja error ID.
     *
     * @param msgError the msg error
     * @param tid the tid
     */
    protected static void manejaErrorID(final String msgError, final Integer tid) {
        String msgErrorR = msgError;
        if (tid != null) {
            msgErrorR = msgError + "(" + tid.toString() + ")";
        }
        throw new ExecutionError(Constantes.ERROR_3, msgErrorR);
    }
}