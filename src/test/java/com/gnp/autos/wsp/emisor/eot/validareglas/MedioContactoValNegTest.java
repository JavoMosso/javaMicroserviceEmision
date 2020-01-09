package com.gnp.autos.wsp.emisor.eot.validareglas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import com.gnp.autos.wsp.negocio.neg.model.ElementoNeg;

@RunWith(SpringRunner.class)
public class MedioContactoValNegTest {
    /* Clase Test. */
    @InjectMocks
    private MedioContactoValNeg base;
    @InjectMocks
    private MedioContactoValNeg equal;
    @InjectMocks
    private MedioContactoValNeg notEqual;
    @InjectMocks
    private MedioContactoValNeg nulo;
    @InjectMocks
    private MedioContactoValNeg object3;
    
    @Before
    public void initMocksBase() {
        List<ElementoNeg> elementos = new ArrayList<>();
        List<ElementoNeg> elementosn = new ArrayList<>();
        elementosn.add(new ElementoNeg());
        base = new MedioContactoValNeg();
        ReflectionTestUtils.setField(base, "elementos", elementos);
        equal = new MedioContactoValNeg();
        ReflectionTestUtils.setField(equal, "elementos", base.getElementos());
        notEqual = new MedioContactoValNeg();
        ReflectionTestUtils.setField(notEqual, "elementos", elementosn);
        nulo = null;
        object3 = new MedioContactoValNeg();
        ReflectionTestUtils.setField(object3, "elementos", base.getElementos());
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test() {
        base.setElementos(new ArrayList<>());
        assertNotNull(base.getElementos());
    }
    
    @Test
    public void testEquals() {
        assertEquals(equal.getElementos(), base.getElementos());
        assertEquals(base, equal);
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(base.hashCode(), equal.hashCode());
    }
    
    @Test
    public void testDifferent() {
        assertNotEquals(notEqual.getElementos(), base.getElementos());
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
        assertNull(nulo.getElementos());
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
