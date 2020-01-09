package com.gnp.autos.wsp.emisor.eot.domain.impl;

import static com.gnp.autos.wsp.emisor.eot.util.Constantes.ERROR_3;
import static com.gnp.autos.wsp.emisor.eot.util.Constantes.ERROR_4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.gnp.autos.wsp.emisor.eot.config.ConfWSP;
import com.gnp.autos.wsp.emisor.eot.domain.Domain;
import com.gnp.autos.wsp.emisor.eot.domain.EmisorDomain;
import com.gnp.autos.wsp.emisor.eot.domain.MovimientoDomain;
import com.gnp.autos.wsp.emisor.eot.domain.ProdTecComDomain;
import com.gnp.autos.wsp.emisor.eot.error.ExecutionError;
import com.gnp.autos.wsp.emisor.eot.pasos.PasosDomain;
import com.gnp.autos.wsp.emisor.eot.pasos.wsdl.ConsultaPasosConfiguradorResponse;
import com.gnp.autos.wsp.emisor.eot.pasos.wsdl.PasoElementoConsultaDto;
import com.gnp.autos.wsp.emisor.eot.rest.service.CatalogoClient;
import com.gnp.autos.wsp.emisor.eot.rest.service.CotizacionClient;
import com.gnp.autos.wsp.emisor.eot.rest.service.TransaccionesClient;
import com.gnp.autos.wsp.emisor.eot.util.Constantes;
import com.gnp.autos.wsp.emisor.eot.util.Utileria;
import com.gnp.autos.wsp.negocio.catalogo.model.CatalogoReq;
import com.gnp.autos.wsp.negocio.catalogo.model.CatalogoResp;
import com.gnp.autos.wsp.negocio.cotizacion.model.resp.CoberturaResp;
import com.gnp.autos.wsp.negocio.cotizacion.model.resp.ConceptoEconomicoResp;
import com.gnp.autos.wsp.negocio.cotizacion.model.resp.PaqueteResp;
import com.gnp.autos.wsp.negocio.cotizacion.model.resp.PaquetesResp;
import com.gnp.autos.wsp.negocio.cotizacion.model.resp.TotalPrimaResp;
import com.gnp.autos.wsp.negocio.cotizacion.model.resp.TotalesPrimaResp;
import com.gnp.autos.wsp.negocio.cotizacion.model.resp.TraductorResp;
import com.gnp.autos.wsp.negocio.emision.model.req.TraductorEmitirReq;
import com.gnp.autos.wsp.negocio.emision.model.resp.TraductorEmitirResp;
import com.gnp.autos.wsp.negocio.muc.soap.CotizacionDto;
import com.gnp.autos.wsp.negocio.muc.soap.ProductosDto;
import com.gnp.autos.wsp.negocio.neg.model.AdaptacionVehNeg;
import com.gnp.autos.wsp.negocio.neg.model.CoberturaNeg;
import com.gnp.autos.wsp.negocio.neg.model.ElementoReq;
import com.gnp.autos.wsp.negocio.neg.model.EmiteNegReq;
import com.gnp.autos.wsp.negocio.neg.model.MedioCobroNeg;
import com.gnp.autos.wsp.negocio.neg.model.PersonaNeg;
import com.gnp.autos.wsp.negocio.umoservice.model.Paquete;
import com.gnp.autos.wsp.negocio.umoservicemodel.EmisionDatos;
import com.gnp.autos.wsp.negocio.util.Utils;

/**
 * The Class EmisorDomainImpl.
 */
@Service
public class EmisorDomainImpl implements EmisorDomain {
    /** The Constant CIEN. */
    private static final int CIEN = 100;

    /** The Constant DIEZ. */
    private static final int DIEZ = 10;

    /** The Constant STRCVEHERRAMIENTA_WSP. */
    private static final String STRCVEHERRAMIENTA_WSP = "WSP";

    /** The Constant PERIODICIDAD. */
    private static final String PERIODICIDAD = "La periodicidad";

    /** The Constant STRCONDUCTOR. */
    private static final String STRCONDUCTOR = "CONDUCTOR";

    /** The Constant STRCONTRATANTE. */
    private static final String STRCONTRATANTE = "CONTRATANTE";

    /** The Constant STRBENEFICIARIO. */
    private static final String STRBENEFICIARIO = "BENEFICIARIO";

    /** The Constant STRPERIODICIDAD. */
    private static final String STRPERIODICIDAD = "PERIODICIDAD";

    /** The Constant STRMONEDA. */
    private static final String STRMONEDA = "MONEDA";

    /** The Constant STRVIAPAGO. */
    private static final String STRVIAPAGO = "VIA_PAGO";

    /** The Constant STRCODIGOPOSTAL. */
    private static final String STRCODIGOPOSTAL = "CODIGO_POSTAL";

    /** The Constant STRESTADO. */
    private static final String STRESTADO = "ESTADO_CIRCULACION";

    /** The Constant STRUNO. */
    private static final String STRUNO = "1";

    /** The Constant STRCERO. */
    private static final String STRCERO = "0";

    /** The Constant STRPREVIO. */
    private static final String STRPREVIO = "P";

    /** The Constant STREMISION. */
    private static final String STREMISION = "E";

    /** The Constant STRLAEDAD. */
    private static final String STRLAEDAD = "La edad";

    /** The Constant STRELSEXO. */
    private static final String STRELSEXO = "El sexo";

    /** The Constant STRELRFC. */
    private static final String STRELRFC = "El rfc";

    /** The Constant STRF. */
    private static final String STRF = "F";

    /** The Constant STRCOMPENSACION. */
    private static final String STRCOMPENSACION = "COM";

    /** The Constant STRBANCARIO. */
    private static final String STRBANCARIO = "BC";

    /** The domain EOT. */
    @Autowired
    private Domain domainEOT;

    /** The domain mov. */
    @Autowired
    private MovimientoDomain domainMov;

    /** The conf. */
    @Autowired
    private ConfWSP conf;

