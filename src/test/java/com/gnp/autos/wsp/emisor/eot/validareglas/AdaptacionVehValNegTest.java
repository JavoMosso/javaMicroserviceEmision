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
public class AdaptacionVehValNegTest {
    /* Clase Test. */
    @InjectMocks
    private AdaptacionVehValNeg base;
    @InjectMocks
    private AdaptacionVehValNeg equal;
    @InjectMocks
    private AdaptacionVehValNeg notEqual;
    @InjectMocks
    private AdaptacionVehValNeg nulo;
    @InjectMocks
    private AdaptacionVehValNeg object3;
    
    @Before
    public void initMocksBase() {
        base = new AdaptacionVehValNeg();
        ReflectionTestUtils.setField(base, "descEquip", "descEquip");
        ReflectionTestUtils.setField(base, "montoFacturacion", "montoFacturacion");
        ReflectionTestUtils.setField(base, "fechaFactura", "fechaFactura");
        ReflectionTestUtils.setField(base, "montoSA", "montoSA");
        ReflectionTestUtils.setField(base, "banEquip", "banEquip");
        equal = new AdaptacionVehValNeg();
        ReflectionTestUtils.setField(equal, "descEquip", base.getDescEquip());
        ReflectionTestUtils.setField(equal, "montoFacturacion", base.getMontoFacturacion());
        ReflectionTestUtils.setField(equal, "fechaFactura", base.getFechaFactura());
        ReflectionTestUtils.setField(equal, "montoSA", base.getMontoSA());
        ReflectionTestUtils.setField(equal, "banEquip", base.getBanEquip());
        notEqual = new AdaptacionVehValNeg();
        ReflectionTestUtils.setField(notEqual, "descEquip", "descEquipn");
        ReflectionTestUtils.setField(notEqual, "montoFacturacion", "montoFacturacionn");
        ReflectionTestUtils.setField(notEqual, "fechaFactura", "fechaFacturan");
        ReflectionTestUtils.setField(notEqual, "montoSA", "montoSAn");
        ReflectionTestUtils.setField(notEqual, "banEquip", "banEquipn");
        nulo = null;
        object3 = new AdaptacionVehValNeg();
        ReflectionTestUtils.setField(object3, "descEquip", base.getDescEquip());
        ReflectionTestUtils.setField(object3, "montoFacturacion", base.getMontoFacturacion());
        ReflectionTestUtils.setField(object3, "fechaFactura", base.getFechaFactura());
        ReflectionTestUtils.setField(object3, "montoSA", base.getMontoSA());
        ReflectionTestUtils.setField(object3, "banEquip", base.getBanEquip());
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test() {
        base.setDescEquip("descEquip");
        assertNotNull(base.getDescEquip());
        base.setMontoFacturacion("montoFacturacion");
        assertNotNull(base.getMontoFacturacion());
        base.setFechaFactura("fechaFactura");
        assertNotNull(base.getFechaFactura());
        base.setMontoSA("montoSA");
        assertNotNull(base.getMontoSA());
        base.setBanEquip("banEquip");
        assertNotNull(base.getBanEquip());
    }
    
    @Test
    public void testEquals() {
        assertEquals(equal.getDescEquip(), base.getDescEquip());
        assertEquals(base, equal);
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(base.hashCode(), equal.hashCode());
    }
    
    @Test
    public void testDifferent() {
        assertNotEquals(notEqual.getDescEquip(), base.getDescEquip());
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
        assertNull(nulo.getDescEquip());
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
