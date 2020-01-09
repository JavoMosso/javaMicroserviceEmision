package com.gnp.autos.wsp.emisor.eot.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.gnp.autos.wsp.emisor.eot.TecCom.wsdl.CatalogosTallerGenericoDto;
import com.gnp.autos.wsp.emisor.eot.TecCom.wsdl.ConsultarTallerGenericoRequest;
import com.gnp.autos.wsp.emisor.eot.calcp.wsdl.CodigoPostalDto;
import com.gnp.autos.wsp.emisor.eot.calcp.wsdl.ConsultaRegionTarificacionPorCp;
import com.gnp.autos.wsp.emisor.eot.cancelacion.wsdl.EmitirCancelacionRequest;
import com.gnp.autos.wsp.emisor.eot.cancelacion.wsdl.TarificarCancelacionRequest;
import com.gnp.autos.wsp.emisor.eot.consulta.wsdl.ConsultarPolizaPorNumPolizaRequest;
import com.gnp.autos.wsp.emisor.eot.emision.wsdl.Cabecera;
import com.gnp.autos.wsp.emisor.eot.emision.wsdl.RegistrarEmision;
import com.gnp.autos.wsp.emisor.eot.emision.wsdl.RegistrarEmisionRequest;
import com.gnp.autos.wsp.emisor.eot.foliador.wsdl.Foliador;
import com.gnp.autos.wsp.emisor.eot.liquidacion.LiquidaRecibosReq;
import com.gnp.autos.wsp.emisor.eot.movimiento.wsdl.ConsultaNegocio;
import com.gnp.autos.wsp.emisor.eot.movimiento.wsdl.MovimientoNegocioComercialDto;
import com.gnp.autos.wsp.emisor.eot.movimiento.wsdl.MovimientoPorcentajeIntermediarioDto;
import com.gnp.autos.wsp.emisor.eot.pasos.wsdl.ConsultaPasosConfigurador;
import com.gnp.autos.wsp.emisor.eot.pasos.wsdl.PasosConsultaConfiguradorDto;
import com.gnp.autos.wsp.emisor.eot.urlimp.BuscaDocResp;
import com.gnp.autos.wsp.emisor.eot.urlimp.Resultado;
import com.gnp.autos.wsp.emisor.eot.wsdl.Emitir;
import com.gnp.autos.wsp.emisor.eot.wsdl.EmitirRequest;
import com.gnp.autos.wsp.emisor.eot.wsdl.EmitirResponse;
import com.gnp.autos.wsp.emisor.eot.wsdl.PolizaSCE;
import com.gnp.autos.wsp.negocio.banderaauto.soap.BanderasAutosDecisionServiceRequestResponse;
import com.gnp.autos.wsp.negocio.banderaauto.soap.Cobertura;
import com.gnp.autos.wsp.negocio.banderaauto.soap.ObjetoAsegurado;
import com.gnp.autos.wsp.negocio.banderaauto.soap.Poliza;
import com.gnp.autos.wsp.negocio.banderaauto.soap.Poliza2;
import com.gnp.autos.wsp.negocio.banderaauto.soap.Vehiculo;
import com.gnp.autos.wsp.negocio.cotizacion.model.resp.ConceptoEconomicoResp;
import com.gnp.autos.wsp.negocio.cotizacion.model.resp.PaqueteResp;
import com.gnp.autos.wsp.negocio.cotizacion.model.resp.PaquetesResp;
import com.gnp.autos.wsp.negocio.cotizacion.model.resp.TotalPrimaResp;
import com.gnp.autos.wsp.negocio.cotizacion.model.resp.TotalesPrimaResp;
import com.gnp.autos.wsp.negocio.cotizacion.model.resp.TraductorResp;
import com.gnp.autos.wsp.negocio.muc.soap.CalcularPrimaAutoRequest;
import com.gnp.autos.wsp.negocio.muc.soap.CalcularPrimaAutoResponse;
import com.gnp.autos.wsp.negocio.muc.soap.CoberturaDto;
import com.gnp.autos.wsp.negocio.muc.soap.CoberturaPrimaDto;
import com.gnp.autos.wsp.negocio.muc.soap.ConceptoDto;
import com.gnp.autos.wsp.negocio.muc.soap.CotizacionDto;
import com.gnp.autos.wsp.negocio.muc.soap.DatosCotizacionDto;
import com.gnp.autos.wsp.negocio.muc.soap.DatosProductoDto;
import com.gnp.autos.wsp.negocio.muc.soap.DatosSolicitudDto;
import com.gnp.autos.wsp.negocio.muc.soap.DetallePrimaDto;
import com.gnp.autos.wsp.negocio.muc.soap.FormaPagoReciboDto;
import com.gnp.autos.wsp.negocio.muc.soap.ModificadorCoberturaDto;
import com.gnp.autos.wsp.negocio.muc.soap.ObjectFactory;
import com.gnp.autos.wsp.negocio.muc.soap.PeticionDto;
import com.gnp.autos.wsp.negocio.muc.soap.ProductosDto;
import com.gnp.autos.wsp.negocio.muc.soap.TotalPrimaDto;
import com.gnp.autos.wsp.negocio.neg.model.AdaptacionVehNeg;
import com.gnp.autos.wsp.negocio.neg.model.AgenteNeg;
import com.gnp.autos.wsp.negocio.neg.model.DescuentoNeg;
import com.gnp.autos.wsp.negocio.neg.model.DomicilioNeg;
import com.gnp.autos.wsp.negocio.neg.model.ElementoNeg;
import com.gnp.autos.wsp.negocio.neg.model.EmiteNegReq;
import com.gnp.autos.wsp.negocio.neg.model.EmiteNegResp;
import com.gnp.autos.wsp.negocio.neg.model.MedioCobroNeg;
import com.gnp.autos.wsp.negocio.neg.model.MedioContactoNeg;
import com.gnp.autos.wsp.negocio.neg.model.PersonaNeg;
import com.gnp.autos.wsp.negocio.neg.model.VehiculoNeg;
import com.gnp.autos.wsp.negocio.tarificadordatos.soap.ConsultarProductoCoberturaCobranzaResponse;
import com.gnp.autos.wsp.negocio.tarificadordatos.soap.TarificadorProductoCobranzaDto;
import com.gnp.autos.wsp.negocio.umoservice.model.Dominios;
import com.gnp.autos.wsp.negocio.umoservice.model.Estructura;
import com.gnp.autos.wsp.negocio.umoservice.model.Leyenda;
import com.gnp.autos.wsp.negocio.umoservice.model.ModeloNegocio;
import com.gnp.autos.wsp.negocio.umoservice.model.Negocio;
import com.gnp.autos.wsp.negocio.umoservice.model.NegocioComercial;
import com.gnp.autos.wsp.negocio.umoservice.model.Normativa;
import com.gnp.autos.wsp.negocio.umoservice.model.Normatividades;
import com.gnp.autos.wsp.negocio.umoservice.model.UnidadMedida;
import com.gnp.autos.wsp.negocio.umoservicemodel.EmisionDatos;
import com.gnp.autos.wsp.negocio.umoservicemodel.UmoServiceResp;

