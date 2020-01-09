package com.gnp.autos.wsp.emisor.eot.domain.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import com.gnp.autos.wsp.emisor.eot.config.ConfWSP;
import com.gnp.autos.wsp.emisor.eot.domain.Domain;
import com.gnp.autos.wsp.emisor.eot.domain.MovimientoDomain;
import com.gnp.autos.wsp.emisor.eot.domain.ProdTecComDomain;
import com.gnp.autos.wsp.emisor.eot.error.ExecutionError;
import com.gnp.autos.wsp.emisor.eot.pasos.PasosDomain;
import com.gnp.autos.wsp.emisor.eot.pasos.wsdl.ConsultaPasosConfiguradorResponse;
import com.gnp.autos.wsp.emisor.eot.pasos.wsdl.PasoElementoConsultaDto;
import com.gnp.autos.wsp.emisor.eot.pasos.wsdl.PasosConsultaConfiguradorDto;
import com.gnp.autos.wsp.emisor.eot.util.ObjetosPruebas;
import com.gnp.autos.wsp.errors.exceptions.WSPSimpleException;
import com.gnp.autos.wsp.negocio.catalogo.model.CatalogoResp;
import com.gnp.autos.wsp.negocio.cotizacion.model.resp.TraductorResp;
import com.gnp.autos.wsp.negocio.emision.model.req.AgentesEmitirReq;
import com.gnp.autos.wsp.negocio.emision.model.req.TraductorEmitirReq;
import com.gnp.autos.wsp.negocio.neg.model.AdaptacionVehNeg;
import com.gnp.autos.wsp.negocio.neg.model.AgenteNeg;
import com.gnp.autos.wsp.negocio.neg.model.ElementoReq;
import com.gnp.autos.wsp.negocio.neg.model.EmiteNegReq;
import com.gnp.autos.wsp.negocio.neg.model.MedioCobroNeg;
import com.gnp.autos.wsp.negocio.neg.model.PersonaNeg;
import com.gnp.autos.wsp.negocio.umoservicemodel.EmisionDatos;
import com.google.gson.Gson;

@RunWith(SpringRunner.class)
public class EmisorDomainImplTest {
    /* String Request. */
    private String strTraductorEmitirReq;
    /* Request. */
    private TraductorEmitirReq traductorReq;
    /* String Request. */
    private String strEmiteNegReq;
    /* Request. */
    private EmiteNegReq consulNegReq;
    @InjectMocks
    private EmisorDomainImpl emisordomainimpl;
    @Mock
    private Domain domainEOT;
    @Mock
    private MovimientoDomain domainMov;
    @Mock
    ConfWSP conf;
    @Mock
    PasosDomain pasosClient;
    @Mock
    ProdTecComDomain prodTCClient;
    @Mock
    private RestTemplate restTemplate = new RestTemplate();
    
    @Value("classpath:/TraductorEmitirReqTest.xml")
    public void setFileTraductorEmitirReq(final Resource myRes) throws IOException {
        try (InputStream is = myRes.getInputStream() ) {
            strTraductorEmitirReq = StreamUtils.copyToString(is, StandardCharsets.UTF_8);
        }
    }
    
    @Value("classpath:/EmiteNegReqTest.json")
    public void setFileEmiteNegReq(final Resource myRes) throws IOException {
        try (InputStream is = myRes.getInputStream() ) {
            strEmiteNegReq = StreamUtils.copyToString(is, StandardCharsets.UTF_8);
        }
    }
    
    @Before
    public void prepararTest() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(TraductorEmitirReq.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        traductorReq = (TraductorEmitirReq) jaxbUnmarshaller.unmarshal(new StringReader(strTraductorEmitirReq));
        Gson gson = new Gson();
        consulNegReq = gson.fromJson(strEmiteNegReq, EmiteNegReq.class);
        consulNegReq.setIdCotizacion(ObjetosPruebas.COT_NOW);
        consulNegReq.setIniVig(ObjetosPruebas.FCH_MOV);
        consulNegReq.setFinVig(ObjetosPruebas.FCHFIN);
        EmisionDatos em = ObjetosPruebas.getEmisionTotal(consulNegReq);
        consulNegReq = em.getEmite();
    }
    
    @Before
    public void initMocks() {
        emisordomainimpl = new EmisorDomainImpl();
        // ReflectionTestUtils.setField(emisordomainimpl, "PERIODICIDAD", "La periodicidad");
        MockitoAnnotations.initMocks(this);
    }
    
    @Test(expected = ExecutionError.class)
    public void getEmisionAETest() {
        when(conf.getUrlCotizacion()).thenReturn("http://wsp-cotizador-uat.oscp.gnp.com.mx/cotizar");
        emisordomainimpl.getEmitir(traductorReq, 0);
    }
    
    @Test(expected = ExecutionError.class)
    public void getEmisionAPTest() {
        when(conf.getUrlCotizacion()).thenReturn("http://wsp-cotizador-uat.oscp.gnp.com.mx/cotizar");
        traductorReq.getSolicitud().setOperacion("P");
        emisordomainimpl.getEmitir(traductorReq, 0);
    }
    
    @Test(expected = ExecutionError.class)
    public void getEmisionAATest() {
        when(conf.getUrlCotizacion()).thenReturn("http://wsp-cotizador-uat.oscp.gnp.com.mx/cotizar");
        traductorReq.getSolicitud().setOperacion("A");
        emisordomainimpl.getEmitir(traductorReq, 0);
    }
    
    @Test(expected = ExecutionError.class)
    public void getEmisionSAgTest() {
        when(conf.getUrlCotizacion()).thenReturn("http://wsp-cotizador-uat.oscp.gnp.com.mx/cotizar");
        traductorReq.setAgentes(new AgentesEmitirReq());
        emisordomainimpl.getEmitir(traductorReq, 0);
    }
    
