package com.gnp.autos.wsp.emisor.eot.movimiento;

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
public class CabeceraReqTest {
    /* Clase Test. */
    @InjectMocks
    private static CabeceraReq base;
    @InjectMocks
    private static CabeceraReq equal;
    @InjectMocks
    private static CabeceraReq notEqual;
    @InjectMocks
    private static CabeceraReq nulo;
    @InjectMocks
    private static CabeceraReq object3;
    
    @Before
    public void initMocks() {
        base = new CabeceraReq();
        ReflectionTestUtils.setField(base, "idTransaccion", "idTransaccion");
        ReflectionTestUtils.setField(base, "cveTransaccion", "cveTransaccion");
        ReflectionTestUtils.setField(base, "idActor", "idActor");
        ReflectionTestUtils.setField(base, "idRol", "idRol");
        ReflectionTestUtils.setField(base, "idPerfil", "idPerfil");
        equal = new CabeceraReq();
        ReflectionTestUtils.setField(equal, "idTransaccion", base.getIdTransaccion());
        ReflectionTestUtils.setField(equal, "cveTransaccion", base.getCveTransaccion());
        ReflectionTestUtils.setField(equal, "idActor", base.getIdActor());
        ReflectionTestUtils.setField(equal, "idRol", base.getIdRol());
        ReflectionTestUtils.setField(equal, "idPerfil", base.getIdPerfil());
        notEqual = new CabeceraReq();
        ReflectionTestUtils.setField(notEqual, "idTransaccion", "idTransaccionnot");
        ReflectionTestUtils.setField(notEqual, "cveTransaccion", "cveTransaccionnot");
        ReflectionTestUtils.setField(notEqual, "idActor", "idActornot");
        ReflectionTestUtils.setField(notEqual, "idRol", "idRolnot");
        ReflectionTestUtils.setField(notEqual, "idPerfil", "idPerfilnot");
        nulo = null;
        object3 = new CabeceraReq();
        ReflectionTestUtils.setField(object3, "idTransaccion", base.getIdTransaccion());
        ReflectionTestUtils.setField(object3, "cveTransaccion", base.getCveTransaccion());
        ReflectionTestUtils.setField(object3, "idActor", base.getIdActor());
        ReflectionTestUtils.setField(object3, "idRol", base.getIdRol());
        ReflectionTestUtils.setField(object3, "idPerfil", base.getIdPerfil());
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test() {
        base.setIdTransaccion("idTransaccion");
        assertNotNull(base.getIdTransaccion());
        base.setCveTransaccion("cveTransaccion");
        assertNotNull(base.getCveTransaccion());
        base.setIdActor("idActor");
        assertNotNull(base.getIdActor());
        base.setIdRol("idRol");
        assertNotNull(base.getIdRol());
        base.setIdPerfil("idPerfil");
        assertNotNull(base.getIdPerfil());
    }
    
    @Test
    public void testEquals() {
        assertEquals("idTransaccion", base.getIdTransaccion());
        assertEquals(base, equal);
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(base.hashCode(), equal.hashCode());
    }
    
    @Test
    public void testDifferent() {
        assertNotEquals("idTransaccionnot", base.getIdTransaccion());
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
        nulo.setIdTransaccion(null);
        assertNull(nulo.getIdTransaccion());
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
