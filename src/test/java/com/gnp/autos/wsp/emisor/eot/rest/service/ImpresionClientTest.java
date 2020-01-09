package com.gnp.autos.wsp.emisor.eot.rest.service;

import static org.junit.Assert.assertEquals;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import com.gnp.autos.wsp.emisor.eot.util.ObjetosPruebas;
import com.gnp.autos.wsp.negocio.neg.model.AdaptacionVehNeg;
import com.gnp.autos.wsp.negocio.neg.model.EmiteNegReq;
import com.gnp.autos.wsp.negocio.neg.model.PersonaNeg;
import com.google.gson.Gson;

@RunWith(SpringRunner.class)
public class ImpresionClientTest {
    /* String Request. */
    private String strEmiteNegReq;
    /* Request. */
    private EmiteNegReq objReq;
    /* Clase Test. */
    @InjectMocks
    private static ImpresionClient base;
    @InjectMocks
    private static ImpresionClient equal;
    @InjectMocks
    private static ImpresionClient notEqual;
    @InjectMocks
    private static ImpresionClient nulo;
    @InjectMocks
    private static ImpresionClient object3;
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
        base = new ImpresionClient(restTemplate);
        ReflectionTestUtils.setField(base, "restTemplate", new RestTemplate());
        equal = new ImpresionClient(restTemplate);
        ReflectionTestUtils.setField(equal, "restTemplate", new RestTemplate());
        notEqual = new ImpresionClient(restTemplate);
        ReflectionTestUtils.setField(notEqual, "restTemplate", new RestTemplate());
        nulo = null;
        object3 = new ImpresionClient(restTemplate);
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
    public void testAssert() {
        assertTrue(base.canEqual(base));
        assertTrue(base.equals(base));
        assertTrue(base.canEqual(equal));
        assertTrue(base.equals(equal));
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
