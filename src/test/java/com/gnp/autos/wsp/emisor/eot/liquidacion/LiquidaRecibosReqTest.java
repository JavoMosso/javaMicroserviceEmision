package com.gnp.autos.wsp.emisor.eot.liquidacion;

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
public class LiquidaRecibosReqTest {
    /* Clase Test. */
    @InjectMocks
    private static LiquidaRecibosReq base;
    @InjectMocks
    private static LiquidaRecibosReq equal;
    @InjectMocks
    private static LiquidaRecibosReq notEqual;
    @InjectMocks
    private static LiquidaRecibosReq nulo;
    @InjectMocks
    private static LiquidaRecibosReq object3;
    
    @Before
    public void initMocks() {
        base = new LiquidaRecibosReq();
        equal = new LiquidaRecibosReq();
        notEqual = new LiquidaRecibosReq();
        ReflectionTestUtils.setField(notEqual, "cveCondPago", "CLno");
        nulo = null;
        object3 = new LiquidaRecibosReq();
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test() {
        base.setCveCondPago("AA");
        assertNotNull(base.getCveCondPago());
    }
    
    @Test
    public void testEquals() {
        assertEquals("CL", base.getCveCondPago());
        assertEquals(base, equal);
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(base.hashCode(), equal.hashCode());
    }
    
    @Test
    public void testDifferent() {
        assertNotEquals("CLnot", base.getCveCondPago());
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
        nulo.setCveCondPago(null);
        assertNull(nulo.getCveCondPago());
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