    /** The pasos client. */
    @Autowired
    private PasosDomain pasosClient;

    /** The prod TC client. */
    @Autowired
    private ProdTecComDomain prodTCClient;

    /** The rest template. */
    @Autowired
    private static RestTemplate restTemplate = new RestTemplate();

    /**
     * Gets the emitir.
     *
     * @param objReq the obj req
     * @param tid    the tid
     * @return the emitir
     */
    @Override
    public TraductorEmitirResp getEmitir(final TraductorEmitirReq objReq, final Integer tid) {
        Utileria.getRegistraLogTime("valida tid_" + tid + "    ");
        EmiteNegReq objReqNeg = valida(objReq.getEmiteNeg(), tid);
        Utileria.getRegistraLogTime("recotiza in");
        TraductorResp respCot;
        TransaccionesClient tc = new TransaccionesClient(restTemplate);
        if (isRecotiza(objReqNeg.getIdUMO(), objReq.getSolicitud().getCveHerramienta())) {
            CotizacionClient cot = new CotizacionClient(restTemplate);
            respCot = cot.getRecotiza(objReqNeg, conf.getUrlCotizacion(), tid);
            Utileria.getRegistraLogTime("recotiza out");
        } else {
            respCot = tc.getIdCotizacion(objReq.getSolicitud().getIdCotizacion(), conf.getUrlTransacciones());
            Utileria.getRegistraLogTime("no recotiza");
        }
        respCot = soloPaqueteSeleccion(respCot, objReq.getPaquete().getCvePaquete(),
                objReq.getSolicitud().getPeriodicidad());
        validaRecotiza(respCot, objReqNeg);
        objReq.getSolicitud().setIdCotizacion(respCot.getSolicitud().getIdCotizacion());
        objReqNeg.setIdCotizacion(respCot.getSolicitud().getIdCotizacion());
        Utileria.getRegistraLogTime("variables cot in");
        EmisionDatos emDatos = tc.getVariablesCot(objReqNeg, objReq.getSolicitud().getIdCotizacion(),
                conf.getUrlTransacciones());
        objReqNeg = emDatos.getEmite();
        objReqNeg = soloPaqueteSeleccionMuc(objReqNeg, objReq.getPaquete().getCvePaquete(),
                objReq.getSolicitud().getPeriodicidad());
        List<CoberturaNeg> coberturas = new ArrayList<>();
        for (int cnt = 0; cnt < respCot.getPaquetes().getPaquetes().get(0).getCoberturas().getCoberturas()
                .size(); cnt++) {
            CoberturaResp cresp = respCot.getPaquetes().getPaquetes().get(0).getCoberturas().getCoberturas().get(cnt);
            CoberturaNeg cob = new CoberturaNeg();
            cob.setCveCobertura(cresp.getCveCobertura());
            cob.setNombre(cresp.getNombre());
            if ("AMPARADA".equalsIgnoreCase(cresp.getSa())) {
                cob.setSa("999999.0");
            } else {
                cob.setSa(cresp.getSa());
            }
            cob.setDeducible(cresp.getDeducible());
            coberturas.add(cob);
        }
        objReqNeg.getPaquetes().get(0).setCoberturas(coberturas);
        Utileria.getRegistraLogTime("variables cot out");
        if (objReqNeg.getMucPrimaAutoReq().getDATOSCOTIZACION().isEmpty()) {
            throw new ExecutionError(ERROR_4, "Cotizacion MUC", "a");
        }
        objReqNeg.setIdVersionNegocio(
                objReqNeg.getMucPrimaAutoReq().getDATOSCOTIZACION().get(0).getDATOSSOLICITUD().getVERSIONNEGOCIO());
        Utileria.getRegistraLogTime("Mov in");
        objReqNeg = domainMov.getMovimientoNC(objReqNeg, tid);
        Utileria.getRegistraLogTime("Mov out");
        validaAgente(objReqNeg);
        ProductosDto dtProdDto = objReqNeg.getMucPrimaAutoResp().getPETICION().get(0).getDATOSPRODUCTOS().get(0);
        objReqNeg.setFchTarifa(dtProdDto.getFECHATARIFA().toString().substring(0, DIEZ).replaceAll("-", ""));
        objReqNeg.setCveTarifa(dtProdDto.getCLAVETARIFA().toString());
        objReqNeg.setVerZonificacion(String.valueOf(dtProdDto.getVERSIONZONIFICACION()));
        objReqNeg.setCveZonificacion(dtProdDto.getCLAVEZONIFICACION());
        boolean isPresent = false;
        for (int cnt = 0; cnt < respCot.getPaquetes().getPaquetes().size(); cnt++) {
            PaqueteResp paqueteR = respCot.getPaquetes().getPaquetes().get(cnt);
            if (paqueteR.getCvePaquete().equalsIgnoreCase(dtProdDto.getIDPRODUCTO())) {
                isPresent = true;
                break;
            }
        }
        if (!isPresent) {
            throw new ExecutionError(2, "No se cotiza sobre el producto indicado");
        }
        List<Paquete> listPaquetes = emDatos.getUmo().getDominios().getCombinaciones().get(0).getPaquetes();
        for (int cnt = 0; cnt < listPaquetes.size(); cnt++) {
            if (listPaquetes.get(cnt).getProductoPersonalizado().toUpperCase()
                    .equalsIgnoreCase(objReq.getPaquete().getCvePaquete().toUpperCase())) {
                objReqNeg.setCveTransformacionTarifa(
                        listPaquetes.get(cnt).getConfiguracion().getReglaTransformacion().getClave());
                objReqNeg.setFchTransformacionTarifa(listPaquetes.get(cnt).getConfiguracion().getReglaTransformacion()
                        .getFecha().substring(0, DIEZ).replaceAll("-", ""));
                break;
            }
        }
        objReqNeg.getVehiculo().setValorVehiculo(objReqNeg.getMucPrimaAutoReq().getDATOSCOTIZACION().get(0)
                .getDATOSPRODUCTO().get(0).getVALORCOMERCIAL());
        TraductorEmitirResp objResp = new TraductorEmitirResp();
        emDatos.setEmite(objReqNeg);
        objResp.getTraductor(domainEOT.getEmitir(emDatos, tid,
                prodTCClient.getProdTecCom(conf.getUrlTransacciones() + "/logintermedia", conf.getUrlProdTecCom(), tid,
                        objReq.getSolicitud().getIdUMO())));
        objResp.getSolicitud().setIdCotizacion(objReqNeg.getIdCotizacion());
        return objResp;
    }

