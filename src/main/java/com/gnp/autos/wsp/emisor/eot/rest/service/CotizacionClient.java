package com.gnp.autos.wsp.emisor.eot.rest.service;

import static com.gnp.autos.wsp.emisor.eot.util.Constantes.ERROR_3;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.gnp.autos.wsp.emisor.eot.error.ExecutionError;
import com.gnp.autos.wsp.errors.xml.ErrorXML;
import com.gnp.autos.wsp.negocio.cotizacion.model.req.AdaptacionVehReq;
import com.gnp.autos.wsp.negocio.cotizacion.model.req.AdaptacionesVehReq;
import com.gnp.autos.wsp.negocio.cotizacion.model.req.CoberturaReq;
import com.gnp.autos.wsp.negocio.cotizacion.model.req.CoberturasReq;
import com.gnp.autos.wsp.negocio.cotizacion.model.req.ConductorReq;
import com.gnp.autos.wsp.negocio.cotizacion.model.req.ContratanteReq;
import com.gnp.autos.wsp.negocio.cotizacion.model.req.DescuentoReq;
import com.gnp.autos.wsp.negocio.cotizacion.model.req.DescuentosReq;
import com.gnp.autos.wsp.negocio.cotizacion.model.req.ElementosReq;
import com.gnp.autos.wsp.negocio.cotizacion.model.req.PaqueteReq;
import com.gnp.autos.wsp.negocio.cotizacion.model.req.PaquetesReq;
import com.gnp.autos.wsp.negocio.cotizacion.model.req.SolicitudReq;
import com.gnp.autos.wsp.negocio.cotizacion.model.req.TraductorReq;
import com.gnp.autos.wsp.negocio.cotizacion.model.req.VehiculoReq;
import com.gnp.autos.wsp.negocio.cotizacion.model.resp.TraductorResp;
import com.gnp.autos.wsp.negocio.neg.model.AdaptacionVehNeg;
import com.gnp.autos.wsp.negocio.neg.model.CoberturaNeg;
import com.gnp.autos.wsp.negocio.neg.model.DescuentoNeg;
import com.gnp.autos.wsp.negocio.neg.model.ElementoNeg;
import com.gnp.autos.wsp.negocio.neg.model.ElementoReq;
import com.gnp.autos.wsp.negocio.neg.model.EmiteNegReq;
import com.gnp.autos.wsp.negocio.neg.model.PaqueteNeg;
import com.gnp.autos.wsp.negocio.neg.model.PersonaNeg;

import lombok.Data;

/**
 * The Class CotizacionClient.
 */
@Data
public class CotizacionClient {
    /** The Constant CONTRATANTE. */
    private static final String CONTRATANTE = "CONTRATANTE";
    
    /** The Constant CONDUCTOR. */
    private static final String CONDUCTOR = "CONDUCTOR";
    
    /** The Constant CVE_TIPO_NOMINA. */
    private static final String CVE_TIPO_NOMINA = "CVE_TIPO_NOMINA";
    
    /** The Constant UBICACION_TRABAJO. */
    private static final String UBICACION_TRABAJO = "UBICACION_TRABAJO";
    
    /** The rest template. */
    private RestTemplate restTemplate;
    
    /**
     * Instantiates a new cotizacion client.
     *
     * @param rest the rest
     */
    public CotizacionClient(final RestTemplate rest) {
        restTemplate = rest;
    }
    
