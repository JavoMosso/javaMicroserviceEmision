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
public class PersonaValNegTest {
    /* Clase Test. */
    @InjectMocks
    private PersonaValNeg base;
    @InjectMocks
    private PersonaValNeg equal;
    @InjectMocks
    private PersonaValNeg notEqual;
    @InjectMocks
    private PersonaValNeg nulo;
    @InjectMocks
    private PersonaValNeg object3;
    
    @Before
    public void initMocksBase() {
        DomicilioValNeg domicilio = new DomicilioValNeg();
        base = new PersonaValNeg();
        ReflectionTestUtils.setField(base, "tipo", "tipo");
        ReflectionTestUtils.setField(base, "tipoPersona", "tipoPersona");
        ReflectionTestUtils.setField(base, "idParticipante", "idParticipante");
        ReflectionTestUtils.setField(base, "rfc", "rfc");
        ReflectionTestUtils.setField(base, "fecNacimiento", "fecNacimiento");
        ReflectionTestUtils.setField(base, "fecConstitucion", "fecConstitucion");
        ReflectionTestUtils.setField(base, "domicilio", domicilio);
        ReflectionTestUtils.setField(base, "banBeneficiarioPreferente", "banBeneficiarioPreferente");
        equal = new PersonaValNeg();
        ReflectionTestUtils.setField(equal, "tipo", base.getTipo());
        ReflectionTestUtils.setField(equal, "tipoPersona", base.getTipoPersona());
        ReflectionTestUtils.setField(equal, "idParticipante", base.getIdParticipante());
        ReflectionTestUtils.setField(equal, "rfc", base.getRfc());
        ReflectionTestUtils.setField(equal, "fecNacimiento", base.getFecNacimiento());
        ReflectionTestUtils.setField(equal, "fecConstitucion", base.getFecConstitucion());
        ReflectionTestUtils.setField(equal, "domicilio", base.getDomicilio());
        ReflectionTestUtils.setField(equal, "banBeneficiarioPreferente", base.getBanBeneficiarioPreferente());
        notEqual = new PersonaValNeg();
        ReflectionTestUtils.setField(notEqual, "tipo", "tipon");
        ReflectionTestUtils.setField(notEqual, "tipoPersona", "tipoPersonan");
        ReflectionTestUtils.setField(notEqual, "idParticipante", "idParticipanten");
        ReflectionTestUtils.setField(notEqual, "rfc", "rfcn");
        ReflectionTestUtils.setField(notEqual, "fecNacimiento", "fecNacimienton");
        ReflectionTestUtils.setField(notEqual, "fecConstitucion", "fecConstitucionn");
        ReflectionTestUtils.setField(notEqual, "domicilio", domicilio);
        ReflectionTestUtils.setField(notEqual, "banBeneficiarioPreferente", "banBeneficiarioPreferenten");
        nulo = null;
        object3 = new PersonaValNeg();
        ReflectionTestUtils.setField(object3, "tipo", base.getTipo());
        ReflectionTestUtils.setField(object3, "tipoPersona", base.getTipoPersona());
        ReflectionTestUtils.setField(object3, "idParticipante", base.getIdParticipante());
        ReflectionTestUtils.setField(object3, "rfc", base.getRfc());
        ReflectionTestUtils.setField(object3, "fecNacimiento", base.getFecNacimiento());
        ReflectionTestUtils.setField(object3, "fecConstitucion", base.getFecConstitucion());
        ReflectionTestUtils.setField(object3, "domicilio", base.getDomicilio());
        ReflectionTestUtils.setField(object3, "banBeneficiarioPreferente", base.getBanBeneficiarioPreferente());
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test() {
        base.setTipo("tipo");
        assertNotNull(base.getTipo());
        base.setTipoPersona("tipoPersona");
        assertNotNull(base.getTipoPersona());
        base.setIdParticipante("idParticipante");
        assertNotNull(base.getIdParticipante());
        base.setTipo("rfc");
        assertNotNull(base.getRfc());
        base.setTipo("fecNacimiento");
        assertNotNull(base.getFecNacimiento());
        base.setFecConstitucion("fecConstitucion");
        assertNotNull(base.getFecConstitucion());
        base.setDomicilio(new DomicilioValNeg());
        assertNotNull(base.getDomicilio());
        base.setBanBeneficiarioPreferente("banBeneficiarioPreferente");
        assertNotNull(base.getBanBeneficiarioPreferente());
    }
    
    @Test
    public void testEquals() {
        assertEquals(equal.getTipo(), base.getTipo());
        assertEquals(base, equal);
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(base.hashCode(), equal.hashCode());
    }
    
    @Test
    public void testDifferent() {
        assertNotEquals(notEqual.getTipo(), base.getTipo());
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
        assertNull(nulo.getTipo());
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
