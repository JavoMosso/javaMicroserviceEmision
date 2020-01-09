package com.gnp.autos.wsp.emisor.eot.domain.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBIntrospector;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;

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

import com.gnp.autos.wsp.emisor.eot.cancelacion.CancelacionReq;
import com.gnp.autos.wsp.emisor.eot.cancelacion.wsdl.Cabecera;
import com.gnp.autos.wsp.emisor.eot.cancelacion.wsdl.CancelaPolizaDto;
import com.gnp.autos.wsp.emisor.eot.cancelacion.wsdl.CancelarRequestData;
import com.gnp.autos.wsp.emisor.eot.cancelacion.wsdl.EmitirCancelacionRequest;
import com.gnp.autos.wsp.emisor.eot.cancelacion.wsdl.EmitirCancelacionResponse2;
import com.gnp.autos.wsp.emisor.eot.cancelacion.wsdl.TarificarCancelacionRequest;
import com.gnp.autos.wsp.emisor.eot.cancelacion.wsdl.TarificarCancelacionResponse2;
import com.gnp.autos.wsp.emisor.eot.config.ConfWSP;
import com.gnp.autos.wsp.emisor.eot.consulta.wsdl.ConsultarPolizaPorNumPolizaResponse;
import com.gnp.autos.wsp.emisor.eot.consulta.wsdl.PolizaDto;
import com.gnp.autos.wsp.emisor.eot.domain.ConsultaDomain;
import com.gnp.autos.wsp.emisor.eot.domain.FoliadorDomain;
import com.gnp.autos.wsp.emisor.eot.error.ExecutionError;
import com.gnp.autos.wsp.emisor.eot.soap.service.CancelacionClient;
import com.gnp.autos.wsp.emisor.eot.util.Constantes;
import com.gnp.autos.wsp.emisor.eot.util.Utileria;

@RunWith(SpringRunner.class)
public class CancelacionDomainImplTest {
    private String strCancelacionReq;
    private CancelacionReq req;
    @InjectMocks
    private CancelacionDomainImpl cancelaciondomainimpl;
    @Mock
    private FoliadorDomain foliador;
    @Mock
    private CancelacionClient tarificar;
    @Mock
    private ConfWSP conf;
    @Mock
    private ConsultaDomain consulta;
    @Mock
    private RestTemplate restTemplate = new RestTemplate();
    
    @Value("classpath:/CancelacionReqTest.xml")
    public void setFileCancelacionReq(final Resource myRes) throws IOException {
        try (InputStream is = myRes.getInputStream() ) {
            strCancelacionReq = StreamUtils.copyToString(is, StandardCharsets.UTF_8);
        }
    }
    
    @Before
    public void initMocks() {
        cancelaciondomainimpl = new CancelacionDomainImpl();
        MockitoAnnotations.initMocks(this);
    }
    