public class ObjetosPruebas {
    public static String COT_NOW = "CIANNE190812251969";
    public static String FCH_INI = "2019-08-12";
    public static String FCH_FIN = "2020-08-12";
    public static String FCH_MOV = "20190812";
    public static String FCHFIN = "20200812";
    
    public static List<PersonaNeg> getListPersonaInc() {
        /*
         * PersonaNeg personabase = new PersonaNeg();// getPersona();
         * personabase.setAMaterno("ERROR_MAT");
         * personabase.setAPaterno("GARCIA");
         * personabase.setEdad("19");
         * personabase.setEstadoCivil("S");
         * personabase.setFecNacimiento("20000701");
         * personabase.setIdParticipante("EGARCI709054");
         * personabase.setNacionalidad("MEX");
         * personabase.setNombre("ELOT");
         * personabase.setPaisNacimiento("MEX");
         * personabase.setRfc("GAME000701");
         * personabase.setSexo("M");
         * personabase.setTipo("CONTRATANTE");
         * personabase.setTipoPersona("F");
         */
        ObjetosPruebas obj = new ObjetosPruebas();
        obj.toString();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(ObjetosPruebas.getPersonaErr());
        return personas;
    }
    
    public static PersonaNeg getPersona() {
        MedioCobroNeg medioCobro = new MedioCobroNeg();
        medioCobro.setFchVencimiento("20501231");
        medioCobro.setNumCobro("23452345234");
        medioCobro.setCveTipoCuentaTarjeta("dfgdsfg354");
        medioCobro.setNumTarjeta("3045125863471500");
        DomicilioNeg domicilio = new DomicilioNeg();
        domicilio.setCalle("EMILIZANO ZAPATA");
        domicilio.setCodigoPostal("03330");
        domicilio.setColonia("Estrella");
        domicilio.setEstado("09");
        domicilio.setMunicipio("014");
        domicilio.setNumExterior("35");
        domicilio.setPais("MEX");
        List<ElementoNeg> elementos = new ArrayList<>();
        ElementoNeg elemento = new ElementoNeg();
        elemento.setClave("CORREO");
        elemento.setNombre("TIPO");
        elemento.setValor("CORREO");
        elementos.add(elemento);
        elemento = new ElementoNeg();
        elemento.setClave("karen.rodriguez@gnp.com.mx");
        elemento.setNombre("CORREO_ELECTRONICO");
        elemento.setValor("karen.rodriguez@gnp.com.mx");
        elementos.add(elemento);
        MedioContactoNeg medioContacto = new MedioContactoNeg();
        medioContacto.setElementos(elementos);
        List<MedioContactoNeg> mediosContacto = new ArrayList<>();
        mediosContacto.add(medioContacto);
        PersonaNeg persona = new PersonaNeg();
        persona.setAMaterno("MONTALVO");
        persona.setAPaterno("GARCIA");
        persona.setDomicilio(domicilio);
        persona.setEdad("19");
        persona.setEstadoCivil("S");
        persona.setFecNacimiento("20000701");
        persona.setMediosContacto(mediosContacto);
        persona.setNacionalidad("MEX");
        persona.setNombre("ELOT");
        persona.setPaisNacimiento("MEX");
        persona.setRfc("GAME000701");
        persona.setSexo("M");
        persona.setTipo("CONTRATANTE");
        persona.setTipoPersona("F");
        persona.setMedioCobro(medioCobro);
        return persona;
    }
    
    public static PersonaNeg getPersonaErr() {
        PersonaNeg persona = new PersonaNeg();
        persona.setAMaterno("MONTALVO_ERR");
        persona.setAPaterno("GARCIA");
        persona.setEdad("19");
        persona.setTipo("CONTRATANTE");
        persona.setTipoPersona("F");
        return persona;
    }
    
    public static EmisionDatos getEmisionDatosCPers(EmiteNegReq consulNegReq, List<PersonaNeg> personas, String cotizacion) {
        consulNegReq.setCveHerramienta("WSP");
        consulNegReq.setPersonas(personas);
        EmisionDatos emDatos = new EmisionDatos();
        emDatos.setEmite(consulNegReq);
        return emDatos;
    }
    
    public static EmisionDatos getEmisionDatos(EmiteNegReq consulNegReq, List<PersonaNeg> personas, String cotizacion) {
        consulNegReq.setCveHerramienta("WSP");
        consulNegReq.setPersonas(personas);
        EmisionDatos emDatos = new EmisionDatos();
        emDatos.setEmite(consulNegReq);
        return emDatos;
    }
    
    public static ResponseEntity<List<PersonaNeg>> getSrvPersonas() {
        List<ElementoNeg> elementos = new ArrayList<>();
        ElementoNeg elemento = new ElementoNeg();
        elemento.setClave("2");
        elemento.setNombre("3");
        elemento.setValor("002");
        elementos.add(elemento);
        elemento = new ElementoNeg();
        elemento.setClave("1");
        elemento.setNombre("1");
        elemento.setValor("003");
        elementos.add(elemento);
        MedioContactoNeg medioContacto = new MedioContactoNeg();
        medioContacto.setElementos(elementos);
        List<MedioContactoNeg> mediosContacto = new ArrayList<>();
        mediosContacto.add(medioContacto);
        PersonaNeg persona = new PersonaNeg();
        persona.setCveClienteOrigen("0056787423");
        persona.setMediosContacto(mediosContacto);
        persona.setIdParticipante("EGARCI265443");
        List<PersonaNeg> lpersonasNeg = new ArrayList<>();
        lpersonasNeg.add(persona);
        // List<Entity> entityList = entityManager.findAll();
        /*
         * List<PersonaNeg> entities = new ArrayList<PersonaNeg>();
         * for (PersonaNeg n : lpersonasNeg) {
         * JSONObject entity = new JSONObject();
         * entity.put("id", n.getAMaterno());
         * entity.put("address", n.getAPaterno());
         * entities.add(entity);
         * }
         */
        // ResponseEntity<List<PersonaNeg>> srvPersonas;
        // return new ResponseEntity<List<PersonaNeg>>(entities, HttpStatus.OK);
        return new ResponseEntity<List<PersonaNeg>>(lpersonasNeg, HttpStatus.OK);
    }
    
    public static ResponseEntity<List<PersonaNeg>> getSrvPersonasNull() {
        List<PersonaNeg> lpersonasNeg = null;
        return new ResponseEntity<List<PersonaNeg>>(lpersonasNeg, HttpStatus.OK);
    }
    
