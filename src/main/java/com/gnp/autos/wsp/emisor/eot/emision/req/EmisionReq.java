package com.gnp.autos.wsp.emisor.eot.emision.req;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import com.gnp.autos.wsp.emisor.eot.emision.wsdl.AgentePolizaDto;
import com.gnp.autos.wsp.emisor.eot.emision.wsdl.BeneficiarioPolizaDto;
import com.gnp.autos.wsp.emisor.eot.emision.wsdl.CatConductoPagoDto;
import com.gnp.autos.wsp.emisor.eot.emision.wsdl.CatFormaPagoDto;
import com.gnp.autos.wsp.emisor.eot.emision.wsdl.CatMonedaDto;
import com.gnp.autos.wsp.emisor.eot.emision.wsdl.CatMotivoMovimientoPolizaDto;
import com.gnp.autos.wsp.emisor.eot.emision.wsdl.CatMovimientoPolizaDto;
import com.gnp.autos.wsp.emisor.eot.emision.wsdl.CoberturaObjetoAseguradoDto;
import com.gnp.autos.wsp.emisor.eot.emision.wsdl.ConductorHabitualPolizaDto;
import com.gnp.autos.wsp.emisor.eot.emision.wsdl.ContratantePolizaDto;
import com.gnp.autos.wsp.emisor.eot.emision.wsdl.DireccionPropPolizaDto;
import com.gnp.autos.wsp.emisor.eot.emision.wsdl.EmisionPolizaAutos;
import com.gnp.autos.wsp.emisor.eot.emision.wsdl.MedioContactoPropPolizaDto;
import com.gnp.autos.wsp.emisor.eot.emision.wsdl.ObjectFactory;
import com.gnp.autos.wsp.emisor.eot.emision.wsdl.ObjetoAseguradoDto;
import com.gnp.autos.wsp.emisor.eot.emision.wsdl.PagadorPolizaDto;
import com.gnp.autos.wsp.emisor.eot.emision.wsdl.ParticipanteAsociadoDto;
import com.gnp.autos.wsp.emisor.eot.emision.wsdl.Poliza;
import com.gnp.autos.wsp.emisor.eot.emision.wsdl.ProductoDto;
import com.gnp.autos.wsp.emisor.eot.emision.wsdl.RegistrarEmision;
import com.gnp.autos.wsp.emisor.eot.emision.wsdl.RegistrarEmisionRequest;
import com.gnp.autos.wsp.emisor.eot.error.ExecutionError;
import com.gnp.autos.wsp.emisor.eot.util.Utileria;
import com.gnp.autos.wsp.negocio.neg.model.AgenteNeg;
import com.gnp.autos.wsp.negocio.neg.model.CoberturaNeg;
import com.gnp.autos.wsp.negocio.neg.model.EmiteNegReq;
import com.gnp.autos.wsp.negocio.neg.model.PaqueteNeg;
import com.gnp.autos.wsp.negocio.neg.model.PersonaNeg;
import com.gnp.autos.wsp.negocio.neg.model.VehiculoNeg;

/**
 * The Class EmisionReq.
 */
public class EmisionReq {
    /** The Constant STRCONDUCTOR. */
    private static final String STRCONDUCTOR = "CONDUCTOR";
    
    /** The Constant STRCONTRATANTE. */
    private static final String STRCONTRATANTE = "CONTRATANTE";
    
    /** The Constant STRBENEFICIARIO. */
    private static final String STRBENEFICIARIO = "BENEFICIARIO";
    
    /** The Constant STRCOD1. */
    private static final String STRCOD1 = "MRAMIR988358";
    
    /** The Constant STRCOD2. */
    private static final String STRCOD2 = "0018499321";
    
    /** The obj F. */
    private ObjectFactory objF;
    
    /**
     * Instantiates a new emision req.
     */
    public EmisionReq() {
        objF = new ObjectFactory();
    }
    
    /**
     * Gets the emision.
     *
     * @param emisionNeg the emision neg
     * @return the emision
     */
    public RegistrarEmisionRequest getEmision(final EmiteNegReq emisionNeg) {
        RegistrarEmisionRequest emision = new RegistrarEmisionRequest();
        emision.setData(obtenerdata(emisionNeg));
        return emision;
    }
    