    /**
     * Gets the recotiza.
     *
     * @param objReq the obj req
     * @param paramUrl the param url
     * @param tid the tid
     * @return the recotiza
     */
    public TraductorResp getRecotiza(final EmiteNegReq objReq, final String paramUrl, final Integer tid) {
        TraductorResp objResp = null;
        TraductorReq objCot = getRequestCot(objReq);
        try {
            ResponseEntity<TraductorResp> response;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_XML);
            HttpEntity<?> requestEntity = new HttpEntity<>(objCot, headers);
            if (tid != null) {
                response = restTemplate.postForEntity(paramUrl + "?tid" + tid.toString(),
                        requestEntity, TraductorResp.class);
            } else {
                response = restTemplate.postForEntity(paramUrl, requestEntity, TraductorResp.class);
            }
            objResp = response.getBody();
        } catch (HttpClientErrorException re) {
            Logger.getRootLogger().info(re);
            if (re.getStatusCode() == HttpStatus.BAD_REQUEST) {
                String msgError = "Error en recotizacion:";
                manejaErrorBAD(msgError, re);
            } else {
                throw new ExecutionError(ERROR_3, "Error en recotizacion -- url:" + paramUrl + "; error: "
                            + re.getMessage());
            }
        } catch (Exception ex) {
            Logger.getRootLogger().info(ex);
            throw new ExecutionError(ERROR_3, ex.getMessage());
        }
        return objResp;
    }
    
    /**
     * Maneja error BAD.
     *
     * @param msgError the msg error
     * @param re the re
     */
    protected void manejaErrorBAD(final String msgError, final HttpClientErrorException re) {
        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(ErrorXML.class);
            ErrorXML objErr = (ErrorXML) jaxbContext.createUnmarshaller()
                    .unmarshal(new StringReader(re.getResponseBodyAsString()));
            throw new ExecutionError(ERROR_3, msgError + objErr.getError());
        } catch (JAXBException e) {
            Logger.getRootLogger().info(e);
            throw new ExecutionError(ERROR_3, msgError + e.getMessage());
        }
    }
    
    /**
     * Gets the request cot.
     *
     * @param objReq the obj req
     * @return the request cot
     */
    private TraductorReq getRequestCot(final EmiteNegReq objReq) {
        TraductorReq objCot = new TraductorReq();
        SolicitudReq solicitud = new SolicitudReq();
        solicitud.setIdCotizacion(objReq.getIdCotizacion());
        solicitud.setUsuario(objReq.getUsuario());
        solicitud.setPassword(objReq.getPassword());
        solicitud.setIdUMO(objReq.getIdUMO());
        solicitud.setIniVig(objReq.getIniVig());
        solicitud.setFinVig(objReq.getFinVig());
        solicitud.setViaPago(objReq.getViaPago());
        solicitud.setPeriodicidad(objReq.getPeriodicidad());
        elementosAdicionales(objReq, solicitud);
        objCot.setSolicitud(solicitud);
        objCot.setVehiculo(getVehReq(objReq));
        setPersona(objReq, objCot);
        if (objReq.getPaquetes() == null || objReq.getPaquetes().isEmpty()) {
            throw new ExecutionError(ERROR_3, "Se debe informar un paquete");
        }
        if (objReq.getPaquetes().size() != 1) {
            throw new ExecutionError(ERROR_3, "Se debe informar solo un paquete");
        }
        List<PaqueteReq> listPaquetes = objReq.getPaquetes().stream().map(this::getPaqueteReq)
                .collect(Collectors.toCollection(ArrayList::new));
        PaquetesReq paquetes = new PaquetesReq();
        paquetes.setPaquetes(listPaquetes);
        objCot.setPaquetes(paquetes);
        objCot.setDescuentos(getDescReq(objReq));
        return objCot;
    }
    
    /**
     * Elementos adicionales.
     *
     * @param objReq the obj req
     * @param solicitud the solicitud
     */
    private void elementosAdicionales(final EmiteNegReq objReq, final SolicitudReq solicitud) {
        ElementosReq elementosReq = new ElementosReq();
        List<ElementoReq> lstElementos = new ArrayList<>();
        if (objReq.getCodigoPromocion() != null && !objReq.getCodigoPromocion().isEmpty()) {
            ElementoReq elemt = new ElementoReq();
            elemt.setClave(objReq.getCodigoPromocion());
            elemt.setNombre("CODIGO_PROMOCION");
            elemt.setValor(objReq.getCodigoPromocion());
            lstElementos.add(elemt);
        }
        if (objReq.getElementos() != null) {
            Optional<ElementoNeg> opElemReq = objReq.getElementos().stream().filter(
                    p -> p.getNombre().equalsIgnoreCase(CVE_TIPO_NOMINA)).findFirst();
            if (opElemReq.isPresent()) {
                ElementoReq ele = new ElementoReq();
                ele.setClave(opElemReq.get().getClave());
                ele.setNombre(CVE_TIPO_NOMINA);
                ele.setValor(opElemReq.get().getValor());
                lstElementos.add(ele);
            }
            Optional<ElementoNeg> opElemUt = objReq.getElementos().stream().filter(
                    p -> p.getNombre().equalsIgnoreCase(UBICACION_TRABAJO)).findFirst();
            if (opElemUt.isPresent()) {
                ElementoReq elem = new ElementoReq();
                elem.setClave(opElemUt.get().getClave());
                elem.setNombre(UBICACION_TRABAJO);
                elem.setValor(opElemUt.get().getValor());
                lstElementos.add(elem);
            }
        }
        if (!lstElementos.isEmpty()) {
            elementosReq.setElementos(lstElementos);
            solicitud.setElementos(elementosReq);
        }
    }
    
    /**
     * Gets the veh req.
     *
     * @param objReq the obj req
     * @return the veh req
     */
    private static VehiculoReq getVehReq(final EmiteNegReq objReq) {
        VehiculoReq vehiculo = new VehiculoReq();
        vehiculo.setSubRamo(objReq.getVehiculo().getSubRamo());
        vehiculo.setTipoVehiculo(objReq.getVehiculo().getTipoVehiculo());
        vehiculo.setModelo(objReq.getVehiculo().getModelo());
        vehiculo.setArmadora(objReq.getVehiculo().getArmadora());
        vehiculo.setCarroceria(objReq.getVehiculo().getCarroceria());
        vehiculo.setVersion(objReq.getVehiculo().getVersion());
        vehiculo.setUso(objReq.getVehiculo().getUso());
        vehiculo.setFormaIndemnizacion(objReq.getVehiculo().getFormaIndemnizacion());
        vehiculo.setValorConvenido(objReq.getVehiculo().getValorConvenido());
        vehiculo.setValorVehiculo(objReq.getVehiculo().getValorVehiculo());
        vehiculo.setValorFactura(objReq.getVehiculo().getValorFactura());
        if (objReq.getVehiculo().getAdaptaciones() != null && !objReq.getVehiculo().getAdaptaciones().isEmpty()) {
            AdaptacionesVehReq adapsReq = new AdaptacionesVehReq();
            List<AdaptacionVehReq> adapConversiones = new ArrayList<>();
            for (AdaptacionVehNeg adapNeg : objReq.getVehiculo().getAdaptaciones()) {
                AdaptacionVehReq adapReq = new AdaptacionVehReq();
                adapReq.setBanEquip(adapNeg.getBanEquip());
                adapReq.setDescEquip(adapNeg.getDescEquip());
                adapReq.setFechaFactura(adapNeg.getFechaFactura());
                adapReq.setMontoFacturacion(adapNeg.getMontoFacturacion());
                adapReq.setMontoSA(adapNeg.getMontoSA());
                adapConversiones.add(adapReq);
            }
            adapsReq.setAdapConversiones(adapConversiones);
            vehiculo.setAdaptaciones(adapsReq);
        }
        return vehiculo;
    }
    
    /**
     * Gets the desc req.
     *
     * @param objReq the obj req
     * @return the desc req
     */
    private static DescuentosReq getDescReq(final EmiteNegReq objReq) {
        if (objReq.getDescuentos() == null || objReq.getDescuentos().isEmpty()) {
            return null;
        }
        DescuentosReq descuentos = new DescuentosReq();
        List<DescuentoReq> desc = new ArrayList<>();
        for (DescuentoNeg dn : objReq.getDescuentos()) {
            if (dn.getCveDescuento() != null && !dn.getCveDescuento().isEmpty()) {
                DescuentoReq dr = new DescuentoReq();
                dr.setCveDescuento(dn.getCveDescuento());
                dr.setValor(dn.getValor());
                dr.setUnidadMedida(dn.getUnidadMedida());
                dr.setBanRecargo(dn.getBanRecargo());
                desc.add(dr);
            }
        }
        descuentos.setDescuentos(desc);
        return descuentos;
    }
    
    /**
     * Gets the paquete req.
     *
     * @param paqNeg the paq neg
     * @return the paquete req
     */
    public PaqueteReq getPaqueteReq(final PaqueteNeg paqNeg) {
        PaqueteReq paquete = new PaqueteReq();
        paquete.setCvePaquete(paqNeg.getCvePaquete());
        paquete.setDescPaquete(paqNeg.getDescPaquete());
        if (paqNeg.getCoberturas() != null && !paqNeg.getCoberturas().isEmpty()) {
            List<CoberturaReq> listCob = paqNeg.getCoberturas().stream().map(this::getCoberturaReq)
                    .collect(Collectors.toCollection(ArrayList::new));
            CoberturasReq cobs = new CoberturasReq();
            cobs.setCoberturas(listCob);
            paquete.setCoberturas(cobs);
        }
        return paquete;
    }
    
    /**
     * Gets the cobertura req.
     *
     * @param cobNeg the cob neg
     * @return the cobertura req
     */
    public CoberturaReq getCoberturaReq(final CoberturaNeg cobNeg) {
        CoberturaReq cobReq = new CoberturaReq();
        cobReq.setCveCobertura(cobNeg.getCveCobertura());
        cobReq.setSa(cobNeg.getSa());
        cobReq.setDeducible(cobNeg.getDeducible());
        cobReq.setNombre(cobNeg.getNombre());
        cobReq.setAdapConversiones(cobNeg.getAdapConversiones());
        cobReq.setBanActualiza(cobNeg.getBanActualiza());
        cobReq.setTipoCobertura(cobNeg.getTipoCobertura());
        cobReq.setUnidadDeducible(cobNeg.getUdDed());
        cobReq.setUnidadSA(cobNeg.getUdSA());
        return cobReq;
    }
    
    /**
     * Sets the persona.
     *
     * @param objReq the obj req
     * @param objCot the obj cot
     */
    private static void setPersona(final EmiteNegReq objReq, final TraductorReq objCot) {
        ContratanteReq contratante = new ContratanteReq();
        ConductorReq conductor = new ConductorReq();
        for (PersonaNeg per : objReq.getPersonas()) {
            if (per.getTipo().equals(CONTRATANTE)) {
                contratante.setCp(per.getDomicilio().getCodigoPostal());
                contratante.setTipoPersona(per.getTipoPersona());
                conductor.setCp(per.getDomicilio().getCodigoPostal());
            }
            if (per.getTipo().equals(CONDUCTOR)) {
                conductor.setFecNacimiento(per.getFecNacimiento());
                conductor.setSexo(per.getSexo());
                conductor.setEdad(per.getEdad());
            }
        }
        objCot.setContratante(contratante);
        objCot.setConductor(conductor);
    }
}