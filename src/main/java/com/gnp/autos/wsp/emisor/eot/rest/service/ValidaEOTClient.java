package com.gnp.autos.wsp.emisor.eot.rest.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.gnp.autos.wsp.emisor.eot.error.ExecutionError;
import com.gnp.autos.wsp.emisor.eot.util.Constantes;
import com.gnp.autos.wsp.emisor.eot.util.Utileria;
import com.gnp.autos.wsp.emisor.eot.validareglas.AgenteValNeg;
import com.gnp.autos.wsp.emisor.eot.validareglas.CoberturaValNeg;
import com.gnp.autos.wsp.emisor.eot.validareglas.ConvenioValNeg;
import com.gnp.autos.wsp.emisor.eot.validareglas.DatosProductoValNeg;
import com.gnp.autos.wsp.emisor.eot.validareglas.DescuentoValNeg;
import com.gnp.autos.wsp.emisor.eot.validareglas.DomicilioValNeg;
import com.gnp.autos.wsp.emisor.eot.validareglas.EmiteNegValReq;
import com.gnp.autos.wsp.emisor.eot.validareglas.PersonaValNeg;
import com.gnp.autos.wsp.emisor.eot.validareglas.ResultadoValidaResp;
import com.gnp.autos.wsp.emisor.eot.validareglas.VehiculoValNeg;
import com.gnp.autos.wsp.errors.ClienteSafeConsumer;
import com.gnp.autos.wsp.errors.exceptions.WSPSimpleException;
import com.gnp.autos.wsp.negocio.muc.soap.CoberturaDto;
import com.gnp.autos.wsp.negocio.muc.soap.ModificadorCoberturaDto;
import com.gnp.autos.wsp.negocio.neg.model.AgenteNeg;
import com.gnp.autos.wsp.negocio.neg.model.CoberturaNeg;
import com.gnp.autos.wsp.negocio.neg.model.DescuentoNeg;
import com.gnp.autos.wsp.negocio.neg.model.EmiteNegReq;
import com.gnp.autos.wsp.negocio.neg.model.PersonaNeg;
import com.gnp.autos.wsp.negocio.neg.model.VehiculoNeg;
import com.gnp.autos.wsp.negocio.tarificadordatos.soap.ConvenioEspecialDto;
import com.gnp.autos.wsp.negocio.umoservice.model.Normativa;
import com.gnp.autos.wsp.negocio.umoservicemodel.EmisionDatos;

import lombok.Data;

/**
 * The Class ValidaEOTClient.
 */
@Data
public class ValidaEOTClient {
    /** The Constant STRCPASEGUR. */
    private static final String STRCPASEGUR = "CPASEGUR";
    
    /** The Constant STRCDDEDUCI. */
    private static final String STRCDDEDUCI = "CDDEDUCI";
    
    /** The obj req val. */
    private EmiteNegValReq objReqVal;
    
    /** The rest template. */
    private RestTemplate restTemplate;
    
    /**
     * Instantiates a new valida EOT client.
     *
     * @param emDatos the em datos
     * @param rest the rest
     */
    public ValidaEOTClient(final EmisionDatos emDatos, final RestTemplate rest) {
        restTemplate = rest;
        EmiteNegReq objReq = emDatos.getEmite();
        objReqVal = new EmiteNegValReq();
        objReqVal.setIdCotizacion(objReq.getIdCotizacion());
        objReqVal.setCanalCobro(objReq.getViaPago());
        objReqVal.setCanalCobroSub(objReq.getViaPagoSucesivos());
        objReqVal.setCodigoPromocion(objReq.getCodigoPromocion());
        objReqVal.setAgentes(getAgentesVal(objReq.getAgentes()));
        DatosProductoValNeg datosProducto = new DatosProductoValNeg();
        datosProducto.setIdproducto(objReq.getPaquetes().get(0).getCvePaquete());
        datosProducto.setCoberturas(getCoberturasValNeg(objReq.getMucPrimaAutoReq().getDATOSCOTIZACION().get(0)
                .getDATOSPRODUCTO().get(0).getCOBERTURA()));
        objReqVal.setDatosProducto(datosProducto);
        objReqVal.setFchCotizacion(Utileria.getStrNow());
        objReqVal.setFchExpedicion(Utileria.getStrNow());
        objReqVal.setIniVig(objReq.getIniVig());
        objReqVal.setFinVig(objReq.getFinVig());
        objReqVal.setFormaPago(objReq.getFormaPago());
        objReqVal.setIdModeloNegocio(emDatos.getUmo().getNegocio().getModeloNegocio().getId());
        objReqVal.setIdNegocioComercial(String.valueOf(emDatos.getUmo().getNegocio().getEstructura()
                .getNegocioComercial().getClave()));
        objReqVal.setIdNegocioOperable(objReq.getIdUMO());
        objReqVal.setDescuentos(getDescuentoVal(objReq.getDescuentos()));
        objReqVal.setPersonas(getPersonasVal(objReq.getPersonas()));
        objReqVal.setPrimaNeta(objReq.getPrimaNeta());
        objReqVal.setVehiculo(getVehiculoVal(objReq.getVehiculo()));
        List<Normativa> normativas = emDatos.getUmo().getDominios().getNormatividades().getNormativas();
        if (normativas.size() > 0) {
            List<ConvenioValNeg> convenios = new ArrayList<>();
            for (int cnt = 0; cnt < normativas.size(); cnt++) {
                ConvenioValNeg obj = new ConvenioValNeg();
                obj.setValor(normativas.get(cnt).getValor());
                obj.setIdUniMedida(normativas.get(cnt).getUnidadMedida().getClave());
                obj.setDesMedida(normativas.get(cnt).getUnidadMedida().getClave());
                obj.setIdConvenio(normativas.get(cnt).getLeyenda().getClave());
                obj.setDescConvenio(normativas.get(cnt).getLeyenda().getNombre());
                convenios.add(obj);
            }
            objReqVal.setConvenios(convenios);
        }
        emDatos.setEmite(objReq);
    }
    
