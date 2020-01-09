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
public class CoberturaValNegTest {
    /* Clase Test. */
    @InjectMocks
    private CoberturaValNeg base;
    @InjectMocks
    private CoberturaValNeg equal;
    @InjectMocks
    private CoberturaValNeg notEqual;
    @InjectMocks
    private CoberturaValNeg nulo;
    @InjectMocks
    private CoberturaValNeg object3;
    
    @Before
    public void initMocksBase() {
        base = new CoberturaValNeg();
        ReflectionTestUtils.setField(base, "clavecobertura", "clavecobertura");
        ReflectionTestUtils.setField(base, "mtoSumaAsegurada", "mtoSumaAsegurada");
        ReflectionTestUtils.setField(base, "mtoDeducible", "mtoDeducible");
        equal = new CoberturaValNeg();
        ReflectionTestUtils.setField(equal, "clavecobertura", base.getClavecobertura());
        ReflectionTestUtils.setField(equal, "mtoSumaAsegurada", base.getMtoSumaAsegurada());
        ReflectionTestUtils.setField(equal, "mtoDeducible", base.getMtoDeducible());
        notEqual = new CoberturaValNeg();
        ReflectionTestUtils.setField(notEqual, "clavecobertura", "clavecoberturan");
        ReflectionTestUtils.setField(notEqual, "mtoSumaAsegurada", "mtoSumaAseguradan");
        ReflectionTestUtils.setField(notEqual, "mtoDeducible", "mtoDeduciblen");
        nulo = null;
        object3 = new CoberturaValNeg();
        ReflectionTestUtils.setField(object3, "clavecobertura", base.getClavecobertura());
        ReflectionTestUtils.setField(object3, "mtoSumaAsegurada", base.getMtoSumaAsegurada());
        ReflectionTestUtils.setField(object3, "mtoDeducible", base.getMtoDeducible());
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test() {
        base.setClavecobertura("clavecobertura");
        assertNotNull(base.getClavecobertura());
        base.setMtoSumaAsegurada("mtoSumaAsegurada");
        assertNotNull(base.getMtoSumaAsegurada());
        base.setMtoDeducible("mtoDeducible");
        assertNotNull(base.getMtoDeducible());
    }
    
    @Test
    public void testEquals() {
        assertEquals(equal.getClavecobertura(), base.getClavecobertura());
        assertEquals(base, equal);
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(base.hashCode(), equal.hashCode());
    }
    
    @Test
    public void testDifferent() {
        assertNotEquals(notEqual.getClavecobertura(), base.getClavecobertura());
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
        assertNull(nulo.getClavecobertura());
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
