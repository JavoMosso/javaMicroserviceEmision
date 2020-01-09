package com.gnp.autos.wsp.emisor.eot.rest.service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gnp.autos.wsp.emisor.eot.error.ExecutionError;
import com.gnp.autos.wsp.emisor.eot.util.Constantes;
import com.gnp.autos.wsp.errors.xml.ErrorXML;
import com.gnp.autos.wsp.negocio.neg.model.EmiteNegReq;
import com.gnp.autos.wsp.negocio.neg.model.PersonaNeg;

import lombok.Data;

/**
 * The Class PersonaClient.
 */
@Data
public class PersonaClient {
    /** The rest template. */
    private RestTemplate restTemplate;
    
    /**
     * Instantiates a new persona client.
     *
     * @param rest the rest
     */
    public PersonaClient(final RestTemplate rest) {
        restTemplate = rest;
    }
    
    /**
     * Gets the persona service.
     *
     * @param personas the personas
     * @param paramUrl the param url
     * @param tid the tid
     * @param objReq the obj req
     * @return the persona service
     */
    public List<PersonaNeg> getPersonaService(final List<PersonaNeg> personas, final String paramUrl, final Integer tid,
            final EmiteNegReq objReq) {
        List<PersonaNeg> logs = null;
        String msgError = "Servicio de alta de personas --  url:" + paramUrl + "; error: ";
        ResponseEntity<List<PersonaNeg>> srvPersonas = null;
        try {
            for (PersonaNeg person : personas) {
                person.setBanContConductor(objReq.getIndContConductor());
                person.setBanContBeneficiario(objReq.getIndContBenef());
            }
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
            HttpEntity<List<PersonaNeg>> requestEntity = new HttpEntity<>(personas, requestHeaders);
            if (tid != null) {
                srvPersonas = restTemplate.exchange(paramUrl + "/persona" + "?tid=" + tid.toString() + "&idCot="
                            + objReq.getIdCotizacion(),
                        HttpMethod.POST, requestEntity, new ParameterizedTypeReference<List<PersonaNeg>>() { });
            } else {
                srvPersonas = restTemplate.exchange(paramUrl + "/persona?idCot=" + objReq.getIdCotizacion(), 
                        HttpMethod.POST, requestEntity, new ParameterizedTypeReference<List<PersonaNeg>>() { });
            }
            logs = srvPersonas.getBody();
        } catch (HttpClientErrorException re) {
            Logger.getRootLogger().info(re);
            if (re.getStatusCode() == HttpStatus.BAD_REQUEST) {
                ObjectMapper om = new ObjectMapper();
                try {
                    ErrorXML objErr = om.readValue(re.getResponseBodyAsString(), ErrorXML.class);
                    msgError = "Servicio de alta de personas -- " + objErr.getError();
                    manejaErrorID(msgError, tid);
                } catch (IOException e) {
                    Logger.getRootLogger().info(e);
                    msgError += e.getMessage();
                    manejaErrorID(msgError, tid);
                }
            } else {
                msgError += re.getMessage();
                manejaErrorID(msgError, tid);
            }
        } catch (Exception ex) {
            Logger.getRootLogger().info(ex);
            msgError += ex.getMessage();
            manejaErrorID(msgError, tid);
        }
        return logs;
    }
    
    /**
     * Maneja error ID.
     *
     * @param msgError the msg error
     * @param tid the tid
     */
    public static void manejaErrorID(final String msgError, final Integer tid) {
        String msgErrorR = msgError;
        if (tid != null) {
            msgErrorR = msgError + "(" + tid.toString() + ")";
        }
        throw new ExecutionError(Constantes.ERROR_3, msgErrorR);
    }
}