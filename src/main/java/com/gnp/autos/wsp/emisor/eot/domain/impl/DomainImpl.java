package com.gnp.autos.wsp.emisor.eot.domain.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBElement;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.gnp.autos.wsp.emisor.eot.config.ConfWSP;
import com.gnp.autos.wsp.emisor.eot.domain.Domain;
import com.gnp.autos.wsp.emisor.eot.error.ExecutionError;
import com.gnp.autos.wsp.emisor.eot.liquidacion.LiquidaRecibosReq;
import com.gnp.autos.wsp.emisor.eot.liquidacion.RegistrarCuentaFinancieraReq;
import com.gnp.autos.wsp.emisor.eot.pagotv.PagoTVResp;
import com.gnp.autos.wsp.emisor.eot.rest.service.CatalogoClient;
import com.gnp.autos.wsp.emisor.eot.rest.service.CuentaFinancieraClient;
import com.gnp.autos.wsp.emisor.eot.rest.service.ImpresionClient;
import com.gnp.autos.wsp.emisor.eot.rest.service.LiquidacionClient;
import com.gnp.autos.wsp.emisor.eot.rest.service.PersonaClient;
import com.gnp.autos.wsp.emisor.eot.rest.service.ValidaEOTClient;
import com.gnp.autos.wsp.emisor.eot.rest.service.ValidaPagoTVClient;
import com.gnp.autos.wsp.emisor.eot.soap.service.EmitirEOTClient;
import com.gnp.autos.wsp.emisor.eot.tarificadorcp.RegionTarifDomain;
import com.gnp.autos.wsp.emisor.eot.urlimp.BuscaDocResp;
import com.gnp.autos.wsp.emisor.eot.util.Constantes;
import com.gnp.autos.wsp.emisor.eot.util.Utileria;
import com.gnp.autos.wsp.emisor.eot.validareglas.ResultadoValidaResp;
import com.gnp.autos.wsp.emisor.eot.wsdl.AdaptacionConversionAutosSCE;
import com.gnp.autos.wsp.emisor.eot.wsdl.AdaptacionesConversionesContratacionAutosSCE;
import com.gnp.autos.wsp.emisor.eot.wsdl.AgenteContratacionAutosSCE;
import com.gnp.autos.wsp.emisor.eot.wsdl.AgentesContratacionAutosSCE;
import com.gnp.autos.wsp.emisor.eot.wsdl.AjustesTarificacionContratacionAutosSCE;
import com.gnp.autos.wsp.emisor.eot.wsdl.BeneficiarioContratacionAutosSCE;
import com.gnp.autos.wsp.emisor.eot.wsdl.BeneficiariosContratacionAutosSCE;
import com.gnp.autos.wsp.emisor.eot.wsdl.Cabecera;
import com.gnp.autos.wsp.emisor.eot.wsdl.CoberturaContratacionAutosSCE;
import com.gnp.autos.wsp.emisor.eot.wsdl.CoberturasContratacionAutosSCE;
import com.gnp.autos.wsp.emisor.eot.wsdl.ConductorHabitualContratacionAutosSCE;
import com.gnp.autos.wsp.emisor.eot.wsdl.ContratanteContratacionAutosSCE;
import com.gnp.autos.wsp.emisor.eot.wsdl.CorreoContratacionAutosSCE;
import com.gnp.autos.wsp.emisor.eot.wsdl.DerechosPolizaContratacionAutosSCE;
import com.gnp.autos.wsp.emisor.eot.wsdl.DescuentosContratacionAutosSCE;
import com.gnp.autos.wsp.emisor.eot.wsdl.DireccionContratacionAutosSCE;
import com.gnp.autos.wsp.emisor.eot.wsdl.Emitir;
import com.gnp.autos.wsp.emisor.eot.wsdl.EmitirRequest;
import com.gnp.autos.wsp.emisor.eot.wsdl.EmitirResponse;
import com.gnp.autos.wsp.emisor.eot.wsdl.EstructuraComercialAutosSCE;
import com.gnp.autos.wsp.emisor.eot.wsdl.EstructuraComercialContratacionAutosSCE;
import com.gnp.autos.wsp.emisor.eot.wsdl.FechasPolizaAutosSCE;
import com.gnp.autos.wsp.emisor.eot.wsdl.MedioCobroContratacionAutosSCE;
import com.gnp.autos.wsp.emisor.eot.wsdl.NotificacionAdicionalContratacionAutosSCE;
import com.gnp.autos.wsp.emisor.eot.wsdl.ObjectFactory;
import com.gnp.autos.wsp.emisor.eot.wsdl.ObjetoAseguradoContratacionAutosSCE;
import com.gnp.autos.wsp.emisor.eot.wsdl.PolizaContratacionAutosSCE;
import com.gnp.autos.wsp.emisor.eot.wsdl.PolizaSCE;
import com.gnp.autos.wsp.emisor.eot.wsdl.ProductoAutosSCE;
import com.gnp.autos.wsp.emisor.eot.wsdl.ReferenciaPolizaContratacionAutosSCE;
import com.gnp.autos.wsp.emisor.eot.wsdl.TarificacionCoberturaContratacionAutosSCE;
import com.gnp.autos.wsp.emisor.eot.wsdl.TelefonoContratacionAutosSCE;
import com.gnp.autos.wsp.emisor.eot.wsdl.TotalesTarificacionPolizaContratacionAutosSCE;
import com.gnp.autos.wsp.emisor.eot.wsdl.VehiculoContratacionAutosSCE;
import com.gnp.autos.wsp.negocio.banderaauto.soap.Cobertura;
import com.gnp.autos.wsp.negocio.catalogo.model.CatalogoReq;
import com.gnp.autos.wsp.negocio.catalogo.model.CatalogoResp;
import com.gnp.autos.wsp.negocio.muc.soap.CoberturaDto;
import com.gnp.autos.wsp.negocio.muc.soap.CoberturaPrimaDto;
import com.gnp.autos.wsp.negocio.muc.soap.ConceptoDto;
import com.gnp.autos.wsp.negocio.muc.soap.CotizacionDto;
import com.gnp.autos.wsp.negocio.muc.soap.DatosProductoDto;
import com.gnp.autos.wsp.negocio.muc.soap.DescuentoDto;
import com.gnp.autos.wsp.negocio.muc.soap.FormaPagoReciboDto;
import com.gnp.autos.wsp.negocio.muc.soap.ModificadorCoberturaDto;
import com.gnp.autos.wsp.negocio.muc.soap.ProductosDto;
import com.gnp.autos.wsp.negocio.neg.model.AdaptacionVehNeg;
import com.gnp.autos.wsp.negocio.neg.model.AgenteNeg;
import com.gnp.autos.wsp.negocio.neg.model.Archivo;
import com.gnp.autos.wsp.negocio.neg.model.CoberturaNeg;
import com.gnp.autos.wsp.negocio.neg.model.ConceptoEconomicoNeg;
import com.gnp.autos.wsp.negocio.neg.model.DomicilioNeg;
import com.gnp.autos.wsp.negocio.neg.model.ElementoNeg;
import com.gnp.autos.wsp.negocio.neg.model.ElementoReq;
import com.gnp.autos.wsp.negocio.neg.model.EmiteNegReq;
import com.gnp.autos.wsp.negocio.neg.model.EmiteNegResp;
import com.gnp.autos.wsp.negocio.neg.model.MedioCobroNeg;
import com.gnp.autos.wsp.negocio.neg.model.MedioContactoNeg;
import com.gnp.autos.wsp.negocio.neg.model.PersonaNeg;
import com.gnp.autos.wsp.negocio.neg.model.VehiculoNeg;
import com.gnp.autos.wsp.negocio.umoservice.model.CobranzaUmo;
import com.gnp.autos.wsp.negocio.umoservicemodel.EmisionDatos;
import com.gnp.autos.wsp.negocio.util.Encriptador;
import com.gnp.autos.wsp.negocio.util.Utils;

/**
 * The Class DomainImpl.
 */
@Component
public class DomainImpl implements Domain {
    /** The Constant CIEN. */
    private static final int CIEN = 100;

    /** The Constant DELAY_IMPRESION. */
    private static final int DELAY_IMPRESION = 2000;

    /** The Constant STRCVEHERRAMIENTA_PORTAL. */
    private static final String STRCVEHERRAMIENTA_PORTAL = "PGN";

    /** The Constant STRCVEHERRAMIENTA_TV. */
    private static final String STRCVEHERRAMIENTA_TV = "TV";

    /** The Constant STRCONDUCTOR. */
    private static final String STRCONDUCTOR = "CONDUCTOR";

    /** The Constant STRCONTRATANTE. */
    private static final String STRCONTRATANTE = "CONTRATANTE";

    /** The Constant STRMAIL. */
    private static final String STRMAIL = "CORREO";

    /** The Constant STRCORREO. */
    private static final String STRCORREO = "CORREO_ELECTRONICO";

    /** The Constant STRTELEFONO. */
    private static final String STRTELEFONO = "TELEFONO";

    /** The Constant STRBENEFICIARIO. */
    private static final String STRBENEFICIARIO = "BENEFICIARIO";

    /** The Constant STRUNO. */
    private static final String STRUNO = "1";

    /** The Constant STRL. */
    private static final String STRL = "L";

    /** The Constant STRE. */
    private static final String STRE = "E";

    /** The Constant STRFISICA. */
    private static final String STRFISICA = "F";

    /** The Constant STRCL. */
    private static final String STRCL = "CL";

    /** The Constant STRTRES. */
    private static final String STRTRES = "3";

    /** The Constant STRCPASEGUR. */
    private static final String STRCPASEGUR = "CPASEGUR";

    /** The Constant STRCPACONTINUAR. */
    private static final String STRCPACONTINUAR = "CONTINUAR";

    /** The Constant STRCDDEDUCI. */
    private static final String STRCDDEDUCI = "CDDEDUCI";

    /** The Constant STRCDDEDUCIAR. */
    private static final String STRCDDEDUCIAR = "CDDEDUCIAR";

    /** The Constant STR_PTEC. */
    private static final String STR_PTEC = "PRIMA_TECNICA";

    /** The Constant STR_PNET. */
    private static final String STR_PNET = "PRIMA_NETA";

    /** The Constant STR_CED. */
    private static final String STR_CED = "PRIMA_CEDIDA";

    /** The Constant STR_IVA. */
    private static final String STR_IVA = "IVA";

    /** The Constant STR_DERPOL. */
    private static final String STR_DERPOL = "DERECHOS_POLIZA";

    /** The Constant STR_RFRAC. */
    private static final String STR_RFRAC = "RECARGO_FRACCIONAMIENTO";

    /** The Constant STR_DESC. */
    private static final String STR_DESC = "DESCUENTO";

    /** The Constant STRVERSION. */
    private static final String STRVERSION = "VERSION";

    /** The Constant STR091606. */
    private static final String STR091606 = "0000000916";

    /** The Constant STR006. */
    private static final String STR006 = "006";

    /** The Constant CVE_TIPO_NOMINA. */
    private static final String CVE_TIPO_NOMINA = "CVE_TIPO_NOMINA";

