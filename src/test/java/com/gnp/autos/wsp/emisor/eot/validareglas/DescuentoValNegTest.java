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
public class DescuentoValNegTest {
    /* Clase Test. */
    @InjectMocks
    private DescuentoValNeg base;
    @InjectMocks
    private DescuentoValNeg equal;
    @InjectMocks
    private DescuentoValNeg notEqual;
    @InjectMocks
    private DescuentoValNeg nulo;
    @InjectMocks
    private DescuentoValNeg object3;
    
    @Before
    public void initMocksBase() {
        base = new DescuentoValNeg();
        ReflectionTestUtils.setField(base, "cveDescuento", "cveDescuento");
        ReflectionTestUtils.setField(base, "descripcion", "descripcion");
        ReflectionTestUtils.setField(base, "unidadMedida", "unidadMedida");
        ReflectionTestUtils.setField(base, "valor", "valor");
        ReflectionTestUtils.setField(base, "banRecargo", "banRecargo");
        equal = new DescuentoValNeg();
        ReflectionTestUtils.setField(equal, "cveDescuento", base.getCveDescuento());
        ReflectionTestUtils.setField(equal, "descripcion", base.getDescripcion());
        ReflectionTestUtils.setField(equal, "unidadMedida", base.getUnidadMedida());
        ReflectionTestUtils.setField(equal, "valor", base.getValor());
        ReflectionTestUtils.setField(equal, "banRecargo", base.getBanRecargo());
        notEqual = new DescuentoValNeg();
        ReflectionTestUtils.setField(notEqual, "cveDescuento", "cveDescuenton");
        ReflectionTestUtils.setField(notEqual, "descripcion", "descripcionn");
        ReflectionTestUtils.setField(notEqual, "unidadMedida", "unidadMedidan");
        ReflectionTestUtils.setField(notEqual, "valor", "valorn");
        ReflectionTestUtils.setField(notEqual, "banRecargo", "banRecargon");
        nulo = null;
        object3 = new DescuentoValNeg();
        ReflectionTestUtils.setField(object3, "cveDescuento", base.getCveDescuento());
        ReflectionTestUtils.setField(object3, "descripcion", base.getDescripcion());
        ReflectionTestUtils.setField(object3, "unidadMedida", base.getUnidadMedida());
        ReflectionTestUtils.setField(object3, "valor", base.getValor());
        ReflectionTestUtils.setField(object3, "banRecargo", base.getBanRecargo());
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test() {
        base.setCveDescuento("cveDescuento");
        assertNotNull(base.getCveDescuento());
        base.setDescripcion("descripcion");
        assertNotNull(base.getDescripcion());
        base.setUnidadMedida("unidadMedida");
        assertNotNull(base.getUnidadMedida());
        base.setValor("valor");
        assertNotNull(base.getValor());
        base.setBanRecargo("banRecargo");
        assertNotNull(base.getBanRecargo());
    }
    
    @Test
    public void testEquals() {
        assertEquals(equal.getCveDescuento(), base.getCveDescuento());
        assertEquals(base, equal);
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(base.hashCode(), equal.hashCode());
    }
    
    @Test
    public void testDifferent() {
        assertNotEquals(notEqual.getCveDescuento(), base.getCveDescuento());
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
        assertNull(nulo.getCveDescuento());
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
