package com.gnp.autos.wsp.emisor.eot.transaccion;

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
public class LogResponseIntermediaTest {
    /* Clase Test. */
    @InjectMocks
    private LogResponseIntermedia base;
    @InjectMocks
    private LogResponseIntermedia equal;
    @InjectMocks
    private LogResponseIntermedia notEqual;
    @InjectMocks
    private LogResponseIntermedia nulo;
    @InjectMocks
    private LogResponseIntermedia object3;
    
    @Before
    public void initMocksBase() {
        base = new LogResponseIntermedia();
        ReflectionTestUtils.setField(base, "idTransaccion", "idTransaccion");
        ReflectionTestUtils.setField(base, "cveServicioIntermedio", "cveServicioIntermedio");
        ReflectionTestUtils.setField(base, "request", "request");
        ReflectionTestUtils.setField(base, "response", "response");
        equal = new LogResponseIntermedia();
        ReflectionTestUtils.setField(equal, "idTransaccion", base.getIdTransaccion());
        ReflectionTestUtils.setField(equal, "cveServicioIntermedio", base.getCveServicioIntermedio());
        ReflectionTestUtils.setField(equal, "request", base.getRequest());
        ReflectionTestUtils.setField(equal, "response", base.getResponse());
        notEqual = new LogResponseIntermedia();
        ReflectionTestUtils.setField(notEqual, "idTransaccion", "idTransaccionn");
        ReflectionTestUtils.setField(notEqual, "cveServicioIntermedio", "cveServicioIntermedion");
        ReflectionTestUtils.setField(notEqual, "request", "requestn");
        ReflectionTestUtils.setField(notEqual, "response", "responsen");
        nulo = null;
        object3 = new LogResponseIntermedia();
        ReflectionTestUtils.setField(object3, "idTransaccion", base.getIdTransaccion());
        ReflectionTestUtils.setField(object3, "cveServicioIntermedio", base.getCveServicioIntermedio());
        ReflectionTestUtils.setField(object3, "request", base.getRequest());
        ReflectionTestUtils.setField(object3, "response", base.getResponse());
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test() {
        base.setIdTransaccion("idTransaccion");
        assertNotNull(base.getIdTransaccion());
        base.setCveServicioIntermedio("cveServicioIntermedio");
        assertNotNull(base.getCveServicioIntermedio());
        base.setRequest("request");
        assertNotNull(base.getRequest());
        base.setResponse("response");
        assertNotNull(base.getResponse());
    }
    
    @Test
    public void testEquals() {
        assertEquals(equal.getRequest(), base.getRequest());
        assertEquals(base, equal);
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(base.hashCode(), equal.hashCode());
    }
    
    @Test
    public void testDifferent() {
        assertNotEquals(notEqual.getRequest(), base.getRequest());
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
        assertNull(nulo.getRequest());
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
