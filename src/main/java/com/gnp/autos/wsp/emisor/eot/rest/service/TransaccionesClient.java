package com.gnp.autos.wsp.emisor.eot.rest.service;

import java.io.StringReader;
import java.util.List;
import java.util.Optional;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.client.RestTemplate;
import org.springframework.xml.transform.StringSource;
import com.gnp.autos.wsp.emisor.eot.error.ExecutionError;
import com.gnp.autos.wsp.emisor.eot.transaccion.LogRespIntermedia;
import com.gnp.autos.wsp.emisor.eot.transaccion.LogResponseIntermedia;
import com.gnp.autos.wsp.negocio.banderaauto.soap.BanderasAutosDecisionServiceRequestRequest;
import com.gnp.autos.wsp.negocio.banderaauto.soap.BanderasAutosDecisionServiceRequestResponse;
import com.gnp.autos.wsp.negocio.cotizacion.model.resp.TraductorResp;
import com.gnp.autos.wsp.negocio.muc.soap.CalcularPrimaAutoRequest;
import com.gnp.autos.wsp.negocio.muc.soap.CalcularPrimaAutoResponse;
import com.gnp.autos.wsp.negocio.neg.model.EmiteNegReq;
import com.gnp.autos.wsp.negocio.umoservicemodel.EmisionDatos;
import com.gnp.autos.wsp.negocio.umoservicemodel.UmoServiceResp;
import lombok.Data;

/**
 * The Class TransaccionesClient.
 */
@Data
public class TransaccionesClient {
    /** The Constant STRERROR. */
    private static final String STRERROR = "No se pudo recuperar datos de la cotización";
    
    /** The rest template. */
    private RestTemplate restTemplate;
    
    /**
     * Instantiates a new transacciones client.
     *
     * @param rest the rest
     */
    public TransaccionesClient(final RestTemplate rest) {
        restTemplate = rest;
    }
    
    /**
     * Obten transaccion intermedia.
     *
     * @param cveModulo the cve modulo
     * @param idCotizacion the id cotizacion
     * @param paramUrl the param url
     * @return the log response intermedia
     */
    public LogResponseIntermedia obtenTransaccionIntermedia(final String cveModulo, final String idCotizacion,
            final String paramUrl) {
        try {
            String paramUrlInt = paramUrl + "/logrecuperaintermedia/" + idCotizacion;
            ResponseEntity<List<LogResponseIntermedia>> logsResponse =
                    restTemplate.exchange(paramUrlInt, HttpMethod.GET, null, 
                            new ParameterizedTypeReference<List<LogResponseIntermedia>>() { });
            if (logsResponse.getStatusCode() != HttpStatus.OK) {
                throw new ExecutionError(1, logsResponse.getBody().toString());
            }
            List<LogResponseIntermedia> logs = logsResponse.getBody();
            Optional<LogResponseIntermedia> logR = logs.stream().filter(
                    p -> p.getCveServicioIntermedio().equalsIgnoreCase(cveModulo)).findFirst();
            if (logR.isPresent()) {
                return logR.get();
            }
        } catch (Exception ex) {
            Logger.getRootLogger().info(ex);
            throw new ExecutionError(1, ex.getMessage());
        }
        return null;
    }
    
    /**
     * Gets the transacciones intermedia.
     *
     * @param idCotizacion the id cotizacion
     * @param paramUrl the param url
     * @return the transacciones intermedia
     */
    public List<LogResponseIntermedia> getTransaccionesIntermedia(final String idCotizacion, final String paramUrl) {
        List<LogResponseIntermedia> logs = null;
        try {
            String paramUrlInt = paramUrl + "/logrecuperaintermedia/trans/" + idCotizacion;
            ResponseEntity<List<LogResponseIntermedia>> transInts =
                    restTemplate.exchange(paramUrlInt, HttpMethod.GET, null, 
                            new ParameterizedTypeReference<List<LogResponseIntermedia>>() { });
            logs = transInts.getBody();
        } catch (Exception ex) {
            Logger.getRootLogger().info(ex);
            throw new ExecutionError(1, ex.getMessage());
        }
        return logs;
    }
    
    /**
     * Gets the trans inter id.
     *
     * @param parametro the parametro
     * @param value the value
     * @return the trans inter id
     */
    public LogRespIntermedia getTransInterId(final String parametro, final String value) {
        LogRespIntermedia logs = null;
        try {
            String paramUrlInt = parametro + "/logrecuperacionId/" + value;
            ResponseEntity<LogRespIntermedia> transInts =
                    restTemplate.exchange(paramUrlInt, HttpMethod.GET, null,
                            new ParameterizedTypeReference<LogRespIntermedia>() { });
            logs = transInts.getBody();
        } catch (Exception ex) {
            Logger.getRootLogger().info(ex);
            throw new ExecutionError(1, ex.getMessage());
        }
        return logs;
    }
    