    /**
     * Solo paquete seleccion.
     *
     * @param tradRes         the trad res
     * @param cvePaquete      the cve paquete
     * @param cvePeriodicidad the cve periodicidad
     * @return the traductor resp
     */
    public TraductorResp soloPaqueteSeleccion(final TraductorResp tradRes, final String cvePaquete,
            final String cvePeriodicidad) {
        TraductorResp seleccion = new TraductorResp();
        seleccion.setSolicitud(tradRes.getSolicitud());
        PaquetesResp paquetes = new PaquetesResp();
        List<PaqueteResp> listpaquetes = new ArrayList<>();
        for (int cnt = 0; cnt < tradRes.getPaquetes().getPaquetes().size(); cnt++) {
            if (cvePaquete.equalsIgnoreCase(tradRes.getPaquetes().getPaquetes().get(cnt).getCvePaquete())) {
                listpaquetes.add(tradRes.getPaquetes().getPaquetes().get(cnt));
                Utileria.getRegistraLogTime("coincide paquete");
                break;
            }
        }
        TotalesPrimaResp totales = new TotalesPrimaResp();
        List<TotalPrimaResp> listtotales = new ArrayList<>();
        for (int cnt = 0; cnt < listpaquetes.get(0).getTotales().getTotales().size(); cnt++) {
            if (cvePeriodicidad
                    .equalsIgnoreCase(listpaquetes.get(0).getTotales().getTotales().get(cnt).getCvePeriodicidad())) {
                listtotales.add(listpaquetes.get(0).getTotales().getTotales().get(cnt));
                Utileria.getRegistraLogTime("coincide periodicidad");
                break;
            }
        }
        totales.setTotales(listtotales);
        listpaquetes.get(0).setTotales(totales);
        paquetes.setPaquetes(listpaquetes);
        seleccion.setPaquetes(paquetes);
        return seleccion;
    }

    /**
     * Solo paquete seleccion muc.
     *
     * @param objReqNeg       the obj req neg
     * @param cvePaquete      the cve paquete
     * @param cvePeriodicidad the cve periodicidad
     * @return the emite neg req
     */
    public EmiteNegReq soloPaqueteSeleccionMuc(final EmiteNegReq objReqNeg, final String cvePaquete,
            final String cvePeriodicidad) {
        EmiteNegReq seleccion = objReqNeg;
        ProductosDto datosproductos = new ProductosDto();
        for (int cnt = 0; cnt < seleccion.getMucPrimaAutoResp().getPETICION().get(0).getDATOSPRODUCTOS()
                .size(); cnt++) {
            if (cvePaquete.equalsIgnoreCase(seleccion.getMucPrimaAutoResp().getPETICION().get(0).getDATOSPRODUCTOS()
                    .get(cnt).getIDPRODUCTO())) {
                datosproductos = seleccion.getMucPrimaAutoResp().getPETICION().get(0).getDATOSPRODUCTOS().get(cnt);
                Utileria.getRegistraLogTime("coincide paquete muc");
                break;
            }
        }
        seleccion.getMucPrimaAutoResp().getPETICION().get(0).getDATOSPRODUCTOS().add(0, datosproductos);
        CotizacionDto datoscotizacion = new CotizacionDto();
        for (int cnt = 0; cnt < datosproductos.getDATOSCOTIZACION().size(); cnt++) {
            if (cvePeriodicidad.equalsIgnoreCase(
                    datosproductos.getDATOSCOTIZACION().get(cnt).getFORMAPAGORECIBO().getCLAVEFORMAPAGO())) {
                datoscotizacion = datosproductos.getDATOSCOTIZACION().get(cnt);
                Utileria.getRegistraLogTime("coincide periodicidad muc");
                break;
            }
        }
        seleccion.getMucPrimaAutoResp().getPETICION().get(0).getDATOSPRODUCTOS().get(0).getDATOSCOTIZACION().add(0,
                datoscotizacion);
        return seleccion;
    }

