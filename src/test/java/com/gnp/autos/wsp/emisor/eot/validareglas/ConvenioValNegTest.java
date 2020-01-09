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
public class ConvenioValNegTest {
    /* Clase Test. */
    @InjectMocks
    private ConvenioValNeg base;
    @InjectMocks
    private ConvenioValNeg equal;
    @InjectMocks
    private ConvenioValNeg notEqual;
    @InjectMocks
    private ConvenioValNeg nulo;
    @InjectMocks
    private ConvenioValNeg object3;
    
    @Before
    public void initMocksBase() {
        base = new ConvenioValNeg();
        ReflectionTestUtils.setField(base, "descConvenio", "descConvenio");
        ReflectionTestUtils.setField(base, "desMedida", "desMedida");
        ReflectionTestUtils.setField(base, "idConvenio", "idConvenio");
        ReflectionTestUtils.setField(base, "idUniMedida", "idUniMedida");
        ReflectionTestUtils.setField(base, "valor", "valor");
        equal = new ConvenioValNeg();
        ReflectionTestUtils.setField(equal, "descConvenio", base.getDescConvenio());
        ReflectionTestUtils.setField(equal, "desMedida", base.getDesMedida());
        ReflectionTestUtils.setField(equal, "idConvenio", base.getIdConvenio());
        ReflectionTestUtils.setField(equal, "idUniMedida", base.getIdUniMedida());
        ReflectionTestUtils.setField(equal, "valor", base.getValor());
        notEqual = new ConvenioValNeg();
        ReflectionTestUtils.setField(notEqual, "descConvenio", "descConvenion");
        ReflectionTestUtils.setField(notEqual, "desMedida", "desMedidan");
        ReflectionTestUtils.setField(notEqual, "idConvenio", "idConvenion");
        ReflectionTestUtils.setField(notEqual, "idUniMedida", "idUniMedidan");
        ReflectionTestUtils.setField(notEqual, "valor", "valorn");
        nulo = null;
        object3 = new ConvenioValNeg();
        ReflectionTestUtils.setField(object3, "descConvenio", base.getDescConvenio());
        ReflectionTestUtils.setField(object3, "desMedida", base.getDesMedida());
        ReflectionTestUtils.setField(object3, "idConvenio", base.getIdConvenio());
        ReflectionTestUtils.setField(object3, "idUniMedida", base.getIdUniMedida());
        ReflectionTestUtils.setField(object3, "valor", base.getValor());
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test() {
        base.setDescConvenio("descConvenio");
        assertNotNull(base.getDescConvenio());
        base.setDesMedida("desMedida");
        assertNotNull(base.getDesMedida());
        base.setIdConvenio("idConvenio");
        assertNotNull(base.getIdConvenio());
        base.setIdUniMedida("idUniMedida");
        assertNotNull(base.getIdUniMedida());
        base.setValor("valor");
        assertNotNull(base.getValor());
    }
    
    @Test
    public void testEquals() {
        assertEquals(equal.getDescConvenio(), base.getDescConvenio());
        assertEquals(base, equal);
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(base.hashCode(), equal.hashCode());
    }
    
    @Test
    public void testDifferent() {
        assertNotEquals(notEqual.getDescConvenio(), base.getDescConvenio());
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
        assertNull(nulo.getDescConvenio());
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
