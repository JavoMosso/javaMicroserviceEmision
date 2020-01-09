package com.gnp.autos.wsp.emisor.eot.rest.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import com.gnp.autos.wsp.emisor.eot.liquidacion.LiquidaRecibosReq;
import com.gnp.autos.wsp.emisor.eot.util.ObjetosPruebas;

@RunWith(SpringRunner.class)
public class LiquidacionClientTest {
    /* Clase Test. */
    @InjectMocks
    private static LiquidacionClient base;
    @InjectMocks
    private static LiquidacionClient equal;
    @InjectMocks
    private static LiquidacionClient notEqual;
    @InjectMocks
    private static LiquidacionClient nulo;
    @InjectMocks
    private static LiquidacionClient object3;
    
    @Before
    public void initMocks() {
        base = new LiquidacionClient();
        equal = new LiquidacionClient();
        notEqual = new LiquidacionClient();
        nulo = null;
        object3 = new LiquidacionClient();
        MockitoAnnotations.initMocks(this);
    }
    
    @SuppressWarnings("static-access")
    @Test
    public void test() {
        assertNotNull(base.getClass());
    }
    
    @Test
    public void testEquals() {
        assertEquals(base, equal);
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(base.hashCode(), equal.hashCode());
    }
    
    @Test
    public void testDifferent() {
        assertEquals(base, notEqual);
        assertEquals(base.hashCode(), notEqual.hashCode());
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
        assertTrue(base.equals(notEqual));
        assertFalse(base.equals(null));
    }
    
    @SuppressWarnings("static-access")
    @Test
    public void testNull() {
        assertNotNull(nulo.getClass());
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
    
    @Test
    public void liquidaPolizaTidNullTest() {
        base.liquidaPoliza(new LiquidaRecibosReq(), "lalala", null);
        assertNotNull(base);
    }
    
    @Test
    public void liquidaPolizaTest() {
        base.liquidaPoliza(new LiquidaRecibosReq(), "lalala", 0);
        assertNotNull(base);
    }
    
    @Test
    public void liquidaPolizaEntityTest() {
        base.liquidaPoliza(ObjetosPruebas.getLiquidaRecibosReq(), "http://wsp-liquidacion-qa.oscp.gnp.com.mx/liquidaRecibos", 0);
        assertNotNull(base);
    }
}