    public static ResponseEntity<List<PersonaNeg>> getSrvPersonasNoContect() {
        List<ElementoNeg> elementos = new ArrayList<>();
        ElementoNeg elemento = new ElementoNeg();
        elemento.setClave("2");
        elemento.setNombre("3");
        elemento.setValor("002");
        elementos.add(elemento);
        elemento = new ElementoNeg();
        elemento.setClave("1");
        elemento.setNombre("1");
        elemento.setValor("003");
        elementos.add(elemento);
        MedioContactoNeg medioContacto = new MedioContactoNeg();
        medioContacto.setElementos(elementos);
        List<MedioContactoNeg> mediosContacto = new ArrayList<>();
        mediosContacto.add(medioContacto);
        PersonaNeg persona = new PersonaNeg();
        persona.setCveClienteOrigen("0056787423");
        persona.setMediosContacto(mediosContacto);
        persona.setIdParticipante("EGARCI265443");
        List<PersonaNeg> lpersonasNeg = new ArrayList<>();
        lpersonasNeg.add(persona);
        return new ResponseEntity<List<PersonaNeg>>(lpersonasNeg, HttpStatus.NO_CONTENT);
    }
    
    public static DatosSolicitudDto getDatosSolicitud() {
        DatosSolicitudDto datossolicitud = new DatosSolicitudDto();
        datossolicitud.setCODIGOPOSTALCONTRATANTE("03330");
        datossolicitud.setMONTODERECHOPOLIZA(250.0);
        datossolicitud.setPOLITICADERECHOPOLIZA("RT");
        datossolicitud.setPORCENTAJECOMISION(1.0);
        datossolicitud.setPORCENTAJECESIONCOMISION(1.0);
        datossolicitud.setFORMAINDEMNIZACION("03");
        datossolicitud.setFORMAAJUSTEIRREGULAR("PR");
        datossolicitud.setFECHAVIGENCIAINICIAL(Utileria.getXMLDateOfStr(ObjetosPruebas.FCH_INI));
        datossolicitud.setFECHAVIGENCIAFINAL(Utileria.getXMLDateOfStr(ObjetosPruebas.FCH_FIN));
        datossolicitud.setIDNEGOCIOOPERABLE("NOVENTADIR");
        datossolicitud.setVERSIONNEGOCIO(22);
        datossolicitud.setCODIGOPROMOCION("COP0000331");
        return datossolicitud;
    }
    
    public static ModificadorCoberturaDto getModificadorCoberturaSa() {
        ModificadorCoberturaDto modificador = new ModificadorCoberturaDto();
        modificador.setCLAVEMODIFICADOR("CPASEGUR");
        modificador.setVALORREQUERIDO(new Double("999999.0"));
        modificador.setUNIDADMEDIDA("IMPT");
        return modificador;
    }
    
    public static ModificadorCoberturaDto getModificadorCoberturaDed() {
        ModificadorCoberturaDto modificador = new ModificadorCoberturaDto();
        modificador.setCLAVEMODIFICADOR("CDDEDUCI");
        modificador.setVALORREQUERIDO(new Double("0"));
        modificador.setUNIDADMEDIDA("PORC");
        return modificador;
    }
    
    public static ModificadorCoberturaDto getModificadorCoberturaDedAr() {
        ModificadorCoberturaDto modificador = new ModificadorCoberturaDto();
        modificador.setCLAVEMODIFICADOR("CDDEDUCIAR");
        modificador.setVALORREQUERIDO(new Double("10"));
        modificador.setUNIDADMEDIDA("PORC");
        return modificador;
    }
    
    public static CoberturaDto getCobertura1268() {
        CoberturaDto cobertura = new CoberturaDto();
        cobertura.setCLAVECOBERTURA("0000001268");
        cobertura.setTIPOCOBERTURA("B");
        cobertura.getMODIFICADOR().add(getModificadorCoberturaSa());
        cobertura.getMODIFICADOR().add(getModificadorCoberturaDed());
        return cobertura;
    }
    
    public static CoberturaDto getCobertura916() {
        CoberturaDto cobertura = new CoberturaDto();
        cobertura.setCLAVECOBERTURA("0000000916");
        cobertura.setTIPOCOBERTURA("B");
        cobertura.getMODIFICADOR().add(getModificadorCoberturaSa());
        cobertura.getMODIFICADOR().add(getModificadorCoberturaDed());
        cobertura.getMODIFICADOR().add(getModificadorCoberturaDedAr());
        return cobertura;
    }
    
    public static DatosProductoDto getDatosProducto() {
        DatosProductoDto datos = new DatosProductoDto();
        datos.setIDPRODUCTO("PRAUTRC0VD");
        datos.setVALORCOMERCIAL("39165.000");
        datos.setCLAVETARIFA(new BigInteger("1"));
        XMLGregorianCalendar date2 = Utileria.getXMLDateOfStr("2018-04-15");
        ObjectFactory factory = new ObjectFactory();
        JAXBElement<XMLGregorianCalendar> dateParam = factory.createDatosProductoBigDecimalDtoFECHATARIFA(date2);
        datos.setFECHATARIFA(dateParam);
        datos.getCOBERTURA().add(getCobertura1268());
        datos.getCOBERTURA().add(getCobertura916());
        return datos;
    }
    
    public static CalcularPrimaAutoRequest insertaMucPrimaAuto() {
        DatosCotizacionDto datos = new DatosCotizacionDto();
        datos.setDATOSSOLICITUD(getDatosSolicitud());
        datos.getDATOSPRODUCTO().add(getDatosProducto());
        CalcularPrimaAutoRequest mucresp = new CalcularPrimaAutoRequest();
        mucresp.getDATOSCOTIZACION().add(datos);
        return mucresp;
    }
    
    public static List<Normativa> getNnormativas() {
        UnidadMedida unidadMedida = new UnidadMedida();
        unidadMedida.setClave("DIAS");
        unidadMedida.setNombre("DIAS");
        Leyenda leyenda = new Leyenda();
        leyenda.setClave("EAR");
        leyenda.setNombre("Emision anticipada renova");
        leyenda.setUnidadMedida(unidadMedida);
        leyenda.setValorDefault("30");
        Normativa normativa = new Normativa();
        normativa.setLeyenda(leyenda);
        normativa.setUnidadMedida(unidadMedida);
        normativa.setValor("60");
        List<Normativa> normativas = new ArrayList<>();
        normativas.add(normativa);
        Normatividades normatividades = new Normatividades();
        normatividades.setNormativas(normativas);
        return normativas;
    }
    
    public static Normatividades getNormatividades() {
        Normatividades normatividades = new Normatividades();
        normatividades.setNormativas(getNnormativas());
        return normatividades;
    }
    
    public static Dominios getDominios() {
        Dominios dominios = new Dominios();
        dominios.setNormatividades(getNormatividades());
        return dominios;
    }
    
