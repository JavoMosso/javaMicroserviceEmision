package com.gnp.autos.wsp.emisor.eot.validareglas;

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
public class DomicilioValNegTest {
    /* Clase Test. */
    @InjectMocks
    private DomicilioValNeg base;
    @InjectMocks
    private DomicilioValNeg equal;
    @InjectMocks
    private DomicilioValNeg notEqual;
    @InjectMocks
    private DomicilioValNeg nulo;
    @InjectMocks
    private DomicilioValNeg object3;
    
    @Before
    public void initMocksBase() {
        base = new DomicilioValNeg();
        ReflectionTestUtils.setField(base, "codigoPostal", "codigoPostal");
        ReflectionTestUtils.setField(base, "estado", "estado");
        ReflectionTestUtils.setField(base, "municipio", "municipio");
        ReflectionTestUtils.setField(base, "pais", "pais");
        equal = new DomicilioValNeg();
        ReflectionTestUtils.setField(equal, "codigoPostal", base.getCodigoPostal());
        ReflectionTestUtils.setField(equal, "estado", base.getEstado());
        ReflectionTestUtils.setField(equal, "municipio", base.getMunicipio());
        ReflectionTestUtils.setField(equal, "pais", base.getPais());
        notEqual = new DomicilioValNeg();
        ReflectionTestUtils.setField(notEqual, "codigoPostal", "codigoPostaln");
        ReflectionTestUtils.setField(notEqual, "estado", "estadon");
        ReflectionTestUtils.setField(notEqual, "municipio", "municipion");
        ReflectionTestUtils.setField(notEqual, "pais", "paisn");
        nulo = null;
        object3 = new DomicilioValNeg();
        ReflectionTestUtils.setField(object3, "codigoPostal", base.getCodigoPostal());
        ReflectionTestUtils.setField(object3, "estado", base.getEstado());
        ReflectionTestUtils.setField(object3, "municipio", base.getMunicipio());
        ReflectionTestUtils.setField(object3, "pais", base.getPais());
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test() {
        base.setCodigoPostal("codigoPostal");
        assertNotNull(base.getCodigoPostal());
        base.setEstado("estado");
        assertNotNull(base.getEstado());
        base.setMunicipio("municipio");
        assertNotNull(base.getMunicipio());
        base.setPais("pais");
        assertNotNull(base.getPais());
    }
    
    @Test
    public void testEquals() {
        assertEquals(equal.getCodigoPostal(), base.getCodigoPostal());
        assertEquals(base, equal);
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(base.hashCode(), equal.hashCode());
    }
    
    @Test
    public void testDifferent() {
        assertNotEquals(notEqual.getCodigoPostal(), base.getCodigoPostal());
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
        assertNull(nulo.getCodigoPostal());
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
