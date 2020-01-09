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
public class ResultadoTest {
    /* Clase Test. */
    @InjectMocks
    private Resultado base;
    @InjectMocks
    private Resultado equal;
    @InjectMocks
    private Resultado notEqual;
    @InjectMocks
    private Resultado nulo;
    @InjectMocks
    private Resultado object3;
    
    @Before
    public void initMocksBase() {
        base = new Resultado();
        ReflectionTestUtils.setField(base, "urlDocumento", "urlDocumento");
        ReflectionTestUtils.setField(base, "nombreDocumento", "nombreDocumento");
        ReflectionTestUtils.setField(base, "extensionArchivo", "extensionArchivo");
        equal = new Resultado();
        ReflectionTestUtils.setField(equal, "urlDocumento", base.getUrlDocumento());
        ReflectionTestUtils.setField(equal, "nombreDocumento", base.getNombreDocumento());
        ReflectionTestUtils.setField(equal, "extensionArchivo", base.getExtensionArchivo());
        notEqual = new Resultado();
        ReflectionTestUtils.setField(notEqual, "urlDocumento", "urlDocumenton");
        ReflectionTestUtils.setField(notEqual, "nombreDocumento", "nombreDocumenton");
        ReflectionTestUtils.setField(notEqual, "extensionArchivo", "extensionArchivon");
        nulo = null;
        object3 = new Resultado();
        ReflectionTestUtils.setField(object3, "urlDocumento", base.getUrlDocumento());
        ReflectionTestUtils.setField(object3, "nombreDocumento", base.getNombreDocumento());
        ReflectionTestUtils.setField(object3, "extensionArchivo", base.getExtensionArchivo());
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test() {
        assertNotNull(base.getUrlDocumento());
    }
    
    @Test
    public void testEquals() {
        assertEquals(equal.getUrlDocumento(), base.getUrlDocumento());
        assertEquals(base, equal);
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(base.hashCode(), equal.hashCode());
    }
    
    @Test
    public void testDifferent() {
        assertNotEquals(notEqual.getUrlDocumento(), base.getUrlDocumento());
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
        assertNull(nulo.getUrlDocumento());
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
