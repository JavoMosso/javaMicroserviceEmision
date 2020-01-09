package com.gnp.autos.wsp.emisor.eot.rest.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import com.gnp.autos.wsp.emisor.eot.pagotv.PagoTVReq;

@RunWith(SpringRunner.class)
public class ValidaPagoTVClientTest {
    /* Clase Test. */
    private static ValidaPagoTVClient base;
    private static ValidaPagoTVClient equal;
    private static ValidaPagoTVClient notEqual;
    private static ValidaPagoTVClient nulo;
    private static ValidaPagoTVClient object3;
    @Mock
    private PagoTVReq objPagoReq;
    @Mock
    private RestTemplate restTemplate;
    
    @Before
    public void initMocks() {
        restTemplate = new RestTemplate();
        MockitoAnnotations.initMocks(this);
    }
    
    @SuppressWarnings("static-access")
    @Test
    public void test() {
        base = new ValidaPagoTVClient("idCotizacion", "numAutorizacion", "usuarioVp", "passVp", restTemplate);
        assertNotNull(base.getClass());
    }
    
    @Test
    public void testEquals() {
        base = new ValidaPagoTVClient("idCotizacion", "numAutorizacion", "usuarioVp", "passVp", restTemplate);
        equal = new ValidaPagoTVClient("idCotizacion", "numAutorizacion", "usuarioVp", "passVp", restTemplate);
        assertEquals(base, equal);
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(base.hashCode(), equal.hashCode());
    }
    
    @Test// (expected = AssertionError.class)
    public void testDifferent() {
        base = new ValidaPagoTVClient("idCotizacion", "numAutorizacion", "usuarioVp", "passVp", restTemplate);
        equal = new ValidaPagoTVClient("idCotizacion", "numAutorizacion", "usuarioVp", "passVp", restTemplate);
        notEqual = new ValidaPagoTVClient("idCotizacionn", "numAutorizacionn", "usuarioVpn", "passVpn", restTemplate);
        nulo = null;
        assertNotEquals(base, notEqual);
        assertNotEquals(base.hashCode(), notEqual.hashCode());
        assertNotEquals(base.hashCode(), nulo);
    }
    
    @Test
    public void testAssert() {
        base = new ValidaPagoTVClient("idCotizacion", "numAutorizacion", "usuarioVp", "passVp", restTemplate);
        equal = new ValidaPagoTVClient("idCotizacion", "numAutorizacion", "usuarioVp", "passVp", restTemplate);
        notEqual = new ValidaPagoTVClient("idCotizacionn", "numAutorizacionn", "usuarioVpn", "passVpn", restTemplate);
        nulo = null;
        assertTrue(base.canEqual(base));
        assertTrue(base.equals(base));
        assertTrue(base.canEqual(equal));
        assertTrue(base.equals(equal));
    }
    
    @Test// (expected = AssertionError.class)
    public void testDeny() {
        base = new ValidaPagoTVClient("idCotizacion", "numAutorizacion", "usuarioVp", "passVp", restTemplate);
        equal = new ValidaPagoTVClient("idCotizacion", "numAutorizacion", "usuarioVp", "passVp", restTemplate);
        notEqual = new ValidaPagoTVClient("idCotizacionn", "numAutorizacionn", "usuarioVpn", "passVpn", restTemplate);
        nulo = null;
        assertFalse(base.equals(notEqual));
        assertFalse(base.equals(null));
    }
    
    @SuppressWarnings("static-access")
    @Test// (expected = AssertionError.class)
    public void testNull() {
        base = new ValidaPagoTVClient("idCotizacion", "numAutorizacion", "usuarioVp", "passVp", restTemplate);
        equal = new ValidaPagoTVClient("idCotizacion", "numAutorizacion", "usuarioVp", "passVp", restTemplate);
        notEqual = new ValidaPagoTVClient("idCotizacionn", "numAutorizacionn", "usuarioVpn", "passVpn", restTemplate);
        nulo = null;
        assertNull(nulo);
    }
    
    @Test
    public void testNotNull() {
        base = new ValidaPagoTVClient("idCotizacion", "numAutorizacion", "usuarioVp", "passVp", restTemplate);
        equal = new ValidaPagoTVClient("idCotizacion", "numAutorizacion", "usuarioVp", "passVp", restTemplate);
        notEqual = new ValidaPagoTVClient("idCotizacionn", "numAutorizacionn", "usuarioVpn", "passVpn", restTemplate);
        nulo = null;
        assertNotNull(base);
        assertNotNull(base.toString());
    }
    
    @Test
    public void testReflexive() {
        base = new ValidaPagoTVClient("idCotizacion", "numAutorizacion", "usuarioVp", "passVp", restTemplate);
        equal = new ValidaPagoTVClient("idCotizacion", "numAutorizacion", "usuarioVp", "passVp", restTemplate);
        notEqual = new ValidaPagoTVClient("idCotizacionn", "numAutorizacionn", "usuarioVpn", "passVpn", restTemplate);
        nulo = null;
        assertEquals(base, base);
    }
    
    @Test
    public void testSymmetric() {
        base = new ValidaPagoTVClient("idCotizacion", "numAutorizacion", "usuarioVp", "passVp", restTemplate);
        equal = new ValidaPagoTVClient("idCotizacion", "numAutorizacion", "usuarioVp", "passVp", restTemplate);
        notEqual = new ValidaPagoTVClient("idCotizacionn", "numAutorizacionn", "usuarioVpn", "passVpn", restTemplate);
        nulo = null;
        assertEquals(base, equal);
        assertEquals(equal, base);
    }
    
    @Test
    public void testTransitive() {
        base = new ValidaPagoTVClient("idCotizacion", "numAutorizacion", "usuarioVp", "passVp", restTemplate);
        equal = new ValidaPagoTVClient("idCotizacion", "numAutorizacion", "usuarioVp", "passVp", restTemplate);
        notEqual = new ValidaPagoTVClient("idCotizacionn", "numAutorizacionn", "usuarioVpn", "passVpn", restTemplate);
        nulo = null;
        assertEquals(base, equal);
        assertNotEquals(equal, object3);
        assertNotEquals(base, object3);
        base.setObjPagoReq(objPagoReq);
    }
}
