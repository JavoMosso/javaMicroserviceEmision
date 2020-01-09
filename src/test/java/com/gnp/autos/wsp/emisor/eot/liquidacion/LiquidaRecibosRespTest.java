package com.gnp.autos.wsp.emisor.eot.liquidacion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(SpringRunner.class)
public class LiquidaRecibosRespTest {
    /* Clase Test. */
    @InjectMocks
    private static LiquidaRecibosResp base;
    @InjectMocks
    private static LiquidaRecibosResp equal;
    @InjectMocks
    private static LiquidaRecibosResp notEqual;
    @InjectMocks
    private static LiquidaRecibosResp nulo;
    @InjectMocks
    private static LiquidaRecibosResp object3;
    
    @Before
    public void initMocks() {
        base = new LiquidaRecibosResp();
        ReflectionTestUtils.setField(base, "numRecibo", "132456");
        ReflectionTestUtils.setField(base, "fechaInicioRecibo", "190101");
        ReflectionTestUtils.setField(base, "fechaVencimientoRecibo", "200101");
        ReflectionTestUtils.setField(base, "cveEstadoRecibo", "A");
        ReflectionTestUtils.setField(base, "cveSubEstadoRecibo", "L");
        ReflectionTestUtils.setField(base, "montoTotalRecibo", new BigDecimal("1500"));
        ReflectionTestUtils.setField(base, "codIntermediario", "132548");
        ReflectionTestUtils.setField(base, "cveMoneda", "MXN");
        ReflectionTestUtils.setField(base, "idFraccionRecibo", 2);
        ReflectionTestUtils.setField(base, "fechaCancelacionRecibo", "191701");
        ReflectionTestUtils.setField(base, "errorLiquidacion", "error");
        ReflectionTestUtils.setField(base, "mensaje", "mensaje");
        ReflectionTestUtils.setField(base, "numConfirmacion", "1325");
        ReflectionTestUtils.setField(base, "numTrabajo", "3");
        ReflectionTestUtils.setField(base, "banOperacionExitosa", true);
        equal = new LiquidaRecibosResp();
        ReflectionTestUtils.setField(equal, "numRecibo", base.getNumRecibo());
        ReflectionTestUtils.setField(equal, "fechaInicioRecibo", base.getFechaInicioRecibo());
        ReflectionTestUtils.setField(equal, "fechaVencimientoRecibo", base.getFechaVencimientoRecibo());
        ReflectionTestUtils.setField(equal, "cveEstadoRecibo", base.getCveEstadoRecibo());
        ReflectionTestUtils.setField(equal, "cveSubEstadoRecibo", base.getCveSubEstadoRecibo());
        ReflectionTestUtils.setField(equal, "montoTotalRecibo", base.getMontoTotalRecibo());
        ReflectionTestUtils.setField(equal, "codIntermediario", base.getCodIntermediario());
        ReflectionTestUtils.setField(equal, "cveMoneda", base.getCveMoneda());
        ReflectionTestUtils.setField(equal, "idFraccionRecibo", base.getIdFraccionRecibo());
        ReflectionTestUtils.setField(equal, "fechaCancelacionRecibo", base.getFechaCancelacionRecibo());
        ReflectionTestUtils.setField(equal, "errorLiquidacion", base.getErrorLiquidacion());
        ReflectionTestUtils.setField(equal, "mensaje", base.getMensaje());
        ReflectionTestUtils.setField(equal, "numConfirmacion", base.getNumConfirmacion());
        ReflectionTestUtils.setField(equal, "numTrabajo", base.getNumTrabajo());
        ReflectionTestUtils.setField(equal, "banOperacionExitosa", base.isBanOperacionExitosa());
        notEqual = new LiquidaRecibosResp();
        ReflectionTestUtils.setField(notEqual, "numRecibo", "1324560");
        ReflectionTestUtils.setField(notEqual, "fechaInicioRecibo", "1901010");
        ReflectionTestUtils.setField(notEqual, "fechaVencimientoRecibo", "2001010");
        ReflectionTestUtils.setField(notEqual, "cveEstadoRecibo", "Anot");
        ReflectionTestUtils.setField(notEqual, "cveSubEstadoRecibo", "Lnot");
        ReflectionTestUtils.setField(notEqual, "montoTotalRecibo", new BigDecimal("15000"));
        ReflectionTestUtils.setField(notEqual, "codIntermediario", "1325480");
        ReflectionTestUtils.setField(notEqual, "cveMoneda", "MXNnot");
        ReflectionTestUtils.setField(notEqual, "idFraccionRecibo", 20);
        ReflectionTestUtils.setField(notEqual, "fechaCancelacionRecibo", "1917010");
        ReflectionTestUtils.setField(notEqual, "errorLiquidacion", "errornot");
        ReflectionTestUtils.setField(notEqual, "mensaje", "mensajenot");
        ReflectionTestUtils.setField(notEqual, "numConfirmacion", "13250");
        ReflectionTestUtils.setField(notEqual, "numTrabajo", "30");
        ReflectionTestUtils.setField(notEqual, "banOperacionExitosa", false);
        nulo = null;
        object3 = new LiquidaRecibosResp();
        ReflectionTestUtils.setField(object3, "numRecibo", base.getNumRecibo());
        ReflectionTestUtils.setField(object3, "fechaInicioRecibo", base.getFechaInicioRecibo());
        ReflectionTestUtils.setField(object3, "fechaVencimientoRecibo", base.getFechaVencimientoRecibo());
        ReflectionTestUtils.setField(object3, "cveEstadoRecibo", base.getCveEstadoRecibo());
        ReflectionTestUtils.setField(object3, "cveSubEstadoRecibo", base.getCveSubEstadoRecibo());
        ReflectionTestUtils.setField(object3, "montoTotalRecibo", base.getMontoTotalRecibo());
        ReflectionTestUtils.setField(object3, "codIntermediario", base.getCodIntermediario());
        ReflectionTestUtils.setField(object3, "cveMoneda", base.getCveMoneda());
        ReflectionTestUtils.setField(object3, "idFraccionRecibo", base.getIdFraccionRecibo());
        ReflectionTestUtils.setField(object3, "fechaCancelacionRecibo", base.getFechaCancelacionRecibo());
        ReflectionTestUtils.setField(object3, "errorLiquidacion", base.getErrorLiquidacion());
        ReflectionTestUtils.setField(object3, "mensaje", base.getMensaje());
        ReflectionTestUtils.setField(object3, "numConfirmacion", base.getNumConfirmacion());
        ReflectionTestUtils.setField(object3, "numTrabajo", base.getNumTrabajo());
        ReflectionTestUtils.setField(object3, "banOperacionExitosa", base.isBanOperacionExitosa());
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test() {
        base.setNumRecibo("132456");
        assertNotNull(base.getNumRecibo());
        base.setFechaInicioRecibo("190101");
        assertNotNull(base.getFechaInicioRecibo());
        base.setFechaVencimientoRecibo("200101");
        assertNotNull(base.getFechaVencimientoRecibo());
        base.setCveEstadoRecibo("A");
        assertNotNull(base.getCveEstadoRecibo());
        base.setCveSubEstadoRecibo("L");
        assertNotNull(base.getCveSubEstadoRecibo());
        base.setMontoTotalRecibo(new BigDecimal("1500"));
        assertNotNull(base.getCveSubEstadoRecibo());
        base.setCodIntermediario("132548");
        assertNotNull(base.getCodIntermediario());
        base.setCveMoneda("MXN");
        assertNotNull(base.getCveMoneda());
        base.setIdFraccionRecibo(2);
        assertNotNull(base.getIdFraccionRecibo());
        base.setFechaCancelacionRecibo("191701");
        assertNotNull(base.getFechaCancelacionRecibo());
        base.setErrorLiquidacion("error");
        assertNotNull(base.getErrorLiquidacion());
        base.setMensaje("mensaje");
        assertNotNull(base.getMensaje());
        base.setNumConfirmacion("1325");
        assertNotNull(base.getNumConfirmacion());
        base.setNumTrabajo("3");
        assertNotNull(base.getNumTrabajo());
        base.setBanOperacionExitosa(true);
        assertNotNull(base.isBanOperacionExitosa());
    }
    
    @Test
    public void testEquals() {
        assertEquals("132456", base.getNumRecibo());
        assertEquals(base, equal);
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(base.hashCode(), equal.hashCode());
    }
    
    @Test
    public void testDifferent() {
        assertNotEquals("1324560", base.getNumRecibo());
        assertNotEquals(base, notEqual);
        assertNotEquals(base.hashCode(), notEqual.hashCode());
        assertNotEquals(base.hashCode(), nulo);
    }
    
    @Test
    public void testAssert() {
        assertTrue(base.canEqual(base));
        assertTrue(base.equals(base));
        assertTrue(base.canEqual(equal));
        assertTrue(base.equals(equal));
    }
    
    @Test
    public void testDeny() {
        assertFalse(base.equals(notEqual));
        assertFalse(base.equals(null));
    }
    
    @Test
    public void testNull() {
        nulo.setNumRecibo(null);
        assertNull(nulo.getNumRecibo());
    }
    
    @Test
    public void testNotNull() {
        assertNotNull(base);
        assertNotNull(base.toString());
    }
    
    @Test
    public void testReflexive() {
        assertEquals(base, base);
    }
    
    @Test
    public void testSymmetric() {
        assertEquals(base, equal);
        assertEquals(equal, base);
    }
    
    @Test
    public void testTransitive() {
        assertEquals(base, equal);
        assertEquals(equal, object3);
        assertEquals(base, object3);
    }
}
