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
public class DataReqTest {
    /* Clase Test. */
    @InjectMocks
    private static DataReq base;
    @InjectMocks
    private static DataReq equal;
    @InjectMocks
    private static DataReq notEqual;
    @InjectMocks
    private static DataReq nulo;
    @InjectMocks
    private static DataReq object3;
    
    @Before
    public void initMocks() {
        MovimientosNegocioReq obj = new MovimientosNegocioReq();
        obj.setIdNegocioOperable("negocio");
        MovimientosNegocioReq objn = new MovimientosNegocioReq();
        objn.setIdNegocioOperable("negocionot");
        base = new DataReq();
        ReflectionTestUtils.setField(base, "movimientosNeg", obj);
        equal = new DataReq();
        ReflectionTestUtils.setField(equal, "movimientosNeg", base.getMovimientosNeg());
        notEqual = new DataReq();
        ReflectionTestUtils.setField(notEqual, "movimientosNeg", objn);
        nulo = null;
        object3 = new DataReq();
        ReflectionTestUtils.setField(object3, "movimientosNeg", base.getMovimientosNeg());
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test() {
        MovimientosNegocioReq obj = new MovimientosNegocioReq();
        obj.setIdNegocioOperable("negocio");
        base.setMovimientosNeg(obj);
        assertNotNull(base.getMovimientosNeg());
    }
    
    @Test
    public void testEquals() {
        assertEquals("negocio", base.getMovimientosNeg().getIdNegocioOperable());
        assertEquals(base, equal);
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(base.hashCode(), equal.hashCode());
    }
    
    @Test
    public void testDifferent() {
        assertNotEquals("negocionot", base.getMovimientosNeg().getIdNegocioOperable());
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
        nulo.setMovimientosNeg(null);
        assertNull(nulo.getMovimientosNeg());
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