    /**
     * Obtenerdata.
     *
     * @param emisionNeg the emision neg
     * @return the registrar emision
     */
    private RegistrarEmision obtenerdata(final EmiteNegReq emisionNeg) {
        RegistrarEmision regEmitir = new RegistrarEmision();
        regEmitir.setEmisionPolizaAutos(obtenDatosPoliza(emisionNeg));
        return regEmitir;
    }
    
    /**
     * Obten datos poliza.
     *
     * @param emisionNeg the emision neg
     * @return the emision poliza autos
     */
    private EmisionPolizaAutos obtenDatosPoliza(final EmiteNegReq emisionNeg) {
        EmisionPolizaAutos poliza = new EmisionPolizaAutos();
        poliza.getAgentePoliza().addAll(obtenerAgentes(emisionNeg.getAgentes()));
        poliza.getCoberturaPoliza().addAll(obtenerCoberturas(emisionNeg.getPaquetes()));
        obtenerPersonas(poliza, emisionNeg.getPersonas());
        poliza.setObjetoAsegurado(obtenerObjAsegurado(emisionNeg.getVehiculo()));
        poliza.getPropositoDireccionPoliza().addAll(obtenerDireccionPol());
        poliza.getPropositoMedioContactoPoliza().addAll(obtenerMedioConPol());
        poliza.setPagadorPoliza(obtenerPagadorPol());
        poliza.setPoliza(obtenerPoliza(emisionNeg));
        poliza.setIdActor("ZRCH002");
        poliza.getParticipanteAsociado().add(obtenerParticipante());
        return poliza;
    }
    
    /**
     * Obtener participante.
     *
     * @return the participante asociado dto
     */
    private static ParticipanteAsociadoDto obtenerParticipante() {
        return null;
    }
    
    /**
     * Obtener agentes.
     *
     * @param agentesNeg the agentes neg
     * @return the list
     */
    private List<AgentePolizaDto> obtenerAgentes(final List<AgenteNeg> agentesNeg) {
        List<AgentePolizaDto> agentes = new ArrayList<>();
        for (AgenteNeg agente : agentesNeg) {
            AgentePolizaDto agenteJ = new AgentePolizaDto();
            agenteJ.setOperacion(objF.createTipoOperacionDtoOperacion("AL"));
            boolean principal = "1".equals(agente.getBanIntermediarioPrincipal()) ? true : false;
            agenteJ.setBanAgentePrincipal(objF.createAgentePolizaDtoBanAgentePrincipal(principal));
            agenteJ.setCodIntermediario(objF.createAgentePolizaDtoCodIntermediario(agente.getCodIntermediario()));
            agenteJ.setCveFuncion(objF.createAgentePolizaDtoCveFuncion(agente.getCveTipoFuncion()));
            agenteJ.setFolioPoliza(objF.createAgentePolizaDtoFolioPoliza("P0014937"));
            agenteJ.setIdAgente(objF.createAgentePolizaDtoIdAgente(agente.getIdParticipante()));
            agenteJ.setPctCesionComision(objF.createAgentePolizaDtoPctCesionComision(
                    new BigDecimal(agente.getPctCesionComision())));
            agenteJ.setPctParticipComision(objF.createAgentePolizaDtoPctParticipComision(
                    new BigDecimal(agente.getPctParticipComision())));
            agenteJ.setPctParticipPoliza(objF.createAgentePolizaDtoPctParticipPoliza(
                    new BigDecimal(agente.getPctComisionPrima())));
            agentes.add(agenteJ);
        }
        return agentes;
    }
    
    /**
     * Obtener coberturas.
     *
     * @param paquetes the paquetes
     * @return the list
     */
    private List<CoberturaObjetoAseguradoDto> obtenerCoberturas(final List<PaqueteNeg> paquetes) {
        List<CoberturaObjetoAseguradoDto> coberturas = new ArrayList<>();
        for (PaqueteNeg paquete : paquetes) {
            for (CoberturaNeg cobertura : paquete.getCoberturas()) {
                coberturas.add(obtenerCobertura(cobertura));
            }
        }
        return coberturas;
    }
    
