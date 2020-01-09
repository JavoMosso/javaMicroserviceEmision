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
public class ConsultaNegocioReqTest {
    /* Clase Test. */
    @InjectMocks
    private static ConsultaNegocioReq base;
    @InjectMocks
    private static ConsultaNegocioReq equal;
    @InjectMocks
    private static ConsultaNegocioReq notEqual;
    @InjectMocks
    private static ConsultaNegocioReq nulo;
    @InjectMocks
    private static ConsultaNegocioReq object3;
    
    @Before
    public void initMocks() {
        CabeceraReq objcab1 = new CabeceraReq();
        objcab1.setIdTransaccion("trans1");
        CabeceraReq objcab2 = new CabeceraReq();
        objcab2.setIdTransaccion("trans2");
        base = new ConsultaNegocioReq();
        ReflectionTestUtils.setField(base, "cabecera", objcab1);
        ReflectionTestUtils.setField(base, "data", new DataReq());
        equal = new ConsultaNegocioReq();
        ReflectionTestUtils.setField(equal, "cabecera", base.getCabecera());
        ReflectionTestUtils.setField(equal, "data", base.getData());
        notEqual = new ConsultaNegocioReq();
        ReflectionTestUtils.setField(notEqual, "cabecera", objcab2);
        ReflectionTestUtils.setField(notEqual, "data", new DataReq());
        nulo = null;
        object3 = new ConsultaNegocioReq();
        ReflectionTestUtils.setField(object3, "cabecera", base.getCabecera());
        ReflectionTestUtils.setField(object3, "data", base.getData());
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test() {
        CabeceraReq objcab1 = new CabeceraReq();
        objcab1.setIdTransaccion("trans1");
        DataReq data = new DataReq();
        data.setMovimientosNeg(new MovimientosNegocioReq());
        base.setCabecera(objcab1);
        assertNotNull(base.getCabecera());
        base.setData(data);
        assertNotNull(base.getData());
    }
    
    @Test
    public void testEquals() {
        assertEquals("trans1", base.getCabecera().getIdTransaccion());
        assertEquals(base, equal);
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(base.hashCode(), equal.hashCode());
    }
    
    @Test
    public void testDifferent() {
        assertNotEquals("trans1not", base.getCabecera().getIdTransaccion());
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
        nulo.setCabecera(null);
        assertNull(nulo.getCabecera());
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