    @Test(expected = ExecutionError.class)
    public void getEmisionPCm100Test() {
        when(conf.getUrlCotizacion()).thenReturn("http://wsp-cotizador-uat.oscp.gnp.com.mx/cotizar");
        traductorReq.getAgentes().getAgentes().get(0).setPctParticipComision("50");
        emisordomainimpl.getEmitir(traductorReq, 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void getEmisionPCTest() {
        when(conf.getUrlCotizacion()).thenReturn("http://wsp-cotizador-uat.oscp.gnp.com.mx/cotizar");
        traductorReq.getAgentes().getAgentes().get(0).setPctParticipComision("");
        emisordomainimpl.getEmitir(traductorReq, 0);
    }
    
    @Test(expected = WSPSimpleException.class)
    public void getEmisionTest() {
        // No paso valida
        when(conf.getUrlCotizacion()).thenReturn("http://wsp-cotizador-uat.oscp.gnp.com.mx/cotizar");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscp.gnp.com.mx/catalogo");
        emisordomainimpl.getEmitir(traductorReq, 0);
    }
    
    public CatalogoResp getCatalogoResp() {
        ElementoReq elemento = new ElementoReq();
        elemento.setClave("MONEDA");
        List<ElementoReq> elementos = new ArrayList<>();
        CatalogoResp catResp = new CatalogoResp();
        catResp.setElementos(elementos);
        return catResp;
    }
    
    @Test(expected = WSPSimpleException.class)
    public void getEmision1Test() {
        // No paso valida
        when(conf.getUrlCotizacion()).thenReturn("http://wsp-cotizador-uat.oscp.gnp.com.mx/cotizar");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscp.gnp.com.mx/catalogo");
        // when(CatalogoClient.getInstancia(restTemplate).obtenCatalogo("MONEDA",
        // "http://wsp-catalogos-uat.oscp.gnp.com.mx/catalogo")).thenReturn(getCatalogoResp());
        getCatalogoResp();
        emisordomainimpl.getEmitir(traductorReq, 0);
    }
    
    @Test
    public void isRecotizaNullTest() {
        when(conf.getCodPromoRecotiza()).thenReturn(null);
        assertTrue(emisordomainimpl.isRecotiza("", ""));
    }
    
    @Test
    public void isRecotizaVacTest() {
        when(conf.getCodPromoRecotiza()).thenReturn("NOVENTADIR:OTRO:PGN");
        assertTrue(emisordomainimpl.isRecotiza("", ""));
    }
    
    @Test
    public void isRecotizaAmbTest() {
        when(conf.getCodPromoRecotiza()).thenReturn("NOVENTADIR:OTRO:PGN");
        assertFalse(emisordomainimpl.isRecotiza("NOVENTADIR", "PGN"));
    }
    
    @Test
    public void isRecotizaCPTest() {
        when(conf.getCodPromoRecotiza()).thenReturn("NOVENTADIR:OTRO:PGN");
        assertFalse(emisordomainimpl.isRecotiza("NOVENTADIR", "AAA"));
    }
    
    @Test
    public void isRecotizaHerrTest() {
        when(conf.getCodPromoRecotiza()).thenReturn("NOVENTADIR:OTRO:PGN");
        assertFalse(emisordomainimpl.isRecotiza("COP00012", "PGN"));
    }
    
    @Test
    public void isRecotizaTest() {
        when(conf.getCodPromoRecotiza()).thenReturn("NOVENTADIR:OTRO:PGN");
        assertTrue(emisordomainimpl.isRecotiza("COP00012", "AAA"));
    }
    
    @Test(expected = ExecutionError.class)
    public void validaAgenteNullTest() {
        EmiteNegReq objReqNeg = traductorReq.getEmiteNeg();
        objReqNeg.setAgentes(null);
        emisordomainimpl.validaAgente(objReqNeg);
    }
    
    @Test(expected = ExecutionError.class)
    public void validaAgenteVacTest() {
        EmiteNegReq objReqNeg = traductorReq.getEmiteNeg();
        objReqNeg.setAgentes(new ArrayList<>());
        emisordomainimpl.validaAgente(objReqNeg);
    }
    
    @Test(expected = ExecutionError.class)
    public void validaAgenteMen0Test() {
        EmiteNegReq objReqNeg = traductorReq.getEmiteNeg();
        objReqNeg.getAgentes().get(0).setPctParticipComision("-50");
        emisordomainimpl.validaAgente(objReqNeg);
    }
    
    @Test(expected = ExecutionError.class)
    public void validaAgenteMay100Test() {
        EmiteNegReq objReqNeg = traductorReq.getEmiteNeg();
        objReqNeg.getAgentes().get(0).setPctParticipComision("150");
        emisordomainimpl.validaAgente(objReqNeg);
    }
    
    @Test
    public void validaAgenteSinCompensacionTest() {
        EmiteNegReq objReqNeg = traductorReq.getEmiteNeg();
        emisordomainimpl.validaAgente(objReqNeg);
        assertNotNull(objReqNeg);
    }
    
    @Test(expected = ExecutionError.class)
    public void validaAgenteCompensacionTest() {
        EmiteNegReq objReqNeg = traductorReq.getEmiteNeg();
        List<AgenteNeg> agentes = objReqNeg.getAgentes();
        AgenteNeg objAgente = agentes.get(0);
        objAgente.setPctCesionComision("1");
        objAgente.setPctParticipComision("10");
        objAgente.setBanIntermediarioPrincipal("1");
        objAgente.setPctComisionPrima("5");
        objAgente.setIdTipoBaseComision("COM");
        objReqNeg.setAgentes(agentes);
        emisordomainimpl.validaAgente(objReqNeg);
    }
    
    @Test
    public void validaAgenteTest() {
        EmiteNegReq objReqNeg = traductorReq.getEmiteNeg();
        objReqNeg.getAgentes().get(0).setPctParticipComision("100");
        objReqNeg.getAgentes().get(0).setIdTipoBaseComision("COM");
        emisordomainimpl.validaAgente(objReqNeg);
        assertNotNull(objReqNeg);
    }
    
    @Test(expected = ExecutionError.class)
    public void validaRecotizaExcTest() {
        EmiteNegReq objReqNeg = traductorReq.getEmiteNeg();
        TraductorResp respCot = new TraductorResp();
        emisordomainimpl.validaRecotiza(respCot, objReqNeg);
    }
    
    @Test(expected = ExecutionError.class)
    public void validaRecotizaDifTotalPagarTest() {
        EmiteNegReq objReqNeg = traductorReq.getEmiteNeg();
        TraductorResp respCot = ObjetosPruebas.getTraductorResp();
        respCot.getPaquetes().getPaquetes().get(0).getTotales().getTotales().get(0).getConceptosEconomicos().get(0).setMonto("15000");
        // prima neta 3667.1
        // iva 669.94 -
        emisordomainimpl.validaRecotiza(respCot, objReqNeg);
    }
    
    @Test(expected = ExecutionError.class)
    public void validaRecotizaDifIvaTest() {
        EmiteNegReq objReqNeg = traductorReq.getEmiteNeg();
        TraductorResp respCot = ObjetosPruebas.getTraductorResp();
        respCot.getPaquetes().getPaquetes().get(0).getTotales().getTotales().get(0).getConceptosEconomicos().get(1).setMonto("600");
        emisordomainimpl.validaRecotiza(respCot, objReqNeg);
    }
    
    @Test(expected = ExecutionError.class)
    public void validaRecotizaDifIvaSinConceptoTest() {
        EmiteNegReq objReqNeg = traductorReq.getEmiteNeg();
        TraductorResp respCot = ObjetosPruebas.getTraductorResp();
        respCot.getPaquetes().getPaquetes().get(0).getTotales().getTotales().get(0).getConceptosEconomicos().get(1).setMonto("600");
        respCot.getPaquetes().getPaquetes().get(0).getTotales().getTotales().get(0).getConceptosEconomicos().get(1).setNombre("");
        emisordomainimpl.validaRecotiza(respCot, objReqNeg);
    }
    
    @Test(expected = ExecutionError.class)
    public void validaRecotizaDifPrimaNetaTest() {
        EmiteNegReq objReqNeg = traductorReq.getEmiteNeg();
        TraductorResp respCot = ObjetosPruebas.getTraductorResp();
        respCot.getPaquetes().getPaquetes().get(0).getTotales().getTotales().get(0).getConceptosEconomicos().get(2).setMonto("1500");
        emisordomainimpl.validaRecotiza(respCot, objReqNeg);
    }
    
    @Test// (expected = ExecutionError.class)
    public void validaRecotizaTest() {
        EmiteNegReq objReqNeg = traductorReq.getEmiteNeg();
        TraductorResp respCot = ObjetosPruebas.getTraductorResp();
        emisordomainimpl.validaRecotiza(respCot, objReqNeg);
        assertNotNull(objReqNeg);
    }
    
    @Test(expected = NullPointerException.class)
    public void validaVehiculoNullTest() {
        Integer tid = 0;
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        when(conf.getUrlTransacciones()).thenReturn("wsp-txlogger-uat.oscp.gnp.com.mx/txlogger");
        emisordomainimpl.validaVehiculo(emiteNegReq, tid);
    }
    
    public ConsultaPasosConfiguradorResponse getTipoCargaElemNull() {
        PasoElementoConsultaDto paso = new PasoElementoConsultaDto();
        paso.setELEMENTO("sadfas");
        PasosConsultaConfiguradorDto pasos = new PasosConsultaConfiguradorDto();
        pasos.getELEMENTOS().add(paso);
        ConsultaPasosConfiguradorResponse cResp = new ConsultaPasosConfiguradorResponse();
        cResp.setPASOCONSULTACONFIGURADOR(null);
        return cResp;
    }
    
    @Test
    public void validaVehiculoElemNullTest() {
        Integer tid = 0;
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        when(conf.getUrlTransacciones()).thenReturn("wsp-txlogger-uat.oscp.gnp.com.mx/txlogger");
        when(pasosClient.getTipoCarga(emiteNegReq, tid, "wsp-txlogger-uat.oscp.gnp.com.mx/txlogger")).thenReturn(getTipoCargaElemNull());
        emisordomainimpl.validaVehiculo(emiteNegReq, tid);
        assertNotNull(emiteNegReq);
    }
    
    public ConsultaPasosConfiguradorResponse getTipoCargaSElemNull() {
        PasoElementoConsultaDto paso = new PasoElementoConsultaDto();
        paso.setELEMENTO("sadfas");
        PasosConsultaConfiguradorDto pasos = new PasosConsultaConfiguradorDto();
        pasos.getELEMENTOS().add(null);
        ConsultaPasosConfiguradorResponse cResp = new ConsultaPasosConfiguradorResponse();
        cResp.setPASOCONSULTACONFIGURADOR(pasos);
        return cResp;
    }
    
    @Test(expected = NullPointerException.class)
    public void validaVehiculoSElemNullTest() {
        Integer tid = 0;
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        when(conf.getUrlTransacciones()).thenReturn("wsp-txlogger-uat.oscp.gnp.com.mx/txlogger");
        when(pasosClient.getTipoCarga(emiteNegReq, tid, "wsp-txlogger-uat.oscp.gnp.com.mx/txlogger")).thenReturn(getTipoCargaSElemNull());
        emisordomainimpl.validaVehiculo(emiteNegReq, tid);
    }
    
    public ConsultaPasosConfiguradorResponse getTipoCargaSE() {
        PasoElementoConsultaDto paso = new PasoElementoConsultaDto();
        paso.setELEMENTO("sadfas");
        PasosConsultaConfiguradorDto pasos = new PasosConsultaConfiguradorDto();
        pasos.getELEMENTOS().add(paso);
        ConsultaPasosConfiguradorResponse cResp = new ConsultaPasosConfiguradorResponse();
        cResp.setPASOCONSULTACONFIGURADOR(pasos);
        return cResp;
    }
    
    @Test
    public void validaVehiculoSETest() {
        Integer tid = 0;
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        when(conf.getUrlTransacciones()).thenReturn("wsp-txlogger-uat.oscp.gnp.com.mx/txlogger");
        when(pasosClient.getTipoCarga(emiteNegReq, tid, "wsp-txlogger-uat.oscp.gnp.com.mx/txlogger")).thenReturn(getTipoCargaSE());
        emisordomainimpl.validaVehiculo(emiteNegReq, tid);
        assertNotNull(emiteNegReq);
    }
    
    public ConsultaPasosConfiguradorResponse getTipoCargaSEmpty() {
        PasoElementoConsultaDto paso = new PasoElementoConsultaDto();
        paso.setELEMENTO("sadfas");
        paso.getVALORESELEMENTO().add(new PasoElementoConsultaDto());
        PasosConsultaConfiguradorDto pasos = new PasosConsultaConfiguradorDto();
        pasos.getELEMENTOS().add(paso);
        ConsultaPasosConfiguradorResponse cResp = new ConsultaPasosConfiguradorResponse();
        cResp.setPASOCONSULTACONFIGURADOR(pasos);
        return cResp;
    }
    
    @Test(expected = NullPointerException.class)
    public void validaVehiculoSEmptyTest() {
        Integer tid = 0;
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        when(conf.getUrlTransacciones()).thenReturn("wsp-txlogger-uat.oscp.gnp.com.mx/txlogger");
        when(pasosClient.getTipoCarga(emiteNegReq, tid, "wsp-txlogger-uat.oscp.gnp.com.mx/txlogger")).thenReturn(getTipoCargaSEmpty());
        emisordomainimpl.validaVehiculo(emiteNegReq, tid);
    }
    
    public ConsultaPasosConfiguradorResponse getTipoCargaSVE() {
        PasoElementoConsultaDto paso = new PasoElementoConsultaDto();
        paso.setELEMENTO("sadfas");
        PasosConsultaConfiguradorDto pasos = new PasosConsultaConfiguradorDto();
        pasos.getELEMENTOS().add(paso);
        ConsultaPasosConfiguradorResponse cResp = new ConsultaPasosConfiguradorResponse();
        cResp.setPASOCONSULTACONFIGURADOR(pasos);
        return cResp;
    }
    
    @Test// (expected = NullPointerException.class)
    public void validaVehiculoSVETest() {
        Integer tid = 0;
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        when(conf.getUrlTransacciones()).thenReturn("wsp-txlogger-uat.oscp.gnp.com.mx/txlogger");
        when(pasosClient.getTipoCarga(emiteNegReq, tid, "wsp-txlogger-uat.oscp.gnp.com.mx/txlogger")).thenReturn(getTipoCargaSVE());
        emisordomainimpl.validaVehiculo(emiteNegReq, tid);
        assertNotNull(emiteNegReq);
    }
    
    public ConsultaPasosConfiguradorResponse getTipoCargaC1() {
        PasoElementoConsultaDto elemcon = new PasoElementoConsultaDto();
        elemcon.setELEMENTO("sdfsa:1");
        PasoElementoConsultaDto paso = new PasoElementoConsultaDto();
        paso.setELEMENTO("sadfas");
        paso.getVALORESELEMENTO().add(elemcon);
        PasosConsultaConfiguradorDto pasos = new PasosConsultaConfiguradorDto();
        pasos.getELEMENTOS().add(paso);
        ConsultaPasosConfiguradorResponse cResp = new ConsultaPasosConfiguradorResponse();
        cResp.setPASOCONSULTACONFIGURADOR(pasos);
        return cResp;
    }
    
    @Test(expected = ExecutionError.class)
    public void validaVehiculoC1Test() {
        Integer tid = 0;
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        when(conf.getUrlTransacciones()).thenReturn("wsp-txlogger-uat.oscp.gnp.com.mx/txlogger");
        when(pasosClient.getTipoCarga(emiteNegReq, tid, "wsp-txlogger-uat.oscp.gnp.com.mx/txlogger")).thenReturn(getTipoCargaC1());
        emisordomainimpl.validaVehiculo(emiteNegReq, tid);
    }
    
    public ConsultaPasosConfiguradorResponse getTipoCargaC0() {
        PasoElementoConsultaDto elemcon = new PasoElementoConsultaDto();
        elemcon.setELEMENTO("sdfsa:0");
        PasoElementoConsultaDto paso = new PasoElementoConsultaDto();
        paso.setELEMENTO("sadfas");
        paso.getVALORESELEMENTO().add(elemcon);
        PasosConsultaConfiguradorDto pasos = new PasosConsultaConfiguradorDto();
        pasos.getELEMENTOS().add(paso);
        ConsultaPasosConfiguradorResponse cResp = new ConsultaPasosConfiguradorResponse();
        cResp.setPASOCONSULTACONFIGURADOR(pasos);
        return cResp;
    }
    
    @Test// (expected = NullPointerException.class)
    public void validaVehiculoC0Test() {
        Integer tid = 0;
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getVehiculo().setTipoCarga("Z");
        when(conf.getUrlTransacciones()).thenReturn("wsp-txlogger-uat.oscp.gnp.com.mx/txlogger");
        when(pasosClient.getTipoCarga(emiteNegReq, tid, "wsp-txlogger-uat.oscp.gnp.com.mx/txlogger")).thenReturn(getTipoCargaC0());
        emisordomainimpl.validaVehiculo(emiteNegReq, tid);
        assertNotNull(emiteNegReq);
    }
    
    @Test// (expected = NullPointerException.class)
    public void validaVehiculoC0ATest() {
        Integer tid = 0;
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getVehiculo().setTipoCarga("A");
        when(conf.getUrlTransacciones()).thenReturn("wsp-txlogger-uat.oscp.gnp.com.mx/txlogger");
        when(pasosClient.getTipoCarga(emiteNegReq, tid, "wsp-txlogger-uat.oscp.gnp.com.mx/txlogger")).thenReturn(getTipoCargaC0());
        emisordomainimpl.validaVehiculo(emiteNegReq, tid);
        assertNotNull(emiteNegReq);
    }
    
    @Test// (expected = NullPointerException.class)
    public void validaVehiculoC0nullTest() {
        Integer tid = 0;
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getVehiculo().setTipoCarga(null);
        when(conf.getUrlTransacciones()).thenReturn("wsp-txlogger-uat.oscp.gnp.com.mx/txlogger");
        when(pasosClient.getTipoCarga(emiteNegReq, tid, "wsp-txlogger-uat.oscp.gnp.com.mx/txlogger")).thenReturn(getTipoCargaC0());
        emisordomainimpl.validaVehiculo(emiteNegReq, tid);
        assertNotNull(emiteNegReq);
    }
    
    public ConsultaPasosConfiguradorResponse getTipoCarga() {
        PasoElementoConsultaDto elemcon = new PasoElementoConsultaDto();
        elemcon.setELEMENTO("sdfsa:1");
        PasoElementoConsultaDto paso = new PasoElementoConsultaDto();
        paso.setELEMENTO("sadfas");
        paso.getVALORESELEMENTO().add(elemcon);
        PasosConsultaConfiguradorDto pasos = new PasosConsultaConfiguradorDto();
        pasos.getELEMENTOS().add(paso);
        ConsultaPasosConfiguradorResponse cResp = new ConsultaPasosConfiguradorResponse();
        cResp.setPASOCONSULTACONFIGURADOR(pasos);
        return cResp;
    }
    
    @Test// (expected = ExecutionError.class)
    public void validaVehiculoTCnullTest() {
        Integer tid = 0;
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getVehiculo().setTipoCarga("A");
        when(conf.getUrlTransacciones()).thenReturn("wsp-txlogger-uat.oscp.gnp.com.mx/txlogger");
        when(pasosClient.getTipoCarga(emiteNegReq, tid, "wsp-txlogger-uat.oscp.gnp.com.mx/txlogger")).thenReturn(getTipoCarga());
        emisordomainimpl.validaVehiculo(emiteNegReq, tid);
        assertNotNull(emiteNegReq);
    }
    
    @Test(expected = ExecutionError.class)
    public void validaVehiculoTest() {
        Integer tid = 0;
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getVehiculo().setTipoCarga("Z");
        when(conf.getUrlTransacciones()).thenReturn("wsp-txlogger-uat.oscp.gnp.com.mx/txlogger");
        when(pasosClient.getTipoCarga(emiteNegReq, tid, "wsp-txlogger-uat.oscp.gnp.com.mx/txlogger")).thenReturn(getTipoCarga());
        emisordomainimpl.validaVehiculo(emiteNegReq, tid);
    }
    
    @Test
    public void validaAdaptacionesSATest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emisordomainimpl.validaAdaptaciones(emiteNegReq);
        assertNotNull(emiteNegReq);
    }
    
    @Test// (expected = ExecutionError.class)
    public void validaAdaptaciones2Test() {
        AdaptacionVehNeg avn = ObjetosPruebas.getAdaptaciones();
        avn.setFechaFactura("12");
        List<AdaptacionVehNeg> adaptaciones = new ArrayList<>();
        // adaptaciones.add(avn);
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getVehiculo().setAdaptaciones(adaptaciones);
        emisordomainimpl.validaAdaptaciones(emiteNegReq);
        assertNotNull(emiteNegReq);
    }
    
    @Test(expected = ExecutionError.class)
    public void validaAdaptacionesFchExTest() {
        AdaptacionVehNeg avn = ObjetosPruebas.getAdaptaciones();
        avn.setFechaFactura("12");
        List<AdaptacionVehNeg> adaptaciones = new ArrayList<>();
        adaptaciones.add(avn);
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getVehiculo().setAdaptaciones(adaptaciones);
        emisordomainimpl.validaAdaptaciones(emiteNegReq);
    }
    
    @Test// (expected = ExecutionError.class)
    public void validaAdaptacionesTest() {
        List<AdaptacionVehNeg> adaptaciones = new ArrayList<>();
        adaptaciones.add(ObjetosPruebas.getAdaptaciones());
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getVehiculo().setAdaptaciones(adaptaciones);
        emisordomainimpl.validaAdaptaciones(emiteNegReq);
        assertNotNull(emiteNegReq);
    }
    
    @Test(expected = ExecutionError.class)
    public void validaPersonaSinContratanteTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getPersonas().get(0).setTipo("OTRO");
        emisordomainimpl.validaPersona(emiteNegReq);
    }
    
