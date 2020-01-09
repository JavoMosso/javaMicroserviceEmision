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
public class AgenteValNegTest {
    /* Clase Test. */
    @InjectMocks
    private AgenteValNeg base;
    @InjectMocks
    private AgenteValNeg equal;
    @InjectMocks
    private AgenteValNeg notEqual;
    @InjectMocks
    private AgenteValNeg nulo;
    @InjectMocks
    private AgenteValNeg object3;
    
    @Before
    public void initMocksBase() {
        base = new AgenteValNeg();
        ReflectionTestUtils.setField(base, "idParticipante", "idParticipante");
        ReflectionTestUtils.setField(base, "codIntermediario", "codIntermediario");
        ReflectionTestUtils.setField(base, "banIntermediarioPrincipal", "banIntermediarioPrincipal");
        ReflectionTestUtils.setField(base, "folio", "folio");
        equal = new AgenteValNeg();
        ReflectionTestUtils.setField(equal, "idParticipante", base.getIdParticipante());
        ReflectionTestUtils.setField(equal, "codIntermediario", base.getCodIntermediario());
        ReflectionTestUtils.setField(equal, "banIntermediarioPrincipal", base.getBanIntermediarioPrincipal());
        ReflectionTestUtils.setField(equal, "folio", base.getFolio());
        notEqual = new AgenteValNeg();
        ReflectionTestUtils.setField(notEqual, "idParticipante", "idParticipanten");
        ReflectionTestUtils.setField(notEqual, "codIntermediario", "codIntermediarion");
        ReflectionTestUtils.setField(notEqual, "banIntermediarioPrincipal", "banIntermediarioPrincipaln");
        ReflectionTestUtils.setField(notEqual, "folio", "folion");
        nulo = null;
        object3 = new AgenteValNeg();
        ReflectionTestUtils.setField(object3, "idParticipante", base.getIdParticipante());
        ReflectionTestUtils.setField(object3, "codIntermediario", base.getCodIntermediario());
        ReflectionTestUtils.setField(object3, "banIntermediarioPrincipal", base.getBanIntermediarioPrincipal());
        ReflectionTestUtils.setField(object3, "folio", base.getFolio());
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test() {
        base.setIdParticipante("idParticipante");
        assertNotNull(base.getIdParticipante());
        base.setCodIntermediario("codIntermediario");
        assertNotNull(base.getCodIntermediario());
        base.setBanIntermediarioPrincipal("banIntermediarioPrincipal");
        assertNotNull(base.getBanIntermediarioPrincipal());
        base.setFolio("folio");
        assertNotNull(base.getFolio());
    }
    
    @Test
    public void testEquals() {
        assertEquals(equal.getIdParticipante(), base.getIdParticipante());
        assertEquals(base, equal);
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(base.hashCode(), equal.hashCode());
    }
    
    @Test
    public void testDifferent() {
        assertNotEquals(notEqual.getIdParticipante(), base.getIdParticipante());
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
        assertNull(nulo.getIdParticipante());
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
