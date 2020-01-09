package com.gnp.autos.wsp.emisor.eot.movimiento;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(SpringRunner.class)
public class MovimientosNegocioReqTest {
    /* Clase Test. */
    @InjectMocks
    private static MovimientosNegocioReq base;
    @InjectMocks
    private static MovimientosNegocioReq equal;
    @InjectMocks
    private static MovimientosNegocioReq notEqual;
    @InjectMocks
    private static MovimientosNegocioReq nulo;
    @InjectMocks
    private static MovimientosNegocioReq object3;
    
    @Before
    public void initMocks() {
        DatosSolicitudReq obj = new DatosSolicitudReq();
        obj.setTipoVehiculo("aut");
        DatosSolicitudReq objn = new DatosSolicitudReq();
        objn.setTipoVehiculo("autn");
        base = new MovimientosNegocioReq();
        ReflectionTestUtils.setField(base, "idNegocioOperable", "negocio");
        ReflectionTestUtils.setField(base, "datosSolicitud", obj);
        equal = new MovimientosNegocioReq();
        ReflectionTestUtils.setField(equal, "idNegocioOperable", base.getIdNegocioOperable());
        ReflectionTestUtils.setField(equal, "datosSolicitud", base.getDatosSolicitud());
        notEqual = new MovimientosNegocioReq();
        ReflectionTestUtils.setField(notEqual, "idNegocioOperable", "negocion");
        ReflectionTestUtils.setField(notEqual, "datosSolicitud", objn);
        nulo = null;
        object3 = new MovimientosNegocioReq();
        ReflectionTestUtils.setField(object3, "idNegocioOperable", base.getIdNegocioOperable());
        ReflectionTestUtils.setField(object3, "datosSolicitud", base.getDatosSolicitud());
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test() {
        DatosSolicitudReq obj = new DatosSolicitudReq();
        obj.setTipoVehiculo("aut");
        base.setIdNegocioOperable("negocio");
        assertNotNull(base.getIdNegocioOperable());
        base.setDatosSolicitud(obj);
        assertNotNull(base.getDatosSolicitud());
    }
    
    @Test
    public void testEquals() {
        assertEquals("negocio", base.getIdNegocioOperable());
        assertEquals(base, equal);
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(base.hashCode(), equal.hashCode());
    }
    
    @Test
    public void testDifferent() {
        assertNotEquals("negocionot", base.getIdNegocioOperable());
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
        nulo.setIdNegocioOperable(null);
        assertNull(nulo.getIdNegocioOperable());
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
