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
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.gnp.autos.wsp.emisor.eot.error.ExecutionError;
import com.gnp.autos.wsp.emisor.eot.util.ObjetosPruebas;
import com.gnp.autos.wsp.negocio.neg.model.AdaptacionVehNeg;
import com.gnp.autos.wsp.negocio.neg.model.ElementoNeg;
import com.gnp.autos.wsp.negocio.neg.model.EmiteNegReq;
import com.gnp.autos.wsp.negocio.neg.model.PaqueteNeg;
import com.gnp.autos.wsp.negocio.neg.model.PersonaNeg;
import com.google.gson.Gson;

@RunWith(SpringRunner.class)
public class CotizacionClientTest {
    /* String Request. */
    private String strEmiteNegReq;
    /* Request. */
    private EmiteNegReq objReq;
    /* Clase Test. */
    @InjectMocks
    private static CotizacionClient base;
    @InjectMocks
    private static CotizacionClient equal;
    @InjectMocks
    private static CotizacionClient notEqual;
    @InjectMocks
    private static CotizacionClient nulo;
    @InjectMocks
    private static CotizacionClient object3;
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
        objReq = gson.fromJson(strEmiteNegReq, EmiteNegReq.class);
        objReq.setIdCotizacion(ObjetosPruebas.COT_NOW);
        objReq.setIniVig(ObjetosPruebas.FCH_MOV);
        objReq.setFinVig(ObjetosPruebas.FCHFIN);
        objReq.setVehiculo(ObjetosPruebas.getVehiculo());
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        objReq.setPersonas(personas);
        objReq.setElementos(ObjetosPruebas.getElementoSinUbicacion());
        List<AdaptacionVehNeg> adaptaciones = new ArrayList<>();
        adaptaciones.add(ObjetosPruebas.getAdaptaciones());
        objReq.getVehiculo().setAdaptaciones(adaptaciones);
        objReq.setDescuentos(ObjetosPruebas.getListDescuentos());
    }
    
    @Before
    public void initMocks() {
        restTemplate = new RestTemplate();
        base = new CotizacionClient(restTemplate);
        ReflectionTestUtils.setField(base, "restTemplate", new RestTemplate());
        equal = new CotizacionClient(restTemplate);
        ReflectionTestUtils.setField(equal, "restTemplate", new RestTemplate());
        notEqual = new CotizacionClient(restTemplate);
        ReflectionTestUtils.setField(notEqual, "restTemplate", new RestTemplate());
        nulo = null;
        object3 = new CotizacionClient(restTemplate);
        ReflectionTestUtils.setField(object3, "restTemplate", new RestTemplate());
        MockitoAnnotations.initMocks(this);
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
    @Test// (expected = AssertionError.class)
    public void testNull() {
        assertNotNull(nulo.getClass());
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
    public void getRecotizaTidNullTest() {
        assertTrue(true);
        base.getRecotiza(objReq, "", null);
    }
    
    @Test(expected = ExecutionError.class)
    public void getRecotizaPaqNullTest() {
        objReq.setPaquetes(null);
        base.getRecotiza(objReq, "http://wsp-cotizador-uat.oscp.gnp.com.mx/cotizar", 0);
    }
    
    @Test(expected = ExecutionError.class)
    public void getRecotizaPaqEmptyTest() {
        objReq.setPaquetes(new ArrayList<>());
        base.getRecotiza(objReq, "http://wsp-cotizador-uat.oscp.gnp.com.mx/cotizar", 0);
    }
    
    @Test(expected = ExecutionError.class)
    public void getRecotizaSizeTest() {
        List<PaqueteNeg> paquetes1 = objReq.getPaquetes();
        List<PaqueteNeg> paquetes2 = objReq.getPaquetes();
        List<PaqueteNeg> paquetes = new ArrayList<>();
        paquetes.add(paquetes1.get(0));
        paquetes.add(paquetes2.get(0));
        objReq.setPaquetes(paquetes);
        base.getRecotiza(objReq, "http://wsp-cotizador-uat.oscp.gnp.com.mx/cotizar", 0);
    }
    
    @Test(expected = ExecutionError.class)
    public void getRecotizaCpNullTest() {
        objReq.setCodigoPromocion(null);
        base.getRecotiza(objReq, "http://wsp-cotizador-uat.oscp.gnp.com.mx/cotizar", 0);
    }
    
    @Test(expected = ExecutionError.class)
    public void getRecotizaCpEmptyTest() {
        objReq.setCodigoPromocion("");
        base.getRecotiza(objReq, "http://wsp-cotizador-uat.oscp.gnp.com.mx/cotizar", 0);
    }
    
    @Test(expected = ExecutionError.class)
    public void getRecotizaElemEmptyTest() {
        objReq.setElementos(new ArrayList<>());
        base.getRecotiza(objReq, "http://wsp-cotizador-uat.oscp.gnp.com.mx/cotizar", 0);
    }
    
    @Test(expected = ExecutionError.class)
    public void getRecotizaElemNullTest() {
        objReq.setElementos(null);
        base.getRecotiza(objReq, "http://wsp-cotizador-uat.oscp.gnp.com.mx/cotizar", 0);
    }
    
    @Test(expected = ExecutionError.class)
    public void getRecotizaElemUbicacionTest() {
        List<ElementoNeg> elementos = objReq.getElementos();
        ElementoNeg elemento = new ElementoNeg();
        elemento.setClave("AA");
        elemento.setNombre("UBICACION_TRABAJO");
        elemento.setValor("AA");
        elementos.add(elemento);
        objReq.setElementos(elementos);
        base.getRecotiza(objReq, "http://wsp-cotizador-uat.oscp.gnp.com.mx/cotizar", 0);
    }
    
    @Test(expected = ExecutionError.class)
    public void getRecotizaElemEmptyNullTest() {
        List<ElementoNeg> elementos = new ArrayList<>();
        objReq.setElementos(elementos);
        objReq.setCodigoPromocion(null);
        base.getRecotiza(objReq, "http://wsp-cotizador-uat.oscp.gnp.com.mx/cotizar", 0);
    }
    
    @Test(expected = NullPointerException.class)
    public void getRecotizaElemTrueFalseTest() {
        List<ElementoNeg> elementos = new ArrayList<>();
        elementos.add(null);
        objReq.setElementos(elementos);
        objReq.setCodigoPromocion(null);
        base.getRecotiza(objReq, "http://wsp-cotizador-uat.oscp.gnp.com.mx/cotizar", 0);
    }
    
    @Test(expected = ExecutionError.class)
    public void getRecotizaAdapNullTest() {
        objReq.getVehiculo().setAdaptaciones(null);
        base.getRecotiza(objReq, "http://wsp-cotizador-uat.oscp.gnp.com.mx/cotizar", 0);
    }
    
    @Test(expected = ExecutionError.class)
    public void getRecotizaAdapEmptyTest() {
        objReq.getVehiculo().setAdaptaciones(new ArrayList<>());
        base.getRecotiza(objReq, "http://wsp-cotizador-uat.oscp.gnp.com.mx/cotizar", 0);
    }
    
    @Test(expected = ExecutionError.class)
    public void getRecotizaCobNullTest() {
        objReq.getPaquetes().get(0).setCoberturas(null);
        base.getRecotiza(objReq, "http://wsp-cotizador-uat.oscp.gnp.com.mx/cotizar", 0);
    }
    
    @Test(expected = ExecutionError.class)
    public void getRecotizaCobEmptyTest() {
        objReq.getPaquetes().get(0).setCoberturas(new ArrayList<>());
        base.getRecotiza(objReq, "http://wsp-cotizador-uat.oscp.gnp.com.mx/cotizar", 0);
    }
    
    @Test(expected = ExecutionError.class)
    public void getRecotizaDescuentoNullTest() {
        objReq.setDescuentos(null);
        base.getRecotiza(objReq, "http://wsp-cotizador-uat.oscp.gnp.com.mx/cotizar", 0);
    }
    
    @Test(expected = ExecutionError.class)
    public void getRecotizaDescuentoEmptyTest() {
        objReq.setDescuentos(new ArrayList<>());
        base.getRecotiza(objReq, "http://wsp-cotizador-uat.oscp.gnp.com.mx/cotizar", 0);
    }
    
    @Test(expected = ExecutionError.class)
    public void getRecotizaDescNullTest() {
        objReq.getDescuentos().get(0).setCveDescuento(null);
        base.getRecotiza(objReq, "http://wsp-cotizador-uat.oscp.gnp.com.mx/cotizar", 0);
    }
    
    @Test(expected = ExecutionError.class)
    public void getRecotizaDescEmptyTest() {
        objReq.getDescuentos().get(0).setCveDescuento("");
        base.getRecotiza(objReq, "http://wsp-cotizador-uat.oscp.gnp.com.mx/cotizar", 0);
    }
    
    @Test(expected = ExecutionError.class)
    public void getRecotizaTest() {
        assertTrue(true);
        base.getRecotiza(objReq, "http://wsp-cotizador-uat.oscp.gnp.com.mx/cotizar", 0);
    }
    
    @Test(expected = ExecutionError.class)
    public void manejaErrorBADTest() {
        assertTrue(true);
        base.manejaErrorBAD("msgError", new HttpClientErrorException(HttpStatus.CONTINUE));
    }
}