    /**
     * Obtener cobertura.
     *
     * @param coberturaNeg the cobertura neg
     * @return the cobertura objeto asegurado dto
     */
    private CoberturaObjetoAseguradoDto obtenerCobertura(final CoberturaNeg coberturaNeg) {
        CoberturaObjetoAseguradoDto cobertura = new CoberturaObjetoAseguradoDto();
        cobertura.setOperacion(objF.createTipoOperacionDtoOperacion("AL"));
        cobertura.setCveCobertura(objF.createCoberturaObjetoAseguradoDtoCveCobertura(coberturaNeg.getCveCobertura()));
        cobertura.setCveMoneda(objF.createCoberturaObjetoAseguradoDtoCveMoneda("MXN"));
        cobertura.setMtoSumaAsegurada(objF.createCoberturaObjetoAseguradoDtoMtoSumaAsegurada(
                new BigDecimal(coberturaNeg.getSa())));
        return cobertura;
    }
    
    /**
     * Obtener personas.
     *
     * @param poliza the poliza
     * @param personas the personas
     * @return the emision poliza autos
     */
    private EmisionPolizaAutos obtenerPersonas(final EmisionPolizaAutos poliza, final List<PersonaNeg> personas) {
        for (PersonaNeg persona : personas) {
            switch (persona.getTipo()) {
            case STRBENEFICIARIO :
                poliza.getBeneficiarioPoliza().add(obtenerBeneficiario(persona));
                break;
            case STRCONDUCTOR :
                poliza.getConductorHabitual().add(obtenerConductor(persona));
                break;
            case STRCONTRATANTE :
                poliza.setContratantePoliza(obtenerContratante(persona));
                break;
            default :
                throw new ExecutionError(2, "tipo persona invalido");
            }
        }
        return poliza;
    }
    
    /**
     * Obtener beneficiario.
     *
     * @param persona the persona
     * @return the beneficiario poliza dto
     */
    private BeneficiarioPolizaDto obtenerBeneficiario(final PersonaNeg persona) {
        BeneficiarioPolizaDto beneficiario = new BeneficiarioPolizaDto();
        beneficiario.setOperacion(objF.createTipoOperacionDtoOperacion("AL"));
        beneficiario.setCveClienteOrigen(objF.createBeneficiarioPolizaDtoCveClienteOrigen(
                persona.getCveClienteOrigen()));
        beneficiario.setIdParticipante(objF.createBeneficiarioPolizaDtoIdParticipante(persona.getIdParticipante()));
        beneficiario.setPctBeneficio(objF.createBeneficiarioPolizaDtoPctBeneficio(
                new BigDecimal(persona.getPctBeneficio())));
        return beneficiario;
    }
    
    /**
     * Obtener conductor.
     *
     * @param persona the persona
     * @return the conductor habitual poliza dto
     */
    private ConductorHabitualPolizaDto obtenerConductor(final PersonaNeg persona) {
        // conductor.setNumPoliza(); y version checar>>
        ConductorHabitualPolizaDto conductor = new ConductorHabitualPolizaDto();
        conductor.setOperacion(objF.createTipoOperacionDtoOperacion("AL"));
        conductor.setCveClienteOrigen(objF.createConductorHabitualPolizaDtoCveClienteOrigen(
                persona.getCveClienteOrigen()));
        conductor.setCveSistema(objF.createConductorHabitualPolizaDtoCveSistema("INFO"));
        conductor.setIdParticipante(objF.createConductorHabitualPolizaDtoIdParticipante(persona.getIdParticipante()));
        conductor.setNumPoliza(objF.createConductorHabitualPolizaDtoNumPoliza("00000374962520"));
        conductor.setNumVersion(objF.createConductorHabitualPolizaDtoNumVersion("0"));
        return conductor;
    }
    