    /** The Constant UBICACION_TRABAJO. */
    private static final String UBICACION_TRABAJO = "UBICACION_TRABAJO";

    /** The Constant DN. */
    private static final String DN = "DN";

    /** The Constant BN. */
    private static final String BN = "BN";

    /** The Constant PODESCAM. */
    private static final String PODESCAM = "PODESCAM";

    /** The Constant VADESVOL. */
    private static final String VADESVOL = "VADESVOL";

    /** The Constant VADESREN. */
    private static final String VADESREN = "VADESREN";

    /** The Constant VADESIGU. */
    private static final String VADESIGU = "VADESIGU";

    /** The Constant STRVACIO. */
    private static final String STRVACIO = "";

    /** The client EOT. */
    @Autowired
    private EmitirEOTClient clientEOT;

    /** The conf. */
    @Autowired
    private ConfWSP conf;

    /** The reg tarif client. */
    @Autowired
    private RegionTarifDomain regTarifClient;

    /** The usuario vp. */
    @Value("${wsp_vp_usuario}")
    private String usuarioVp;

    /** The pass vp. */
    @Value("${wsp_vp_pass}")
    private String passVp;

    /** The rest template. */
    @Autowired
    private static RestTemplate restTemplate = new RestTemplate();

    /**
     * Gets the emitir.
     *
     * @param emDatos    the em datos
     * @param tid        the tid
     * @param prodTecCom the prod tec com
     * @return the emitir
     */
    @Override
    public EmiteNegResp getEmitir(final EmisionDatos emDatos, final Integer tid, final String prodTecCom) {
        EmiteNegResp response;
        EmitirResponse objResp;
        EmiteNegReq objReq = emDatos.getEmite();
        if (!objReq.getCveHerramienta().equals(STRCVEHERRAMIENTA_PORTAL)) {
            Utileria.getRegistraLogTime("personas in");
            PersonaClient pers = new PersonaClient(restTemplate);
            List<PersonaNeg> personasresp = pers.getPersonaService(objReq.getPersonas(), conf.getUrlPersona(), tid,
                    objReq);
            if (personasresp == null) {
                String msgError = "Ocurrio un error en el servicio de personas.   ";
                if (tid != null) {
                    msgError = msgError + "(" + tid.toString() + ")";
                    msgError = msgError + " --- " + conf.getUrlPersona();
                }
                throw new ExecutionError(Constantes.ERROR_3, msgError);
            }
            Utileria.getRegistraLogTime("persona out");
            objReq.setPersonas(personasresp);
            emDatos.setEmite(objReq);
            if (!objReq.getCveHerramienta().equals(STRCVEHERRAMIENTA_TV)) {
                isValidaEOT(emDatos, tid);
            }
            Utileria.getRegistraLogTime("validas out");
            regCuentaFinanciera(objReq, tid);
            Utileria.getRegistraLogTime("registra cuenta  financiera out");
            complementaBanderas(objReq);
        }

        objReq.getVehiculo().setEstadoCirculacion(regTarifClient.getRegionTarif(tid, objReq.getVehiculo().getCp()));
        emDatos.setEmite(objReq);
        EmitirRequest requestEOT = obtenRequest(emDatos, prodTecCom);
        if (objReq.getAccion().equals(STRE)) {
            clientEOT.setDefaultUri(conf.getUrlEmisionEOT());
            objResp = clientEOT.getEmitir(requestEOT, tid, conf.getUrlTransacciones() + "/logintermedia");
            Utileria.getRegistraLogTime("contratacion autos out");
            if (objResp == null) {
                throw new ExecutionError(Constantes.ERROR_6);
            }
        } else {
            objResp = new EmitirResponse();
            PolizaSCE pol = new PolizaSCE();
            pol.setNUMPOLIZA("");
            pol.setINDTIPOEMISION("");
            pol.setNUMVERSION(0);
            objResp.setPoliza(pol);
        }
        response = getRespNeg(objResp, objReq);
        if (objReq.getAccion().equals(STRE)) {
            imprimeURL(response, objReq, tid);
            liquidaRecibo(objReq, response, objResp, tid);
            Utileria.getRegistraLogTime("liquida out");
        }
        return response;
    }

    /**
     * Complementa banderas.
     *
     * @param objReq the obj req
     */
    protected void complementaBanderas(final EmiteNegReq objReq) {
        Boolean isAltoRiesgo = objReq.getBanAutoResp().getPoliza().getPoliza().getObjetoAsegurado().getValue()
                .getVehiculo().isBANALTORIESGO();
        if (isAltoRiesgo) {
            objReq.getVehiculo().setAltoRiesgo("1");
            Optional<Cobertura> cobNeg = objReq.getBanAutoResp().getPoliza().getPoliza().getObjetoAsegurado().getValue()
                    .getCoberturasBasicas().stream().filter(p -> p.getClave().equals(STR091606)).findFirst();
            if (cobNeg.isPresent()) {
                Double porcentajeAR = cobNeg.get().getPCTDEDUCIBLEALTORIESGO();
                Optional<CoberturaDto> cobReq = objReq.getMucPrimaAutoReq().getDATOSCOTIZACION().get(0)
                        .getDATOSPRODUCTO().get(0).getCOBERTURA().stream()
                        .filter(p -> p.getCLAVECOBERTURA().equals(STR091606)).findFirst();
                if (cobReq.isPresent()) {
                    ModificadorCoberturaDto altRMod = new ModificadorCoberturaDto();
                    altRMod.setCLAVEMODIFICADOR(STRCDDEDUCIAR);
                    altRMod.setVALORREQUERIDO(porcentajeAR);
                    cobReq.get().getMODIFICADOR().add(altRMod);
                }
            }
        }
    }

    /**
     * Imprime URL.
     *
     * @param response the response
     * @param objReq   the obj req
     * @param tid      the tid
     */
    protected void imprimeURL(final EmiteNegResp response, final EmiteNegReq objReq, final Integer tid) {
        if (objReq.getBanImpresion().equals(STRUNO)) {
            try {
                ImpresionClient imp = new ImpresionClient(restTemplate);
                Thread.sleep(DELAY_IMPRESION);
                BuscaDocResp respImp = imp.getBuscaDocImp(response, conf.getUrlImpresion(), tid);
                Utileria.getRegistraLogTime("impresion out");
                if (respImp.getResultado().getUrlDocumento() != null) {
                    List<Archivo> archivos = new ArrayList<>();
                    Archivo arch = new Archivo();
                    arch.setUrl(respImp.getResultado().getUrlDocumento());
                    archivos.add(arch);
                    response.setArchivos(archivos);
                }
            } catch (Exception ex) {
                Logger.getRootLogger().info(ex);
            }
        }
    }

    /**
     * Checks if is valida EOT.
     *
     * @param emDatos the em datos
     * @param tid     the tid
     */
    protected void isValidaEOT(final EmisionDatos emDatos, final Integer tid) {
        ValidaEOTClient valClient = new ValidaEOTClient(emDatos, restTemplate);
        ResultadoValidaResp resulVal = valClient.getValidaReglas(conf.getUrlValidas(), tid);
        if (!resulVal.getResultadoValida().equalsIgnoreCase(STRCPACONTINUAR)) {
            String causas = resulVal.getListaCausas().stream().collect(Collectors.joining());
            causas = getDataValida(resulVal.getResultadoValida(), causas, emDatos.getEmite().getIdCotizacion());
            throw new ExecutionError(Constantes.ERROR_3, causas);
        }
    }

    /**
     * Gets the data valida.
     *
     * @param resultadoValida the resultado valida
     * @param causas          the causas
     * @param idCot           the id cot
     * @return the data valida
     */
    private String getDataValida(final String resultadoValida, final String causas, final String idCot) {
        if (resultadoValida.equalsIgnoreCase(Constantes.STR_RETENER)) {
            return String.format("RETENER: %s", causas);
        }
        if (resultadoValida.equalsIgnoreCase(Constantes.STR_RECHAZAR)) {
            return String.format("RECHAZAR: %s", causas);
        }

        if (resultadoValida.equalsIgnoreCase(Constantes.STR_RESOLUCION)) {
            return String.format("Su solicitud (%s) se enviara a resolucion", idCot);
        }

        return causas;
    }

    /**
     * Valida pago TV.
     *
     * @param strIdUMO        the str id UMO
     * @param idCotizacion    the id cotizacion
     * @param numAutorizacion the num autorizacion
     * @param vpUser          the vp user
     * @param vpPass          the vp pass
     */
    protected void validaPagoTV(final String strIdUMO, final String idCotizacion, final String numAutorizacion,
            final String vpUser, final String vpPass) {
        if (conf.getValidaPago().equals(STRUNO) && !isCambiaPago(strIdUMO)) {
            ValidaPagoTVClient valClient = new ValidaPagoTVClient(idCotizacion, numAutorizacion, vpUser, vpPass,
                    restTemplate);
            PagoTVResp pagoResp = valClient.getValidaPagotv(conf.getUrlWSPagoTV(), null);
            if (!pagoResp.getErrores().isEmpty()) {
                throw new ExecutionError(Constantes.ERROR_3, "El numero de autorizacion");
            }
        }
    }

    /**
     * Reg cuenta financiera.
     *
     * @param objEmi the obj emi
     * @param tid    the tid
     */
    protected void regCuentaFinanciera(final EmiteNegReq objEmi, final Integer tid) {
        if (!("BC".equalsIgnoreCase(objEmi.getViaPago()) || "CL".equalsIgnoreCase(objEmi.getViaPago())
                || "BN".equalsIgnoreCase(objEmi.getViaPago()))) {
            return;
        }
        Optional<PersonaNeg> contNeg = objEmi.getPersonas().stream()
                .filter(p -> p.getTipo().equalsIgnoreCase(STRCONTRATANTE)).findFirst();
        if (contNeg.isPresent()) {
            if (contNeg.get().getMedioCobro() == null) {
                return;
            }
            validaPagoTV(objEmi.getIdUMO(), objEmi.getIdCotizacion(), contNeg.get().getMedioCobro().getNumCobro(),
                    usuarioVp, passVp);
            String cveTipoCuentaTarjeta;
            String numTarjeta;
            String cveTipoDatoBancario;
            cveTipoCuentaTarjeta = contNeg.get().getMedioCobro().getCveTipoCuentaTarjeta();
            cveTipoDatoBancario = contNeg.get().getMedioCobro().getCveTipoBancario();
            numTarjeta = contNeg.get().getMedioCobro().getNumTarjeta();
            Encriptador enc = new Encriptador();
            numTarjeta = enc.encriptar(numTarjeta);
            CuentaFinancieraClient cF = new CuentaFinancieraClient(restTemplate);
            RegistrarCuentaFinancieraReq cFReq = new RegistrarCuentaFinancieraReq();
            cFReq.setBanCtaPrincipal(false);
            cFReq.setCodFiliacion(contNeg.get().getCodigoCliente());
            cFReq.setCveEntidadFinanciera(contNeg.get().getMedioCobro().getCveEntidadFinanciera());
            cFReq.setCveMoneda(objEmi.getCveMoneda());
            cFReq.setCveTipoCuentaTarjeta(cveTipoCuentaTarjeta);
            cFReq.setCveTipoDatoBancario(cveTipoDatoBancario);
            cFReq.setDiaCobroPreferido((short) 0);
            cFReq.setFchVencimiento(contNeg.get().getMedioCobro().getFchVencimiento());
            cFReq.setIdParticipante(contNeg.get().getIdParticipante());
            cFReq.setIdTransaccion(objEmi.getIdCotizacion());
            cFReq.setNumCuenta(numTarjeta);
            if ("M".equalsIgnoreCase(contNeg.get().getTipoPersona())
                    || "J".equalsIgnoreCase(contNeg.get().getTipoPersona())) {
                cFReq.setTitular("");
                Optional<PersonaNeg> condNeg = objEmi.getPersonas().stream()
                        .filter(p -> p.getTipo().equalsIgnoreCase(STRCONDUCTOR)).findFirst();
                setTitular(cFReq, condNeg);
            } else {
                cFReq.setTitular(contNeg.get().getNombre() + " " + contNeg.get().getAMaterno() + " "
                        + contNeg.get().getAPaterno());
            }
            cF.getCuentaFinancieraService(cFReq, conf.getUrlLiquidacion(), tid);
        }
    }

