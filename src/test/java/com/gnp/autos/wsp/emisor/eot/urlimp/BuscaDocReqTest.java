package com.gnp.autos.wsp.emisor.eot.urlimp;

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
public class BuscaDocReqTest {
    /* Clase Test. */
    @InjectMocks
    private BuscaDocReq base;
    @InjectMocks
    private BuscaDocReq equal;
    @InjectMocks
    private BuscaDocReq notEqual;
    @InjectMocks
    private BuscaDocReq nulo;
    @InjectMocks
    private BuscaDocReq object3;
    
    @Before
    public void initMocksBase() {
        base = new BuscaDocReq();
        ReflectionTestUtils.setField(base, "numPoliza", "numPoliza");
        ReflectionTestUtils.setField(base, "numVersion", "numVersion");
        ReflectionTestUtils.setField(base, "extensionArchivo", "extensionArchivo");
        equal = new BuscaDocReq();
        ReflectionTestUtils.setField(equal, "numPoliza", base.getNumPoliza());
        ReflectionTestUtils.setField(equal, "numVersion", base.getNumVersion());
        ReflectionTestUtils.setField(equal, "extensionArchivo", base.getExtensionArchivo());
        notEqual = new BuscaDocReq();
        ReflectionTestUtils.setField(notEqual, "numPoliza", "numPolizan");
        ReflectionTestUtils.setField(notEqual, "numVersion", "numVersionn");
        ReflectionTestUtils.setField(notEqual, "extensionArchivo", "extensionArchivon");
        nulo = null;
        object3 = new BuscaDocReq();
        ReflectionTestUtils.setField(object3, "numPoliza", base.getNumPoliza());
        ReflectionTestUtils.setField(object3, "numVersion", base.getNumVersion());
        ReflectionTestUtils.setField(object3, "extensionArchivo", base.getExtensionArchivo());
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test() {
        assertNotNull(base.getNumPoliza());
    }
    
    @Test
    public void testEquals() {
        assertEquals(equal.getNumPoliza(), base.getNumPoliza());
        assertEquals(base, equal);
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(base.hashCode(), equal.hashCode());
    }
    
    @Test
    public void testDifferent() {
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
