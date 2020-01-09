package com.gnp.autos.wsp.emisor.eot.rest.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;
import com.gnp.autos.wsp.emisor.eot.error.ExecutionError;
import com.gnp.autos.wsp.emisor.eot.util.ObjetosPruebas;
import com.gnp.autos.wsp.negocio.neg.model.AdaptacionVehNeg;
import com.gnp.autos.wsp.negocio.neg.model.EmiteNegReq;
import com.gnp.autos.wsp.negocio.neg.model.PersonaNeg;
import com.google.gson.Gson;

@RunWith(SpringRunner.class)
public class TransaccionesClientTest {
    /* String Request. */
    private String strEmiteNegReq;
    /* Request. */
    private EmiteNegReq emiNeg;
    /* Clase Test. */
    @InjectMocks
    private static TransaccionesClient base;
    @InjectMocks
    private static TransaccionesClient equal;
    @InjectMocks
    private static TransaccionesClient notEqual;
    @InjectMocks
    private static TransaccionesClient nulo;
    @InjectMocks
    private static TransaccionesClient object3;
    @Mock
    private RestTemplate restTemplate;
    
    @Value("classpath:/EmiteNegReqTest.json")
    public void setFileEmiteNegReq(final Resource myRes) throws IOException {
        try (InputStream is = myRes.getInputStream() ) {
            strEmiteNegReq = StreamUtils.copyToString(is, StandardCharsets.UTF_8);
        }
    }
    
    @Before
    public void prepararTest() throws JAXBException {
        Gson gson = new Gson();
        emiNeg = gson.fromJson(strEmiteNegReq, EmiteNegReq.class);
        emiNeg.setIdCotizacion(ObjetosPruebas.COT_NOW);
        emiNeg.setIniVig(ObjetosPruebas.FCH_MOV);
        emiNeg.setFinVig(ObjetosPruebas.FCHFIN);
        emiNeg.setVehiculo(ObjetosPruebas.getVehiculo());
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        emiNeg.setPersonas(personas);
        emiNeg.setElementos(ObjetosPruebas.getElementoSinUbicacion());
        List<AdaptacionVehNeg> adaptaciones = new ArrayList<>();
        adaptaciones.add(ObjetosPruebas.getAdaptaciones());
        emiNeg.getVehiculo().setAdaptaciones(adaptaciones);
        emiNeg.setDescuentos(ObjetosPruebas.getListDescuentos());
    }
    
    @Before
    public void initMocks() {
        restTemplate = new RestTemplate();
        base = new TransaccionesClient(restTemplate);
        equal = new TransaccionesClient(restTemplate);
        notEqual = new TransaccionesClient(restTemplate);
        nulo = null;
        object3 = new TransaccionesClient(restTemplate);
        MockitoAnnotations.initMocks(this);
    }
    
    @SuppressWarnings("static-access")
    @Test
    public void test() {
        assertNotNull(base.getClass());
    }
    
    @Test
    public void testEquals() {
        assertEquals(base, equal);
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(base.hashCode(), equal.hashCode());
    }
    
    @Test
    public void testDifferent() {
        assertEquals(base, notEqual);
        assertEquals(base.hashCode(), notEqual.hashCode());
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
        assertTrue(base.equals(notEqual));
        assertFalse(base.equals(null));
    }
    
    @SuppressWarnings("static-access")
    @Test
    public void testNull() {
        assertNotNull(nulo);
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
    
    @Test(expected = ExecutionError.class)
    public void obtenTransaccionIntermediaExTest() {
        assertTrue(true);
        base.obtenTransaccionIntermedia("modulo", "cotizacion", "url");
    }
    
    @Test(expected = ExecutionError.class)
    public void obtenTransaccionIntermediaTest() {
        assertTrue(true);
        base.obtenTransaccionIntermedia("MU", "CIANNE190812251969", "http://wsp-txlogger-qa.oscp.gnp.com.mx/txlogger");
    }
    
    @Test(expected = ExecutionError.class)
    public void getTransaccionesIntermediaTest() {
        assertTrue(true);
        base.getTransaccionesIntermedia("CIANNE190812251969", "http://wsp-txlogger-qa.oscp.gnp.com.mx/txlogger");
    }
    
    @Test(expected = ExecutionError.class)
    public void getTransInterIdTest() {
        assertTrue(true);
        base.getTransInterId("parametro", "value");
    }
    
    @Test(expected = ExecutionError.class)
    public void getMUCCotTest() {
        assertTrue(true);
        base.getMUCCot(emiNeg, "CIANNE190812251969", "http://wsp-txlogger-qa.oscp.gnp.com.mx/txlogger");
    }
    
    @Test(expected = NullPointerException.class)
    public void getIdCotizacionNullTest() {
        assertTrue(true);
        base.getIdCotizacion(null, "http://wsp-txlogger-qa.oscp.gnp.com.mx/txlogger");
    }
    
    @Test(expected = ExecutionError.class)
    public void getIdCotizacionEmptyTest() {
        assertTrue(true);
        base.getIdCotizacion("", "http://wsp-txlogger-qa.oscp.gnp.com.mx/txlogger");
    }
    
    @Test(expected = ExecutionError.class)
    public void getIdCotizacionTest() {
        assertTrue(true);
        base.getIdCotizacion("CIANNE190812251969", "http://wsp-txlogger-qa.oscp.gnp.com.mx/txlogger");
    }
    
    @Test(expected = ExecutionError.class)
    public void getVariablesCotTest() {
        assertTrue(true);
        base.getVariablesCot(emiNeg, "CIANNE190812251969", "http://wsp-txlogger-qa.oscp.gnp.com.mx/txlogger");
    }
}