    public static UmoServiceResp getUmo() {
        NegocioComercial negocioComercial = new NegocioComercial();
        negocioComercial.setClave("NCO0000000");
        Estructura estructura = new Estructura();
        estructura.setNegocioComercial(negocioComercial);
        ModeloNegocio modeloNegocio = new ModeloNegocio();
        modeloNegocio.setId("1");
        Negocio negocio = new Negocio();
        negocio.setNombre("nombre");
        negocio.setModeloNegocio(modeloNegocio);
        negocio.setEstructura(estructura);
        UmoServiceResp umo = new UmoServiceResp();
        umo.setNegocio(negocio);
        umo.setId(1);
        umo.setDominios(getDominios());
        return umo;
    }
    
    public static List<DescuentoNeg> getListDescuentos() {
        DescuentoNeg descuento = new DescuentoNeg();
        descuento.setCveDescuento("POVASEP");
        descuento.setMonto("10");
        descuento.setUnidadMedida("PORC");
        List<DescuentoNeg> descuentos = new ArrayList<>();
        descuentos.add(descuento);
        return descuentos;
    }
    
    public static VehiculoNeg getVehiculo() {
        VehiculoNeg vehiculo = new VehiculoNeg();
        vehiculo.setArmadora("FR");
        vehiculo.setAltoRiesgo("0");
        vehiculo.setCarroceria("29");
        vehiculo.setClaveMarca("01");
        vehiculo.setEstadoCirculacion("09");
        vehiculo.setFormaIndemnizacion("03");
        vehiculo.setModelo("2007");
        vehiculo.setPlacas("DAZ456");
        vehiculo.setSerie("ABCZ000000000004");
        vehiculo.setSubRamo("01");
        vehiculo.setTipoCarga("");
        vehiculo.setTipoVehiculo("AUT");
        vehiculo.setUso("01");
        vehiculo.setValorVehiculo("101115.00");
        return vehiculo;
    }
    
    public static Vehiculo getVehiculoBanderas() {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setArmadora("FR");
        vehiculo.setBANALTORIESGO(true);
        vehiculo.setClaveCarroceria("29");
        vehiculo.setMarca("01");
        vehiculo.setEstadoCirculacion("09");
        vehiculo.setTipoValor("03");
        vehiculo.setModelo("2007");
        vehiculo.setNumeroSerie("ABCZ000000000004");
        vehiculo.setProcedencia("01");
        vehiculo.setTipo("AUT");
        vehiculo.setUso("01");
        vehiculo.setValor(101115.00);
        return vehiculo;
    }
    
    public static Cobertura getCoberturasBasica() {
        Cobertura coberturasBasica = new Cobertura();
        coberturasBasica.setClave("0000000916");
        coberturasBasica.setPCTDEDUCIBLEALTORIESGO(1.0);
        return coberturasBasica;
    }
    
    public static BanderasAutosDecisionServiceRequestResponse getBanAutoResp() {
        ObjetoAsegurado obj = new ObjetoAsegurado();
        obj.setVehiculo(getVehiculoBanderas());
        obj.getCoberturasBasicas().add(getCoberturasBasica());
        com.gnp.autos.wsp.negocio.banderaauto.soap.ObjectFactory factory = new com.gnp.autos.wsp.negocio.banderaauto.soap.ObjectFactory();
        JAXBElement<ObjetoAsegurado> objetoAsegurado = factory.createPolizaObjetoAsegurado(obj);
        Poliza poliza = new Poliza();
        poliza.setINDAFECTABONO("");
        poliza.setObjetoAsegurado(objetoAsegurado);
        Poliza2 poliza2 = new Poliza2();
        poliza2.setPoliza(poliza);
        BanderasAutosDecisionServiceRequestResponse banAutoResp = new BanderasAutosDecisionServiceRequestResponse();
        banAutoResp.setPoliza(poliza2);
        return banAutoResp;
    }
    
    public static EmisionDatos setComplementoSinMC(EmisionDatos emDatos) {
        emDatos.getEmite().setMucPrimaAutoReq(insertaMucPrimaAuto());
        emDatos.getEmite().setDescuentos(getListDescuentos());
        emDatos.getEmite().setPrimaNeta("3667.1");
        emDatos.getEmite().setVehiculo(getVehiculo());
        emDatos.getEmite().setBanAutoResp(getBanAutoResp());
        emDatos.getEmite().setBanImpresion("1");
        emDatos.getEmite().setViaPago("IN");
        emDatos.getEmite().getPersonas().get(0).setMedioCobro(null);
        emDatos.setUmo(getUmo());
        return emDatos;
    }
    
    public static ConceptoDto getConceptoPrimaTecnica() {
        ConceptoDto conceptoeconomico = new ConceptoDto();
        conceptoeconomico.setNOMBRE("PRIMA_TECNICA");
        conceptoeconomico.setMONTO("1000");
        return conceptoeconomico;
    }
    
    public static ConceptoDto getConceptoPrimaNeta() {
        ConceptoDto conceptoeconomico = new ConceptoDto();
        conceptoeconomico.setNOMBRE("PRIMA_NETA");
        conceptoeconomico.setMONTO("1000");
        return conceptoeconomico;
    }
    
    public static ConceptoDto getConceptoDescuento() {
        ConceptoDto conceptoeconomico = new ConceptoDto();
        conceptoeconomico.setNOMBRE("DESCUENTO");
        conceptoeconomico.setMONTO("1000");
        return conceptoeconomico;
    }
    
    public static ConceptoDto getConceptoEconomicoRD() {
        ConceptoDto conceptoeconomico = new ConceptoDto();
        conceptoeconomico.setNOMBRE("RECARGO_DESCUENTO");
        conceptoeconomico.setMONTO("1000");
        return conceptoeconomico;
    }
    
    public static ConceptoDto getConceptoEconomicoPrimaCedidas() {
        ConceptoDto conceptoeconomico = new ConceptoDto();
        conceptoeconomico.setNOMBRE("PRIMA_CEDIDA");
        conceptoeconomico.setMONTO("1000");
        return conceptoeconomico;
    }
    
    public static ConceptoDto getConceptoEconomicoRPF() {
        ConceptoDto conceptoeconomico = new ConceptoDto();
        conceptoeconomico.setNOMBRE("RECARGO_PAGO_FRACC");
        conceptoeconomico.setMONTO("1000");
        return conceptoeconomico;
    }
    
    public static ConceptoDto getConceptoEconomicoIva() {
        ConceptoDto conceptoeconomico = new ConceptoDto();
        conceptoeconomico.setNOMBRE("IVA");
        conceptoeconomico.setMONTO("1000");
        return conceptoeconomico;
    }
    
    public static ConceptoDto getConceptoEconomicoDPoliza() {
        ConceptoDto conceptoeconomico = new ConceptoDto();
        conceptoeconomico.setNOMBRE("DERECHOS_POLIZA");
        conceptoeconomico.setMONTO("1000");
        return conceptoeconomico;
    }
    