    /**
     * Obtener contratante.
     *
     * @param persona the persona
     * @return the contratante poliza dto
     */
    private ContratantePolizaDto obtenerContratante(final PersonaNeg persona) {
        ContratantePolizaDto contratante = new ContratantePolizaDto();
        contratante.setOperacion(objF.createTipoOperacionDtoOperacion("AL"));
        contratante.setCveClienteOrigen(objF.createContratantePolizaDtoCveClienteOrigen(persona.getCveClienteOrigen()));
        contratante.setIdParticipante(objF.createContratantePolizaDtoIdParticipante(persona.getIdParticipante()));
        return contratante;
    }
    
    /**
     * Obtener obj asegurado.
     *
     * @param vehiculo the vehiculo
     * @return the objeto asegurado dto
     */
    private ObjetoAseguradoDto obtenerObjAsegurado(final VehiculoNeg vehiculo) {
        ObjetoAseguradoDto objAsegurado = new ObjetoAseguradoDto();
        objAsegurado.setOperacion(objF.createTipoOperacionDtoOperacion("AL"));
        objAsegurado.setCveArmadoraVehiculo(objF.createObjetoAseguradoDtoCveArmadoraVehiculo(vehiculo.getArmadora()));
        objAsegurado.setCveCarroceriaVehiculo(objF.createObjetoAseguradoDtoCveCarroceriaVehiculo(
                vehiculo.getCarroceria()));
        objAsegurado.setCveSubramoAuto(vehiculo.getSubRamo());
        objAsegurado.setCveTipoVehiculo(objF.createObjetoAseguradoDtoCveTipoVehiculo(vehiculo.getTipoVehiculo()));
        objAsegurado.setCveUsoVehiculo(vehiculo.getUso());
        objAsegurado.setCveVersionVehiculo(objF.createObjetoAseguradoDtoCveVersionVehiculo("00"));
        objAsegurado.setIdObjetoSistemaOrigen(objF.createObjetoAseguradoDtoIdObjetoSistemaOrigen("1"));
        objAsegurado.setMarcaVehiculo(objF.createObjetoAseguradoDtoMarcaVehiculo("AUTO ANTIGUO"));
        objAsegurado.setModeloVehiculo(objF.createObjetoAseguradoDtoModeloVehiculo(
                new BigInteger(vehiculo.getModelo())));
        objAsegurado.setNumeroMotor(objF.createObjetoAseguradoDtoNumeroMotor(vehiculo.getMotor()));
        objAsegurado.setNumeroPlaca(objF.createObjetoAseguradoDtoNumeroPlaca(vehiculo.getPlacas()));
        objAsegurado.setNumeroSerie(objF.createObjetoAseguradoDtoNumeroSerie(vehiculo.getSerie()));
        objAsegurado.setValorFactura(objF.createObjetoAseguradoDtoValorFactura(
                new BigDecimal(vehiculo.getValorFactura())));
        return objAsegurado;
    }
    
    /**
     * Obtener direccion pol.
     *
     * @return the list
     */
    private List<DireccionPropPolizaDto> obtenerDireccionPol() {
        List<DireccionPropPolizaDto> direcciones = new ArrayList<>();
        DireccionPropPolizaDto direccion = new DireccionPropPolizaDto();
        direccion.setOperacion(objF.createTipoOperacionDtoOperacion("AL"));
        direccion.setCveClienteOrigen(objF.createDireccionPropPolizaDtoCveClienteOrigen(STRCOD2));
        direccion.setCvePropositoContacto(objF.createDireccionPropPolizaDtoCvePropositoContacto("CTA"));
        direccion.setCveReferenciaDir(objF.createDireccionPropPolizaDtoCveReferenciaDir("EMS"));
        direccion.setIdDireccion(objF.createDireccionPropPolizaDtoIdDireccion(new Long("1")));
        direccion.setValorCveOrigen(objF.createDireccionPropPolizaDtoValorCveOrigen("001"));
        direccion.setIdParticipante(objF.createDireccionPropPolizaDtoIdParticipante(STRCOD1));
        direcciones.add(direccion);
        return direcciones;
    }
    
