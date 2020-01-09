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
public class LogRespIntermediaTest {
    /* Clase Test. */
    @InjectMocks
    private LogRespIntermedia base;
    @InjectMocks
    private LogRespIntermedia equal;
    @InjectMocks
    private LogRespIntermedia notEqual;
    @InjectMocks
    private LogRespIntermedia nulo;
    @InjectMocks
    private LogRespIntermedia object3;
    
    @Before
    public void initMocksBase() {
        base = new LogRespIntermedia();
        ReflectionTestUtils.setField(base, "folioWSP", 12345);
        ReflectionTestUtils.setField(base, "request", "request");
        ReflectionTestUtils.setField(base, "response", "response");
        ReflectionTestUtils.setField(base, "idTransaccion", "idTransaccion");
        equal = new LogRespIntermedia();
        ReflectionTestUtils.setField(equal, "folioWSP", base.getFolioWSP());
        ReflectionTestUtils.setField(equal, "request", base.getRequest());
        ReflectionTestUtils.setField(equal, "response", base.getResponse());
        ReflectionTestUtils.setField(equal, "idTransaccion", base.getIdTransaccion());
        notEqual = new LogRespIntermedia();
        ReflectionTestUtils.setField(notEqual, "folioWSP", 12366);
        ReflectionTestUtils.setField(notEqual, "request", "requestn");
        ReflectionTestUtils.setField(notEqual, "response", "responsen");
        ReflectionTestUtils.setField(notEqual, "idTransaccion", "idTransaccionn");
        nulo = null;
        object3 = new LogRespIntermedia();
        ReflectionTestUtils.setField(object3, "folioWSP", base.getFolioWSP());
        ReflectionTestUtils.setField(object3, "request", base.getRequest());
        ReflectionTestUtils.setField(object3, "response", base.getResponse());
        ReflectionTestUtils.setField(object3, "idTransaccion", base.getIdTransaccion());
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test() {
        base.setFolioWSP(12366);
        assertNotNull(base.getFolioWSP());
        base.setRequest("request");
        assertNotNull(base.getRequest());
        base.setResponse("response");
        assertNotNull(base.getResponse());
        base.setIdTransaccion("idTransaccion");
        assertNotNull(base.getIdTransaccion());
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