    public static ConceptoDto getConceptoEconomicoIvaDPoliza() {
        ConceptoDto conceptoeconomico = new ConceptoDto();
        conceptoeconomico.setNOMBRE("IVA_DERECHOS_POLIZA");
        conceptoeconomico.setMONTO("12");
        return conceptoeconomico;
    }
    
    public static ConceptoDto getConceptoEconomicoRecargoDP() {
        ConceptoDto conceptoeconomico = new ConceptoDto();
        conceptoeconomico.setNOMBRE("IVA_DERECHOS_POLIZA");
        conceptoeconomico.setMONTO("12");
        return conceptoeconomico;
    }
    
    public static ConceptoDto getConceptoDetalle() {
        ConceptoDto conceptoeconomico = new ConceptoDto();
        conceptoeconomico.setNOMBRE("VADESVOL");
        conceptoeconomico.setMONTO("1000");
        return conceptoeconomico;
    }
    
    public static FormaPagoReciboDto getFormaPagoRecibo() {
        FormaPagoReciboDto formapagorecibo = new FormaPagoReciboDto();
        formapagorecibo.setMONTOTOTAL(new Double(10000.0));
        formapagorecibo.setMONTOPRIMERRECIBO(new Double(1001.0));
        formapagorecibo.setMONTORECIBOSUBSECUENTE(new Double(500.0));
        formapagorecibo.setNUMRECIBOSSUB(new BigInteger("5"));
        formapagorecibo.setNUMEROPAGOS(new BigInteger("6"));
        return formapagorecibo;
    }
    
    public static CotizacionDto getCotizacionMucResp() {
        TotalPrimaDto totalprima = new TotalPrimaDto();
        totalprima.getPRIMA().add(getConceptoPrimaTecnica());
        totalprima.getPRIMA().add(getConceptoPrimaNeta());
        totalprima.getPRIMA().add(getConceptoDescuento());
        totalprima.getCONCEPTOECONOMICO().add(getConceptoEconomicoRD());
        totalprima.getCONCEPTOECONOMICO().add(getConceptoEconomicoPrimaCedidas());
        totalprima.getCONCEPTOECONOMICO().add(getConceptoEconomicoRPF());
        totalprima.getCONCEPTOECONOMICO().add(getConceptoEconomicoIva());
        totalprima.getCONCEPTOECONOMICO().add(getConceptoEconomicoDPoliza());
        totalprima.getDETALLEDESCUENTO().add(getConceptoDetalle());
        CoberturaPrimaDto cprima = new CoberturaPrimaDto();
        cprima.setCLAVECOBERTURA("0000001268");
        CotizacionDto datoscotizacion = new CotizacionDto();
        datoscotizacion.setTOTALPRIMA(totalprima);
        datoscotizacion.setFORMAPAGORECIBO(getFormaPagoRecibo());
        datoscotizacion.getCOBERTURAPRIMA().add(cprima);
        DetallePrimaDto detalle = new DetallePrimaDto();
        detalle.getCONCEPTOECONOMICO().add(getConceptoEconomicoIvaDPoliza());
        detalle.getCONCEPTOECONOMICO().add(getConceptoEconomicoRecargoDP());
        datoscotizacion.getDETALLEDERECHOS().add(detalle);
        return datoscotizacion;
    }
    
    public static ProductosDto getProductoMucResp() {
        ProductosDto datosproductos = new ProductosDto();
        datosproductos.getDATOSCOTIZACION().add(getCotizacionMucResp());
        datosproductos.setIDPRODUCTO("PRAUTRC0VD");
        return datosproductos;
    }
    
    public static PeticionDto getPeticionMucResp() {
        PeticionDto peticion = new PeticionDto();
        peticion.getDATOSPRODUCTOS().add(getProductoMucResp());
        return peticion;
    }
    
    public static CalcularPrimaAutoResponse getMucPrimaAutoResp() {
        CalcularPrimaAutoResponse mucPrimaAutoResp = new CalcularPrimaAutoResponse();
        mucPrimaAutoResp.getPETICION().add(getPeticionMucResp());
        return mucPrimaAutoResp;
    }
    
    public static ConsultarProductoCoberturaCobranzaResponse getTarificadorResp() {
        com.gnp.autos.wsp.negocio.tarificadordatos.soap.DatosSolicitudDto datossolicitud =
                new com.gnp.autos.wsp.negocio.tarificadordatos.soap.DatosSolicitudDto();
        datossolicitud.setCONTRATOSADE("0098765");
        TarificadorProductoCobranzaDto tarificadorproducto = new TarificadorProductoCobranzaDto();
        tarificadorproducto.setDATOSSOLICITUD(datossolicitud);
        ConsultarProductoCoberturaCobranzaResponse tarificadordResp = new ConsultarProductoCoberturaCobranzaResponse();
        tarificadordResp.setTARIFICADORPRODUCTO(tarificadorproducto);
        return tarificadordResp;
    }
    
    public static List<ElementoNeg> getElementos() {
        List<ElementoNeg> elementos = new ArrayList<>();
        ElementoNeg elemento = new ElementoNeg();
        elemento.setClave("S");
        elemento.setNombre("CVE_TIPO_NOMINA");
        elemento.setValor("S");
        elementos.add(elemento);
        elemento = new ElementoNeg();
        elemento.setClave("AA");
        elemento.setNombre("UBICACION_TRABAJO");
        elemento.setValor("AA");
        elementos.add(elemento);
        elemento = new ElementoNeg();
        elemento.setClave("llav");
        elemento.setNombre("LLAVE_ACCESO_NOMINA");
        elemento.setValor("llav");
        elementos.add(elemento);
        return elementos;
    }
    
    public static List<ElementoNeg> getElementoSinTipoNomina() {
        List<ElementoNeg> elementos = new ArrayList<>();
        ElementoNeg elemento = new ElementoNeg();
        elemento.setClave("520");
        elemento.setNombre("DERECHO_POLIZA");
        elemento.setValor("520");
        elementos.add(elemento);
        return elementos;
    }
    
    public static List<ElementoNeg> getElementoSinUbicacion() {
        List<ElementoNeg> elementos = new ArrayList<>();
        ElementoNeg elemento = new ElementoNeg();
        elemento.setClave("S");
        elemento.setNombre("CVE_TIPO_NOMINA");
        elemento.setValor("S");
        elementos.add(elemento);
        elemento = new ElementoNeg();
        elemento.setClave("520");
        elemento.setNombre("DERECHO_POLIZA");
        elemento.setValor("520");
        elementos.add(elemento);
        return elementos;
    }
    