    /**
     * Checks if is recotiza.
     *
     * @param strNegocio     the str negocio
     * @param strHerramienta the str herramienta
     * @return true, if is recotiza
     */
    protected boolean isRecotiza(final String strNegocio, final String strHerramienta) {
        if (conf.getCodPromoRecotiza() == null) {
            return true;
        }
        for (String codigo : conf.getCodPromoRecotiza().split(":")) {
            if (codigo.equalsIgnoreCase(strHerramienta) || codigo.equalsIgnoreCase(strNegocio)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Valida agente.
     *
     * @param objReqNeg the obj req neg
     */
    protected void validaAgente(final EmiteNegReq objReqNeg) {
        if (objReqNeg.getAgentes() == null || objReqNeg.getAgentes().isEmpty()) {
            throw new ExecutionError(ERROR_4, "El agente", "o");
        }
        objReqNeg.getAgentes().stream().forEach(p -> {
            double partCom = Double.parseDouble(p.getPctParticipComision());
            if (partCom <= 0 || partCom > CIEN) {
                throw new ExecutionError(ERROR_4, "El porcentaje de participación del agente", "a");
            }
        });
        Optional<Double> sumaAge = objReqNeg.getAgentes().stream()
                .filter(p -> p.getIdTipoBaseComision().equalsIgnoreCase(STRCOMPENSACION))
                .map(e -> Double.parseDouble(e.getPctParticipComision())).reduce(Double::sum);
        if (sumaAge.isPresent() && sumaAge.get() != CIEN) {
            throw new ExecutionError(ERROR_4, "La suma de participación del agente", "a");
        }
    }

    /**
     * Valida recotiza.
     *
     * @param respCot   the resp cot
     * @param objReqNeg the obj req neg
     */
    protected void validaRecotiza(final TraductorResp respCot, final EmiteNegReq objReqNeg) {
        // valida el monto para determinar si se emite
        List<ConceptoEconomicoResp> totalesCotResp = null;
        try {
            totalesCotResp = respCot.getPaquetes().getPaquetes().get(0).getTotales().getTotales().get(0)
                    .getConceptosEconomicos();
        } catch (Exception eC) {
            org.apache.log4j.Logger.getRootLogger().info(eC);
            throw new ExecutionError(2, "No fue posible cotizar, intentelo más tarde");
        }
        BigDecimal mPrimaT = new BigDecimal(getElementoCon(totalesCotResp, "TOTAL_PAGAR"));
        BigDecimal mIVA = new BigDecimal(getElementoCon(totalesCotResp, "IVA"));
        BigDecimal mPrimaN = new BigDecimal(getElementoCon(totalesCotResp, "PRIMA_NETA"));
        String msgCotizado = " es diferente al cotizado:";
        int res = mPrimaT.compareTo(new BigDecimal(objReqNeg.getPrimaTotal().trim()));
        if (res != 0) {
            throw new ExecutionError(2, "El monto proporcionado de la prima total:" + objReqNeg.getPrimaTotal()
                    + msgCotizado + mPrimaT.toString());
        }
        res = mIVA.compareTo(new BigDecimal(objReqNeg.getImpIva().trim()));
        if (res != 0) {
            throw new ExecutionError(2,
                    "El monto proporcionado del IVA:" + objReqNeg.getImpIva() + msgCotizado + mIVA.toString());
        }
        res = mPrimaN.compareTo(new BigDecimal(objReqNeg.getPrimaNeta().trim()));
        if (res != 0) {
            throw new ExecutionError(2, "El monto proporcionado de la prima neta:" + objReqNeg.getPrimaNeta()
                    + msgCotizado + mPrimaN.toString());
        }
    }

    /**
     * Valida.
     *
     * @param objReqNeg the obj req neg
     * @param tid       the tid
     * @return the emite neg req
     */
    public EmiteNegReq valida(final EmiteNegReq objReqNeg, final Integer tid) {
        if (!objReqNeg.getAccion().equals(STRPREVIO) && !objReqNeg.getAccion().equals(STREMISION)) {
            throw new ExecutionError(ERROR_4, "La operación", "a");
        }
        if (objReqNeg.getAgentes() != null) {
            Optional<Double> sumaAge = objReqNeg.getAgentes().stream().filter(p -> Optional.ofNullable(p).isPresent())
                    .filter(p -> Optional.ofNullable(p.getPctParticipComision()).isPresent())
                    .filter(p -> Optional.ofNullable(p.getFolio()).isPresent())
                    .filter(p -> Utileria.existeValor(p.getFolio()))
                    .map(e -> Double.parseDouble(e.getPctParticipComision())).reduce(Double::sum);
            if (sumaAge.isPresent() && sumaAge.get().compareTo(Constantes.CIEN) != 0) {
                throw new ExecutionError(ERROR_4, "La suma de participacion del agente", "a");
            }
        }
        validaStrNullEmpty(objReqNeg.getCveMoneda(), "La moneda", "a");
        String moneda = objReqNeg.getCveMoneda();
        CatalogoResp catResp = CatalogoClient.getInstancia(restTemplate).obtenCatalogo(STRMONEDA,
                conf.getUrlCatalogo());
        Optional<ElementoReq> elemento = catResp.getElementos().stream()
                .filter(p -> p.getClave().equalsIgnoreCase(moneda)).findFirst();
        if (!elemento.isPresent()) {
            throw new ExecutionError(ERROR_4, "La moneda", "a");
        }
        validaBanderas(objReqNeg.getBanAjusteIrregular(), "La bandera de ajuste irregular", "a");
        validaBanderas(objReqNeg.getBanImpresionCentralizada(), "La bandera de impresión centralizada", "a");
        validaBanderas(objReqNeg.getBanRenovacionAutomatica(), "La bandera de renovación automática", "a");
        /** validaProdTecnicoCom(objReqNeg); */
        validaStrNullEmpty(objReqNeg.getIdUMO(), "El negocio operable", "o");
        validaObjNullEmpty(objReqNeg.getPersonas(), "La persona", "a");
        validaPersona(objReqNeg);
        validaStrNullEmpty(objReqNeg.getCveFormaAjusteIrregular(), "La clave forma irregular", "a");
        validaObjNullEmpty(objReqNeg.getPaquetes(), "El paquete", "o");
        validaStrNullEmpty(objReqNeg.getPrimaTotal(), "La prima", "a");
        validaVehiculo(objReqNeg, tid);
        return complementaDatos(objReqNeg);
    }

    /**
     * Valida vehiculo.
     *
     * @param emiteNegReq the emite neg req
     * @param tid         the tid
     */
    public void validaVehiculo(final EmiteNegReq emiteNegReq, final Integer tid) {
        validaStrLonNullEmpty(emiteNegReq.getVehiculo().getArmadora(), "La armadora", "a", 2);
        validaStrLonNullEmpty(emiteNegReq.getVehiculo().getCarroceria(), "La carrocería", "a", 2);
        validaStrLonNullEmpty(emiteNegReq.getVehiculo().getVersion(), "La versión", "a", 2);
        validaStrLonNullEmpty(emiteNegReq.getVehiculo().getTipoVehiculo(), "El tipo de vehículo", "o", 2 + 1);
        validaStrLonNullEmpty(emiteNegReq.getVehiculo().getModelo(), "El modelo", "o", 2 + 2);
        validaStrLonNullEmpty(emiteNegReq.getVehiculo().getSubRamo(), "El subramo", "o", 2);
        validaStrNullEmpty(emiteNegReq.getVehiculo().getSerie(), "El número de serie", "o");
        if (Utileria.existeValor(emiteNegReq.getVehiculo().getMotor())) {
            validaStrLonMaxNullEmpty(emiteNegReq.getVehiculo().getMotor(), "El motor", "o", Constantes.VEINTE);
        }
        emiteNegReq.getVehiculo()
                .setValorConvenido(Utileria.soloNumeros(emiteNegReq.getVehiculo().getValorConvenido()));
        emiteNegReq.getVehiculo().setValorFactura(Utileria.soloNumeros(emiteNegReq.getVehiculo().getValorFactura()));
        ConsultaPasosConfiguradorResponse respVP = pasosClient.getTipoCarga(emiteNegReq, tid,
                conf.getUrlTransacciones());
        if (respVP.getPASOCONSULTACONFIGURADOR() != null) {
            for (PasoElementoConsultaDto elem : respVP.getPASOCONSULTACONFIGURADOR().getELEMENTOS()) {
                validaCargaV(elem, emiteNegReq);
            }
        }
        validaAdaptaciones(emiteNegReq);
    }

    /**
     * Valida carga V.
     *
     * @param elem        the elem
     * @param emiteNegReq the emite neg req
     */
    public void validaCargaV(final PasoElementoConsultaDto elem, final EmiteNegReq emiteNegReq) {
        if (!elem.getVALORESELEMENTO().isEmpty()) {
            String[] elemento = elem.getVALORESELEMENTO().get(0).getELEMENTO().split(":");
            String validaTipoCarga = elemento[elemento.length - 1];
            if (validaTipoCarga.equals(STRUNO) && (emiteNegReq.getVehiculo().getTipoCarga() == null
                    || !emiteNegReq.getVehiculo().getTipoCarga().matches("[ABC]"))) {
                throw new ExecutionError(ERROR_4, "El tipo de carga", "o");
            }
        }
    }

    /**
     * Valida adaptaciones.
     *
     * @param emiteNegReq the emite neg req
     */
    public void validaAdaptaciones(final EmiteNegReq emiteNegReq) {
        if (emiteNegReq.getVehiculo().getAdaptaciones() == null
                || emiteNegReq.getVehiculo().getAdaptaciones().isEmpty()) {
            return;
        }
        emiteNegReq.getVehiculo().getAdaptaciones().stream().forEach(this::validaDataAdaptacion);
    }

    /**
     * Valida data adaptacion.
     *
     * @param adpNeg the adp neg
     */
    public void validaDataAdaptacion(final AdaptacionVehNeg adpNeg) {
        validaStrNullEmpty(adpNeg.getDescEquip(), "La descripción de la adaptación", "o");
        validaStrNullEmpty(adpNeg.getMontoFacturacion(), "El monto de facturación", "o");
        validaStrNullEmpty(adpNeg.getMontoSA(), "El monto de la suma asegurada", "o");
        validaStrNullEmpty(adpNeg.getFechaFactura(), "La fecha de factura", "a");
        adpNeg.setMontoFacturacion(Utileria.soloNumeros(adpNeg.getMontoFacturacion()));
        adpNeg.setMontoSA(Utileria.soloNumeros(adpNeg.getMontoSA()));
        if (!Utileria.isDateaaaammdd(adpNeg.getFechaFactura())) {
            throw new ExecutionError(ERROR_4, "La fecha de factura", "a");
        }
    }

    /**
     * Valida persona.
     *
     * @param emiteNegReq the emite neg req
     */
    public void validaPersona(final EmiteNegReq emiteNegReq) {
        List<PersonaNeg> personas = emiteNegReq.getPersonas();
        Optional<PersonaNeg> contNeg = personas.stream().filter(p -> p.getTipo().equalsIgnoreCase(STRCONTRATANTE))
                .findFirst();
        if (!contNeg.isPresent()) {
            throw new ExecutionError(2, "El contratante es necesario");
        } else {
            validaStrNullEmpty(contNeg.get().getRfc(), STRELRFC, "o");
            validaPersonaSub(contNeg.get(), emiteNegReq);
            validaObjNullEmpty(contNeg.get().getDomicilio(), "El domicilio", "o");
            if (Utileria.existeValor(contNeg.get().getDomicilio().getNumExterior())
                    && contNeg.get().getDomicilio().getNumExterior().length() > Constantes.DIEZ) {
                throw new ExecutionError(2, "El numero exterior debe ser menor a 10 caracteres");
            }
            validaObjNullEmpty(contNeg.get().getMediosContacto(), "El medio de contacto", "o");
            validaMedioCobro(emiteNegReq, contNeg.get().getMedioCobro());
            personas.stream().forEach(Utileria::getTipoPersona);
            banderaConductor(emiteNegReq, personas, contNeg);
            banderaBeneficiario(emiteNegReq, personas, contNeg);
        }

    }

    /**
     * Bandera conductor.
     *
     * @param emiteNegReq the emite neg req
     * @param personas    the personas
     * @param contNeg     the cont neg
     */
    protected static void banderaConductor(final EmiteNegReq emiteNegReq, final List<PersonaNeg> personas,
            final Optional<PersonaNeg> contNeg) {
        Optional<PersonaNeg> condNeg = personas.stream().filter(p -> p.getTipo().equalsIgnoreCase(STRCONDUCTOR))
                .findFirst();
        if (emiteNegReq.getIndContConductor() != null) {
            // bandera es 1
            if (emiteNegReq.getIndContConductor().equalsIgnoreCase(STRUNO)) {
                List<PersonaNeg> per = emiteNegReq.getPersonas();
                per.removeIf(p -> p.getTipo().equalsIgnoreCase(STRCONDUCTOR));
                copiaConductor(emiteNegReq, contNeg);
            } else if (emiteNegReq.getIndContConductor().equalsIgnoreCase(STRCERO)) { /** bandera es 0 */
                banConductorCero(condNeg);
            } else {
                throw new ExecutionError(2, "La bandera de conductor ", "a");
            }
        } else if (emiteNegReq.getIndContConductor() == null && !condNeg.isPresent()) {
            throw new ExecutionError(2, "El conductor es necesario");
        } else {
            throw new ExecutionError(2, "La bandera conductor es necesaria");
        }
    }

    /**
     * Ban conductor cero.
     *
     * @param condNeg the cond neg
     */
    private static void banConductorCero(final Optional<PersonaNeg> condNeg) {
        // no tiene tag y bandera es 0
        if (!condNeg.isPresent()) {
            throw new ExecutionError(2, "El conductor es necesario");
        } else {
            // tiene tag y bandera es 0
            validaStrNullEmpty(condNeg.get().getSexo(), STRELSEXO, "o");
            validaSexo(condNeg.get().getSexo());
            validaStrNullEmptyEdad(condNeg.get().getEdad(), STRLAEDAD, "a");
        }
    }

    /**
     * Copia conductor.
     *
     * @param emiteNegReq the emite neg req
     * @param contNeg     the cont neg
     */
    protected static void copiaConductor(final EmiteNegReq emiteNegReq, final Optional<PersonaNeg> contNeg) {
        if (contNeg.isPresent()) {
            List<PersonaNeg> personasContr = emiteNegReq.getPersonas();
            PersonaNeg cond = new PersonaNeg();
            cond.setTipo(STRCONDUCTOR);
            cond.setTipoPersona(STRF);
            cond.setRfc(contNeg.get().getRfc());
            cond.setNombre(contNeg.get().getNombre());
            cond.setAPaterno(contNeg.get().getAPaterno());
            cond.setAMaterno(contNeg.get().getAMaterno());
            cond.setSexo(contNeg.get().getSexo());
            cond.setEstadoCivil(contNeg.get().getEstadoCivil());
            calculaEdad(contNeg, cond);
            personasContr.add(cond);
        }
    }

    /**
     * Calcula edad.
     *
     * @param persNeg the pers neg
     * @param cond    the cond
     */
    protected static void calculaEdad(final Optional<PersonaNeg> persNeg, final PersonaNeg cond) {
        if (persNeg.isPresent()) {
            cond.setFecNacimiento(persNeg.get().getFecNacimiento());
            if (!persNeg.get().getEdad().isEmpty()) {
                cond.setEdad(persNeg.get().getEdad());
            } else {
                int edad = Utileria.getEdad(persNeg.get().getFecNacimiento());
                cond.setEdad(String.valueOf(edad));
            }
        }
    }

    /**
     * Bandera beneficiario.
     *
     * @param emiteNegReq the emite neg req
     * @param personas    the personas
     * @param contNeg     the cont neg
     */
    protected static void banderaBeneficiario(final EmiteNegReq emiteNegReq, final List<PersonaNeg> personas,
            final Optional<PersonaNeg> contNeg) {
        Optional<PersonaNeg> benefNeg = personas.stream().filter(p -> p.getTipo().equalsIgnoreCase(STRBENEFICIARIO))
                .findFirst();
        if (emiteNegReq.getIndContBenef() != null) {
            if (emiteNegReq.getIndContBenef().equalsIgnoreCase(STRUNO)) {
                // bandera es 1
                List<PersonaNeg> per = emiteNegReq.getPersonas();
                per.removeIf(p -> p.getTipo().equalsIgnoreCase(STRBENEFICIARIO));
                copiaBeneficiario(emiteNegReq, contNeg);
            } else if (emiteNegReq.getIndContBenef().equalsIgnoreCase(STRCERO)) {
                // bandera es 0
                banBeneficiarioCero(benefNeg);
            } else {
                throw new ExecutionError(ERROR_4, "La bandera de beneficiario ", "a");
            }
        } else if (!benefNeg.isPresent()) {
            throw new ExecutionError(2, "El beneficiario es necesario");
        } else {
            throw new ExecutionError(2, "La bandera beneficiario es necesaria");
        }
    }

    /**
     * Ban beneficiario cero.
     *
     * @param benefNeg the benef neg
     */
    protected static void banBeneficiarioCero(final Optional<PersonaNeg> benefNeg) {
        if (!benefNeg.isPresent()) {
            // no tiene tag y bandera es 0
            throw new ExecutionError(2, "El beneficiario es necesario");
        } else {
            // tiene tag y la bandera es 0
            validaStrNullEmpty(benefNeg.get().getBanIrrevocable(), "La bandera irrevocable", "a");
            if (benefNeg.get().getTipoPersona().equalsIgnoreCase(STRF)) {
                validaStrNullEmpty(benefNeg.get().getAPaterno(), "El apellido paterno", "o");
                validaStrNullEmpty(benefNeg.get().getNombre(), "El nombre", "o");
            } else {
                validaStrNullEmpty(benefNeg.get().getRazonSocial(), "La razón social", "a");
            }
        }
    }

    /**
     * Copia beneficiario.
     *
     * @param emiteNegReq the emite neg req
     * @param contNeg     the cont neg
     */
    private static void copiaBeneficiario(final EmiteNegReq emiteNegReq, final Optional<PersonaNeg> contNeg) {
        if (contNeg.isPresent()) {
            List<PersonaNeg> personasBenef = emiteNegReq.getPersonas();
            PersonaNeg ben = new PersonaNeg();
            ben.setTipo(STRBENEFICIARIO);
            ben.setBanIrrevocable("0");
            ben.setNombre(contNeg.get().getNombre());
            ben.setAPaterno(contNeg.get().getAPaterno());
            ben.setAMaterno(contNeg.get().getAMaterno());
            ben.setRazonSocial(contNeg.get().getRazonSocial());
            ben.setEdad(contNeg.get().getEdad());
            ben.setFecNacimiento(contNeg.get().getFecNacimiento());
            ben.setFecConstitucion(contNeg.get().getFecConstitucion());
            ben.setRfc(contNeg.get().getRfc());
            ben.setPctBeneficio("1");
            ben.setTipoPersona(contNeg.get().getTipoPersona());
            ben.setCveClienteOrigen(contNeg.get().getCveClienteOrigen());
            ben.setIdParticipante(contNeg.get().getIdParticipante());
            ben.setSecBeneficiario("1");
            ben.setBanPreferente("0");
            personasBenef.add(ben);
        }
    }

    /**
     * Valida str null empty edad.
     *
     * @param edad     the edad
     * @param msgError the msg error
     * @param msgFin   the msg fin
     */
    private static void validaStrNullEmptyEdad(final String edad, final String msgError, final String msgFin) {
        validaStrNullEmpty(edad, msgError, msgFin);
        if (edad.matches("^[0-9]")) {
            throw new ExecutionError(ERROR_4, msgError, msgFin);
        }
    }

    /**
     * Valida persona sub.
     *
     * @param personaNeg  the persona neg
     * @param emiteNegReq the emite neg req
     */
    private static void validaPersonaSub(final PersonaNeg personaNeg, final EmiteNegReq emiteNegReq) {
        if (personaNeg.getTipoPersona().equalsIgnoreCase(STRF)) {
            validaStrNullEmpty(personaNeg.getAPaterno(), "El apellido paterno", "o");
            validaStrNullEmpty(personaNeg.getNombre(), "El nombre", "o");
            validaStrNullEmpty(personaNeg.getFecNacimiento(), "La fecha de nacimiento", "a");
            validaSexo(personaNeg.getSexo());
            if ((personaNeg.getEdad() == null || personaNeg.getEdad().isEmpty())) {
                personaNeg.setEdad(String.valueOf(Utileria.getEdad(personaNeg.getFecNacimiento())));
            }
            validaStrNullEmptyEdad(personaNeg.getEdad(), STRLAEDAD, "a");
            if (!Utileria.validarRFCFisica(personaNeg.getRfc())) {
                throw new ExecutionError(ERROR_4, STRELRFC, "o");
            }
        } else {
            validaPersonaSubMoral(emiteNegReq, personaNeg);
        }
    }

    /**
     * Valida persona sub moral.
     *
     * @param emiteNegReq the emite neg req
     * @param personaNeg  the persona neg
     */
    private static void validaPersonaSubMoral(final EmiteNegReq emiteNegReq, final PersonaNeg personaNeg) {
        validaStrNullEmpty(personaNeg.getRazonSocial(), "La razón social", "a");
        validaStrNullEmpty(personaNeg.getFecConstitucion(), "La fecha de constitución", "a");
        if (!"M".equalsIgnoreCase(personaNeg.getTipoPersona())) {
            throw new ExecutionError(ERROR_4, "El tipo de persona", "o");
        }
        if (emiteNegReq.getIndContConductor().equals(STRUNO)) {
            throw new ExecutionError(ERROR_3, "El conductor no puede ser una persona moral");
        }
        if (!Utileria.validarRFCMoral(personaNeg.getRfc())) {
            throw new ExecutionError(ERROR_4, STRELRFC, "o");
        }
    }

    /**
     * Valida sexo.
     *
     * @param sexo the sexo
     */
    private static void validaSexo(final String sexo) {
        if (sexo == null) {
            throw new ExecutionError(ERROR_4, STRELSEXO, "o");
        }
        if (!("F".equalsIgnoreCase(sexo) || "M".equalsIgnoreCase(sexo))) {
            throw new ExecutionError(ERROR_4, STRELSEXO, "o");
        }
    }

    /**
     * Valida str null empty.
     *
     * @param valor    the valor
     * @param msgError the msg error
     * @param msgFin   the msg fin
     */
    private static void validaStrNullEmpty(final String valor, final String msgError, final String msgFin) {
        if (valor == null || valor.isEmpty()) {
            throw new ExecutionError(ERROR_4, msgError, msgFin);
        }
    }

    /**
     * Valida obj null empty.
     *
     * @param valor    the valor
     * @param msgError the msg error
     * @param msgFin   the msg fin
     */
    private static void validaObjNullEmpty(final Object valor, final String msgError, final String msgFin) {
        if (valor == null) {
            throw new ExecutionError(ERROR_4, msgError, msgFin);
        }
    }

    /**
     * Valida str lon null empty.
     *
     * @param valor    the valor
     * @param msgError the msg error
     * @param msgFin   the msg fin
     * @param lon      the lon
     */
    private static void validaStrLonNullEmpty(final String valor, final String msgError, final String msgFin,
            final Integer lon) {
        if (valor == null || valor.isEmpty()) {
            throw new ExecutionError(ERROR_4, msgError, msgFin);
        }
        if (valor.length() != lon) {
            throw new ExecutionError(ERROR_4, msgError, msgFin);
        }
    }

    /**
     * Valida str lon null empty.
     *
     * @param valor    the valor
     * @param msgError the msg error
     * @param msgFin   the msg fin
     * @param lon      the lon
     */
    private static void validaStrLonMaxNullEmpty(final String valor, final String msgError, final String msgFin,
            final Integer lon) {
        if (valor == null || valor.isEmpty()) {
            throw new ExecutionError(ERROR_4, msgError, msgFin);
        }
        if (valor.length() > lon) {
            throw new ExecutionError(ERROR_4, msgError, msgFin);
        }
    }

    /**
     * Valida banderas.
     *
     * @param valor    the valor
     * @param msgError the msg error
     * @param msgFin   the msg fin
     */
    protected static void validaBanderas(final String valor, final String msgError, final String msgFin) {
        if (valor == null || !(valor.equals(STRUNO) || valor.equals(STRCERO))) {
            throw new ExecutionError(ERROR_4, msgError, msgFin);
        }
    }

    /**
     * Valida medio cobro.
     *
     * @param emiteNegReq the emite neg req
     * @param medioCobro  the medio cobro
     */
    private static void validaMedioCobro(final EmiteNegReq emiteNegReq, final MedioCobroNeg medioCobro) {
        if (emiteNegReq.getViaPago().equalsIgnoreCase(STRBANCARIO) && medioCobro == null) {
            throw new ExecutionError(ERROR_3, "Debe especificar medio de cobro cuando la vía de pago sea BC");
        } else {
            if (medioCobro != null) {
                validaStrNullEmpty(medioCobro.getCveEntidadFinanciera(), "La entidad financiera", "a");
                validaStrLonNullEmpty(medioCobro.getCveTipoBancario(), "El tipo bancario", "o", 1);
                validaStrLonNullEmpty(medioCobro.getCveTipoCuentaTarjeta(), "El tipo de cuenta", "a", 2);
                validaStrLonNullEmpty(medioCobro.getFchVencimiento(), "La fecha de vencimiento", "a", 2 + 2 + 2);
            }
        }
    }

    /**
     * Valida neg.
     *
     * @param objReqNeg the obj req neg
     * @return the emite neg req
     */
    public EmiteNegReq validaNeg(final EmiteNegReq objReqNeg) {
        CatalogoResp catResp = CatalogoClient.getInstancia(restTemplate).obtenCatalogo(STRPERIODICIDAD,
                conf.getUrlCatalogo());
        Optional<ElementoReq> elemento = catResp.getElementos().stream()
                .filter(p -> p.getClave().equals(objReqNeg.getPeriodicidad())).findFirst();
        if (!elemento.isPresent()) {
            throw new ExecutionError(ERROR_4, PERIODICIDAD, "a");
        } else {
            objReqNeg.setPeriodicidad(elemento.get().getNombre().trim());
        }
        catResp = CatalogoClient.getInstancia(restTemplate).obtenCatalogo(STRVIAPAGO, conf.getUrlCatalogo());
        Optional<ElementoReq> elementoVP = catResp.getElementos().stream()
                .filter(p -> p.getClave().equals(objReqNeg.getViaPago())).findFirst();
        if (!elementoVP.isPresent()) {
            throw new ExecutionError(ERROR_4, "La via de pago", "a");
        }
        Optional<PersonaNeg> objCont = objReqNeg.getPersonas().stream().filter(p -> p.getTipo().equals(STRCONTRATANTE))
                .findFirst();
        String strCP = objCont.isPresent() ? objCont.get().getDomicilio().getCodigoPostal() : "";
        CatalogoReq catReq = new CatalogoReq();
        catReq.setFecha("");
        catReq.setIdUMO("");
        catReq.setPassword("");
        catReq.setTipoCatalogo(STRESTADO);
        List<ElementoReq> elementosCat = new ArrayList<>();
        ElementoReq elemCat = new ElementoReq();
        elemCat.setNombre(STRCODIGOPOSTAL);
        elemCat.setClave(strCP);
        elementosCat.add(elemCat);
        catReq.setElementos(elementosCat);
        catResp = CatalogoClient.getInstancia(catReq, restTemplate).obtenCatalogo(conf.getUrlCatalogo());
        if (catResp.getElementos() == null) {
            throw new ExecutionError(ERROR_4, "El codigo postal", "o");
        }
        Optional<ElementoReq> elementoCP = catResp.getElementos().stream().findFirst();
        if (!elementoCP.isPresent()) {
            throw new ExecutionError(ERROR_4, "El codigo postal", "o");
        }
        objReqNeg.getVehiculo().setEstadoCirculacion(elementoCP.get().getClave().substring(1));
        return objReqNeg;
    }

    /**
     * Complementa datos.
     *
     * @param objReqNeg the obj req neg
     * @return the emite neg req
     */
    protected static EmiteNegReq complementaDatos(final EmiteNegReq objReqNeg) {
        if (objReqNeg.getVehiculo().getCp() == null || objReqNeg.getVehiculo().getCp().isEmpty()) {
            Optional<PersonaNeg> objCont = objReqNeg.getPersonas().stream()
                    .filter(p -> p.getTipo().equals(STRCONTRATANTE)).findFirst();
            String strCP = objCont.isPresent() ? objCont.get().getDomicilio().getCodigoPostal() : "";
            objReqNeg.getVehiculo().setCp(strCP);
        }
        if (objReqNeg.getVehiculo().getEstadoCirculacion() == null
                || objReqNeg.getVehiculo().getEstadoCirculacion().isEmpty()) {
            Optional<PersonaNeg> objCont = objReqNeg.getPersonas().stream()
                    .filter(p -> p.getTipo().equals(STRCONTRATANTE)).findFirst();
            String strEstado = objCont.isPresent() ? objCont.get().getDomicilio().getEstado() : "";
            objReqNeg.getVehiculo().setEstadoCirculacion(strEstado);
        }
        objReqNeg.setCveHerramienta(Utils.ifNull(objReqNeg.getCveHerramienta(), STRCVEHERRAMIENTA_WSP));
        objReqNeg.setCveCanalAcceso("TELEM");
        objReqNeg.setBanAjusteIrregular(isVigenciaIrregular(objReqNeg.getIniVig(), objReqNeg.getFinVig()));
        return objReqNeg;
    }

    /**
     * Checks if is vigencia irregular.
     *
     * @param iniVig the ini vig
     * @param finVig the fin vig
     * @return the string
     */
    private static String isVigenciaIrregular(final String iniVig, final String finVig) {
        Date fecIni = Utileria.getFecha(iniVig);
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecIni);
        cal.add(Calendar.YEAR, 1);
        Date fecIniBis = cal.getTime();
        Date fecFin = Utileria.getFecha(finVig);
        if (fecFin.compareTo(fecIniBis) == 0) {
            return "0";
        }
        return "1";
    }

    /**
     * Gets the elemento con.
     *
     * @param lstConcepto the lst concepto
     * @param clave       the clave
     * @return the elemento con
     */
    private String getElementoCon(final List<ConceptoEconomicoResp> lstConcepto, final String clave) {
        Optional<ConceptoEconomicoResp> concep = lstConcepto.stream().filter(p -> p.getNombre().equalsIgnoreCase(clave))
                .findAny();
        if (concep.isPresent()) {
            return concep.get().getMonto();
        }
        return "0";
    }
}