    @Before
    public void prepararTest() throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(CancelacionReq.class);
        Unmarshaller u = jc.createUnmarshaller();
        req = (CancelacionReq) JAXBIntrospector.getValue(u.unmarshal(new StringReader(strCancelacionReq)));
    }
    
    public ConsultarPolizaPorNumPolizaResponse getCRespNull() {
        ConsultarPolizaPorNumPolizaResponse cResp = new ConsultarPolizaPorNumPolizaResponse();
        return cResp;
    }
    
    @Test(expected = ExecutionError.class)
    public void cancelacionNTUVacPolizaTest() {
        when(consulta.consultarPoliza("00001404597534", "1", null)).thenReturn(getCRespNull());
        assertNotNull(cancelaciondomainimpl.cancelacionNTU(req));
    }
    
    @Test(expected = ExecutionError.class)
    public void cancelacionNTUNotPolizaTest() {
        when(consulta.consultarPoliza("00001404597534", "1", null)).thenReturn(null);
        assertNotNull(cancelaciondomainimpl.cancelacionNTU(req));
    }
    
    public ConsultarPolizaPorNumPolizaResponse getCRespFechaVigencia() {
        PolizaDto e = new PolizaDto();
        e.setFchInicioPoliza("20180808");
        ConsultarPolizaPorNumPolizaResponse cResp = new ConsultarPolizaPorNumPolizaResponse();
        cResp.getPolizaDto().add(e);
        return cResp;
    }
    
    @Test(expected = ExecutionError.class)
    public void cancelacionNTUFechaVigenciaTest() {
        when(consulta.consultarPoliza("00001404597534", "1", null)).thenReturn(getCRespFechaVigencia());
        cancelaciondomainimpl.cancelacionNTU(req);
    }
    
    public ConsultarPolizaPorNumPolizaResponse getCRespEstatusPoliza() {
        PolizaDto e = new PolizaDto();
        e.setFchInicioPoliza("20190808");
        e.setEstPoliza("CANCELADA");
        ConsultarPolizaPorNumPolizaResponse cResp = new ConsultarPolizaPorNumPolizaResponse();
        cResp.getPolizaDto().add(e);
        return cResp;
    }
    
    @Test(expected = ExecutionError.class)
    public void cancelacionNTUEstatusPolizaTest() {
        when(consulta.consultarPoliza("00001404597534", "1", null)).thenReturn(getCRespEstatusPoliza());
        cancelaciondomainimpl.cancelacionNTU(req);
    }
    
    @Test
    public void obtenerRespTest() throws DatatypeConfigurationException, ParseException {
        EmitirCancelacionResponse2 resCancela = getTarificarCancelacion(req);
        assertNotNull(cancelaciondomainimpl.obtenerResp(resCancela));
    }
    
    public ConsultarPolizaPorNumPolizaResponse getCResp() {
        PolizaDto e = new PolizaDto();
        e.setArmadora("AA");
        e.setFchInicioPoliza(Utileria.getStrNow());
        e.setEstPoliza("ACTIVA");
        ConsultarPolizaPorNumPolizaResponse cResp = new ConsultarPolizaPorNumPolizaResponse();
        cResp.getPolizaDto().add(e);
        return cResp;
    }
    
    public ConsultarPolizaPorNumPolizaResponse getCRespCancelada() {
        PolizaDto e = new PolizaDto();
        e.setArmadora("AA");
        e.setFchInicioPoliza(Utileria.getStrNow());
        e.setEstPoliza("CANCELADA");
        ConsultarPolizaPorNumPolizaResponse cResp = new ConsultarPolizaPorNumPolizaResponse();
        cResp.getPolizaDto().add(e);
        return cResp;
    }
    
    public ConsultarPolizaPorNumPolizaResponse getCRespNow() {
        PolizaDto e = new PolizaDto();
        e.setArmadora("AA");
        e.setFchInicioPoliza("20190819");
        e.setEstPoliza("ACTIVA");
        ConsultarPolizaPorNumPolizaResponse cResp = new ConsultarPolizaPorNumPolizaResponse();
        cResp.getPolizaDto().add(e);
        return cResp;
    }
    
    public String getStrUrlTInter() {
        return "http://wsp-txlogger-uat.oscpuat.gnp.com.mx/txlogger";
    }
    
    public String getFolioStr() {
        return "00001404597534";
    }
    
    public TarificarCancelacionRequest obtenerContratoErr(CancelacionReq req) throws DatatypeConfigurationException, ParseException {
        Cabecera cabecera = new Cabecera();
        cabecera.setCVETRANSACCION(Constantes.CVE_TRANSACCION);
        cabecera.setIDACTOR(Constantes.UNO);
        cabecera.setIDPERFIL(Constantes.UNO);
        cabecera.setIDROL(Constantes.UNO);
        cabecera.setIDTRANSACCION(null);
        CancelaPolizaDto data = new CancelaPolizaDto();
        data.setCVECANCELACIONNEGOCIO(Constantes.CANCELANEG);
        data.setCVERAMO(Constantes.AUTOS);
        data.setFCHEFECTOMOVIMIENTO(Utileria.getXMLDateOfStr("2019-07-30"));
        data.setNUMPOLIZA(req.getNumPoliza());
        data.setNUMVERSION(Constantes.VERSIONPOL);
        TarificarCancelacionRequest contratoReq = new TarificarCancelacionRequest();
        contratoReq.setCabecera(cabecera);
        contratoReq.setDataTarificarCancelacion(data);
        return contratoReq;
    }
    
    public TarificarCancelacionResponse2 getTarificarCancelacion() {
        TarificarCancelacionResponse2 resTarifica = new TarificarCancelacionResponse2();
        resTarifica.setNUMPOLIZA("00001404597534");
        resTarifica.setNUMVERSION("0");
        return resTarifica;
    }
    
    public EmitirCancelacionRequest obtenerEmitirCanRequest(CancelacionReq req) throws DatatypeConfigurationException, ParseException {
        Cabecera cabecera = new Cabecera();
        cabecera.setCVETRANSACCION(Constantes.CVE_TRANSACCION);
        cabecera.setIDACTOR(Constantes.UNO);
        cabecera.setIDPERFIL(Constantes.UNO);
        cabecera.setIDROL(Constantes.UNO);
        cabecera.setIDTRANSACCION(null);
        CancelaPolizaDto datosPoliza = new CancelaPolizaDto();
        datosPoliza.setCVECANCELACIONNEGOCIO(Constantes.CANCELANEG);
        datosPoliza.setCVERAMO(Constantes.AUTOS);
        datosPoliza.setFCHEFECTOMOVIMIENTO(Utileria.getXMLDateOfStr("2019-07-30"));
        datosPoliza.setNUMPOLIZA(req.getNumPoliza());
        datosPoliza.setNUMVERSION(Constantes.VERSIONPOL);
        CancelarRequestData data = new CancelarRequestData();
        data.setDatosPoliza(datosPoliza);
        EmitirCancelacionRequest reqCancelacion = new EmitirCancelacionRequest();
        reqCancelacion.setCabecera(cabecera);
        reqCancelacion.setData(data);
        return reqCancelacion;
    }
    
    public EmitirCancelacionResponse2 getTarificarCancelacion(CancelacionReq req) {
        EmitirCancelacionResponse2 resCancela = new EmitirCancelacionResponse2();
        resCancela.setESTADOPOLIZA("CANCELADA");
        resCancela.setNUMPOLIZA(req.getNumPoliza());
        resCancela.setNUMVERSION("1");
        return resCancela;
    }
    
    @Test(expected = ExecutionError.class)
    public void cancelacionNTUPolizaCancelacionTest() throws DatatypeConfigurationException, ParseException {
        when(consulta.consultarPoliza("00001404597534", "1", null)).thenReturn(getCResp());
        when(conf.getUrlTransacciones()).thenReturn(getStrUrlTInter());
        when(foliador.getFolio(0, "clave")).thenReturn(getFolioStr());
        TarificarCancelacionRequest contratoReqAr = obtenerContratoErr(req);
        when(tarificar.getTarificar(contratoReqAr, 0)).thenReturn(getTarificarCancelacion());
        EmitirCancelacionRequest reqCancelacion = obtenerEmitirCanRequest(req);
        when(tarificar.getCancelacion(reqCancelacion, 0)).thenReturn(getTarificarCancelacion(req));
        cancelaciondomainimpl.cancelacionNTU(req).getEstPoliza();
    }
    
    @Test(expected = ExecutionError.class)
    public void cancelacionNTUPCancelacionTest() throws DatatypeConfigurationException, ParseException {
        when(consulta.consultarPoliza("00001404597534", "1", null)).thenReturn(getCRespNow());
        when(conf.getUrlTransacciones()).thenReturn(getStrUrlTInter());
        when(foliador.getFolio(0, "clave")).thenReturn(getFolioStr());
        TarificarCancelacionRequest contratoReqAr = obtenerContratoErr(req);
        when(tarificar.getTarificar(contratoReqAr, 0)).thenReturn(getTarificarCancelacion());
        EmitirCancelacionRequest reqCancelacion = obtenerEmitirCanRequest(req);
        when(tarificar.getCancelacion(reqCancelacion, 0)).thenReturn(getTarificarCancelacion(req));
        cancelaciondomainimpl.cancelacionNTU(req).getEstPoliza();
    }
    
    @Test(expected = ExecutionError.class)
    public void cancelacionNTUsadfasdfTest() throws DatatypeConfigurationException, ParseException {
        when(consulta.consultarPoliza("00001404597534", "1", null)).thenReturn(getCRespNow());
        when(conf.getUrlTransacciones()).thenReturn(getStrUrlTInter());
        when(foliador.getFolio(0, "clave")).thenReturn(getFolioStr());
        TarificarCancelacionRequest contratoReqAr = obtenerContratoErr(req);
        when(tarificar.getTarificar(contratoReqAr, 0)).thenReturn(getTarificarCancelacion());
        EmitirCancelacionRequest reqCancelacion = obtenerEmitirCanRequest(req);
        when(tarificar.getCancelacion(reqCancelacion, 0)).thenReturn(getTarificarCancelacion(req));
        cancelaciondomainimpl.cancelacionNTU(req).getEstPoliza();
    }
    
    @Test// (expected = ExecutionError.class)
    public void validarHoyTest() throws Exception {
        when(consulta.consultarPoliza("00001404597534", "1", null)).thenReturn(getCResp());
        when(conf.getUrlTransacciones()).thenReturn(getStrUrlTInter());
        when(foliador.getFolio(0, "clave")).thenReturn(getFolioStr());
        TarificarCancelacionRequest contratoReqAr = obtenerContratoErr(req);
        when(tarificar.getTarificar(contratoReqAr, 0)).thenReturn(getTarificarCancelacion());
        EmitirCancelacionRequest reqCancelacion = obtenerEmitirCanRequest(req);
        when(tarificar.getCancelacion(reqCancelacion, 0)).thenReturn(getTarificarCancelacion(req));
        cancelaciondomainimpl.validar(req);
        assertNotNull(req);
    }
    
    @Test(expected = ExecutionError.class)
    public void validarCanceladaTest() throws Exception {
        when(consulta.consultarPoliza("00001404597534", "1", null)).thenReturn(getCRespCancelada());
        when(conf.getUrlTransacciones()).thenReturn(getStrUrlTInter());
        when(foliador.getFolio(0, "clave")).thenReturn(getFolioStr());
        TarificarCancelacionRequest contratoReqAr = obtenerContratoErr(req);
        when(tarificar.getTarificar(contratoReqAr, 0)).thenReturn(getTarificarCancelacion());
        EmitirCancelacionRequest reqCancelacion = obtenerEmitirCanRequest(req);
        when(tarificar.getCancelacion(reqCancelacion, 0)).thenReturn(getTarificarCancelacion(req));
        cancelaciondomainimpl.validar(req);
    }
}