    /**
     * Obtener medio con pol.
     *
     * @return the list
     */
    private List<MedioContactoPropPolizaDto> obtenerMedioConPol() {
        List<MedioContactoPropPolizaDto> mediosCont = new ArrayList<>();
        MedioContactoPropPolizaDto medioCont = new MedioContactoPropPolizaDto();
        medioCont.setOperacion(objF.createTipoOperacionDtoOperacion("AL"));
        medioCont.setCveClienteOrigen(objF.createMedioContactoPropPolizaDtoCveClienteOrigen(STRCOD2));
        medioCont.setCvePropositoContacto(objF.createMedioContactoPropPolizaDtoCvePropositoContacto("NOT"));
        medioCont.setIdMedioContacto(objF.createMedioContactoPropPolizaDtoIdMedioContacto(new Long("1")));
        medioCont.setValorCveOrigen(objF.createMedioContactoPropPolizaDtoValorCveOrigen("003"));
        medioCont.setIdParticipante(objF.createMedioContactoPropPolizaDtoIdParticipante(STRCOD1));
        MedioContactoPropPolizaDto medioCont2 = new MedioContactoPropPolizaDto();
        medioCont2.setOperacion(objF.createTipoOperacionDtoOperacion("AL"));
        medioCont2.setCveClienteOrigen(objF.createMedioContactoPropPolizaDtoCveClienteOrigen(STRCOD2));
        medioCont2.setCvePropositoContacto(objF.createMedioContactoPropPolizaDtoCvePropositoContacto("NOT"));
        medioCont2.setIdMedioContacto(objF.createMedioContactoPropPolizaDtoIdMedioContacto(new Long("2")));
        medioCont2.setValorCveOrigen(objF.createMedioContactoPropPolizaDtoValorCveOrigen("002"));
        medioCont2.setIdParticipante(objF.createMedioContactoPropPolizaDtoIdParticipante(STRCOD1));
        mediosCont.add(medioCont);
        mediosCont.add(medioCont2);
        return mediosCont;
    }
    
    /**
     * Obtener pagador pol.
     *
     * @return the pagador poliza dto
     */
    private PagadorPolizaDto obtenerPagadorPol() {
        PagadorPolizaDto pagador = new PagadorPolizaDto();
        pagador.setOperacion(objF.createTipoOperacionDtoOperacion("AL"));
        pagador.setCveClienteOrigen(objF.createPagadorPolizaDtoCveClienteOrigen(STRCOD2));
        pagador.setIdParticipante(objF.createPagadorPolizaDtoIdParticipante(STRCOD1));
        return pagador;
    }
    
