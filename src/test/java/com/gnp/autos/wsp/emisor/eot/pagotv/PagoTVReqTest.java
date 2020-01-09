package com.gnp.autos.wsp.emisor.eot.pagotv;

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
public class PagoTVReqTest {
    /* Clase Test. */
    @InjectMocks
    private static PagoTVReq base;
    @InjectMocks
    private static PagoTVReq equal;
    @InjectMocks
    private static PagoTVReq notEqual;
    @InjectMocks
    private static PagoTVReq nulo;
    @InjectMocks
    private static PagoTVReq object3;
    
    @Before
    public void initMocks() {
        base = new PagoTVReq();
        ReflectionTestUtils.setField(base, "id", "id");
        ReflectionTestUtils.setField(base, "usuario", "usuario");
        ReflectionTestUtils.setField(base, "pw", "pw");
        ReflectionTestUtils.setField(base, "numAutorizacion", "numAutorizacion");
        ReflectionTestUtils.setField(base, "numPoliza", "numPoliza");
        ReflectionTestUtils.setField(base, "numSeguimiento", "numSeguimiento");
        ReflectionTestUtils.setField(base, "idCotizacion", "idCotizacion");
        equal = new PagoTVReq();
        ReflectionTestUtils.setField(equal, "id", base.getId());
        ReflectionTestUtils.setField(equal, "usuario", base.getUsuario());
        ReflectionTestUtils.setField(equal, "pw", base.getPw());
        ReflectionTestUtils.setField(equal, "numAutorizacion", base.getNumAutorizacion());
        ReflectionTestUtils.setField(equal, "numPoliza", base.getNumPoliza());
        ReflectionTestUtils.setField(equal, "numSeguimiento", base.getNumSeguimiento());
        ReflectionTestUtils.setField(equal, "idCotizacion", base.getIdCotizacion());
        notEqual = new PagoTVReq();
        ReflectionTestUtils.setField(notEqual, "id", "idn");
        ReflectionTestUtils.setField(notEqual, "usuario", "usuarion");
        ReflectionTestUtils.setField(notEqual, "pw", "pwn");
        ReflectionTestUtils.setField(notEqual, "numAutorizacion", "numAutorizacionn");
        ReflectionTestUtils.setField(notEqual, "numPoliza", "numPolizan");
        ReflectionTestUtils.setField(notEqual, "numSeguimiento", "numSeguimienton");
        ReflectionTestUtils.setField(notEqual, "idCotizacion", "idCotizacionn");
        nulo = null;
        object3 = new PagoTVReq();
        ReflectionTestUtils.setField(object3, "id", base.getId());
        ReflectionTestUtils.setField(object3, "usuario", base.getUsuario());
        ReflectionTestUtils.setField(object3, "pw", base.getPw());
        ReflectionTestUtils.setField(object3, "numAutorizacion", base.getNumAutorizacion());
        ReflectionTestUtils.setField(object3, "numPoliza", base.getNumPoliza());
        ReflectionTestUtils.setField(object3, "numSeguimiento", base.getNumSeguimiento());
        ReflectionTestUtils.setField(object3, "idCotizacion", base.getIdCotizacion());
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test() {
        base.setId("id");
        assertNotNull(base.getId());
        base.setUsuario("usuario");
        assertNotNull(base.getUsuario());
        base.setPw("pw");
        assertNotNull(base.getPw());
        base.setNumAutorizacion("numAutorizacion");
        assertNotNull(base.getNumAutorizacion());
        base.setNumPoliza("numPoliza");
        assertNotNull(base.getNumPoliza());
        base.setNumSeguimiento("numSeguimiento");
        assertNotNull(base.getNumSeguimiento());
        base.setIdCotizacion("idCotizacion");
        assertNotNull(base.getIdCotizacion());
    }
    
    @Test
    public void testEquals() {
        assertEquals("usuario", base.getUsuario());
        assertEquals(base, equal);
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(base.hashCode(), equal.hashCode());
    }
    
    @Test
    public void testDifferent() {
        assertNotEquals("usuarion", base.getUsuario());
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
        nulo.setUsuario(null);
        assertNull(nulo.getUsuario());
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