    /**
     * Gets the valida reglas.
     *
     * @param paramUrl the param url
     * @param tid the tid
     * @return the valida reglas
     */
    public ResultadoValidaResp getValidaReglas(final String paramUrl, final Integer tid) {
        String msgErr = "ValidaReglas --";
        ClienteSafeConsumer<EmiteNegValReq> safe = new ClienteSafeConsumer<>(restTemplate);
        ResultadoValidaResp lRes = null;
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
        try {
            if (tid != null) {
                lRes = safe.uncheckedConsume(getObjReqVal(), paramUrl + "?tid=" + tid.toString(), 
                        ResultadoValidaResp.class, MediaType.APPLICATION_JSON);
            } else {
                lRes = safe.uncheckedConsume(getObjReqVal(), paramUrl, ResultadoValidaResp.class,
                        MediaType.APPLICATION_JSON);
            }
        } catch (Exception e) {
            Logger.getRootLogger().info(e);
            msgErr += "url:" + paramUrl + "; error: " + e.getMessage();
            if (tid != null) {
                msgErr += "(" + tid.toString() + ")";
            }
            throw new ExecutionError(Constantes.ERROR_3, msgErr);
        }
        if (lRes != null) {
            return lRes;
        } else {
            throw new WSPSimpleException("", Constantes.ERROR_3, paramUrl);
        }
    }
    
    /**
     * Gets the convenios.
     *
     * @param convDto the conv dto
     * @return the convenios
     */
    public ConvenioValNeg getConvenios(final ConvenioEspecialDto convDto) {
        ConvenioValNeg convenio = new ConvenioValNeg();
        convenio.setDescConvenio(convDto.getCONVENIOESPECIAL());
        convenio.setDesMedida(convDto.getCVEUNIDADMEDIDA());
        convenio.setIdConvenio(convDto.getIDCONVENIOESPECIAL());
        convenio.setIdUniMedida(convDto.getCVEUNIDADMEDIDA());
        convenio.setValor(convDto.getVALOR().toString());
        return convenio;
    }
    
    /**
     * Gets the agentes val.
     *
     * @param agentesNeg the agentes neg
     * @return the agentes val
     */
    public List<AgenteValNeg> getAgentesVal(final List<AgenteNeg> agentesNeg) {
        List<AgenteValNeg> agentesVal = new ArrayList<>();
        if (agentesNeg == null || agentesNeg.isEmpty()) {
            return agentesVal;
        }
        for (AgenteNeg a : agentesNeg) {
            AgenteValNeg ageValNeg = new AgenteValNeg();
            ageValNeg.setBanIntermediarioPrincipal(a.getBanIntermediarioPrincipal());
            ageValNeg.setCodIntermediario(a.getCodIntermediario());
            ageValNeg.setFolio(a.getFolio());
            ageValNeg.setIdParticipante(a.getIdParticipante());
            agentesVal.add(ageValNeg);
        }
        return agentesVal;
    }
    
    /**
     * Gets the coberturas val.
     *
     * @param coberturasNeg the coberturas neg
     * @return the coberturas val
     */
    public List<CoberturaValNeg> getCoberturasVal(final List<CoberturaNeg> coberturasNeg) {
        List<CoberturaValNeg> coberturasVal = new ArrayList<>();
        if (coberturasNeg == null || coberturasNeg.isEmpty()) {
            return coberturasVal;
        }
        for (CoberturaNeg c : coberturasNeg) {
            CoberturaValNeg cobValNeg = new CoberturaValNeg();
            cobValNeg.setClavecobertura(c.getCveCobertura());
            cobValNeg.setMtoDeducible(c.getDeducible());
            cobValNeg.setMtoSumaAsegurada(c.getSa());
            coberturasVal.add(cobValNeg);
        }
        return coberturasVal;
    }
    
    /**
     * Gets the coberturas val neg.
     *
     * @param coberturasDto the coberturas dto
     * @return the coberturas val neg
     */
    public List<CoberturaValNeg> getCoberturasValNeg(final List<CoberturaDto> coberturasDto) {
        List<CoberturaValNeg> coberturasValNeg = new ArrayList<>();
        for (CoberturaDto c : coberturasDto) {
            coberturasValNeg.add(getCoberturaDto(c));
        }
        return coberturasValNeg;
    }
    
