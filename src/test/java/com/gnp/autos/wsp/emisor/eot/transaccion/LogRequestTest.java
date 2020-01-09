package com.gnp.autos.wsp.emisor.eot.transaccion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.sql.Timestamp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(SpringRunner.class)
public class LogRequestTest {
    /* Clase Test. */
    @InjectMocks
    private LogRequest base;
    @InjectMocks
    private LogRequest equal;
    @InjectMocks
    private LogRequest notEqual;
    @InjectMocks
    private LogRequest nulo;
    @InjectMocks
    private LogRequest object3;
    
    @Before
    public void initMocksBase() {
        base = new LogRequest();
        ReflectionTestUtils.setField(base, "request", "request");
        ReflectionTestUtils.setField(base, "response", "response");
        ReflectionTestUtils.setField(base, "tiempoPeticion", new Timestamp(1325));
        ReflectionTestUtils.setField(base, "tiempoRespuesta", new Timestamp(1325));
        ReflectionTestUtils.setField(base, "isExitoso", true);
        ReflectionTestUtils.setField(base, "cveModulo", "cveModulo");
        ReflectionTestUtils.setField(base, "idError", 0);
        ReflectionTestUtils.setField(base, "cveSistemaEmisor", "cveSistemaEmisor");
        ReflectionTestUtils.setField(base, "cveSistemaOrigen", "cveSistemaOrigen");
        ReflectionTestUtils.setField(base, "cveUsuario", "cveUsuario");
        ReflectionTestUtils.setField(base, "importePrimaTotal", new Double("10000"));
        ReflectionTestUtils.setField(base, "importePrimaNeta", new Double("9000"));
        ReflectionTestUtils.setField(base, "folioWSP", 12365);
        ReflectionTestUtils.setField(base, "idTransaccion", "idTransaccion");
        ReflectionTestUtils.setField(base, "cveOperacion", "E");
        equal = new LogRequest();
        ReflectionTestUtils.setField(equal, "request", base.getRequest());
        ReflectionTestUtils.setField(equal, "response", base.getResponse());
        ReflectionTestUtils.setField(equal, "tiempoPeticion", base.getTiempoPeticion());
        ReflectionTestUtils.setField(equal, "tiempoRespuesta", base.getTiempoRespuesta());
        ReflectionTestUtils.setField(equal, "isExitoso", base.isExitoso());
        ReflectionTestUtils.setField(equal, "cveModulo", base.getCveModulo());
        ReflectionTestUtils.setField(equal, "idError", base.getIdError());
        ReflectionTestUtils.setField(equal, "cveSistemaEmisor", base.getCveSistemaEmisor());
        ReflectionTestUtils.setField(equal, "cveSistemaOrigen", base.getCveSistemaOrigen());
        ReflectionTestUtils.setField(equal, "cveUsuario", base.getCveUsuario());
        ReflectionTestUtils.setField(equal, "importePrimaTotal", base.getImportePrimaTotal());
        ReflectionTestUtils.setField(equal, "importePrimaNeta", base.getImportePrimaNeta());
        ReflectionTestUtils.setField(equal, "folioWSP", base.getFolioWSP());
        ReflectionTestUtils.setField(equal, "idTransaccion", base.getIdTransaccion());
        ReflectionTestUtils.setField(equal, "cveOperacion", base.getCveOperacion());
        notEqual = new LogRequest();
        ReflectionTestUtils.setField(notEqual, "request", "requestn");
        ReflectionTestUtils.setField(notEqual, "response", "responsen");
        ReflectionTestUtils.setField(notEqual, "tiempoPeticion", new Timestamp(13250));
        ReflectionTestUtils.setField(notEqual, "tiempoRespuesta", new Timestamp(13250));
        ReflectionTestUtils.setField(notEqual, "isExitoso", false);
        ReflectionTestUtils.setField(notEqual, "cveModulo", "cveModulon");
        ReflectionTestUtils.setField(notEqual, "idError", 1);
        ReflectionTestUtils.setField(notEqual, "cveSistemaEmisor", "cveSistemaEmisorn");
        ReflectionTestUtils.setField(notEqual, "cveSistemaOrigen", "cveSistemaOrigenn");
        ReflectionTestUtils.setField(notEqual, "cveUsuario", "cveUsuarion");
        ReflectionTestUtils.setField(notEqual, "importePrimaTotal", new Double("11000"));
        ReflectionTestUtils.setField(notEqual, "importePrimaNeta", new Double("9100"));
        ReflectionTestUtils.setField(notEqual, "folioWSP", 12366);
        ReflectionTestUtils.setField(notEqual, "idTransaccion", "idTransaccionn");
        ReflectionTestUtils.setField(notEqual, "cveOperacion", "C");
        nulo = null;
        object3 = new LogRequest();
        ReflectionTestUtils.setField(object3, "request", base.getRequest());
        ReflectionTestUtils.setField(object3, "response", base.getResponse());
        ReflectionTestUtils.setField(object3, "tiempoPeticion", base.getTiempoPeticion());
        ReflectionTestUtils.setField(object3, "tiempoRespuesta", base.getTiempoRespuesta());
        ReflectionTestUtils.setField(object3, "isExitoso", base.isExitoso());
        ReflectionTestUtils.setField(object3, "cveModulo", base.getCveModulo());
        ReflectionTestUtils.setField(object3, "idError", base.getIdError());
        ReflectionTestUtils.setField(object3, "cveSistemaEmisor", base.getCveSistemaEmisor());
        ReflectionTestUtils.setField(object3, "cveSistemaOrigen", base.getCveSistemaOrigen());
        ReflectionTestUtils.setField(object3, "cveUsuario", base.getCveUsuario());
        ReflectionTestUtils.setField(object3, "importePrimaTotal", base.getImportePrimaTotal());
        ReflectionTestUtils.setField(object3, "importePrimaNeta", base.getImportePrimaNeta());
        ReflectionTestUtils.setField(object3, "folioWSP", base.getFolioWSP());
        ReflectionTestUtils.setField(object3, "idTransaccion", base.getIdTransaccion());
        ReflectionTestUtils.setField(object3, "cveOperacion", base.getCveOperacion());
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test() {
        base.setRequest("request");
        assertNotNull(base.getRequest());
        base.setResponse("response");
        assertNotNull(base.getResponse());
        base.setTiempoPeticion(new Timestamp(13250));
        assertNotNull(base.getTiempoPeticion());
        base.setTiempoRespuesta(new Timestamp(13250));
        assertNotNull(base.getTiempoRespuesta());
        base.setExitoso(false);
        assertNotNull(base.isExitoso());
        base.setCveModulo("cveModulo");
        assertNotNull(base.getCveModulo());
        base.setIdError(1);
        assertNotNull(base.getIdError());
        base.setCveSistemaEmisor("cveSistemaEmisor");
        assertNotNull(base.getCveSistemaEmisor());
        base.setCveSistemaOrigen("cveSistemaOrigen");
        assertNotNull(base.getCveSistemaOrigen());
        base.setCveUsuario("cveUsuario");
        assertNotNull(base.getCveUsuario());
        base.setImportePrimaTotal(new Double("11000"));
        assertNotNull(base.getImportePrimaTotal());
        base.setImportePrimaNeta(new Double("9100"));
        assertNotNull(base.getImportePrimaNeta());
        base.setFolioWSP(12366);
        assertNotNull(base.getFolioWSP());
        base.setIdTransaccion("idTransaccion");
        assertNotNull(base.getIdTransaccion());
        base.setCveOperacion("cveOperacion");
        assertNotNull(base.getCveOperacion());
    }
    
    @Test
    public void testEquals() {
        assertEquals(equal.getRequest(), base.getRequest());
        assertEquals(base, equal);
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(base.hashCode(), equal.hashCode());
    }
    
    @Test
    public void testDifferent() {
        assertNotEquals(notEqual.getRequest(), base.getRequest());
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
        assertNull(nulo.getRequest());
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
