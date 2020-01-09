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
public class PagoTVRespTest {
    /* Clase Test. */
    @InjectMocks
    private static PagoTVResp base;
    @InjectMocks
    private static PagoTVResp equal;
    @InjectMocks
    private static PagoTVResp notEqual;
    @InjectMocks
    private static PagoTVResp nulo;
    @InjectMocks
    private static PagoTVResp object3;
    
    @Before
    public void initMocks() {
        base = new PagoTVResp();
        ReflectionTestUtils.setField(base, "id", "id");
        ReflectionTestUtils.setField(base, "idCotizacion", "idCotizacion");
        ReflectionTestUtils.setField(base, "numAutorizacion", "numAutorizacion");
        ReflectionTestUtils.setField(base, "numPoliza", "numPoliza");
        ReflectionTestUtils.setField(base, "numSeguimiento", "numSeguimiento");
        ReflectionTestUtils.setField(base, "errores", "errores");
        equal = new PagoTVResp();
        ReflectionTestUtils.setField(equal, "id", base.getId());
        ReflectionTestUtils.setField(equal, "idCotizacion", base.getIdCotizacion());
        ReflectionTestUtils.setField(equal, "numAutorizacion", base.getNumAutorizacion());
        ReflectionTestUtils.setField(equal, "numPoliza", base.getNumPoliza());
        ReflectionTestUtils.setField(equal, "numSeguimiento", base.getNumSeguimiento());
        ReflectionTestUtils.setField(equal, "errores", base.getErrores());
        notEqual = new PagoTVResp();
        ReflectionTestUtils.setField(notEqual, "id", "idn");
        ReflectionTestUtils.setField(notEqual, "idCotizacion", "idCotizacionn");
        ReflectionTestUtils.setField(notEqual, "numAutorizacion", "numAutorizacionn");
        ReflectionTestUtils.setField(notEqual, "numPoliza", "numPolizan");
        ReflectionTestUtils.setField(notEqual, "numSeguimiento", "numSeguimienton");
        ReflectionTestUtils.setField(notEqual, "errores", "erroresn");
        nulo = null;
        object3 = new PagoTVResp();
        ReflectionTestUtils.setField(object3, "id", base.getId());
        ReflectionTestUtils.setField(object3, "idCotizacion", base.getIdCotizacion());
        ReflectionTestUtils.setField(object3, "numAutorizacion", base.getNumAutorizacion());
        ReflectionTestUtils.setField(object3, "numPoliza", base.getNumPoliza());
        ReflectionTestUtils.setField(object3, "numSeguimiento", base.getNumSeguimiento());
        ReflectionTestUtils.setField(object3, "errores", base.getErrores());
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test() {
        base.setId("id");
        assertNotNull(base.getId());
        base.setIdCotizacion("idCotizacion");
        assertNotNull(base.getIdCotizacion());
        base.setNumAutorizacion("numAutorizacion");
        assertNotNull(base.getNumAutorizacion());
        base.setNumPoliza("numPoliza");
        assertNotNull(base.getNumPoliza());
        base.setNumSeguimiento("numSeguimiento");
        assertNotNull(base.getNumSeguimiento());
        base.setErrores("errores");
        assertNotNull(base.getErrores());
    }
    
    @Test
    public void testEquals() {
        assertEquals("idCotizacion", base.getIdCotizacion());
        assertEquals(base, equal);
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(base.hashCode(), equal.hashCode());
    }
    
    @Test
    public void testDifferent() {
        assertNotEquals("idCotizacionn", base.getIdCotizacion());
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
        nulo.setIdCotizacion(null);
        assertNull(nulo.getIdCotizacion());
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