    /**
     * Gets the MUC cot.
     *
     * @param emiNeg the emi neg
     * @param idCotizacion the id cotizacion
     * @param paramUrl the param url
     * @return the MUC cot
     */
    @SuppressWarnings("unchecked")
    public EmiteNegReq getMUCCot(final EmiteNegReq emiNeg, final String idCotizacion, final String paramUrl) {
        LogResponseIntermedia logI = obtenTransaccionIntermedia("MU", idCotizacion, paramUrl);
        if (logI != null) {
            StringSource sourceReq = new StringSource(logI.getRequest());
            StringSource source = new StringSource(logI.getResponse());
            Jaxb2Marshaller m = new Jaxb2Marshaller();
            m.setContextPath("com.gnp.autos.wsp.negocio.muc.soap");
            JAXBElement<CalcularPrimaAutoRequest> calEleR =
                    (JAXBElement<CalcularPrimaAutoRequest>) m.unmarshal(sourceReq);
            JAXBElement<CalcularPrimaAutoResponse> calEle =
                    (JAXBElement<CalcularPrimaAutoResponse>) m.unmarshal(source);
            emiNeg.setMucPrimaAutoReq(calEleR.getValue());
            emiNeg.setMucPrimaAutoResp(calEle.getValue());
        }
        return emiNeg;
    }
    
    /**
     * Gets the variables cot.
     *
     * @param emiNeg the emi neg
     * @param idCotizacion the id cotizacion
     * @param paramUrl the param url
     * @return the variables cot
     */
    @SuppressWarnings("unchecked")
    public EmisionDatos getVariablesCot(final EmiteNegReq emiNeg, final String idCotizacion, final String paramUrl) {
        List<LogResponseIntermedia> logsI = getTransaccionesIntermedia(idCotizacion, paramUrl);
        String strMU = "MU";
        String strBD = "SB";
        String strUS = "US";
        Optional<LogResponseIntermedia> logMU = logsI.stream().filter(
                p -> p.getCveServicioIntermedio().equalsIgnoreCase(strMU)).findFirst();
        if (!logMU.isPresent()) {
            throw new ExecutionError(2, STRERROR);
        }
        StringSource sourceReq = new StringSource(logMU.get().getRequest());
        StringSource source = new StringSource(logMU.get().getResponse());
        Jaxb2Marshaller m = new Jaxb2Marshaller();
        m.setContextPath("com.gnp.autos.wsp.negocio.muc.soap");
        JAXBElement<CalcularPrimaAutoRequest> calEleR = (JAXBElement<CalcularPrimaAutoRequest>) m.unmarshal(sourceReq);
        JAXBElement<CalcularPrimaAutoResponse> calEle = (JAXBElement<CalcularPrimaAutoResponse>) m.unmarshal(source);
        emiNeg.setMucPrimaAutoReq(calEleR.getValue());
        emiNeg.setMucPrimaAutoResp(calEle.getValue());
        Optional<LogResponseIntermedia> logSB = logsI.stream().filter(p -> p.getCveServicioIntermedio()
                .equalsIgnoreCase(strBD)).findFirst();
        if (!logSB.isPresent()) {
            throw new ExecutionError(2, STRERROR);
        }
        StringSource sourceReqSB = new StringSource(logSB.get().getRequest());
        StringSource sourceSB = new StringSource(logSB.get().getResponse());
        m = new Jaxb2Marshaller();
        m.setContextPath("com.gnp.autos.wsp.negocio.banderaauto.soap");
        BanderasAutosDecisionServiceRequestRequest calSBR =
                (BanderasAutosDecisionServiceRequestRequest) m.unmarshal(sourceReqSB);
        BanderasAutosDecisionServiceRequestResponse calSB =
                (BanderasAutosDecisionServiceRequestResponse) m.unmarshal(sourceSB);
        emiNeg.setBanAutoReq(calSBR);
        emiNeg.setBanAutoResp(calSB);
        Optional<LogResponseIntermedia> logUS = logsI.stream().filter(
                p -> p.getCveServicioIntermedio().equalsIgnoreCase(strUS)).findFirst();
        if (!logUS.isPresent()) {
            throw new ExecutionError(2, STRERROR);
        }
        final int num = 26;
        String subs = logUS.get().getResponse().substring(num, (logUS.get().getResponse().length() - (num + 2)));
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"UmoServiceResp\" :");
        sb.append(subs);
        sb.append("}");
        JSONObject json = new JSONObject(sb.toString());
        String xml = XML.toString(json);
        JAXBContext jaxbContext;
        UmoServiceResp umo;
        try {
            jaxbContext = JAXBContext.newInstance(UmoServiceResp.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            umo = (UmoServiceResp) jaxbUnmarshaller.unmarshal(new StringReader(xml));
        } catch (JAXBException e) {
            throw new ExecutionError(2, "No se pudo recuperar datos del umo");
        }
        EmisionDatos emDatos = new EmisionDatos();
        emDatos.setEmite(emiNeg);
        emDatos.setUmo(umo);
        return emDatos;
    }
    
    /**
     * Gets the id cotizacion.
     *
     * @param idCotizacion the id cotizacion
     * @param urlTransacciones the url transacciones
     * @return the id cotizacion
     */
    public TraductorResp getIdCotizacion(final String idCotizacion, final String urlTransacciones) {
        if (idCotizacion.isEmpty()) {
            throw new ExecutionError(2, "Para este negocio debe enviar un id de cotización");
        }
        LogRespIntermedia logT = getTransInterId(urlTransacciones, idCotizacion);
        if (logT.getFolioWSP().equals(0)) {
            throw new ExecutionError(2, "Es necesario enviar una cotización válida");
        }
        Jaxb2Marshaller m = new Jaxb2Marshaller();
        m.setClassesToBeBound(TraductorResp.class);
        StringSource sourceSB = new StringSource(logT.getResponse());
        TraductorResp tradRes;
        tradRes = (TraductorResp) m.unmarshal(sourceSB);
        return tradRes;
    }
}