    public static EmisionDatos setComplementoSinTipoNomina(EmisionDatos emDatos) {
        emDatos.setUmo(getUmo());
        emDatos.getEmite().setMucPrimaAutoReq(insertaMucPrimaAuto());
        emDatos.getEmite().setDescuentos(getListDescuentos());
        emDatos.getEmite().setPrimaNeta("3667.1");
        emDatos.getEmite().setVehiculo(getVehiculo());
        emDatos.getEmite().setBanAutoResp(getBanAutoResp());
        emDatos.getEmite().setBanImpresion("1");
        emDatos.getEmite().setViaPago("BN");
        emDatos.getEmite().setIdUMO("1");
        emDatos.getEmite().setMucPrimaAutoResp(getMucPrimaAutoResp());
        emDatos.getEmite().setViaPagoSucesivos("IN");
        emDatos.getEmite().setFchTarifa("20180415");
        emDatos.getEmite().setCveTarifa("1");
        emDatos.getEmite().setFchTransformacionTarifa("20140526");
        emDatos.getEmite().setCveTransformacionTarifa("0000000000");
        emDatos.getEmite().setBanRenovacionAutomatica("1");
        emDatos.getEmite().setTarificadordResp(getTarificadorResp());
        emDatos.getEmite().setElementos(getElementoSinTipoNomina());
        return emDatos;
    }
    
    public static EmisionDatos setComplementoSinUbicacion(EmisionDatos emDatos) {
        emDatos.setUmo(getUmo());
        emDatos.getEmite().setMucPrimaAutoReq(insertaMucPrimaAuto());
        emDatos.getEmite().setDescuentos(getListDescuentos());
        emDatos.getEmite().setPrimaNeta("3667.1");
        emDatos.getEmite().setVehiculo(getVehiculo());
        emDatos.getEmite().setBanAutoResp(getBanAutoResp());
        emDatos.getEmite().setBanImpresion("1");
        emDatos.getEmite().setViaPago("BN");
        emDatos.getEmite().setIdUMO("1");
        emDatos.getEmite().setMucPrimaAutoResp(getMucPrimaAutoResp());
        emDatos.getEmite().setViaPagoSucesivos("IN");
        emDatos.getEmite().setFchTarifa("20180415");
        emDatos.getEmite().setCveTarifa("1");
        emDatos.getEmite().setFchTransformacionTarifa("20190415");
        emDatos.getEmite().setCveTransformacionTarifa("2");
        emDatos.getEmite().setBanRenovacionAutomatica("1");
        emDatos.getEmite().setTarificadordResp(getTarificadorResp());
        emDatos.getEmite().setElementos(getElementoSinUbicacion());
        return emDatos;
    }
    
    public static List<AgenteNeg> getAgentes() {
        AgenteNeg agente = new AgenteNeg();
        agente.setCveClaseIntermediarioInfo("A");
        agente.setBanIntermediarioPrincipal("1");
        agente.setCodIntermediario("0050000001");
        agente.setFolio("P0017340");
        agente.setIdParticipante("GRNPSB130D84");
        List<AgenteNeg> agentes = new ArrayList<>();
        agentes.add(agente);
        return agentes;
    }
    
    public static PersonaNeg getPersonas() {
        DomicilioNeg domicilio = new DomicilioNeg();
        domicilio.setCalle("EMILIANO ZAPATA");
        domicilio.setCodigoPostal("03330");
        domicilio.setColonia("Estrella");
        domicilio.setCveTipoVia("");
        domicilio.setEstado("09");
        domicilio.setMunicipio("014");
        domicilio.setNumExterior("35");
        domicilio.setPais("MEX");
        ElementoNeg elemento = new ElementoNeg();
        elemento.setClave("TELEFONO");
        elemento.setNombre("TIPO");
        elemento.setValor("TELEFONO");
        List<ElementoNeg> elementos = new ArrayList<>();
        elementos.add(elemento);
        elemento = new ElementoNeg();
        elemento.setClave("55");
        elemento.setNombre("CVE_LADA");
        elemento.setValor("55");
        elementos.add(elemento);
        elemento = new ElementoNeg();
        elemento.setClave("55");
        elemento.setNombre("CVE_LADA_NACIONAL");
        elemento.setValor("55");
        elementos.add(elemento);
        elemento = new ElementoNeg();
        elemento.setClave("54238525");
        elemento.setNombre("NUMERO_TELEFONO");
        elemento.setValor("54238525");
        elementos.add(elemento);
        MedioContactoNeg medio = new MedioContactoNeg();
        medio.setElementos(elementos);
        List<MedioContactoNeg> mediosContacto = new ArrayList<>();
        mediosContacto.add(medio);
        elementos = new ArrayList<>();
        elemento = new ElementoNeg();
        elemento.setClave("CORREO");
        elemento.setNombre("TIPO");
        elemento.setValor("CORREO");
        elementos.add(elemento);
        elemento = new ElementoNeg();
        elemento.setClave("karen.rodriguez@gnp.com.mx");
        elemento.setNombre("CORREO_ELECTRONICO");
        elemento.setValor("karen.rodriguez@gnp.com.mx");
        elementos.add(elemento);
        medio = new MedioContactoNeg();
        medio.setElementos(elementos);
        mediosContacto.add(medio);
        PersonaNeg persona = new PersonaNeg();
        persona.setAMaterno("MONTALVO");
        persona.setAPaterno("GARCIA");
        persona.setBanIrrevocable("");
        persona.setBanPreferente("");
        persona.setCodigoCliente("");
        persona.setCurp("");
        persona.setCveClienteOrigen("");
        persona.setCveTextoBeneficiario("r3");
        persona.setDescuentoSiniestralidad("");
        persona.setDomicilio(domicilio);
        persona.setEdad("19");
        persona.setEstadoCivil("S");
        persona.setFecConstitucion("");
        persona.setFecNacimiento("20000701");
        persona.setFiel("");
        persona.setGiro("");
        persona.setIdParticipante("EGARCI709054");
        persona.setMediosContacto(mediosContacto);
        persona.setNacionalidad("MEX");
        persona.setNombre("ELOT");
        persona.setNumeroIdentificacion("");
        persona.setOcupacion("");
        persona.setPaisNacimiento("MEX");
        persona.setPctBeneficio("");
        persona.setRazonSocial("");
        persona.setRfc("GAME000701");
        persona.setSecBeneficiario("");
        persona.setSexo("M");
        persona.setTextoBeneficio("");
        persona.setTipo("CONTRATANTE");
        persona.setTipoDocumento("");
        persona.setTipoParticipante("");
        persona.setTipoPersona("F");
        // List<PersonaNeg> personas = new ArrayList<>();
        // personas.add(persona);
        /*
         * persona = new PersonaNeg();
         * persona.setIdParticipante("EGARCI709054");
         * persona.setFecNacimiento("20000701");
         * persona.setTipo("CONDUCTOR");
         */
        return persona;
    }
    