    /**
     * Obtener poliza.
     *
     * @param emisionNeg the emision neg
     * @return the poliza
     */
    private Poliza obtenerPoliza(final EmiteNegReq emisionNeg) {
        XMLGregorianCalendar fecha = Utileria.getXMLDateOfStr("2017-10-07T12:24:10.101088-05:00");
        Poliza poliza = new Poliza();
        poliza.setOperacion(objF.createTipoOperacionDtoOperacion("AL"));
        poliza.setAnoPoliza(objF.createPolizaAnoPoliza(new Short("2017")));
        poliza.setConductoPago(objF.createPolizaConductoPago(obtenerConductoP()));
        poliza.setEstPoliza(objF.createPolizaEstPoliza("A"));
        poliza.setFchEmision(objF.createPolizaFchEmision(fecha));
        poliza.setFchInicioPoliza(objF.createPolizaFchInicioPoliza(fecha));
        poliza.setFchVerFinal(objF.createPolizaFchVerFinal(fecha));
        poliza.setFchVerInicial(objF.createPolizaFchVerInicial(fecha));
        poliza.setFchVigenciaFinal(objF.createPolizaFchVigenciaFinal(fecha));
        poliza.setFchVigenciaInicial(objF.createPolizaFchVigenciaInicial(fecha));
        poliza.setFormaPago(objF.createPolizaFormaPago(obtenerFormaP()));
        poliza.setCveSistema(objF.createPolizaCveSistema("INFO"));
        poliza.setMoneda(objF.createPolizaMoneda(obtenerMoneda()));
        poliza.setMotivoMovimiento(objF.createPolizaMotivoMovimiento(obtenerMotMov()));
        poliza.setMovimientoPoliza(objF.createPolizaMovimientoPoliza(obtenerMovPol()));
        poliza.setMtoCesionComision(objF.createPolizaMtoCesionComision(new BigDecimal("0")));
        poliza.setMtoDerechoPoliza(objF.createPolizaMtoDerechoPoliza(new BigDecimal("390")));
        poliza.setMtoIva(objF.createPolizaMtoIva(new BigDecimal("601.19")));
        poliza.setMtoPrimaNeta(objF.createPolizaMtoPrimaNeta(new BigDecimal(emisionNeg.getPrimaNeta())));
        poliza.setMtoPrimaTotal(objF.createPolizaMtoPrimaTotal(new BigDecimal(emisionNeg.getPrimaTotal())));
        poliza.setMtoRecargoPagoFracc(objF.createPolizaMtoRecargoPagoFracc(new BigDecimal("258.89")));
        poliza.setNumCotizacion(objF.createPolizaNumCotizacion("41519"));
        poliza.setNumPoliza(objF.createPolizaNumPoliza("98000148"));
        poliza.setNumVersionUltEco(objF.createPolizaNumVersionUltEco(new Short("0")));
        poliza.setNumVersionUltSituacion(objF.createPolizaNumVersionUltSituacion(new Short("0")));
        poliza.setProducto(objF.createPolizaProducto(obtenerProductos()));
        poliza.setIdAgrupacionRecargo(1);
        poliza.setIdProductoNegocio("PRP0001229");
        poliza.setIdNegocioOperable("NOVENTADIR");
        poliza.setIdNegocioComercial("NCO0000000");
        poliza.setCvePeriodicidadCobro("T");
        poliza.setCveHerramienta("PGN");
        poliza.setCveFormaAjusteIrregular("PR");
        return poliza;
    }
    
    /**
     * Obtener conducto P.
     *
     * @return the cat conducto pago dto
     */
    private CatConductoPagoDto obtenerConductoP() {
        CatConductoPagoDto conducto = new CatConductoPagoDto();
        conducto.setCveConductoPago(objF.createCatConductoPagoDtoCveConductoPago("CL"));
        return conducto;
    }
    
    /**
     * Obtener forma P.
     *
     * @return the cat forma pago dto
     */
    private CatFormaPagoDto obtenerFormaP() {
        CatFormaPagoDto formaPago = new CatFormaPagoDto();
        formaPago.setCveFormaPago(objF.createCatFormaPagoDtoCveFormaPago("T"));
        return formaPago;
    }
    
    /**
     * Obtener moneda.
     *
     * @return the cat moneda dto
     */
    private CatMonedaDto obtenerMoneda() {
        CatMonedaDto moneda = new CatMonedaDto();
        moneda.setCveMoneda(objF.createCatMonedaDtoCveMoneda("MXN"));
        return moneda;
    }
    
    /**
     * Obtener mot mov.
     *
     * @return the cat motivo movimiento poliza dto
     */
    private CatMotivoMovimientoPolizaDto obtenerMotMov() {
        CatMotivoMovimientoPolizaDto movimiento = new CatMotivoMovimientoPolizaDto();
        movimiento.setCveMotivoMovimiento(objF.createCatMotivoMovimientoPolizaDtoCveMotivoMovimiento("29002"));
        return movimiento;
    }
    
    /**
     * Obtener mov pol.
     *
     * @return the cat movimiento poliza dto
     */
    private CatMovimientoPolizaDto obtenerMovPol() {
        CatMovimientoPolizaDto movPoliza = new CatMovimientoPolizaDto();
        movPoliza.setCveMovimientoPoliza(objF.createCatMovimientoPolizaDtoCveMovimientoPoliza("29"));
        return movPoliza;
    }
    
    /**
     * Obtener productos.
     *
     * @return the producto dto
     */
    private ProductoDto obtenerProductos() {
        ProductoDto producto = new ProductoDto();
        producto.setCveProdComercial(objF.createProductoDtoCveProdComercial("00001"));
        producto.setCveProdTecnico(objF.createProductoDtoCveProdTecnico("A2400LNEVO"));
        return producto;
    }
}