    /**
     * Sets the titular.
     *
     * @param cFReq   the c F req
     * @param condNeg the cond neg
     */
    protected static void setTitular(final RegistrarCuentaFinancieraReq cFReq, final Optional<PersonaNeg> condNeg) {
        if (condNeg.isPresent()) {
            cFReq.setTitular(
                    condNeg.get().getNombre() + " " + condNeg.get().getAMaterno() + " " + condNeg.get().getAPaterno());
        }
    }

    /**
     * Liquida recibo.
     *
     * @param objEmi   the obj emi
     * @param response the response
     * @param objResp  the obj resp
     * @param tid      the tid
     */
    protected void liquidaRecibo(final EmiteNegReq objEmi, final EmiteNegResp response, final EmitirResponse objResp,
            final Integer tid) {
        // Para liquidar IND_TIPO_EMISION = L y CVE_CONDUCTO_PAGO = CL
        if (!(objResp.getPoliza().getINDTIPOEMISION().equalsIgnoreCase(STRL)
                && objEmi.getViaPago().equalsIgnoreCase(STRCL))) {
            return;
        }
        try {
            LiquidacionClient tc = new LiquidacionClient();
            LiquidaRecibosReq objReq = new LiquidaRecibosReq();
            objReq.setCanalCobro(objEmi.getViaPago());
            objReq.setCveCondPago(objEmi.getViaPago());
            String cveTipoCuentaTarjeta = "";
            String medioCobro = STR006;
            String numTarjeta = "";
            String numCobro = "";
            objReq.setOrigen(conf.getLiquidaOrigenDefault());
            if (isCambiaPago(objEmi.getIdUMO())) {
                objReq.setOrigen("VD");
                medioCobro = "223";
            }
            Optional<PersonaNeg> contNeg = objEmi.getPersonas().stream()
                    .filter(p -> p.getTipo().equalsIgnoreCase(STRCONTRATANTE)).findFirst();
            if (contNeg.isPresent()) {
                cveTipoCuentaTarjeta = contNeg.get().getMedioCobro().getCveTipoCuentaTarjeta();
                numTarjeta = contNeg.get().getMedioCobro().getNumTarjeta();
                if (numTarjeta.substring(0, 1).equalsIgnoreCase(STRTRES)) {
                    medioCobro = "007";
                }
                if (!Utileria.isTarjetaEncriptada(numTarjeta)) {
                    Encriptador enc = new Encriptador();
                    numTarjeta = enc.encriptar(numTarjeta);
                }
                numCobro = contNeg.get().getMedioCobro().getNumCobro();
            }
            objReq.setCveTipoCuentaTarjeta(cveTipoCuentaTarjeta);
            objReq.setCveTransaccion("5");
            objReq.setFechaVigenciaInicial(objEmi.getIniVig());
            objReq.setFechaVigenciaFinal(objEmi.getFinVig());
            objReq.setIdActor(objEmi.getUsuario());
            objReq.setIdPerfil(conf.getCabIdPerfil());
            objReq.setIdRol(conf.getCabIdRol());
            objReq.setIdTransaccion(objEmi.getIdCotizacion());
            objReq.setIndTipoEmi(response.getIndTipoEmision());
            objReq.setMedioCobro(medioCobro);
            objReq.setNumCobro(numCobro);
            objReq.setNumPoliza(response.getNumPoliza());
            objReq.setNumTarjeta(numTarjeta);
            objReq.setReferencia(objEmi.getIdCotizacion());
            objReq.setReintento(false);
            objReq.setUsuarioAudit("");
            tc.liquidaPoliza(objReq, conf.getUrlLiquidacion(), tid);
        } catch (Exception ex) {
            Logger.getRootLogger().error(ex);
        }
    }