    public static EmisionDatos setComplemento(EmisionDatos emDatos) {
        emDatos.setUmo(getUmo());
        emDatos.getEmite().setMucPrimaAutoReq(insertaMucPrimaAuto());
        emDatos.getEmite().setDescuentos(getListDescuentos());
        emDatos.getEmite().setPrimaNeta("3667.1");
        emDatos.getEmite().setVehiculo(getVehiculo());
        emDatos.getEmite().setBanAutoResp(getBanAutoResp());
        emDatos.getEmite().setBanImpresion("1");
        emDatos.getEmite().setViaPago("IN");
        emDatos.getEmite().setIdUMO("1");
        emDatos.getEmite().setMucPrimaAutoResp(getMucPrimaAutoResp());
        emDatos.getEmite().setViaPagoSucesivos("IN");
        emDatos.getEmite().setFchTarifa("20180415");
        emDatos.getEmite().setCveTarifa("1");
        emDatos.getEmite().setFchTransformacionTarifa("20190415");
        emDatos.getEmite().setCveTransformacionTarifa("2");
        emDatos.getEmite().setBanRenovacionAutomatica("1");
        emDatos.getEmite().setTarificadordResp(getTarificadorResp());
        emDatos.getEmite().setElementos(getElementos());
        emDatos.getEmite().setBanAjusteIrregular("0");
        emDatos.getEmite().setIdAgrupaRecargo("1");
        emDatos.getEmite().setAgentes(getAgentes());
        emDatos.getEmite().setBanImpresionCentralizada("1");
        emDatos.getEmite().setVerZonificacion("1");
        emDatos.getEmite().setCveZonificacion("1");
        emDatos.getEmite().setFchEfectoMovimiento(ObjetosPruebas.FCH_MOV);
        emDatos.getEmite().setFchFinEfectoMovimiento(ObjetosPruebas.FCH_MOV);
        emDatos.getEmite().setIdVersionNegocio(1);
        emDatos.getEmite().setTipoCanalAcceso("tipoCanalAcceso");
        emDatos.getEmite().setFormaPago("A");
        return emDatos;
    }
    
    public static EmisionDatos setComplementoSinDescuentos(EmisionDatos emDatos) {
        emDatos.getEmite().setMucPrimaAutoReq(insertaMucPrimaAuto());
        emDatos.getEmite().setPrimaNeta("3667.1");
        emDatos.getEmite().setVehiculo(getVehiculo());
        emDatos.setUmo(getUmo());
        return emDatos;
    }
    
    public static EmiteNegResp getEmiteNegResp() {
        EmiteNegResp emResponse = new EmiteNegResp();
        return emResponse;
    }
    
    public static EmitirResponse getEmitirResponse() {
        PolizaSCE poliza = new PolizaSCE();
        poliza.setINDTIPOEMISION("L");
        EmitirResponse emitir = new EmitirResponse();
        emitir.setPoliza(poliza);
        return emitir;
    }
    
    public static ResponseEntity<BuscaDocResp> getImpresion() {
        Resultado resultado = new Resultado();
        resultado.setExtensionArchivo("");
        resultado.setNombreDocumento("Poliza_00000376085213_181122113111.pdf");
        resultado.setUrlDocumento(
                "https://eotuat.gnp.com.mx//urlFilenetWeb/getDocument?id=v7Mcqy2ehkkl2QlpHEQBKErANL93G2zIKFldYXHyTpn2UfT0RNnmk9klJHX9J89A70fbCxLiwejojzADOT1KSg==");
        BuscaDocResp busca = new BuscaDocResp();
        busca.setResultado(resultado);
        return new ResponseEntity<BuscaDocResp>(busca, HttpStatus.OK);
    }
    
    public static EmisionDatos getBeneficiariosBanU(EmisionDatos emDatos) {
        PersonaNeg bene = getPersona();
        bene.setTipo("BENEFICIARIO");
        bene.setBanIrrevocable("1");
        emDatos.getEmite().getPersonas().add(bene);
        return emDatos;
    }
    
    public static EmisionDatos getBeneficiariosBanCNull(EmisionDatos emDatos) {
        PersonaNeg bene = getPersona();
        bene.setTipo("BENEFICIARIO");
        bene.setBanIrrevocable("0");
        bene.setCveClienteOrigen("0");
        bene.setIdParticipante("LMARTI278081");
        bene.setPctBeneficio(null);
        bene.setSecBeneficiario("1");
        emDatos.getEmite().getPersonas().add(bene);
        return emDatos;
    }
    
    public static EmisionDatos getBeneficiariosBanC(EmisionDatos emDatos) {
        PersonaNeg bene = getPersona();
        bene.setTipo("BENEFICIARIO");
        bene.setBanIrrevocable("0");
        bene.setCveClienteOrigen("0");
        bene.setIdParticipante("LMARTI278081");
        bene.setPctBeneficio("1");
        bene.setSecBeneficiario("1");
        emDatos.getEmite().getPersonas().add(bene);
        return emDatos;
    }
    
    public static EmisionDatos getAgentesBanInterSec(EmisionDatos emDatos) {
        List<AgenteNeg> agentes = emDatos.getEmite().getAgentes();
        agentes.get(0).setPctCesionComision("1");
        agentes.get(0).setPctParticipComision("10");
        agentes.get(0).setBanIntermediarioPrincipal("0");
        emDatos.getEmite().setAgentes(agentes);
        return emDatos;
    }
    
    public static EmisionDatos getAgentesBanNull(EmisionDatos emDatos) {
        List<AgenteNeg> agentes = emDatos.getEmite().getAgentes();
        agentes.get(0).setPctCesionComision("1");
        agentes.get(0).setPctParticipComision("10");
        agentes.get(0).setBanIntermediarioPrincipal(null);
        agentes.get(0).setPctComisionPrima("5");
        emDatos.getEmite().setAgentes(agentes);
        return emDatos;
    }
    
    public static EmisionDatos getAgentes(EmisionDatos emDatos) {
        List<AgenteNeg> agentes = emDatos.getEmite().getAgentes();
        agentes.get(0).setPctCesionComision("1");
        agentes.get(0).setPctParticipComision("10");
        agentes.get(0).setBanIntermediarioPrincipal("1");
        agentes.get(0).setPctComisionPrima("5");
        emDatos.getEmite().setAgentes(agentes);
        return emDatos;
    }
    
    public static EmisionDatos getProductos(EmisionDatos emDatos) {
        CalcularPrimaAutoRequest mucPrimaAutoReq = insertaMucPrimaAuto();
        emDatos.getEmite().setMucPrimaAutoReq(mucPrimaAutoReq);
        emDatos.getEmite().setMucPrimaAutoResp(getMucPrimaAutoResp());
        return emDatos;
    }
    
