package com.gnp.autos.wsp.negocio.umoservicemodel;

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
import com.gnp.autos.wsp.negocio.umoservice.model.Dominios;
import com.gnp.autos.wsp.negocio.umoservice.model.Negocio;

@RunWith(SpringRunner.class)
public class UmoServiceRespTest {
    /* Clase Test. */
    @InjectMocks
    private static UmoServiceResp base;
    @InjectMocks
    private static UmoServiceResp equal;
    @InjectMocks
    private static UmoServiceResp notEqual;
    @InjectMocks
    private static UmoServiceResp nulo;
    @InjectMocks
    private static UmoServiceResp object3;
    
    @Before
    public void initMocks() {
        base = new UmoServiceResp();
        ReflectionTestUtils.setField(base, "id", 0);
        ReflectionTestUtils.setField(base, "nombre", "nombre");
        ReflectionTestUtils.setField(base, "codigoPromocion", "codigoPromocion");
        ReflectionTestUtils.setField(base, "esUmo", false);
        ReflectionTestUtils.setField(base, "negocio", new Negocio());
        ReflectionTestUtils.setField(base, "dominios", new Dominios());
        equal = new UmoServiceResp();
        ReflectionTestUtils.setField(equal, "id", 0);
        ReflectionTestUtils.setField(equal, "nombre", base.getNombre());
        ReflectionTestUtils.setField(equal, "codigoPromocion", base.getCodigoPromocion());
        ReflectionTestUtils.setField(equal, "esUmo", base.isEsUmo());
        ReflectionTestUtils.setField(equal, "negocio", base.getNegocio());
        ReflectionTestUtils.setField(equal, "dominios", base.getDominios());
        notEqual = new UmoServiceResp();
        ReflectionTestUtils.setField(notEqual, "id", 1);
        ReflectionTestUtils.setField(notEqual, "nombre", "notEqual");
        ReflectionTestUtils.setField(notEqual, "codigoPromocion", "notEqual");
        ReflectionTestUtils.setField(notEqual, "esUmo", true);
        ReflectionTestUtils.setField(notEqual, "negocio", base.getNegocio());
        ReflectionTestUtils.setField(notEqual, "dominios", base.getDominios());
        nulo = null;
        object3 = new UmoServiceResp();
        ReflectionTestUtils.setField(object3, "id", 0);
        ReflectionTestUtils.setField(object3, "nombre", base.getNombre());
        ReflectionTestUtils.setField(object3, "codigoPromocion", base.getCodigoPromocion());
        ReflectionTestUtils.setField(object3, "esUmo", base.isEsUmo());
        ReflectionTestUtils.setField(object3, "negocio", base.getNegocio());
        ReflectionTestUtils.setField(object3, "dominios", base.getDominios());
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test() {
        base.setId(0);
        assertNotNull(base.getId());
        base.setNombre("nombre");
        assertNotNull(base.getNombre());
        base.setCodigoPromocion("codigoPromocion");
        assertNotNull(base.getCodigoPromocion());
        base.setEsUmo(false);
        assertNotNull(base.isEsUmo());
        base.setNegocio(new Negocio());
        assertNotNull(base.getNegocio());
        base.setDominios(new Dominios());
        assertNotNull(base.getDominios());
    }
    
    @Test
    public void testEquals() {
        assertEquals(new Double(0), new Double(base.getId()));
        assertEquals("nombre", base.getNombre());
        assertEquals("codigoPromocion", base.getCodigoPromocion());
        assertEquals(false, base.isEsUmo());
        assertEquals(base, equal);
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(base.hashCode(), equal.hashCode());
    }
    
    @Test
    public void testDifferent() {
        assertNotEquals("nombreNot", base.getNombre());
        assertNotEquals("codigoPromocionNot", base.getCodigoPromocion());
        assertNotEquals(true, base.isEsUmo());
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
        assertNull(nulo.getCodigoPromocion());
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