    /**
     * Checks if is cambia pago.
     *
     * @param strNOP the str NOP
     * @return true, if is cambia pago
     */
    private boolean isCambiaPago(final String strNOP) {
        if (conf.getCambiaPago() != null) {
            for (String nop : conf.getCambiaPago().split(":")) {
                if (nop.equalsIgnoreCase(strNOP)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets the resp neg.
     *
     * @param objResp the obj resp
     * @param objReq  the obj req
     * @return the resp neg
     */
    protected static EmiteNegResp getRespNeg(final EmitirResponse objResp, final EmiteNegReq objReq) {
        EmiteNegResp response = new EmiteNegResp();
        response.setNumPoliza(objResp.getPoliza().getNUMPOLIZA());
        response.setNumVersion(String.valueOf(objResp.getPoliza().getNUMVERSION()));
        response.setIndTipoEmision(objResp.getPoliza().getINDTIPOEMISION());
        List<ConceptoEconomicoNeg> conceptoEconomico = new ArrayList<>();
        CotizacionDto cotizaDto = objReq.getMucPrimaAutoResp().getPETICION().get(0).getDATOSPRODUCTOS().get(0)
                .getDATOSCOTIZACION().get(0);

        List<ConceptoDto> totalesPrima = objReq.getMucPrimaAutoResp().getPETICION().get(0).getDATOSPRODUCTOS().get(0)
                .getDATOSCOTIZACION().get(0).getTOTALPRIMA().getPRIMA();
        List<ConceptoDto> totalesConc = objReq.getMucPrimaAutoResp().getPETICION().get(0).getDATOSPRODUCTOS().get(0)
                .getDATOSCOTIZACION().get(0).getTOTALPRIMA().getCONCEPTOECONOMICO();
        FormaPagoReciboDto fpDto = objReq.getMucPrimaAutoResp().getPETICION().get(0).getDATOSPRODUCTOS().get(0)
                .getDATOSCOTIZACION().get(0).getFORMAPAGORECIBO();
        conceptoEconomico.add(new ConceptoEconomicoNeg("TOTAL_PAGAR", fpDto.getMONTOTOTAL().toString()));
        conceptoEconomico.add(new ConceptoEconomicoNeg("IMP_PRIMER_RECIBO", fpDto.getMONTOPRIMERRECIBO().toString()));
        conceptoEconomico
                .add(new ConceptoEconomicoNeg("IMP_RECIBO_SUBSECUENTE", fpDto.getMONTORECIBOSUBSECUENTE().toString()));
        conceptoEconomico.add(new ConceptoEconomicoNeg("RECARGO_DESCUENTO",
                getConcepto(totalesConc, "RECARGO_DESCUENTO").toString()));
        conceptoEconomico.add(new ConceptoEconomicoNeg(STR_PTEC, getConcepto(totalesPrima, STR_PTEC).toString()));
        conceptoEconomico.add(new ConceptoEconomicoNeg(STR_PNET, getConcepto(totalesPrima, STR_PNET).toString()));
        conceptoEconomico.add(new ConceptoEconomicoNeg(STR_CED, getConcepto(totalesConc, STR_CED).toString()));
        conceptoEconomico
                .add(new ConceptoEconomicoNeg("RECARGO_PAGO_FRACC", getConcepto(totalesConc, STR_RFRAC).toString()));
        conceptoEconomico.add(new ConceptoEconomicoNeg(STR_IVA, getConcepto(totalesConc, STR_IVA).toString()));
        conceptoEconomico.add(new ConceptoEconomicoNeg(STR_DERPOL, getConcepto(totalesConc, STR_DERPOL).toString()));

        BigDecimal descuento = cotizaDto.getTOTALPRIMA().getDETALLEDESCUENTO().stream()
                .map(m -> new BigDecimal(m.getMONTO())).reduce(BigDecimal.ZERO, BigDecimal::add);

        conceptoEconomico.add(new ConceptoEconomicoNeg(STR_DESC, descuento.toString()));
        conceptoEconomico.add(new ConceptoEconomicoNeg("NUM_RECIBOS_SUB", fpDto.getNUMRECIBOSSUB().toString()));
        conceptoEconomico.add(new ConceptoEconomicoNeg("NUM_PAGOS", fpDto.getNUMEROPAGOS().toString()));
        response.setConceptoEconomico(conceptoEconomico);
        return response;
    }

    /**
     * Obten request.
     *
     * @param emDatos    the em datos
     * @param prodTecCom the prod tec com
     * @return the emitir request
     */
    protected EmitirRequest obtenRequest(final EmisionDatos emDatos, final String prodTecCom) {
        EmiteNegReq objReq = emDatos.getEmite();
        EmitirRequest objReqE = new EmitirRequest();
        Emitir objEmiEOT = new Emitir();
        Cabecera cab = new Cabecera();
        cab.setCVETRANSACCION(conf.getCabCveTransaccion());
        cab.setIDACTOR(objReq.getUsuario());
        cab.setIDPERFIL(conf.getCabIdPerfil());
        cab.setIDROL(conf.getCabIdRol());
        cab.setIDTRANSACCION(objReq.getIdCotizacion());
        objReqE.setCabecera(cab);
        PolizaContratacionAutosSCE poliza = new PolizaContratacionAutosSCE();
        poliza.setCVEFORMAPAGO(objReq.getPeriodicidad());
        if (objReq.getViaPagoSucesivos() != null && !objReq.getViaPagoSucesivos().isEmpty()) {
            poliza.setCVECONDUCTOPAGOSUBSECUENTE(objReq.getViaPagoSucesivos());
        }
        poliza.setFCHTARIFA(Utileria.getGregCalShort(objReq.getFchTarifa()));
        poliza.setCVETARIFA(objReq.getCveTarifa());
        poliza.setCVETRANSFORMACIONVARTARIF(objReq.getCveTransformacionTarifa());
        poliza.setFCHTRANSFORMACIONVARTARIF(Utileria.getGregCalShort(objReq.getFchTransformacionTarifa()));
        if (objReq.getBanRenovacionAutomatica().equals(STRUNO)) {
            poliza.setBANRENOVACIONAUTOMATICA(true);
        }
        poliza.setCVECONDUCTOPAGO(objReq.getViaPago());
        validaDatosDN(emDatos, poliza);
        if (!objReq.getCodigoPromocion().isEmpty()) {
            poliza.setIDCODIGOPROMOCION(objReq.getCodigoPromocion());
        }

        poliza.setCVEHERRAMIENTA(objReq.getCveHerramienta());
        poliza.setCVETIPOCANALACCESO(objReq.getCveCanalAcceso());
        poliza.setCVESUBRAMOAUTO(objReq.getVehiculo().getSubRamo());
        poliza.setCVEMONEDA(objReq.getCveMoneda());
        poliza.setINDAFECTABONO(objReq.getBanAutoResp().getPoliza().getPoliza().getINDAFECTABONO());

        poliza.setBANVIGENCIAIRREGULAR(objReq.getBanAjusteIrregular().equals(STRUNO));

        poliza.setCVEFORMAAJUSTEIRREGULAR(objReq.getCveFormaAjusteIrregular());
        poliza.setIDAGRUPACIONRECARGO(Integer.parseInt(objReq.getIdAgrupaRecargo()));
        JAXBElement<String> strCveInterm = new ObjectFactory().createPolizaContratacionAutosSCECVETIPOINTERMEDIARIO(
                objReq.getAgentes().get(0).getCveClaseIntermediarioInfo());
        poliza.getRest().add(strCveInterm);
        JAXBElement<String> strNumCot = new ObjectFactory()
                .createPolizaContratacionAutosSCENUMCOTIZACION(objReq.getIdCotizacion());
        poliza.getRest().add(strNumCot);
        boolean banImpCen = false;
        if (objReq.getBanImpresionCentralizada().equals(STRUNO)) {
            banImpCen = true;
        }
        JAXBElement<Boolean> strBanImpC = new ObjectFactory()
                .createPolizaContratacionAutosSCEBANIMPRESIONCENTRALIZADA(banImpCen);
        poliza.getRest().add(strBanImpC);
        JAXBElement<BigInteger> strVerZon = new ObjectFactory()
                .createPolizaContratacionAutosSCEVERSIONZONIFICACION(new BigInteger(objReq.getVerZonificacion()));
        poliza.getRest().add(strVerZon);
        JAXBElement<String> strCveZon = new ObjectFactory()
                .createPolizaContratacionAutosSCECLAVEZONIFICACION(objReq.getCveZonificacion());
        poliza.getRest().add(strCveZon);
        boolean banRenAut = false;
        if (objReq.getBanRenovacionAutomatica().equals(STRUNO)) {
            banRenAut = true;
        }
        poliza.setBANRENOVACIONAUTOMATICA(banRenAut);
        getJAXBFechas(poliza, objReq);
        getJAXBEstructura(poliza, objReq);
        getJAXBProducto(poliza, objReq, prodTecCom);
        getJAXBReferencia(poliza, objReq);
        getJAXBContr(poliza, objReq, conf.getUrlCatalogo());
        getJAXBMedioCobro(poliza, objReq);
        getJAXBBenef(poliza, objReq);
        getJAXBAgente(poliza, objReq);
        getJAXBObjAse(poliza, objReq);
        getJAXBDesc(poliza, objReq);
        getJAXBAjusteTar(poliza, objReq);
        getJAXBDerechos(poliza, objReq);
        getJAXBTotales(poliza, objReq);
        getJAXBNotificaciones(poliza, objReq);
        objEmiEOT.setPoliza(poliza);
        objReqE.setData(objEmiEOT);
        return objReqE;
    }

    /**
     * Gets the JAXB ajuste tar.
     *
     * @param poliza the poliza
     * @param objReq the obj req
     */
    private void getJAXBAjusteTar(final PolizaContratacionAutosSCE poliza, final EmiteNegReq objReq) {
        AjustesTarificacionContratacionAutosSCE objDesc = new AjustesTarificacionContratacionAutosSCE();
        List<DescuentoDto> descuentosDto = objReq.getMucPrimaAutoResp().getPETICION().get(0).getDATOSPRODUCTOS().get(0)
                .getDESCUENTO();
        List<ConceptoDto> conceptoDto = objReq.getMucPrimaAutoResp().getPETICION().get(0).getDATOSPRODUCTOS().get(0)
                .getDATOSCOTIZACION().get(0).getTOTALPRIMA().getDETALLEDESCUENTO();
        if (!descuentosDto.isEmpty() && !conceptoDto.isEmpty()) {

            BigDecimal mtoVaDesI = getConcepto(conceptoDto, VADESIGU);
            BigDecimal mtoVaDesR = getConcepto(conceptoDto, VADESREN);

            getJaxBDescIRen(objDesc, mtoVaDesI, mtoVaDesR);
            JAXBElement<AjustesTarificacionContratacionAutosSCE> descSCE = new ObjectFactory()
                    .createPolizaContratacionAutosSCEAjustesTarificacion(objDesc);
            if (BigDecimal.ZERO.compareTo(mtoVaDesI) != 0 || BigDecimal.ZERO.compareTo(mtoVaDesR) != 0) {
                poliza.getRest().add(descSCE);
            }
        }

    }

    /**
     * Gets the jax B desc I ren.
     *
     * @param objDesc   the obj desc
     * @param mtoVaDesI the mto va des I
     * @param mtoVaDesR the mto va des R
     */
    private void getJaxBDescIRen(final AjustesTarificacionContratacionAutosSCE objDesc, final BigDecimal mtoVaDesI,
            final BigDecimal mtoVaDesR) {
        if (BigDecimal.ZERO.compareTo(mtoVaDesI) != 0) {
            if (BigDecimal.ZERO.compareTo(mtoVaDesI) == 1) {
                objDesc.setMTODESCTOIGUALACION(mtoVaDesI.abs());
            } else {
                objDesc.setMTORECARGOIGUALACION(mtoVaDesI);
            }
        }

        if (BigDecimal.ZERO.compareTo(mtoVaDesR) != 0) {
            if (BigDecimal.ZERO.compareTo(mtoVaDesR) == 1) {
                objDesc.setMTODESCTOAJUSTERENOVACION(mtoVaDesR.abs());
            } else {
                objDesc.setMTORECARGOAJUSTERENOVACION(mtoVaDesR);
            }
        }

    }

    /**
     * Valida datos DN.
     *
     * @param emDatos the em datos
     * @param poliza  the poliza
     */
    private void validaDatosDN(final EmisionDatos emDatos, final PolizaContratacionAutosSCE poliza) {
        EmiteNegReq objReq = emDatos.getEmite();
        if (!objReq.getViaPago().equalsIgnoreCase(DN) && !objReq.getViaPago().equalsIgnoreCase(BN)) {
            return;
        }
        List<CobranzaUmo> cobranzas = emDatos.getUmo().getDominios().getCobranzas().getCobranzas();

        Optional<ElementoNeg> elemNeg = objReq.getElementos().stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(CVE_TIPO_NOMINA)).findFirst();
        if (elemNeg.isPresent()) {
            poliza.setCVETIPONOMINA(elemNeg.get().getClave());
        } else {
            throw new ExecutionError(Constantes.ERROR_4, "el tipo de nómina");
        }

        setContratoSADE(cobranzas, poliza, objReq.getViaPago());

        Optional<ElementoNeg> elemUt = objReq.getElementos().stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(UBICACION_TRABAJO)).findFirst();
        if (elemUt.isPresent()) {
            poliza.setUBICACIONTRABAJO(elemUt.get().getClave());
        } else {
            throw new ExecutionError(Constantes.ERROR_4, "la ubicacion de trabajo");
        }
    }

    /**
     * Sets the contrato SADE.
     *
     * @param cobranzas the cobranzas
     * @param poliza    the poliza
     * @param viaPago   the via pago
     */
    private static void setContratoSADE(final List<CobranzaUmo> cobranzas, final PolizaContratacionAutosSCE poliza,
            final String viaPago) {
        cobranzas.stream().filter(c -> c.getConductoCobro().getClave().equalsIgnoreCase(viaPago)).findFirst()
                .ifPresent(umo -> {
                    umo.getNominas().stream().filter(p -> String.valueOf(p.getTipoNominaNegocio().getId())
                            .equalsIgnoreCase(poliza.getCVETIPONOMINA())).findFirst().ifPresent(pr -> {
                                if ("DN".equalsIgnoreCase(umo.getConductoCobro().getClave().trim())) {
                                    poliza.setNUMCONTRATOSADE(pr.getTipoNominaNegocio().getIdCobranza().trim());
                                }
                                poliza.setCVEPERIODICIDADCOBRO(Optional
                                        .ofNullable(pr.getTipoNominaNegocio().getCvePeriodicidadCobro()).isPresent()
                                                ? pr.getTipoNominaNegocio().getCvePeriodicidadCobro().trim()
                                                : null);
                            });
                });

    }

    /**
     * Gets the JAXB fechas.
     *
     * @param poliza the poliza
     * @param objReq the obj req
     */
    private static void getJAXBFechas(final PolizaContratacionAutosSCE poliza, final EmiteNegReq objReq) {
        // FECHAS
        FechasPolizaAutosSCE fecpol = new FechasPolizaAutosSCE();
        fecpol.setFCHEFECTOMOVIMIENTO(Utileria.getGregCalShort(objReq.getFchEfectoMovimiento()));
        fecpol.setFCHFINEFECTOMOVIMIENTO(Utileria.getGregCalShort(objReq.getFchFinEfectoMovimiento()));
        fecpol.setFCHVIGENCIAFINAL(Utileria.getGregCalShort(objReq.getFinVig()));
        fecpol.setFCHVIGENCIAINICIAL(Utileria.getGregCalShort(objReq.getIniVig()));
        JAXBElement<FechasPolizaAutosSCE> fechasSCE = new ObjectFactory()
                .createPolizaContratacionAutosSCEFechas(fecpol);
        poliza.getRest().add(fechasSCE);
    }

    /**
     * Gets the JAXB estructura.
     *
     * @param poliza the poliza
     * @param objReq the obj req
     */
    private static void getJAXBEstructura(final PolizaContratacionAutosSCE poliza, final EmiteNegReq objReq) {
        EstructuraComercialContratacionAutosSCE objECom = new ObjectFactory()
                .createEstructuraComercialContratacionAutosSCE();
        objECom.setIDNEGOCIOOPERABLE(objReq.getIdUMO());
        if (objReq.getIdVersionNegocio() != null) {
            objECom.setVERSIONNEGOCIO(new BigInteger(objReq.getIdVersionNegocio().toString()));
        }
        if (objReq.getTipoCanalAcceso() != null) {
            validaVacio(objReq.getTipoCanalAcceso(), objECom, objReq);
        }

        if (objReq.getElementos() != null) {
            String puntoVenta = getElemento(objReq.getElementos(), Constantes.PUNTO_VENTA);
            if (!puntoVenta.isEmpty()) {
                objECom.setCVEFILIALSUBDEPENDENCIA(puntoVenta);
            }
        }

        JAXBElement<EstructuraComercialAutosSCE> estructuraComercialSCE = new ObjectFactory()
                .createPolizaContratacionAutosSCEEstructuraComercial(objECom);
        poliza.getRest().add(estructuraComercialSCE);
    }

    /**
     * Gets the JAXB referencia.
     *
     * @param poliza the poliza
     * @param objReq the obj req
     */
    private static void getJAXBReferencia(final PolizaContratacionAutosSCE poliza, final EmiteNegReq objReq) {
        if (objReq.getElementos() == null || objReq.getElementos().stream()
                .noneMatch(p -> p.getNombre().equalsIgnoreCase(Constantes.REFERENCIA_EXTERNA))) {
            return;
        }
        ReferenciaPolizaContratacionAutosSCE refPoliza = new ObjectFactory()
                .createReferenciaPolizaContratacionAutosSCE();
        refPoliza.setCVEREFERENCIAPOLIZAEXTERNA(getElemento(objReq.getElementos(), Constantes.REFERENCIA_EXTERNA));
        refPoliza.setREFERENCIAPOLIZAEXTERNA(getElementoValor(objReq.getElementos(), Constantes.REFERENCIA_EXTERNA));
        JAXBElement<ReferenciaPolizaContratacionAutosSCE> sceRefPoliza = new ObjectFactory()
                .createPolizaContratacionAutosSCEReferencia(refPoliza);
        poliza.getRest().add(sceRefPoliza);
    }

    /**
     * Valida vacio.
     *
     * @param tipoCanalAcceso the tipo canal acceso
     * @param objECom         the obj E com
     * @param objReq          the obj req
     */
    private static void validaVacio(final String tipoCanalAcceso, final EstructuraComercialContratacionAutosSCE objECom,
            final EmiteNegReq objReq) {
        if (!tipoCanalAcceso.equalsIgnoreCase(STRVACIO)) {
            objECom.setCVEEMPRESADEPENDENCIA(objReq.getTipoCanalAcceso());
        }
    }

    /**
     * Gets the JAXB producto.
     *
     * @param poliza     the poliza
     * @param objReq     the obj req
     * @param prodTecCom the prod tec com
     */
    private static void getJAXBProducto(final PolizaContratacionAutosSCE poliza, final EmiteNegReq objReq,
            final String prodTecCom) {
        if (objReq.getPaquetes().isEmpty()) {
            throw new ExecutionError(Constantes.ERROR_3, "El paquete");
        }
        ProductoAutosSCE objProdCA = new ProductoAutosSCE();
        objProdCA.setCVEPRODTECNICO(prodTecCom.split(":")[0]);
        objProdCA.setCVEPRODCOMERCIAL(prodTecCom.split(":")[1]);
        objProdCA.setIDPRODUCTOPERSONALIZADO(objReq.getPaquetes().get(0).getCvePaquete());
        JAXBElement<ProductoAutosSCE> productoSCE = new ObjectFactory()
                .createPolizaContratacionAutosSCEProducto(objProdCA);
        poliza.getRest().add(productoSCE);
    }

    /**
     * Gets the JAXB contr.
     *
     * @param poliza the poliza
     * @param objReq the obj req
     * @param urlCat the url cat
     */
    private static void getJAXBContr(final PolizaContratacionAutosSCE poliza, final EmiteNegReq objReq,
            final String urlCat) {
        ContratanteContratacionAutosSCE objContratante = new ContratanteContratacionAutosSCE();
        Optional<PersonaNeg> contNeg = objReq.getPersonas().stream()
                .filter(p -> p.getTipo().equalsIgnoreCase(STRCONTRATANTE)).findFirst();
        if (!contNeg.isPresent()) {
            throw new ExecutionError(Constantes.ERROR_3, "El contratante");
        }
        objContratante.setCorreo(obtenerMCMail(contNeg.get()));
        objContratante.setIDPARTICIPANTE(contNeg.get().getIdParticipante());
        objContratante.setCVECLIENTEORIGEN(contNeg.get().getCveClienteOrigen());
        objContratante.setDireccion(obtenerDireccion(contNeg.get().getDomicilio(), urlCat));
        if (contNeg.get().getTipoPersona().equalsIgnoreCase(STRFISICA)) {
            objContratante.setAPELLIDOPATERNO(contNeg.get().getAPaterno());
            if (Utileria.existeValor(contNeg.get().getAMaterno())) {
                objContratante.setAPELLIDOMATERNO(contNeg.get().getAMaterno());
            }
            objContratante.setNOMBRES(contNeg.get().getNombre());
        } else {
            objContratante.setRAZONSOCIAL(contNeg.get().getRazonSocial());
        }
        objContratante.setRFC(contNeg.get().getRfc());
        Optional<MedioContactoNeg> medioContactoTel = contNeg.get().getMediosContacto().stream()
                .filter(p -> p.getElementos().stream().anyMatch(q -> q.getClave().equalsIgnoreCase(STRTELEFONO)))
                .findFirst();
        if (medioContactoTel.isPresent()) {
            TelefonoContratacionAutosSCE tel = new TelefonoContratacionAutosSCE();
            tel.setCVEPROPOSITOCONTACTO("NOT");
            tel.setIDMEDIOCONTACTO(getElemento(medioContactoTel.get().getElementos(), "ID_MEDIO_CONTACTO"));
            tel.setLADANAC(getElemento(medioContactoTel.get().getElementos(), "CVE_LADA_NACIONAL"));
            tel.setNUMTELEFONO(getElemento(medioContactoTel.get().getElementos(), "NUMERO_TELEFONO"));
            tel.setVALORCVEORIGEN(getElemento(medioContactoTel.get().getElementos(), "CVE_VALOR_ORIGEN"));
            objContratante.setTelefono(tel);
        }
        objContratante.setTIPOPARTICIPANTE(contNeg.get().getTipoPersona());
        if (objReq.getElementos() != null && !objReq.getElementos().isEmpty()) {
            Optional<ElementoNeg> eleAccNomina = objReq.getElementos().stream()
                    .filter(p -> "LLAVE_ACCESO_NOMINA".equalsIgnoreCase(p.getNombre())).findFirst();
            if (eleAccNomina.isPresent()) {
                objContratante.setLLAVEACCESONOMINA(eleAccNomina.get().getValor());
            }
        }
        JAXBElement<ContratanteContratacionAutosSCE> contratanteSCE = new ObjectFactory()
                .createPolizaContratacionAutosSCEContratante(objContratante);
        poliza.getRest().add(contratanteSCE);
    }

    /**
     * Obtener MC mail.
     *
     * @param contNeg the cont neg
     * @return the correo contratacion autos SCE
     */
    private static CorreoContratacionAutosSCE obtenerMCMail(final PersonaNeg contNeg) {
        Optional<MedioContactoNeg> medioContacto = contNeg.getMediosContacto().stream()
                .filter(p -> p.getElementos().stream().anyMatch(q -> q.getClave().equalsIgnoreCase(STRMAIL)))
                .findFirst();
        if (medioContacto.isPresent()) {
            CorreoContratacionAutosSCE mail = new CorreoContratacionAutosSCE();
            mail.setCORREOELECTRONICO(getElemento(medioContacto.get().getElementos(), STRCORREO));
            mail.setCVEPROPOSITOCONTACTO("NOT");
            mail.setIDMEDIOCONTACTO(getElemento(medioContacto.get().getElementos(), "ID_MEDIO_CONTACTO"));
            mail.setVALORCVEORIGEN(getElemento(medioContacto.get().getElementos(), "CVE_VALOR_ORIGEN"));
            return mail;
        } else {
            return null;
        }
    }

    /**
     * Obtener direccion.
     *
     * @param objDom the obj dom
     * @param urlCat the url cat
     * @return the direccion contratacion autos SCE
     */
    private static DireccionContratacionAutosSCE obtenerDireccion(final DomicilioNeg objDom, final String urlCat) {
        DireccionContratacionAutosSCE dir = new DireccionContratacionAutosSCE();
        dir.setCALLE(objDom.getCalle());
        dir.setCODIGOPOSTAL(objDom.getCodigoPostal());
        dir.setCOLONIA(objDom.getColonia());
        dir.setCVEPAIS(objDom.getPais());
        dir.setCVEPROPOSITOCONTACTO("NOT");
        dir.setCVETIPOVIA(objDom.getCveTipoVia());
        CatalogoReq req = new CatalogoReq();
        req.setTipoCatalogo("ESTADO");
        List<ElementoReq> elementos = new ArrayList<>();
        elementos.add(new ElementoReq("ESTADO", objDom.getEstado(), objDom.getEstado()));
        req.setElementos(elementos);
        CatalogoResp catResp = CatalogoClient.getInstancia(req, restTemplate).obtenCatalogo(urlCat);
        if (!catResp.getElementos().isEmpty()) {
            dir.setESTADO(catResp.getElementos().get(0).getNombre());
        }
        dir.setIDDIRECCION(new BigInteger(Utils.ifEmpty(objDom.getIdDomicilio(), STRUNO)));
        dir.setMUNICIPIO(objDom.getMunicipio());
        if (Utileria.existeValor(objDom.getNumExterior())) {
            dir.setNUMEXTERIOR(objDom.getNumExterior());
        }
        if (Utileria.existeValor(objDom.getNumInterior())) {
            dir.setNUMINTERIOR(objDom.getNumInterior());
        }
        dir.setPAIS(objDom.getPais());
        dir.setVALORCVEORIGEN(objDom.getCveOrigen());
        return dir;
    }

    /**
     * Gets the JAXB medio cobro.
     *
     * @param poliza the poliza
     * @param objReq the obj req
     */
    private static void getJAXBMedioCobro(final PolizaContratacionAutosSCE poliza, final EmiteNegReq objReq) {
        MedioCobroContratacionAutosSCE objMC = new MedioCobroContratacionAutosSCE();
        Optional<PersonaNeg> contNeg = objReq.getPersonas().stream()
                .filter(p -> p.getTipo().equalsIgnoreCase(STRCONTRATANTE)).findFirst();
        MedioCobroNeg mcNeg = contNeg.map(c -> c.getMedioCobro()).orElse(null);
        if (mcNeg != null && mcNeg.getNumTarjeta() != null) {
            objMC.setCUENTAFINANCIERA(mcNeg.getCuentaFinanciera());
            objMC.setCVEENTIDADFINANCIERA(mcNeg.getCveEntidadFinanciera());
            objMC.setCVETIPOCUENTATARJETA(mcNeg.getCveTipoCuentaTarjeta());
            objMC.setCVETIPODATOBANCARIO(mcNeg.getCveTipoBancario());
            objMC.setFCHVENCIMIENTOTARJETA(mcNeg.getFchVencimiento());
            if (mcNeg.getIdCuentaFinanciera() == null || mcNeg.getIdCuentaFinanciera().isEmpty()) {
                objMC.setIDCUENTAFINANCIERA("1");
            } else {
                objMC.setIDCUENTAFINANCIERA(mcNeg.getIdCuentaFinanciera());
            }
            objMC.setNUMTARJETA(mcNeg.getNumTarjeta());
            JAXBElement<MedioCobroContratacionAutosSCE> mCSCE = new ObjectFactory()
                    .createPolizaContratacionAutosSCEMedioCobro(objMC);
            poliza.getRest().add(mCSCE);
        }
    }

    /**
     * Gets the JAXB benef.
     *
     * @param poliza the poliza
     * @param objReq the obj req
     */
    private static void getJAXBBenef(final PolizaContratacionAutosSCE poliza, final EmiteNegReq objReq) {
        BeneficiariosContratacionAutosSCE objBeneficiarios = new BeneficiariosContratacionAutosSCE();
        BeneficiarioContratacionAutosSCE ben = new BeneficiarioContratacionAutosSCE();
        Optional<PersonaNeg> benefNeg = objReq.getPersonas().stream()
                .filter(p -> p.getTipo().equalsIgnoreCase(STRBENEFICIARIO)).findFirst();
        if (!benefNeg.isPresent()) {
            throw new ExecutionError(Constantes.ERROR_4, "El beneficiario");
        }
        ben.setBANIRREVOCABLE(false);
        if (benefNeg.get().getBanIrrevocable().equals(STRUNO)) {
            ben.setBANIRREVOCABLE(true);
        }
        ben.setCVECLIENTEORIGEN(benefNeg.get().getCveClienteOrigen());
        ben.setIDPARTICIPANTE(benefNeg.get().getIdParticipante());
        if (benefNeg.get().getPctBeneficio() != null) {
            ben.setPCTBENEFICIO(new BigDecimal(benefNeg.get().getPctBeneficio()));
        }
        ben.setSECBENEFICIARIO(new BigInteger(
                benefNeg.get().getSecBeneficiario() != null ? benefNeg.get().getSecBeneficiario() : "1"));
        if (benefNeg.get().getCveTextoBeneficiario() != null) {
            ben.setCVETEXTOBENEFICIARIO(benefNeg.get().getCveTextoBeneficiario());
        }
        objBeneficiarios.getBeneficiario().add(ben);
        JAXBElement<BeneficiariosContratacionAutosSCE> beneficiariosSCE = new ObjectFactory()
                .createPolizaContratacionAutosSCEBeneficiarios(objBeneficiarios);
        poliza.getRest().add(beneficiariosSCE);
    }

    /**
     * Gets the JAXB agente.
     *
     * @param poliza the poliza
     * @param objReq the obj req
     */
    private static void getJAXBAgente(final PolizaContratacionAutosSCE poliza, final EmiteNegReq objReq) {
        AgentesContratacionAutosSCE objAgentes = new AgentesContratacionAutosSCE();
        objReq.getAgentes().stream().forEach(p -> objAgentes.getAgente().add(getAgenteSCE(p)));
        JAXBElement<AgentesContratacionAutosSCE> agentesSCE = new ObjectFactory()
                .createPolizaContratacionAutosSCEAgentes(objAgentes);
        poliza.getRest().add(agentesSCE);
    }

    /**
     * Gets the agente SCE.
     *
     * @param agNeg the ag neg
     * @return the agente SCE
     */
    private static AgenteContratacionAutosSCE getAgenteSCE(final AgenteNeg agNeg) {
        AgenteContratacionAutosSCE agent = new AgenteContratacionAutosSCE();
        agent.setIDPARTICIPANTE(agNeg.getIdParticipante());
        agent.setBANAGENTEPRINCIPAL(false);
        if (agNeg.getBanIntermediarioPrincipal() != null && agNeg.getBanIntermediarioPrincipal().equals(STRUNO)) {
            agent.setBANAGENTEPRINCIPAL(true);
        }
        agent.setCODINTERMEDIARIO(agNeg.getCodIntermediario());
        agent.setCVECLASEINTERMEDIARIOINFO(agNeg.getCveClaseIntermediarioInfo());
        agent.setCVEOFICINADIRECCIONAGENCIA(agNeg.getCveOficinaDireccionAgencia());
        agent.setFOLIO(agNeg.getFolio());
        agent.setIDTIPOBASECOMISION(agNeg.getIdTipoBaseComision());
        agent.setPCTCESIONCOMISION(
                new BigDecimal(String.valueOf(Double.parseDouble(agNeg.getPctCesionComision()) / CIEN)));
        agent.setPCTPARTICIPCOMISION(
                new BigDecimal(String.valueOf(Double.parseDouble(agNeg.getPctParticipComision()) / CIEN)));
        agent.setPCTCOMISIONPRIMA(
                new BigDecimal(String.valueOf(Double.parseDouble(agNeg.getPctComisionPrima()) / CIEN)));
        return agent;
    }

    /**
     * Gets the JAXB obj ase.
     *
     * @param poliza the poliza
     * @param objReq the obj req
     */
    private void getJAXBObjAse(final PolizaContratacionAutosSCE poliza, final EmiteNegReq objReq) {
        ObjetoAseguradoContratacionAutosSCE objOA = new ObjetoAseguradoContratacionAutosSCE();
        Optional<ProductosDto> paqTarResp = objReq.getMucPrimaAutoResp().getPETICION().get(0).getDATOSPRODUCTOS()
                .stream().filter(p -> p.getIDPRODUCTO().equals(objReq.getPaquetes().get(0).getCvePaquete()))
                .findFirst();
        Optional<DatosProductoDto> paqTarReq = objReq.getMucPrimaAutoReq().getDATOSCOTIZACION().get(0)
                .getDATOSPRODUCTO().stream()
                .filter(p -> p.getIDPRODUCTO().equalsIgnoreCase(objReq.getPaquetes().get(0).getCvePaquete()))
                .findFirst();
        if (!paqTarResp.isPresent() || !paqTarReq.isPresent()) {
            throw new ExecutionError(Constantes.ERROR_4, "El paquete");
        }
        List<CoberturaPrimaDto> coberturasPrimaDto = paqTarResp.get().getDATOSCOTIZACION().get(0).getCOBERTURAPRIMA();
        List<CoberturaDto> cobsDto = paqTarReq.get().getCOBERTURA();
        CoberturasContratacionAutosSCE cobs = new CoberturasContratacionAutosSCE();
        List<CoberturaContratacionAutosSCE> coberturas = coberturasPrimaDto.stream()
                .map(p -> getCoberturasSCE(p, cobsDto, objReq)).collect(Collectors.toCollection(ArrayList::new));
        cobs.getCobertura().addAll(coberturas);
        objOA.setCoberturas(cobs);
        fillPersonas(objReq, objOA);
        VehiculoNeg vehNeg = objReq.getVehiculo();
        fillVehiculo(vehNeg, objOA);
        JAXBElement<ObjetoAseguradoContratacionAutosSCE> oASCE = new ObjectFactory()
                .createPolizaContratacionAutosSCEObjetoAsegurado(objOA);
        poliza.getRest().add(oASCE);
    }

    /**
     * Fill vehiculo.
     *
     * @param vehNeg the veh neg
     * @param objOA  the obj OA
     */
    private void fillVehiculo(final VehiculoNeg vehNeg, final ObjetoAseguradoContratacionAutosSCE objOA) {
        VehiculoContratacionAutosSCE veh = new VehiculoContratacionAutosSCE();
        if (vehNeg.getAdaptaciones() != null) {
            AdaptacionesConversionesContratacionAutosSCE adap = new AdaptacionesConversionesContratacionAutosSCE();
            List<AdaptacionConversionAutosSCE> adaptaciones = vehNeg.getAdaptaciones().stream()
                    .map(this::getAdaptacionesSCE).collect(Collectors.toCollection(ArrayList::new));
            adap.getAdaptacionConversion().addAll(adaptaciones);
            veh.setAdaptacionesConversiones(adap);
        }
        if (vehNeg.getAltoRiesgo() != null && vehNeg.getAltoRiesgo().equals(STRUNO)) {
            veh.setBANVEHICULOALTORIESGO(true);
        }
        veh.setCODIGOPOSTAL(vehNeg.getCp());
        veh.setCVEARMADORAVEHICULO(vehNeg.getArmadora());
        veh.setCVECARROCERIAVEHICULO(vehNeg.getCarroceria());
        veh.setCVEENTIDADCIRCULA(Utileria.rellenarCero(vehNeg.getEstadoCirculacion(), "3"));
        veh.setCVEFORMAINDEMNIZACION(vehNeg.getFormaIndemnizacion());
        if (vehNeg.getTipoCarga() != null && !vehNeg.getTipoCarga().isEmpty()) {
            veh.setCVETIPOCARGAVEHICULO(vehNeg.getTipoCarga());
        }
        veh.setCVETIPOVEHICULO(vehNeg.getTipoVehiculo());
        veh.setCVEUSOVEHICULO(vehNeg.getUso());
        veh.setCVEVERSIONVEHICULO(vehNeg.getVersion());
        if (vehNeg.getDescripcionFactura() != null && !vehNeg.getDescripcionFactura().isEmpty()) {
            veh.setDESCRIPCIONFACTURAVEHICULO(vehNeg.getDescripcionFactura());
        }
        CatalogoReq req = new CatalogoReq();
        req.setTipoCatalogo("VEHICULOS");
        List<ElementoReq> elementos = new ArrayList<>();
        elementos.add(new ElementoReq("TIPO_VEHICULO", vehNeg.getTipoVehiculo(), vehNeg.getTipoVehiculo()));
        elementos.add(new ElementoReq("ARMADORA", vehNeg.getArmadora(), vehNeg.getArmadora()));
        elementos.add(new ElementoReq("CARROCERIA", vehNeg.getCarroceria(), vehNeg.getCarroceria()));
        elementos.add(new ElementoReq(STRVERSION, vehNeg.getVersion(), vehNeg.getVersion()));
        elementos.add(new ElementoReq("MODELO", vehNeg.getModelo(), vehNeg.getModelo()));
        req.setElementos(elementos);
        CatalogoResp catResp = CatalogoClient.getInstancia(req, restTemplate).obtenCatalogo(conf.getUrlCatalogo());
        Optional<ElementoReq> elementoV = catResp.getElementos().stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(STRVERSION)).findFirst();
        veh.setMARCAVEHICULO(elementoV.map(e -> e.getValor()).orElse(null));
        veh.setVALORVEHICULO(new BigDecimal(vehNeg.getValorVehiculo()));
        fillVehiculoAdic(veh, vehNeg);
        objOA.setVehiculo(veh);
    }

    /**
     * Fill vehiculo adic.
     *
     * @param veh    the veh
     * @param vehNeg the veh neg
     */
    private static void fillVehiculoAdic(final VehiculoContratacionAutosSCE veh, final VehiculoNeg vehNeg) {
        veh.setMODELOVEHICULO(new BigInteger(vehNeg.getModelo()));
        veh.setNUMEROMOTOR(vehNeg.getMotor());
        if (vehNeg.getNumPasajeros() != null) {
            veh.setNUMEROPASAJEROS(new BigInteger(vehNeg.getNumPasajeros()));
        }
        if (Utileria.existeValor(vehNeg.getPlacas())) {
            veh.setNUMEROPLACA(vehNeg.getPlacas());
        }
        veh.setNUMEROSERIE(vehNeg.getSerie());
    }

    /**
     * Fill personas.
     *
     * @param objReq the obj req
     * @param objOA  the obj OA
     */
    private void fillPersonas(final EmiteNegReq objReq, final ObjetoAseguradoContratacionAutosSCE objOA) {
        if (!objReq.getIndContConductor().equals(STRUNO)) {
            fillConductor(objReq, objOA);
        } else {
            objReq.getPersonas().stream().filter(p -> p.getTipo().equalsIgnoreCase(STRCONTRATANTE)).findFirst()
                    .ifPresent(c -> {
                        ConductorHabitualContratacionAutosSCE cond = new ConductorHabitualContratacionAutosSCE();
                        if (Utileria.existeValor(c.getAMaterno())) {
                            cond.setAPELLIDOMATERNO(c.getAMaterno());
                        }
                        cond.setAPELLIDOPATERNO(c.getAPaterno());
                        cond.setCVECLIENTEORIGEN(c.getCveClienteOrigen());
                        cond.setCVESEXO(c.getSexo());
                        if (c.getEdad() != null && !c.getEdad().isEmpty()) {
                            cond.setEDADCONDUCTORHABITUAL(new BigInteger(c.getEdad()));
                        } else {
                            cond.setEDADCONDUCTORHABITUAL(
                                    new BigInteger(String.valueOf(Utileria.getEdad(c.getFecNacimiento()))));
                        }
                        cond.setFCHNACIMIENTO(Utileria.getGregCalShort(c.getFecNacimiento()));
                        cond.setIDPARTICIPANTE(c.getIdParticipante());
                        cond.setNOMBRES(c.getNombre());
                        cond.setRFC(c.getRfc());
                        cond.setSEXO(c.getSexo());
                        objOA.setConductorHabitual(cond);
                    });
        }
    }

    /**
     * Fill conductor.
     *
     * @param objReq the obj req
     * @param objOA  the obj OA
     */
    private void fillConductor(final EmiteNegReq objReq, final ObjetoAseguradoContratacionAutosSCE objOA) {
        Optional<PersonaNeg> condNeg = objReq.getPersonas().stream()
                .filter(p -> p.getTipo().equalsIgnoreCase(STRCONDUCTOR)).findFirst();
        if (!condNeg.isPresent()) {
            throw new ExecutionError(Constantes.ERROR_4, "El conductor");
        }
        ConductorHabitualContratacionAutosSCE cond = new ConductorHabitualContratacionAutosSCE();
        if (Utileria.existeValor(condNeg.get().getAMaterno())) {
            cond.setAPELLIDOMATERNO(condNeg.get().getAMaterno());
        }

        cond.setAPELLIDOPATERNO(condNeg.get().getAPaterno());
        cond.setCVECLIENTEORIGEN(condNeg.get().getCveClienteOrigen());
        cond.setCVESEXO(condNeg.get().getSexo());
        cond.setEDADCONDUCTORHABITUAL(new BigInteger(condNeg.get().getEdad()));
        cond.setFCHNACIMIENTO(Utileria.getGregCalShort(condNeg.get().getFecNacimiento()));
        cond.setIDPARTICIPANTE(condNeg.get().getIdParticipante());
        cond.setNOMBRES(condNeg.get().getNombre());
        cond.setRFC(condNeg.get().getRfc());
        objOA.setConductorHabitual(cond);
    }

    /**
     * Gets the coberturas SCE.
     *
     * @param cobPrimaDto   the cob prima dto
     * @param coberturasDto the coberturas dto
     * @param objReq        the obj req
     * @return the coberturas SCE
     */
    private static CoberturaContratacionAutosSCE getCoberturasSCE(final CoberturaPrimaDto cobPrimaDto,
            final List<CoberturaDto> coberturasDto, final EmiteNegReq objReq) {
        CoberturaContratacionAutosSCE cob = new CoberturaContratacionAutosSCE();
        cob.setCVECOBERTURA(cobPrimaDto.getCLAVECOBERTURA());
        Optional<CoberturaDto> cobOpc = coberturasDto.stream()
                .filter(p -> p.getCLAVECOBERTURA().equalsIgnoreCase(cobPrimaDto.getCLAVECOBERTURA())).findFirst();
        cobOpc.ifPresent(opc -> {
            Optional<ModificadorCoberturaDto> sa = opc.getMODIFICADOR().stream()
                    .filter(p -> p.getCLAVEMODIFICADOR().equalsIgnoreCase(STRCPASEGUR)).findFirst();
            if (sa.isPresent()) {
                cob.setMTOSUMAASEGURADA(Utileria.getDecimalFormat(sa.get().getVALORREQUERIDO(), 2));
                cob.setUNIDADMEDIDASA(sa.get().getUNIDADMEDIDA());
            } else {
                for (int cnt = 0; cnt < objReq.getPaquetes().get(0).getCoberturas().size(); cnt++) {
                    CoberturaNeg cne = objReq.getPaquetes().get(0).getCoberturas().get(cnt);
                    if (cobPrimaDto.getCLAVECOBERTURA().equalsIgnoreCase(cne.getCveCobertura())
                            && "999999.0".equalsIgnoreCase(cne.getSa())) {
                        cob.setMTOSUMAASEGURADA(Utileria.getDecimalFormat(new Double("999999.0"), 2));
                        cob.setUNIDADMEDIDASA("IMPT");
                        break;
                    }
                }
            }
            Optional<ModificadorCoberturaDto> ded = opc.getMODIFICADOR().stream()
                    .filter(p -> p.getCLAVEMODIFICADOR().equalsIgnoreCase(STRCDDEDUCI)).findFirst();
            if (ded.isPresent()) {
                cob.setPCTDEDUCIBLE(Utileria.getDecimalFormat(ded.get().getVALORREQUERIDO(), 2));
                cob.setUNIDADMEDIDADEDUCI(ded.get().getUNIDADMEDIDA());
            }
            Optional<ModificadorCoberturaDto> dedAR = opc.getMODIFICADOR().stream()
                    .filter(p -> p.getCLAVEMODIFICADOR().equalsIgnoreCase(STRCDDEDUCIAR)).findFirst();
            if (dedAR.isPresent()) {
                cob.setPCTDEDUCIBLEALTORIESGO(Utileria.getDecimalFormat(dedAR.get().getVALORREQUERIDO(), 2));
                cob.setUNIDADMEDIDADEDUCI(dedAR.get().getUNIDADMEDIDA());
            }
        });
        cob.setFACTORBASE(getConcepto(cobPrimaDto.getCONCEPTOECONOMICO(), "FACTOR_BASE"));
        cob.setPRIMABASE(getConcepto(cobPrimaDto.getCONCEPTOECONOMICO(), "PRIMA_BASE"));
        TarificacionCoberturaContratacionAutosSCE tarificacion = new TarificacionCoberturaContratacionAutosSCE();
        tarificacion.setIMPPRIMATOTALACTUAL(getConcepto(cobPrimaDto.getPRIMA(), "PRIMA_TOTAL"));
        tarificacion.setMTOIVA(getConcepto(cobPrimaDto.getCONCEPTOECONOMICO(), "IVA"));
        tarificacion.setMTOPRIMACOMERCIAL(getConcepto(cobPrimaDto.getPRIMA(), "PRIMA_COMERCIAL"));
        tarificacion.setMTOPRIMAFINAL(getConcepto(cobPrimaDto.getPRIMA(), "PRIMA_FINAL"));
        tarificacion.setMTOPRIMANETA(getConcepto(cobPrimaDto.getPRIMA(), STR_PNET));
        tarificacion.setMTOPRIMATECNICA(getConcepto(cobPrimaDto.getPRIMA(), STR_PTEC));
        tarificacion.setMTORECARGOPAGOFRACC(getConcepto(cobPrimaDto.getCONCEPTOECONOMICO(), STR_RFRAC));

        BigDecimal descuento = cobPrimaDto.getDETALLEDESCUENTO().stream().map(m -> new BigDecimal(m.getMONTO()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Double porcCesionCom = objReq.getMucPrimaAutoReq().getDATOSCOTIZACION().get(0).getDATOSSOLICITUD()
                .getPORCENTAJECESIONCOMISION();

        tarificacion.setMTOCESIONCOMDEUDORPRIMA(getConcepto(cobPrimaDto.getCONCEPTOECONOMICO(), STR_CED));
        tarificacion.setPCTCESIONCOMISION(BigDecimal.valueOf(porcCesionCom / Constantes.CIEN));

        tarificacion.setMTODESCUENTOCOMERCIAL(descuento);
        tarificacion.setMTOGASTOS(getConcepto(cobPrimaDto.getCONCEPTOECONOMICO(), "MTO_GASTOS"));
        tarificacion.setMTOIVAGASTOS(getConcepto(cobPrimaDto.getCONCEPTOECONOMICO(), "MTO_IVAGASTOS"));
        tarificacion.setMTOTOTALGASTOS(getConcepto(cobPrimaDto.getCONCEPTOECONOMICO(), "MTO_TOTAL_GASTOS"));
        tarificacion.setMTORECARGOGASTOS(getConcepto(cobPrimaDto.getCONCEPTOECONOMICO(), "MTO_RECARGOGASTOS"));

        cob.setTarificacion(tarificacion);
        return cob;
    }

    /**
     * Gets the concepto.
     *
     * @param cobPrimaDto the cob prima dto
     * @param nombre      the nombre
     * @return the concepto
     */
    private static BigDecimal getConcepto(final List<ConceptoDto> cobPrimaDto, final String nombre) {
        Optional<ConceptoDto> concDto = cobPrimaDto.stream().filter(p -> p.getNOMBRE().equalsIgnoreCase(nombre))
                .findFirst();
        if (concDto.isPresent()) {
            return new BigDecimal(concDto.get().getMONTO());
        }
        return new BigDecimal("0");
    }

    /**
     * Gets the adaptaciones SCE.
     *
     * @param adpNeg the adp neg
     * @return the adaptaciones SCE
     */
    public AdaptacionConversionAutosSCE getAdaptacionesSCE(final AdaptacionVehNeg adpNeg) {
        AdaptacionConversionAutosSCE adp = new AdaptacionConversionAutosSCE();
        if (adpNeg.getBanEquip().equals(STRUNO)) {
            adp.setBANEQUIPAMIENTOBLINDAJE(true);
        }
        adp.setDESCRIPCIONEQUIPAMIENTO(adpNeg.getDescEquip());
        adp.setFCHFACTURAEQUIPAMIENTO(Utileria.getGregCalShort(adpNeg.getFechaFactura()));
        adp.setMTOFACTURACION(new BigDecimal(adpNeg.getMontoFacturacion().replaceAll(",", "")));
        adp.setMTOSUMAASEGURADA(new BigDecimal(adpNeg.getMontoSA().replaceAll(",", "")));
        return adp;
    }

    /**
     * Gets the JAXB derechos.
     *
     * @param poliza the poliza
     * @param objReq the obj req
     */
    private static void getJAXBDerechos(final PolizaContratacionAutosSCE poliza, final EmiteNegReq objReq) {
        DerechosPolizaContratacionAutosSCE objDerechos = new DerechosPolizaContratacionAutosSCE();
        objDerechos.setINDCOBRODERECHOPOLIZA(objReq.getMucPrimaAutoResp().getPETICION().get(0).getDATOSPRODUCTOS()
                .get(0).getDATOSCOTIZACION().get(0).getFORMAPAGORECIBO().getPOLITICADERECHOPOLIZA());
        CotizacionDto cotizaDto = objReq.getMucPrimaAutoResp().getPETICION().get(0).getDATOSPRODUCTOS().get(0)
                .getDATOSCOTIZACION().get(0);
        objDerechos.setMTODERECHOPOLIZA(getConcepto(cotizaDto.getTOTALPRIMA().getCONCEPTOECONOMICO(), STR_DERPOL));
        objDerechos.setMTOIVADERECHOPOLIZA(
                getConcepto(cotizaDto.getDETALLEDERECHOS().get(0).getCONCEPTOECONOMICO(), "IVA_DERECHOS_POLIZA"));
        objDerechos.setMTORECARGODERECHOPOLIZA(
                getConcepto(cotizaDto.getDETALLEDERECHOS().get(0).getCONCEPTOECONOMICO(), "RECARGO_DERECHOS"));
        JAXBElement<DerechosPolizaContratacionAutosSCE> derechosSCE = new ObjectFactory()
                .createPolizaContratacionAutosSCEDerechos(objDerechos);
        poliza.getRest().add(derechosSCE);
    }

    /**
     * Gets the JAXB totales.
     *
     * @param poliza the poliza
     * @param objReq the obj req
     */
    private static void getJAXBTotales(final PolizaContratacionAutosSCE poliza, final EmiteNegReq objReq) {
        TotalesTarificacionPolizaContratacionAutosSCE objTotales = new TotalesTarificacionPolizaContratacionAutosSCE();
        CotizacionDto cotizaDto = objReq.getMucPrimaAutoResp().getPETICION().get(0).getDATOSPRODUCTOS().get(0)
                .getDATOSCOTIZACION().get(0);
        objTotales.setIMPPRIMATOTALACTUAL(new BigDecimal(cotizaDto.getFORMAPAGORECIBO().getMONTOTOTAL().toString()));
        objTotales.setMTOCESIONCOMDEUDORPRIMA(getConcepto(cotizaDto.getTOTALPRIMA().getCONCEPTOECONOMICO(), STR_CED));

        BigDecimal descuento = cotizaDto.getTOTALPRIMA().getDETALLEDESCUENTO().stream()
                .map(m -> new BigDecimal(m.getMONTO())).reduce(BigDecimal.ZERO, BigDecimal::add);

        objTotales.setMTODESCUENTOCOMERCIAL(descuento);

        objTotales.setMTOIVA(getConcepto(cotizaDto.getTOTALPRIMA().getCONCEPTOECONOMICO(), "IVA"));
        objTotales.setMTOPRIMANETA(getConcepto(cotizaDto.getTOTALPRIMA().getPRIMA(), STR_PNET));
        objTotales.setMTORECARGOPAGOFRACC(getConcepto(cotizaDto.getTOTALPRIMA().getCONCEPTOECONOMICO(), STR_RFRAC));
        objTotales.setMTOSUMASINIESTRALIDAD(new BigDecimal("0"));
        Double pctIVA = Double.parseDouble(objTotales.getMTOIVA().toString())
                / (Double.parseDouble(objTotales.getIMPPRIMATOTALACTUAL().toString())
                        - Double.parseDouble(objTotales.getMTOIVA().toString()))
                * CIEN;
        objTotales.setPCTZONAIVA(new BigDecimal(pctIVA.toString()).setScale(0, BigDecimal.ROUND_HALF_EVEN));
        JAXBElement<TotalesTarificacionPolizaContratacionAutosSCE> totalesSCE = new ObjectFactory()
                .createPolizaContratacionAutosSCETotalesTarificacion(objTotales);
        poliza.getRest().add(totalesSCE);
    }

    /**
     * Gets the JAXB desc.
     *
     * @param poliza the poliza
     * @param objReq the obj req
     */
    private static void getJAXBDesc(final PolizaContratacionAutosSCE poliza, final EmiteNegReq objReq) {
        DescuentosContratacionAutosSCE objDesc = new DescuentosContratacionAutosSCE();
        List<DescuentoDto> descuentosDto = objReq.getMucPrimaAutoResp().getPETICION().get(0).getDATOSPRODUCTOS().get(0)
                .getDESCUENTO();
        List<ConceptoDto> conceptoDto = objReq.getMucPrimaAutoResp().getPETICION().get(0).getDATOSPRODUCTOS().get(0)
                .getDATOSCOTIZACION().get(0).getTOTALPRIMA().getDETALLEDESCUENTO();
        if (!descuentosDto.isEmpty() && !conceptoDto.isEmpty()) {
            objDesc.setPCTDESCTOVOLUMEN(getDescuentoVal(descuentosDto, VADESVOL));
            objDesc.setMTODESCTOVOLUMEN(getDescuentoMonto(conceptoDto, VADESVOL));
            objDesc.setPCTDESCTOCAMPANA(getDescuentoVal(descuentosDto, PODESCAM));
            objDesc.setMTODESCTOCAMPANA(getDescuentoMonto(conceptoDto, PODESCAM));
            objDesc.setPCTDESCTOCLIENTEINTEGRAL(getDescuentoVal(descuentosDto, "PODESCLI"));
            objDesc.setMTODESCTOCLIENTEINTEGRAL(getDescuentoMonto(conceptoDto, "PODESCLI"));
            objDesc.setMTODESCTOCLIENTELEAL(getDescuentoVal(descuentosDto, "PODESMVE"));
            objDesc.setMTODESCTOCLIENTELEAL(getDescuentoMonto(conceptoDto, "PODESMVE"));
            objDesc.setPCTDESCTOEXPSINIESTRAL(getDescuentoVal(descuentosDto, "PODEXPSI"));
            objDesc.setPCTDESCTOEXPSINIESTRAL(getDescuentoMonto(conceptoDto, "PODEXPSI"));
            objDesc.setPCTDESCTOPAGOANTICIPADO(getDescuentoVal(descuentosDto, "TCVADEPA"));
            objDesc.setMTODESCTOPAGOANTICIPADO(getDescuentoMonto(conceptoDto, "TCVADEPA"));
            objDesc.setPCTDESCTOFUERZAPRODUCTORA(getDescuentoVal(descuentosDto, "POFUEPRO"));
            objDesc.setMTODESCTOFUERZAPRODUCTORA(getDescuentoMonto(conceptoDto, "POFUEPRO"));
            objDesc.setPCTDESCTOMEDICOCIRCULO(getDescuentoVal(descuentosDto, "POMEDCON"));
            objDesc.setMTODESCTOMEDICOCIRCULO(getDescuentoMonto(conceptoDto, "POMEDCON"));
            objDesc.setPCTDESCTOEXCEPCIONRIESGO(getDescuentoVal(descuentosDto, "VAEXCRIE"));
            objDesc.setMTODESCTOEXCEPCIONRIESGO(getDescuentoMonto(conceptoDto, "VAEXCRIE"));
            objDesc.setPCTDESCTOGESTIONCOMERCIAL(getDescuentoVal(descuentosDto, "VAGESCOM"));
            objDesc.setMTODESCTOGESTIONCOMERCIAL(getDescuentoMonto(conceptoDto, "VAGESCOM"));
            objDesc.setPCTDESCTOSEGUROPREVIO(getDescuentoVal(descuentosDto, "POVASEGP"));
            objDesc.setMTODESCTOSEGUROPREVIO(getDescuentoMonto(conceptoDto, "POVASEGP"));
            JAXBElement<DescuentosContratacionAutosSCE> descSCE = new ObjectFactory()
                    .createPolizaContratacionAutosSCEDescuentos(objDesc);
            poliza.getRest().add(descSCE);
        }
    }

    /**
     * Gets the descuento val.
     *
     * @param descuentos the descuentos
     * @param clave      the clave
     * @return the descuento val
     */
    private static BigDecimal getDescuentoVal(final List<DescuentoDto> descuentos, final String clave) {
        Optional<DescuentoDto> dDTo = descuentos.stream().filter(p -> p.getCLAVEDESCUENTO().equalsIgnoreCase(clave))
                .findAny();
        if (dDTo.isPresent()) {
            return new BigDecimal(dDTo.get().getCOEFICIENTE().toString());
        }
        return null;
    }

    /**
     * Gets the descuento monto.
     *
     * @param descuentos the descuentos
     * @param clave      the clave
     * @return the descuento monto
     */
    private static BigDecimal getDescuentoMonto(final List<ConceptoDto> descuentos, final String clave) {
        Optional<ConceptoDto> dDTo = descuentos.stream().filter(p -> p.getNOMBRE().equalsIgnoreCase(clave)).findAny();
        if (dDTo.isPresent()) {
            return new BigDecimal(dDTo.get().getMONTO());
        }
        return null;
    }

    /**
     * Gets the JAXB notificaciones.
     *
     * @param poliza the poliza
     * @param objReq the obj req
     */
    private static void getJAXBNotificaciones(final PolizaContratacionAutosSCE poliza, final EmiteNegReq objReq) {
        NotificacionAdicionalContratacionAutosSCE objNotif = new NotificacionAdicionalContratacionAutosSCE();
        if (objReq.getNotificacionAdicional() != null
                && Utileria.isValidEmailAddress(objReq.getNotificacionAdicional())) {
            objNotif.setCORREOELECTRONICO(objReq.getNotificacionAdicional());
            JAXBElement<NotificacionAdicionalContratacionAutosSCE> notifSCE = new ObjectFactory()
                    .createPolizaContratacionAutosSCENotificacionAdicional(objNotif);
            poliza.getRest().add(notifSCE);
        }
    }

    /**
     * Gets the elemento.
     *
     * @param elementos the elementos
     * @param dato      the dato
     * @return the elemento
     */
    public static String getElemento(final List<ElementoNeg> elementos, final String dato) {
        String strValor = "";
        Optional<ElementoNeg> elem = elementos.stream().filter(p -> p.getNombre().equalsIgnoreCase(dato)).findFirst();
        if (elem.isPresent()) {
            strValor = elem.get().getClave();
        }
        return strValor;
    }

    /**
     * Gets the elemento valor.
     *
     * @param elementos the elementos
     * @param valor     the valor
     * @return the elemento valor
     */
    public static String getElementoValor(final List<ElementoNeg> elementos, final String valor) {
        if (!Optional.ofNullable(elementos).isPresent()) {
            return "";
        }
        return elementos.stream().filter(p -> p.getNombre().equalsIgnoreCase(valor)).findFirst()
                .map(ElementoNeg::getValor).orElse("");
    }
}