    public static AdaptacionVehNeg getAdaptaciones() {
        com.gnp.autos.wsp.negocio.neg.model.AdaptacionVehNeg adap = new AdaptacionVehNeg();
        adap.setBanEquip("1");
        adap.setDescEquip("asd");
        adap.setFechaFactura("20190101");
        adap.setMontoFacturacion("13200");
        adap.setMontoSA("10000");
        return adap;
    }
    
    public static EmisionDatos getEmisionTotal(EmiteNegReq consulNegReq) {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().getVehiculo().setAdaptaciones(new ArrayList<>());
        emDatos.getEmite().getVehiculo().getAdaptaciones().add(ObjetosPruebas.getAdaptaciones());
        emDatos.getEmite().getVehiculo().setAltoRiesgo("1");
        emDatos.getEmite().getVehiculo().setTipoCarga("A");
        emDatos.getEmite().getVehiculo().setDescripcionFactura("quemacocos");
        emDatos.getEmite().getVehiculo().setValorFactura("250000");
        emDatos.getEmite().setAccion("E");
        return emDatos;
    }
    
    public static TraductorResp getTraductorResp() {
        ConceptoEconomicoResp concep = new ConceptoEconomicoResp();
        concep.setMonto("4857.04");
        concep.setNombre("TOTAL_PAGAR");
        List<ConceptoEconomicoResp> conceptosEconomicos = new ArrayList<>();
        conceptosEconomicos.add(concep);
        concep = new ConceptoEconomicoResp();
        concep.setMonto("669.94");
        concep.setNombre("IVA");
        conceptosEconomicos.add(concep);
        concep = new ConceptoEconomicoResp();
        concep.setMonto("3667.1");
        concep.setNombre("PRIMA_NETA");
        conceptosEconomicos.add(concep);
        TotalPrimaResp ce = new TotalPrimaResp();
        ce.setConceptosEconomicos(conceptosEconomicos);
        List<TotalPrimaResp> total = new ArrayList<>();
        total.add(ce);
        TotalesPrimaResp totales = new TotalesPrimaResp();
        totales.setTotales(total);
        PaqueteResp paq = new PaqueteResp();
        paq.setTotales(totales);
        List<PaqueteResp> listpaq = new ArrayList<>();
        listpaq.add(paq);
        PaquetesResp paquetes = new PaquetesResp();
        paquetes.setPaquetes(listpaq);
        TraductorResp respCot = new TraductorResp();
        respCot.setPaquetes(paquetes);
        return respCot;
    }
    
    public static MovimientoPorcentajeIntermediarioDto getMovimiento() {
        MovimientoPorcentajeIntermediarioDto agentDto = new MovimientoPorcentajeIntermediarioDto();
        agentDto.setBANPRINCIPAL(1);
        agentDto.setCODIGOINTERMEDIARIO("001563");
        agentDto.setCLASETIPOINTERMEDIARIO("A");
        agentDto.setCVEOFICINADIRECCIONAGENCIA("012");
        agentDto.setFOLIOAGENTE("N0156");
        agentDto.setIDPARTICIPANTEAGENTE("0015489");
        agentDto.setPORCENTAJECESIONCOMISION(new BigDecimal("2"));
        agentDto.setPORCENTAJEDISTRIBUCION(new BigDecimal("5"));
        agentDto.setPORCENTAJECOMISION(new BigDecimal("5"));
        return agentDto;
    }
    
    public static LiquidaRecibosReq getLiquidaRecibosReq() {
        LiquidaRecibosReq liquida = new LiquidaRecibosReq();
        liquida.setCveCondPago("CL");
        liquida.setIndTipoEmi("L");
        liquida.setIdTransaccion("CIANNE171002022481");
        liquida.setCveTransaccion("5");
        liquida.setIdActor("TSUAUT");
        liquida.setIdRol("");
        liquida.setIdPerfil("Empleado");
        liquida.setNumPoliza("00000374954592");
        liquida.setFechaVigenciaInicial("2017-10-06");
        liquida.setFechaVigenciaFinal("2018-10-06");
        liquida.setCanalCobro("CL");
        liquida.setMedioCobro("006");
        liquida.setNumTarjeta("5470464940536400");
        liquida.setOrigen("VD");
        liquida.setReferencia("XFD45345345");
        liquida.setReintento(false);
        liquida.setCveTipoCuentaTarjeta("VD");
        liquida.setNumCobro("0000110951");
        liquida.setUsuarioAudit("TSUAUT");
        return liquida;
    }
    
    public static TarificarCancelacionRequest getTarificarCancelacionRequest() {
        TarificarCancelacionRequest objReq = new TarificarCancelacionRequest();
        return objReq;
    }
    
    public static EmitirCancelacionRequest getEmitirCancelacionRequest() {
        EmitirCancelacionRequest contratoReq = new EmitirCancelacionRequest();
        return contratoReq;
    }
    
    public static ConsultarPolizaPorNumPolizaRequest getConsultarPolizaPorNumPolizaRequest() {
        ConsultarPolizaPorNumPolizaRequest objReq = new ConsultarPolizaPorNumPolizaRequest();
        return objReq;
    }
    
    public static RegistrarEmisionRequest getRegistrarEmisionRequest() {
        RegistrarEmisionRequest objEmi = new RegistrarEmisionRequest();
        objEmi.setCabecera(new Cabecera());
        objEmi.setData(new RegistrarEmision());
        return objEmi;
    }
    
    public static EmitirRequest getEmitirRequest() {
        EmitirRequest objEmi = new EmitirRequest();
        objEmi.setCabecera(new com.gnp.autos.wsp.emisor.eot.wsdl.Cabecera());
        objEmi.setData(new Emitir());
        return objEmi;
    }
    
    public static Foliador getFoliador() {
        Foliador objReq = new Foliador();
        objReq.setCVETIPOTRANSACCION("A");
        return objReq;
    }
    
    public static ConsultaNegocio getConsultaNegocio() {
        ConsultaNegocio objConsulta = new ConsultaNegocio();
        objConsulta.setMOVIMIENTOSNEGOCIO(new MovimientoNegocioComercialDto());
        return objConsulta;
    }
    
    public static ConsultaPasosConfigurador getConsultaPasosConfigurador() {
        ConsultaPasosConfigurador objReq = new ConsultaPasosConfigurador();
        objReq.setPASOCONSULTACONFIGURADOR(new PasosConsultaConfiguradorDto());
        return objReq;
    }
    
    public static ConsultarTallerGenericoRequest getConsultarTallerGenericoRequest() {
        ConsultarTallerGenericoRequest objProd = new ConsultarTallerGenericoRequest();
        objProd.setCATALOGOS(new CatalogosTallerGenericoDto());
        return objProd;
    }
    
    public static ConsultaRegionTarificacionPorCp getConsultaRegionTarificacionPorCp() {
        ConsultaRegionTarificacionPorCp objReq = new ConsultaRegionTarificacionPorCp();
        objReq.setCodigoPostal(new CodigoPostalDto());
        return objReq;
    }
}
