package com.gnp.autos.wsp.emisor.eot.cancelacion;

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
public class CancelacionReqTest {
    /* Clase Test. */
    @InjectMocks
    private CancelacionReq base;
    @InjectMocks
    private CancelacionReq equal;
    @InjectMocks
    private CancelacionReq notEqual;
    @InjectMocks
    private CancelacionReq nulo;
    @InjectMocks
    private CancelacionReq object3;
    
    @Before
    public void initMocksBase() {
        base = new CancelacionReq();
        ReflectionTestUtils.setField(base, "usuario", "usuario");
        ReflectionTestUtils.setField(base, "password", "password");
        ReflectionTestUtils.setField(base, "numPoliza", "numPoliza");
        equal = new CancelacionReq();
        ReflectionTestUtils.setField(equal, "usuario", base.getUsuario());
        ReflectionTestUtils.setField(equal, "password", base.getPassword());
        ReflectionTestUtils.setField(equal, "numPoliza", base.getNumPoliza());
        notEqual = new CancelacionReq();
        ReflectionTestUtils.setField(notEqual, "usuario", "notEqual");
        ReflectionTestUtils.setField(notEqual, "password", "notEqual");
        ReflectionTestUtils.setField(notEqual, "numPoliza", "notEqual");
        nulo = null;
        object3 = new CancelacionReq();
        ReflectionTestUtils.setField(object3, "usuario", base.getUsuario());
        ReflectionTestUtils.setField(object3, "password", base.getPassword());
        ReflectionTestUtils.setField(object3, "numPoliza", base.getNumPoliza());
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test() {
        base.setNumPoliza("numPoliza");
        assertNotNull(base.getNumPoliza());
        base.setPassword("password");
        assertNotNull(base.getPassword());
        base.setUsuario("usuario");
        assertNotNull(base.getUsuario());
    }
    
    @Test
    public void testEquals() {
        assertEquals(equal.getUsuario(), base.getUsuario());
        assertEquals(equal.getPassword(), base.getPassword());
        assertEquals(equal.getNumPoliza(), base.getNumPoliza());
        assertEquals(base, equal);
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(base.hashCode(), equal.hashCode());
    }
    
    @Test
    public void testDifferent() {
        assertNotEquals(notEqual.getUsuario(), base.getUsuario());
        assertNotEquals(notEqual.getPassword(), base.getPassword());
        assertNotEquals(notEqual.getNumPoliza(), base.getNumPoliza());
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
        assertNull(nulo.getUsuario());
        assertNull(nulo.getPassword());
        assertNull(nulo.getNumPoliza());
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
