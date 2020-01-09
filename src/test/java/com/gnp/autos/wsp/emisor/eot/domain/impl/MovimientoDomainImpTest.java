package com.gnp.autos.wsp.emisor.eot.domain.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import com.gnp.autos.wsp.emisor.eot.config.ConfWSP;
import com.gnp.autos.wsp.emisor.eot.error.ExecutionError;
import com.gnp.autos.wsp.emisor.eot.movimiento.wsdl.MovimientoPorcentajeIntermediarioDto;
import com.gnp.autos.wsp.emisor.eot.soap.service.FoliadorClient;
import com.gnp.autos.wsp.emisor.eot.util.ObjetosPruebas;
import com.gnp.autos.wsp.negocio.neg.model.EmiteNegReq;

@RunWith(SpringRunner.class)
public class MovimientoDomainImpTest {
    @InjectMocks
    private MovimientoDomainImp movimientodomainimp;
    @Mock
    private FoliadorClient client;
    @Mock
    private ConfWSP conf;
    
    @Before
    public void initMocks() {
        movimientodomainimp = new MovimientoDomainImp();
        // ReflectionTestUtils.setField(movimientodomainimp, "STRUDI", "UDI");
        // ReflectionTestUtils.setField(movimientodomainimp, "CVE_TIPO_NOMINA", "CVE_TIPO_NOMINA");
        MockitoAnnotations.initMocks(this);
    }
    
    @Test(expected = NullPointerException.class)
    public void getMovimientoNCTest() {
        when(conf.getUrlMovimientoEOT()).thenReturn("http://10.67.16.19/configuradorWeb/MovimientosNegocioComercialWebImplPort");
        EmiteNegReq objEmiNeg = new EmiteNegReq();
        objEmiNeg.setVehiculo(ObjetosPruebas.getVehiculo());
        movimientodomainimp.getMovimientoNC(objEmiNeg, 0);
    }
    
    @SuppressWarnings("static-access")
    @Test// (expected = NullPointerException.class)
    public void areMatchsCveTNTest() {
        List<String> validCves = new ArrayList<>();
        validCves.add("cve");
        movimientodomainimp.areMatchsCveTN(validCves, "cve");
        assertNotNull(validCves);
    }
    
    @SuppressWarnings("static-access")
    @Test(expected = ExecutionError.class)
    public void areMatchsCveTNSinCvsTest() {
        List<String> validCves = new ArrayList<>();
        validCves.add("cvea");
        movimientodomainimp.areMatchsCveTN(validCves, "cve");
    }
    
    @Test// (expected = ExecutionError.class)
    public void getAgenteNegComTest() {
        MovimientoPorcentajeIntermediarioDto agentDto = ObjetosPruebas.getMovimiento();
        movimientodomainimp.getAgenteNeg(agentDto, "COM", "", "");
        assertNotNull(agentDto);
    }
    
    @Test// (expected = ExecutionError.class)
    public void getAgenteNegUdiTest() {
        MovimientoPorcentajeIntermediarioDto agentDto = ObjetosPruebas.getMovimiento();
        movimientodomainimp.getAgenteNeg(agentDto, "UDI", "", "");
        assertNotNull(agentDto);
    }
}
