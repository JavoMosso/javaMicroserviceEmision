package com.gnp.autos.wsp.emisor.eot.rest.service;

import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.gnp.autos.wsp.emisor.eot.error.ExecutionError;
import com.gnp.autos.wsp.emisor.eot.urlimp.BuscaDocReq;
import com.gnp.autos.wsp.emisor.eot.urlimp.BuscaDocResp;
import com.gnp.autos.wsp.emisor.eot.util.Constantes;
import com.gnp.autos.wsp.negocio.neg.model.EmiteNegResp;

import lombok.Data;

/**
 * The Class ImpresionClient.
 */
@Data
public class ImpresionClient {
    /** The rest template. */
    private RestTemplate restTemplate;
    
    /**
     * Instantiates a new impresion client.
     *
     * @param rest the rest
     */
    public ImpresionClient(final RestTemplate rest) {
        restTemplate = rest;
    }
    
    /**
     * Gets the busca doc imp.
     *
     * @param objReq the obj req
     * @param paramUrl the param url
     * @param tid the tid
     * @return the busca doc imp
     */
    public BuscaDocResp getBuscaDocImp(final EmiteNegResp objReq, final String paramUrl, final Integer tid) {
        try {
            BuscaDocReq objBImp = new BuscaDocReq();
            objBImp.setNumPoliza(objReq.getNumPoliza());
            ResponseEntity<BuscaDocResp> response;
            if (tid != null) {
                response = restTemplate.postForEntity(paramUrl + "/impresion/buscarDoc" + "?tid=" + tid.toString(),
                        objBImp, BuscaDocResp.class);
            } else {
                response = restTemplate.postForEntity(paramUrl + "/impresion/buscarDoc", objBImp, BuscaDocResp.class);
            }
            BuscaDocResp lResp;
            lResp = response.getBody();
            return lResp;
        } catch (Exception ex) {
            Logger.getRootLogger().info(ex);
            throw new ExecutionError(Constantes.ERROR_3, ex.getMessage());
        }
    }
}