    /**
     * Gets the cobertura dto.
     *
     * @param coberturaDto the cobertura dto
     * @return the cobertura dto
     */
    public CoberturaValNeg getCoberturaDto(final CoberturaDto coberturaDto) {
        CoberturaValNeg cob = new CoberturaValNeg();
        cob.setClavecobertura(coberturaDto.getCLAVECOBERTURA());
        Optional<ModificadorCoberturaDto> sa = coberturaDto.getMODIFICADOR().stream().filter(
                p -> p.getCLAVEMODIFICADOR().equalsIgnoreCase(STRCPASEGUR)).findFirst();
        if (sa.isPresent()) {
            cob.setMtoSumaAsegurada(sa.get().getVALORREQUERIDO().toString());
        }
        Optional<ModificadorCoberturaDto> ded = coberturaDto.getMODIFICADOR().stream().filter(
                p -> p.getCLAVEMODIFICADOR().equalsIgnoreCase(STRCDDEDUCI)).findFirst();
        if (ded.isPresent()) {
            cob.setMtoDeducible(ded.get().getVALORREQUERIDO().toString());
        }
        return cob;
    }
    
    /**
     * Gets the descuento val.
     *
     * @param descuentosNeg the descuentos neg
     * @return the descuento val
     */
    public List<DescuentoValNeg> getDescuentoVal(final List<DescuentoNeg> descuentosNeg) {
        List<DescuentoValNeg> descuentosVal = new ArrayList<>();
        if (descuentosNeg == null || descuentosNeg.isEmpty()) {
            return descuentosVal;
        }
        for (DescuentoNeg d : descuentosNeg) {
            DescuentoValNeg descNeg = new DescuentoValNeg();
            descNeg.setBanRecargo(d.getBanRecargo());
            descNeg.setCveDescuento(d.getCveDescuento());
            descNeg.setDescripcion(d.getDescripcion());
            descNeg.setUnidadMedida(d.getUnidadMedida());
            descNeg.setValor(d.getValor());
            descuentosVal.add(descNeg);
        }
        return descuentosVal;
    }
    
    /**
     * Gets the personas val.
     *
     * @param personasNeg the personas neg
     * @return the personas val
     */
    public List<PersonaValNeg> getPersonasVal(final List<PersonaNeg> personasNeg) {
        List<PersonaValNeg> personasVal = null;
        if (personasNeg != null) {
            personasVal = new ArrayList<>();
            for (PersonaNeg p : personasNeg) {
                PersonaValNeg valNeg = new PersonaValNeg();
                valNeg.setBanBeneficiarioPreferente(p.getBanPreferente());
                if (p.getDomicilio() != null) {
                    DomicilioValNeg domicilio = new DomicilioValNeg();
                    domicilio.setCodigoPostal(p.getDomicilio().getCodigoPostal());
                    domicilio.setEstado(p.getDomicilio().getEstado());
                    domicilio.setMunicipio(p.getDomicilio().getMunicipio());
                    domicilio.setPais(p.getDomicilio().getPais());
                    valNeg.setDomicilio(domicilio);
                }
                valNeg.setFecConstitucion(p.getFecConstitucion());
                valNeg.setFecNacimiento(p.getFecNacimiento());
                valNeg.setIdParticipante(p.getIdParticipante());
                valNeg.setRfc(p.getRfc());
                valNeg.setTipo(p.getTipo());
                valNeg.setTipoPersona(p.getTipoPersona());
                personasVal.add(valNeg);
            }
        }
        return personasVal;
    }
    
    /**
     * Gets the vehiculo val.
     *
     * @param vehNeg the veh neg
     * @return the vehiculo val
     */
    public VehiculoValNeg getVehiculoVal(final VehiculoNeg vehNeg) {
        VehiculoValNeg vehVal = new VehiculoValNeg();
        vehVal.setArmadora(vehNeg.getArmadora());
        vehVal.setAltoRiesgo(vehNeg.getAltoRiesgo());
        vehVal.setCarroceria(vehNeg.getCarroceria());
        vehVal.setClaveMarca(vehNeg.getClaveMarca());
        vehVal.setEstadoCirculacion(vehNeg.getEstadoCirculacion());
        vehVal.setFormaIndemnizacion(vehNeg.getFormaIndemnizacion());
        vehVal.setModelo(vehNeg.getModelo());
        vehVal.setPlacas(vehNeg.getPlacas());
        vehVal.setSerie(vehNeg.getSerie());
        vehVal.setSubRamo(vehNeg.getSubRamo());
        vehVal.setTipoCarga(vehNeg.getTipoCarga());
        vehVal.setTipoValorVehiculo(vehNeg.getFormaIndemnizacion());
        vehVal.setTipoVehiculo(vehNeg.getTipoVehiculo());
        vehVal.setUso(vehNeg.getUso());
        vehVal.setValorVehiculo(vehNeg.getValorVehiculo());
        return vehVal;
    }
}