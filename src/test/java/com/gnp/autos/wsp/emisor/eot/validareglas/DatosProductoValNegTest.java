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

@RunWith(SpringRunner.class)
public class DatosProductoValNegTest {
    /* Clase Test. */
    @InjectMocks
    private DatosProductoValNeg base;
    @InjectMocks
    private DatosProductoValNeg equal;
    @InjectMocks
    private DatosProductoValNeg notEqual;
    @InjectMocks
    private DatosProductoValNeg nulo;
    @InjectMocks
    private DatosProductoValNeg object3;
    
    @Before
    public void initMocksBase() {
        List<CoberturaValNeg> coberturas = new ArrayList<>();
        List<CoberturaValNeg> coberturasn = new ArrayList<>();
        coberturasn.add(new CoberturaValNeg());
        base = new DatosProductoValNeg();
        ReflectionTestUtils.setField(base, "idproducto", "idproducto");
        ReflectionTestUtils.setField(base, "coberturas", coberturas);
        equal = new DatosProductoValNeg();
        ReflectionTestUtils.setField(equal, "idproducto", base.getIdproducto());
        ReflectionTestUtils.setField(equal, "coberturas", base.getCoberturas());
        notEqual = new DatosProductoValNeg();
        ReflectionTestUtils.setField(notEqual, "idproducto", "idproducton");
        ReflectionTestUtils.setField(notEqual, "coberturas", coberturasn);
        nulo = null;
        object3 = new DatosProductoValNeg();
        ReflectionTestUtils.setField(object3, "idproducto", base.getIdproducto());
        ReflectionTestUtils.setField(object3, "coberturas", base.getCoberturas());
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test() {
        base.setIdproducto("idproducto");
        assertNotNull(base.getIdproducto());
        base.setCoberturas(new ArrayList<>());
        assertNotNull(base.getCoberturas());
    }
    
    @Test
    public void testEquals() {
        assertEquals(equal.getIdproducto(), base.getIdproducto());
        assertEquals(base, equal);
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(base.hashCode(), equal.hashCode());
    }
    
    @Test
    public void testDifferent() {
        assertNotEquals(notEqual.getIdproducto(), base.getIdproducto());
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
        assertNull(nulo.getIdproducto());
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
