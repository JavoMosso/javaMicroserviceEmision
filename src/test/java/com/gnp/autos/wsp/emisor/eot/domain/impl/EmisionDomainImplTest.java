package com.gnp.autos.wsp.emisor.eot.domain.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

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

import com.gnp.autos.wsp.emisor.eot.config.ConfWSP;
import com.gnp.autos.wsp.emisor.eot.error.ExecutionError;
import com.gnp.autos.wsp.emisor.eot.soap.service.EmisionEOTClient;
import com.gnp.autos.wsp.emisor.eot.util.ObjetosPruebas;
import com.gnp.autos.wsp.negocio.emision.model.req.TraductorEmitirReq;
import com.gnp.autos.wsp.negocio.neg.model.EmiteNegReq;
import com.gnp.autos.wsp.negocio.neg.model.PersonaNeg;
import com.gnp.autos.wsp.negocio.umoservicemodel.EmisionDatos;
import com.google.gson.Gson;

@RunWith(SpringRunner.class)
public class EmisionDomainImplTest {
    /* String Request. */
    private String strTraductorEmitirReq;
    /* Request. */
    private TraductorEmitirReq traductorReq;
    /* String Request. */
    private String strEmiteNegReq;
    /* Request. */
    private EmiteNegReq consulNegReq;
    @InjectMocks
    private EmisionDomainImpl emisiondomainimpl;
    @Mock
    private ConfWSP conf;
    @Mock
    private EmisionEOTClient client;
    
    @Value("classpath:/TraductorEmitirReqTest.xml")
    public void setFileTraductorEmitirReq(final Resource myRes) throws IOException {
        try (InputStream is = myRes.getInputStream() ) {
            strTraductorEmitirReq = StreamUtils.copyToString(is, StandardCharsets.UTF_8);
        }
    }
    
    @Value("classpath:/EmiteNegReqTest.json")
    public void setFileEmiteNegReq(final Resource myRes) throws IOException {
        try (InputStream is = myRes.getInputStream() ) {
            strEmiteNegReq = StreamUtils.copyToString(is, StandardCharsets.UTF_8);
        }
    }
    
    @Before
    public void prepararTest() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(TraductorEmitirReq.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        traductorReq = (TraductorEmitirReq) jaxbUnmarshaller.unmarshal(new StringReader(strTraductorEmitirReq));
        Gson gson = new Gson();
        consulNegReq = gson.fromJson(strEmiteNegReq, EmiteNegReq.class);
        consulNegReq.setIdCotizacion(ObjetosPruebas.COT_NOW);
        consulNegReq.setIniVig(ObjetosPruebas.FCH_MOV);
        consulNegReq.setFinVig(ObjetosPruebas.FCHFIN);
        /*
         * consulNegReq.setAgentes(ObjetosPruebas.getAgentes());
         * consulNegReq.getAgentes().get(0).setPctCesionComision("5");
         * consulNegReq.getAgentes().get(0).setPctParticipComision("4");
         * consulNegReq.getAgentes().get(0).setPctComisionPrima("3");
         * List<PersonaNeg> personas = new ArrayList<>();
         * PersonaNeg personabase = ObjetosPruebas.getPersona();
         * personabase.setEdad(null);
         * personas.add(personabase);
         * personabase = ObjetosPruebas.getPersona();
         * personabase.setTipo("CONDUCTOR");
         * personas.add(personabase);
         * personabase = ObjetosPruebas.getPersona();
         * personabase.setTipo("BENEFICIARIO");
         * personabase.setPctBeneficio("5");
         * personas.add(personabase);
         * consulNegReq.setPersonas(personas);
         */
        EmisionDatos em = ObjetosPruebas.getEmisionTotal(consulNegReq);
        consulNegReq = em.getEmite();
    }
    
    @Before
    public void initMocks() {
        emisiondomainimpl = new EmisionDomainImpl();
        MockitoAnnotations.initMocks(this);
    }
    
    @Test(expected = NullPointerException.class)
    public void getEmisionTest() {
        when(conf.getUrlEmsion()).thenReturn("http://10.67.16.13:7959/mbr/au/sp/sce/contrato/ContratacionAutos");
        emisiondomainimpl.getEmision(traductorReq, 0);
    }
    
    @Test
    public void obtenerRequestNullTest() {
        assertNull(emisiondomainimpl.obtenerRequest(null));
    }
    
    @Test// (expected = NullPointerException.class)
    public void obtenerRequestSinAgentesTest() {
        consulNegReq.setAgentes(new ArrayList<>());
        assertNotNull(emisiondomainimpl.obtenerRequest(consulNegReq));
    }
    
    @Test// (expected = NullPointerException.class)
    public void obtenerRequestAgenteSecundarioTest() {
        consulNegReq.getAgentes().get(0).setBanIntermediarioPrincipal("0");
        assertNotNull(emisiondomainimpl.obtenerRequest(consulNegReq));
    }
    
    @Test(expected = ExecutionError.class)
    public void obtenerRequestPersInvTest() {
        List<PersonaNeg> personas = consulNegReq.getPersonas();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("OTRO");
        personas.add(personabase);
        consulNegReq.setPersonas(personas);
        emisiondomainimpl.obtenerRequest(consulNegReq);
    }
    
    @Test(expected = ExecutionError.class)
    public void obtenerRequestPersVacTest() {
        List<PersonaNeg> personas = consulNegReq.getPersonas();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("");
        personas.add(personabase);
        consulNegReq.setPersonas(personas);
        emisiondomainimpl.obtenerRequest(consulNegReq);
    }
    
    @Test(expected = NullPointerException.class)
    public void obtenerRequestPersNullTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(null);
        consulNegReq.setPersonas(personas);
        emisiondomainimpl.obtenerRequest(consulNegReq);
    }
    
    @Test// (expected = NullPointerException.class)
    public void obtenerRequestTest() {
        assertNotNull(emisiondomainimpl.obtenerRequest(consulNegReq));
    }
}