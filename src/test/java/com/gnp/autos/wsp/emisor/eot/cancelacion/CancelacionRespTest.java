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
public class CancelacionRespTest {
    /* Clase Test. */
    @InjectMocks
    private CancelacionResp base;
    @InjectMocks
    private CancelacionResp equal;
    @InjectMocks
    private CancelacionResp notEqual;
    @InjectMocks
    private CancelacionResp nulo;
    @InjectMocks
    private CancelacionResp object3;
    
    @Before
    public void initMocksBase() {
        base = new CancelacionResp();
        ReflectionTestUtils.setField(base, "numPoliza", "numPoliza");
        ReflectionTestUtils.setField(base, "numVersion", "numVersion");
        ReflectionTestUtils.setField(base, "estPoliza", "estPoliza");
        equal = new CancelacionResp();
        ReflectionTestUtils.setField(equal, "numPoliza", base.getNumPoliza());
        ReflectionTestUtils.setField(equal, "numVersion", base.getNumVersion());
        ReflectionTestUtils.setField(equal, "estPoliza", base.getEstPoliza());
        notEqual = new CancelacionResp();
        ReflectionTestUtils.setField(notEqual, "numPoliza", "notEqual");
        ReflectionTestUtils.setField(notEqual, "numVersion", "notEqual");
        ReflectionTestUtils.setField(notEqual, "estPoliza", "notEqual");
        nulo = null;
        object3 = new CancelacionResp();
        ReflectionTestUtils.setField(object3, "numPoliza", base.getNumPoliza());
        ReflectionTestUtils.setField(object3, "numVersion", base.getNumVersion());
        ReflectionTestUtils.setField(object3, "estPoliza", base.getEstPoliza());
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test() {
        base.setEstPoliza("estPoliza");
        assertNotNull(base.getEstPoliza());
        base.setNumPoliza("numPoliza");
        assertNotNull(base.getNumPoliza());
        base.setNumVersion("numVersion");
        assertNotNull(base.getNumVersion());
    }
    
    @Test
    public void testEquals() {
        assertEquals(equal.getNumPoliza(), base.getNumPoliza());
        assertEquals(equal.getNumVersion(), base.getNumVersion());
        assertEquals(equal.getEstPoliza(), base.getEstPoliza());
        assertEquals(base, equal);
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(base.hashCode(), equal.hashCode());
    }
    
    @Test
    public void testDifferent() {
        assertNotEquals(notEqual.getNumPoliza(), base.getNumPoliza());
        assertNotEquals(notEqual.getNumVersion(), base.getNumVersion());
        assertNotEquals(notEqual.getEstPoliza(), base.getEstPoliza());
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
        assertNull(nulo.getNumPoliza());
        assertNull(nulo.getNumVersion());
        assertNull(nulo.getEstPoliza());
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
