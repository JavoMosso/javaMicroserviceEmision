package com.gnp.autos.wsp.emisor.eot.rest.service;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import com.gnp.autos.wsp.negocio.neg.model.AgenteNeg;
import com.gnp.autos.wsp.negocio.neg.model.EmiteNegReq;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;

/**
 * The Class TxEmisorClient.
 */
@Data
public class TxEmisorClient {
    /**
     * Tx emite negocio.
     *
     * @param objReq the obj req
     * @param urlTxEmi the url tx emi
     */
    public void txEmiteNegocio(final EmiteNegReq objReq, final String urlTxEmi) {
        try {
            Gson gs = new GsonBuilder().excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                    .serializeNulls().create();
            EmiteNegReq req = validaObjetoNegReq(objReq);
            String cad = gs.toJson(req);
            Logger.getRootLogger().info("OBJ-REQ: " + cad);
            AsyncRestTemplate rest = new AsyncRestTemplate();
            ListenableFuture<ResponseEntity<String>> response;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<EmiteNegReq> entityRequest = new HttpEntity<>(objReq, headers);
            String url = urlTxEmi + "insertarobjeto/EmiteNegReq";
            response = rest.postForEntity(url, entityRequest, String.class);
            if (response.isDone()) {
                Logger.getRootLogger().info("RESULTADO INSERTAR EMISION: " + response.get().getBody());
            }
        } catch (HttpClientErrorException re) {
            Logger.getRootLogger().info(re);
        } catch (RestClientException ex2) {
            Logger.getRootLogger().info(ex2);
        } catch (Exception ex3) {
            Logger.getRootLogger().info(ex3);
        }
    }
    
    /**
     * Valida objeto neg req.
     *
     * @param objOrig the obj orig
     * @return the emite neg req
     */
    public EmiteNegReq validaObjetoNegReq(final EmiteNegReq objOrig) {
        EmiteNegReq nvoObj = new EmiteNegReq();
        nvoObj.setAccion(objOrig.getAccion());
        nvoObj.setBanAfectaBono(objOrig.getBanAfectaBono());
        nvoObj.setBanAjusteIrregular(objOrig.getBanAjusteIrregular());
        nvoObj.setBanImpresion(objOrig.getBanImpresion());
        nvoObj.setBanImpresionCentralizada(objOrig.getBanImpresionCentralizada());
        nvoObj.setBanRenovacionAutomatica(objOrig.getBanRenovacionAutomatica());
        nvoObj.setBeneficiario(objOrig.getBeneficiario());
        nvoObj.setCanalCobro(objOrig.getCanalCobro());
        nvoObj.setCanalCobroSub(objOrig.getCanalCobroSub());
        nvoObj.setCodigoCEC(objOrig.getCodigoCEC());
        nvoObj.setCodigoPromocion(objOrig.getCodigoPromocion());
        nvoObj.setCveCanalAcceso(objOrig.getCveCanalAcceso());
        nvoObj.setCveFormaAjusteIrregular(objOrig.getCveFormaAjusteIrregular());
        nvoObj.setCveHerramienta(objOrig.getCveHerramienta());
        nvoObj.setCveMoneda(objOrig.getCveMoneda());
        nvoObj.setCveMotivoRechazo(objOrig.getCveMotivoRechazo());
        nvoObj.setCveTarifa(objOrig.getCveTarifa());
        nvoObj.setCveTransformacionTarifa(objOrig.getCveTransformacionTarifa());
        nvoObj.setCveZonificacion(objOrig.getCveZonificacion());
        nvoObj.setDescuentoGC(objOrig.getDescuentoGC());
        nvoObj.setDescuentos(objOrig.getDescuentos());
        nvoObj.setElementos(objOrig.getElementos());
        nvoObj.setFchEfectoMovimiento(objOrig.getFchEfectoMovimiento());
        nvoObj.setFchFinEfectoMovimiento(objOrig.getFchFinEfectoMovimiento());
        nvoObj.setFchTarifa(objOrig.getFchTarifa());
        nvoObj.setFchTransformacionTarifa(objOrig.getFchTransformacionTarifa());
        nvoObj.setFinVig(objOrig.getFinVig());
        nvoObj.setIniVig(objOrig.getIniVig());
        nvoObj.setFormaPago(objOrig.getFormaPago());
        nvoObj.setFrecPagos(objOrig.getFrecPagos());
        nvoObj.setIdAgrupaRecargo(objOrig.getIdAgrupaRecargo());
        nvoObj.setIdCotizacion(objOrig.getIdCotizacion());
        nvoObj.setIdUMO(objOrig.getIdUMO());
        nvoObj.setImpIva(objOrig.getImpIva());
        nvoObj.setIndContConductor(objOrig.getIndContConductor());
        nvoObj.setMetodoPago(objOrig.getMetodoPago());
        nvoObj.setMotivoRechazo(objOrig.getMotivoRechazo());
        nvoObj.setNumeroCaratula(objOrig.getNumeroCaratula());
        nvoObj.setNumeroPagos(objOrig.getNumeroPagos());
        nvoObj.setPaquetes(objOrig.getPaquetes());
        nvoObj.setPassword(objOrig.getPassword());
        nvoObj.setPeriodicidad(objOrig.getPeriodicidad());
        nvoObj.setPersonas(objOrig.getPersonas());
        nvoObj.setPolizaEmitida(objOrig.getPolizaEmitida());
        nvoObj.setPrimaNeta(objOrig.getPrimaNeta());
        nvoObj.setPrimaTotal(objOrig.getPrimaTotal());
        nvoObj.setReferencia1(objOrig.getReferencia1());
        nvoObj.setReferencia2(objOrig.getReferencia2());
        nvoObj.setRfv(objOrig.getRfv());
        nvoObj.setTienda(objOrig.getTienda());
        nvoObj.setUsuario(objOrig.getUsuario());
        nvoObj.setVehiculo(objOrig.getVehiculo());
        nvoObj.setVerZonificacion(objOrig.getVerZonificacion());
        nvoObj.setViaPago(objOrig.getViaPago());
        nvoObj.setViaPagoSucesivos(objOrig.getViaPagoSucesivos());
        return nvoObj;
    }
    
    /**
     * Valida agentes.
     *
     * @param lAgentesOrig the l agentes orig
     * @return the list
     */
    public List<AgenteNeg> validaAgentes(final List<AgenteNeg> lAgentesOrig) {
        List<AgenteNeg> agentesVal = new ArrayList<>();
        for (AgenteNeg ag : lAgentesOrig) {
            ag.setCveClaseIntermediarioInfo("AG");
            agentesVal.add(ag);
        }
        return agentesVal;
    }
}