    @Test(expected = ExecutionError.class)
    public void validaPersonaValorNullTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getPersonas().get(0).setRfc(null);
        emisordomainimpl.validaPersona(emiteNegReq);
    }
    
    @Test(expected = ExecutionError.class)
    public void validaPersonaValorEmptyTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getPersonas().get(0).setRfc("");
        emisordomainimpl.validaPersona(emiteNegReq);
    }
    
    @Test(expected = ExecutionError.class)
    public void validaPersonaMoralJTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getPersonas().get(0).setTipoPersona("J");
        emiteNegReq.getPersonas().get(0).setRazonSocial("asdas");
        emiteNegReq.getPersonas().get(0).setFecConstitucion("20190101");
        emisordomainimpl.validaPersona(emiteNegReq);
    }
    
    @Test(expected = ExecutionError.class)
    public void validaPersonaMoralConductTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getPersonas().get(0).setTipoPersona("M");
        emiteNegReq.getPersonas().get(0).setRazonSocial("asdas");
        emiteNegReq.getPersonas().get(0).setFecConstitucion("20190101");
        emiteNegReq.setIndContConductor("1");
        emisordomainimpl.validaPersona(emiteNegReq);
    }
    
    @Test(expected = ExecutionError.class)
    public void validaPersonaMoralRFCErrTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getPersonas().get(0).setTipoPersona("M");
        emiteNegReq.getPersonas().get(0).setRazonSocial("asdas");
        emiteNegReq.getPersonas().get(0).setFecConstitucion("20190101");
        emiteNegReq.getPersonas().get(0).setRfc("ASJ1403185L6A");
        emisordomainimpl.validaPersona(emiteNegReq);
    }
    
    @Test(expected = ExecutionError.class)
    public void validaPersonaMoralRFCerrTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getPersonas().get(0).setTipoPersona("M");
        emiteNegReq.getPersonas().get(0).setRazonSocial("asdas");
        emiteNegReq.getPersonas().get(0).setFecConstitucion("20190101");
        emiteNegReq.getPersonas().get(0).setRfc("ASJ1403185L6AA");
        emiteNegReq.setIndContConductor("0");
        emisordomainimpl.validaPersona(emiteNegReq);
    }
    
    @Test// (expected = ExecutionError.class)
    public void validaPersonaMoralRFCTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getPersonas().get(0).setTipoPersona("M");
        emiteNegReq.getPersonas().get(0).setRazonSocial("asdas");
        emiteNegReq.getPersonas().get(0).setFecConstitucion("20190101");
        emiteNegReq.getPersonas().get(0).setRfc("ASJ1403185L6");
        emiteNegReq.setIndContConductor("0");
        emisordomainimpl.validaPersona(emiteNegReq);
        assertNotNull(emiteNegReq);
    }
    
    @Test(expected = ExecutionError.class)
    public void validaPersonaEdadTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getPersonas().get(0).setFecNacimiento(null);
        emiteNegReq.getPersonas().get(0).setEdad(null);
        emisordomainimpl.validaPersona(emiteNegReq);
    }
    
    @Test
    public void validaPersonaFcNacNullEdadVacTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        // emiteNegReq.getPersonas().get(0).setFecNacimiento(null);
        emiteNegReq.getPersonas().get(0).setEdad("19");
        emisordomainimpl.validaPersona(emiteNegReq);
        assertNotNull(emiteNegReq);
    }
    
    @Test// (expected = ExecutionError.class)
    public void validaPersonaFcNacEdadVacTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getPersonas().get(0).setEdad("");
        emisordomainimpl.validaPersona(emiteNegReq);
        assertNotNull(emiteNegReq);
    }
    
    @Test// (expected = ExecutionError.class)
    public void validaPersonaFcNacEdadNullTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getPersonas().get(0).setEdad(null);
        emisordomainimpl.validaPersona(emiteNegReq);
        assertNotNull(emiteNegReq);
    }
    
    @Test(expected = ExecutionError.class)
    public void validaPersonaRFCerrTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getPersonas().get(0).setRfc("RICA800925PL7x");
        emisordomainimpl.validaPersona(emiteNegReq);
    }
    
    @Test(expected = ExecutionError.class)
    public void validaPersonaRFClistNegraTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getPersonas().get(0).setRfc("BUEY800925PL7");
        emisordomainimpl.validaPersona(emiteNegReq);
    }
    
    @Test// (expected = ExecutionError.class)
    public void validaPersonaTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emisordomainimpl.validaPersona(emiteNegReq);
        assertNotNull(emiteNegReq);
    }
    
    @Test(expected = ExecutionError.class)
    public void validaPersonaSexoNullTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getPersonas().get(0).setSexo(null);
        emisordomainimpl.validaPersona(emiteNegReq);
    }
    
    @Test(expected = ExecutionError.class)
    public void validaPersonaSexoDifTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getPersonas().get(0).setSexo("J");
        emisordomainimpl.validaPersona(emiteNegReq);
    }
    
    @Test// (expected = ExecutionError.class)
    public void validaPersonaSexoFTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getPersonas().get(0).setSexo("F");
        emisordomainimpl.validaPersona(emiteNegReq);
        assertNotNull(emiteNegReq);
    }
    
    @Test// (expected = ExecutionError.class)
    public void validaPersonaSexoMTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getPersonas().get(0).setSexo("M");
        emisordomainimpl.validaPersona(emiteNegReq);
        assertNotNull(emiteNegReq);
    }
    
    @Test(expected = ExecutionError.class)
    public void validaPersonaDomNullTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getPersonas().get(0).setDomicilio(null);
        emisordomainimpl.validaPersona(emiteNegReq);
    }
    
    @Test(expected = ExecutionError.class)
    public void validaPersonaMedioCobroVpBcMcNullTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getPersonas().get(0).setMedioCobro(null);
        emiteNegReq.setViaPago("BC");
        emisordomainimpl.validaPersona(emiteNegReq);
    }
    
    @Test
    public void validaPersonaMedioCobroNullTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.setViaPago("IN");
        emisordomainimpl.validaPersona(emiteNegReq);
        assertNotNull(emiteNegReq);
    }
    
    @Test(expected = ExecutionError.class)
    public void validaPersonaMedioCobroLngNullTest() {
        MedioCobroNeg medioCobro = new MedioCobroNeg();
        medioCobro.setCveEntidadFinanciera("assdasd");
        medioCobro.setCveTipoBancario(null);
        medioCobro.setCveTipoCuentaTarjeta("32452345");
        medioCobro.setFchVencimiento("20200101");
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getPersonas().get(0).setMedioCobro(medioCobro);
        emiteNegReq.setViaPago("BC");
        emisordomainimpl.validaPersona(emiteNegReq);
    }
    
    @Test(expected = ExecutionError.class)
    public void validaPersonaEdadErrTest() {
        MedioCobroNeg medioCobro = new MedioCobroNeg();
        medioCobro.setCveEntidadFinanciera("assdasd");
        medioCobro.setCveTipoBancario(null);
        medioCobro.setCveTipoCuentaTarjeta("32452345");
        medioCobro.setFchVencimiento("20200101");
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getPersonas().get(0).setMedioCobro(medioCobro);
        emiteNegReq.setViaPago("BC");
        emiteNegReq.getPersonas().get(0).setEdad("5");
        emisordomainimpl.validaPersona(emiteNegReq);
    }
    
    @Test(expected = ExecutionError.class)
    public void validaPersonaMedioCobroLngEmpTest() {
        MedioCobroNeg medioCobro = new MedioCobroNeg();
        medioCobro.setCveEntidadFinanciera("assdasd");
        medioCobro.setCveTipoBancario("");
        medioCobro.setCveTipoCuentaTarjeta("32452345");
        medioCobro.setFchVencimiento("20200101");
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getPersonas().get(0).setMedioCobro(medioCobro);
        emiteNegReq.setViaPago("IN");
        emisordomainimpl.validaPersona(emiteNegReq);
    }
    
    @Test(expected = ExecutionError.class)
    public void validaPersonaMedioCobroLngErrTest() {
        MedioCobroNeg medioCobro = new MedioCobroNeg();
        medioCobro.setCveEntidadFinanciera("assdasd");
        medioCobro.setCveTipoBancario("435234");
        medioCobro.setCveTipoCuentaTarjeta("32452345");
        medioCobro.setFchVencimiento("20200101");
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getPersonas().get(0).setMedioCobro(medioCobro);
        emiteNegReq.setViaPago("IN");
        emisordomainimpl.validaPersona(emiteNegReq);
    }
    
    @Test// (expected = ExecutionError.class)
    public void validaPersonaMedioCobroTest() {
        MedioCobroNeg medioCobro = new MedioCobroNeg();
        medioCobro.setCveEntidadFinanciera("assdasd");
        medioCobro.setCveTipoBancario("5");
        medioCobro.setCveTipoCuentaTarjeta("32");
        medioCobro.setFchVencimiento("200101");
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getPersonas().get(0).setMedioCobro(medioCobro);
        emiteNegReq.setViaPago("IN");
        emisordomainimpl.validaPersona(emiteNegReq);
        assertNotNull(emiteNegReq);
    }
    
    @Test(expected = WSPSimpleException.class)
    public void validaNegTest() {
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscp.gnp.com.mx/catalogo");
        // when(CatalogoClient.getInstancia(restTemplate).obtenCatalogo("PERIODICIDAD",
        // "http://wsp-catalogos-uat.oscp.gnp.com.mx/catalogo")).thenReturn(getCatalogoPeriodicidadResp());
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emisordomainimpl.validaNeg(emiteNegReq);
    }
    
    @SuppressWarnings("static-access")
    @Test// (expected = NullPointerException.class)
    public void complementaDatosCpNullTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emisordomainimpl.complementaDatos(emiteNegReq);
        assertNotNull(emiteNegReq);
    }
    
    @SuppressWarnings("static-access")
    @Test// (expected = NullPointerException.class)
    public void complementaDatosCpVacTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getVehiculo().setCp("");
        emisordomainimpl.complementaDatos(emiteNegReq);
        assertNotNull(emiteNegReq);
    }
    
    @SuppressWarnings("static-access")
    @Test(expected = NullPointerException.class)
    public void complementaDatosCntNullTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getVehiculo().setCp("");
        emiteNegReq.getPersonas().get(0).setDomicilio(null);
        emisordomainimpl.complementaDatos(emiteNegReq);
    }
    
    @SuppressWarnings("static-access")
    @Test// (expected = NullPointerException.class)
    public void complementaDatosCntCpNullTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getVehiculo().setCp("");
        emiteNegReq.getPersonas().get(0).getDomicilio().setCodigoPostal(null);
        emisordomainimpl.complementaDatos(emiteNegReq);
        assertNotNull(emiteNegReq);
    }
    
    @SuppressWarnings("static-access")
    @Test// (expected = NullPointerException.class)
    public void complementaDatosCntVacTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getVehiculo().setCp("");
        emiteNegReq.getPersonas().get(0).getDomicilio().setCodigoPostal("");
        emisordomainimpl.complementaDatos(emiteNegReq);
        assertNotNull(emiteNegReq);
    }
    
    @SuppressWarnings("static-access")
    @Test// (expected = NullPointerException.class)
    public void complementaDatosTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getVehiculo().setCp("03330");
        emisordomainimpl.complementaDatos(emiteNegReq);
        assertNotNull(emiteNegReq);
    }
    
    @SuppressWarnings("static-access")
    @Test// (expected = NullPointerException.class)
    public void complementaDatosEdoCircNullTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getVehiculo().setCp("03330");
        emiteNegReq.getVehiculo().setEstadoCirculacion(null);
        emisordomainimpl.complementaDatos(emiteNegReq);
        assertNotNull(emiteNegReq);
    }
    
    @SuppressWarnings("static-access")
    @Test
    public void complementaDatosEdoCircVacTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getVehiculo().setCp("03330");
        emiteNegReq.getVehiculo().setEstadoCirculacion("");
        emisordomainimpl.complementaDatos(emiteNegReq);
        assertNotNull(emiteNegReq);
    }
    
    @SuppressWarnings("static-access")
    @Test(expected = ExecutionError.class)
    public void validaBanderasTest() {
        assertTrue(true);
        emisordomainimpl.validaBanderas("valor", "msgError", "msgFin");
    }
    
    @SuppressWarnings("static-access")
    @Test(expected = ExecutionError.class)
    public void validaBanderasValNullTest() {
        assertTrue(true);
        emisordomainimpl.validaBanderas(null, "msgError", "msgFin");
    }
    
    @SuppressWarnings("static-access")
    @Test
    public void validaBanderasVal1Test() {
        assertTrue(true);
        emisordomainimpl.validaBanderas("1", "msgError", "msgFin");
    }
    
    @SuppressWarnings("static-access")
    @Test
    public void validaBanderasVal0Test() {
        assertTrue(true);
        emisordomainimpl.validaBanderas("0", "msgError", "msgFin");
    }
    
    @SuppressWarnings("static-access")
    @Test
    public void calculaEdadTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getVehiculo().setCp("03330");
        emiteNegReq.getVehiculo().setEstadoCirculacion("");
        PersonaNeg cond = emiteNegReq.getPersonas().get(0);
        emisordomainimpl.calculaEdad(Optional.of(emiteNegReq.getPersonas().get(0)), cond);
        assertNotNull(emiteNegReq);
    }
    
    @Test(expected = NullPointerException.class)
    public void validaCargaVnullTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getVehiculo().setCp("03330");
        emiteNegReq.getVehiculo().setEstadoCirculacion("");
        emiteNegReq.getAgentes().get(0).setPctParticipComision("");
        PasoElementoConsultaDto elem = new PasoElementoConsultaDto();
        elem.setELEMENTO("alala");
        elem.getVALORESELEMENTO().add(elem);
        elem.getVALORESELEMENTO().set(0, null);
        emisordomainimpl.validaCargaV(elem, emiteNegReq);
    }
    
    @Test
    public void validaCargaVTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getVehiculo().setCp("03330");
        emiteNegReq.getVehiculo().setEstadoCirculacion("");
        emiteNegReq.getAgentes().get(0).setPctParticipComision("");
        PasoElementoConsultaDto elem = new PasoElementoConsultaDto();
        emisordomainimpl.validaCargaV(elem, emiteNegReq);
        assertNotNull(emiteNegReq);
    }
    
    @SuppressWarnings("static-access")
    @Test(expected = ExecutionError.class)
    public void banderaConductorNullTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getVehiculo().setCp("03330");
        emiteNegReq.getVehiculo().setEstadoCirculacion("");
        emiteNegReq.getAgentes().get(0).setPctParticipComision("");
        emiteNegReq.setIndContConductor(null);
        emisordomainimpl.banderaConductor(emiteNegReq, emiteNegReq.getPersonas(), Optional.of(emiteNegReq.getPersonas().get(0)));
    }
    
    @SuppressWarnings("static-access")
    @Test(expected = ExecutionError.class)
    public void banderaConductorB3Test() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getVehiculo().setCp("03330");
        emiteNegReq.getVehiculo().setEstadoCirculacion("");
        emiteNegReq.getAgentes().get(0).setPctParticipComision("");
        emiteNegReq.setIndContConductor("3");
        emisordomainimpl.banderaConductor(emiteNegReq, emiteNegReq.getPersonas(), Optional.of(emiteNegReq.getPersonas().get(0)));
    }
    
    @SuppressWarnings("static-access")
    @Test(expected = ExecutionError.class)
    public void banderaConductorNullNoPresentTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getVehiculo().setCp("03330");
        emiteNegReq.getVehiculo().setEstadoCirculacion("");
        emiteNegReq.getAgentes().get(0).setPctParticipComision("");
        emiteNegReq.setIndContConductor(null);
        emiteNegReq.getPersonas().get(1).setTipo("BENEFICIARIO");
        emisordomainimpl.banderaConductor(emiteNegReq, emiteNegReq.getPersonas(), Optional.of(emiteNegReq.getPersonas().get(0)));
    }
    
    @SuppressWarnings("static-access")
    @Test// (expected = ExecutionError.class)
    public void banderaConductorNullPresentTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getVehiculo().setCp("03330");
        emiteNegReq.getVehiculo().setEstadoCirculacion("");
        emiteNegReq.getAgentes().get(0).setPctParticipComision("");
        // emiteNegReq.setIndContConductor(null);
        emiteNegReq.getPersonas().get(1).setTipo("BENEFICIARIO");
        emisordomainimpl.banderaConductor(emiteNegReq, emiteNegReq.getPersonas(), Optional.of(emiteNegReq.getPersonas().get(0)));
        assertNotNull(emiteNegReq);
    }
    
    @SuppressWarnings("static-access")
    @Test(expected = ExecutionError.class)
    public void banderaConductorCerPresentTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getVehiculo().setCp("03330");
        emiteNegReq.getVehiculo().setEstadoCirculacion("");
        emiteNegReq.getAgentes().get(0).setPctParticipComision("");
        emiteNegReq.setIndContConductor("0");
        emiteNegReq.getPersonas().get(1).setTipo("BENEFICIARIO");
        emisordomainimpl.banderaConductor(emiteNegReq, emiteNegReq.getPersonas(), Optional.of(emiteNegReq.getPersonas().get(0)));
    }
    
    @SuppressWarnings("static-access")
    @Test// (expected = ExecutionError.class)
    public void banderaBeneficiarioSinBTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getVehiculo().setCp("03330");
        emiteNegReq.getVehiculo().setEstadoCirculacion("");
        emiteNegReq.getAgentes().get(0).setPctParticipComision("");
        emiteNegReq.setIndContConductor("0");
        emiteNegReq.getPersonas().get(2).setTipo("OTRO");
        emisordomainimpl.banderaBeneficiario(emiteNegReq, emiteNegReq.getPersonas(), Optional.of(emiteNegReq.getPersonas().get(0)));
        assertNotNull(emiteNegReq);
    }
    
    @SuppressWarnings("static-access")
    @Test(expected = ExecutionError.class)
    public void banderaBeneficiarioIndNullTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getVehiculo().setCp("03330");
        emiteNegReq.getVehiculo().setEstadoCirculacion("");
        emiteNegReq.getAgentes().get(0).setPctParticipComision("");
        emiteNegReq.setIndContConductor("0");
        emiteNegReq.getPersonas().get(2).setTipo("OTRO");
        emiteNegReq.setIndContBenef(null);
        emisordomainimpl.banderaBeneficiario(emiteNegReq, emiteNegReq.getPersonas(), Optional.of(emiteNegReq.getPersonas().get(0)));
    }
    
    @SuppressWarnings("static-access")
    @Test// (expected = ExecutionError.class)
    public void banderaBeneficiarioInd0Test() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getVehiculo().setCp("03330");
        emiteNegReq.getVehiculo().setEstadoCirculacion("");
        emiteNegReq.getAgentes().get(0).setPctParticipComision("");
        emiteNegReq.setIndContBenef("0");
        emisordomainimpl.banderaBeneficiario(emiteNegReq, emiteNegReq.getPersonas(), Optional.of(emiteNegReq.getPersonas().get(0)));
        assertNotNull(emiteNegReq);
    }
    
    @SuppressWarnings("static-access")
    @Test(expected = ExecutionError.class)
    public void banderaBeneficiarioIndotroTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getVehiculo().setCp("03330");
        emiteNegReq.getVehiculo().setEstadoCirculacion("");
        emiteNegReq.getAgentes().get(0).setPctParticipComision("");
        emiteNegReq.setIndContBenef("2");
        emisordomainimpl.banderaBeneficiario(emiteNegReq, emiteNegReq.getPersonas(), Optional.of(emiteNegReq.getPersonas().get(0)));
    }
    
    @SuppressWarnings("static-access")
    @Test(expected = ExecutionError.class)
    public void banderaBeneficiarioNullFalseTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getVehiculo().setCp("03330");
        emiteNegReq.getVehiculo().setEstadoCirculacion("");
        emiteNegReq.getAgentes().get(0).setPctParticipComision("");
        emiteNegReq.setIndContBenef(null);
        emisordomainimpl.banderaBeneficiario(emiteNegReq, emiteNegReq.getPersonas(), Optional.of(emiteNegReq.getPersonas().get(0)));
    }
    
    @SuppressWarnings("static-access")
    @Test// (expected = ExecutionError.class)
    public void banBeneficiarioCeroPersonaMTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        emiteNegReq.getPersonas().get(0).setBanIrrevocable("1");
        emiteNegReq.getPersonas().get(0).setTipoPersona("M");
        emiteNegReq.getPersonas().get(0).setRazonSocial("asd");
        Optional<PersonaNeg> benefNeg = Optional.of(emiteNegReq.getPersonas().get(0));
        emisordomainimpl.banBeneficiarioCero(benefNeg);
        assertNotNull(emiteNegReq);
    }
    
    @SuppressWarnings("static-access")
    @Test(expected = ExecutionError.class)
    public void banBeneficiarioCeroTest() {
        EmiteNegReq emiteNegReq = traductorReq.getEmiteNeg();
        Optional<PersonaNeg> benefNeg = Optional.of(emiteNegReq.getPersonas().get(0));
        emisordomainimpl.banBeneficiarioCero(benefNeg);
    }
}