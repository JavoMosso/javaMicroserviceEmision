package com.gnp.autos.wsp.emisor.eot.domain.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.gnp.autos.wsp.emisor.eot.config.ConfWSP;
import com.gnp.autos.wsp.emisor.eot.domain.ProdTecComDomain;
import com.gnp.autos.wsp.emisor.eot.error.ExecutionError;
import com.gnp.autos.wsp.emisor.eot.liquidacion.RegistrarCuentaFinancieraReq;
import com.gnp.autos.wsp.emisor.eot.rest.service.PersonaClient;
import com.gnp.autos.wsp.emisor.eot.soap.service.EmitirEOTClient;
import com.gnp.autos.wsp.emisor.eot.soap.service.ProdTecComClient;
import com.gnp.autos.wsp.emisor.eot.tarificadorcp.RegionTarifDomain;
import com.gnp.autos.wsp.emisor.eot.urlimp.BuscaDocReq;
import com.gnp.autos.wsp.emisor.eot.urlimp.BuscaDocResp;
import com.gnp.autos.wsp.emisor.eot.util.ObjetosPruebas;
import com.gnp.autos.wsp.emisor.eot.wsdl.EmitirResponse;
import com.gnp.autos.wsp.errors.exceptions.WSPSimpleException;
import com.gnp.autos.wsp.negocio.muc.soap.DescuentoDto;
import com.gnp.autos.wsp.negocio.muc.soap.ModificadorCoberturaDto;
import com.gnp.autos.wsp.negocio.muc.soap.TotalPrimaDto;
import com.gnp.autos.wsp.negocio.neg.model.ElementoNeg;
import com.gnp.autos.wsp.negocio.neg.model.EmiteNegReq;
import com.gnp.autos.wsp.negocio.neg.model.EmiteNegResp;
import com.gnp.autos.wsp.negocio.neg.model.MedioCobroNeg;
import com.gnp.autos.wsp.negocio.neg.model.MedioContactoNeg;
import com.gnp.autos.wsp.negocio.neg.model.PaqueteNeg;
import com.gnp.autos.wsp.negocio.neg.model.PersonaNeg;
import com.gnp.autos.wsp.negocio.umoservicemodel.EmisionDatos;
import com.google.gson.Gson;

@RunWith(SpringRunner.class)
public class DomainImplTest {
    /* String Request. */
    private String strEmiteNegReq;
    /* Request. */
    private EmiteNegReq consulNegReq;
    /*
     * String Request. * private String strCalcularPrimaAutoRequest; Request. *
     * private CalcularPrimaAutoRequest mucresp;
     */
    @InjectMocks
    private DomainImpl domainImpl;
    @Mock
    private EmitirEOTClient clientEOT;
    @Mock
    private ProdTecComClient clientProd;
    @Mock
    private ConfWSP conf;
    @Mock
    private ProdTecComDomain prodTec;
    @Mock
    private RegionTarifDomain regTarifClient;
    @Mock
    private RestTemplate restTemplate = new RestTemplate();

    @Value("classpath:/EmiteNegReqTest.json")
    public void setFileEmiteNegReq(final Resource myRes) throws IOException {
        try (InputStream is = myRes.getInputStream()) {
            strEmiteNegReq = StreamUtils.copyToString(is, StandardCharsets.UTF_8);
        }
    }

    @Before
    public void prepararTest() throws JAXBException {
        Gson gson = new Gson();
        consulNegReq = gson.fromJson(strEmiteNegReq, EmiteNegReq.class);
        consulNegReq.setIdCotizacion(ObjetosPruebas.COT_NOW);
        consulNegReq.setIniVig(ObjetosPruebas.FCH_MOV);
        consulNegReq.setFinVig(ObjetosPruebas.FCHFIN);
    }
    /*
     * @Value("classpath:/CalcularPrimaAutoRequestTest.xml") public void
     * setCalcularPrimaAutoRequest(final Resource myRes) throws IOException { try
     * (InputStream is = myRes.getInputStream() ) { strCalcularPrimaAutoRequest =
     * StreamUtils.copyToString(is, StandardCharsets.UTF_8); } }
     * 
     * @Before public void prepararCalcularPrimaAutoRequest() throws JAXBException {
     * JAXBContext jaxbContext =
     * JAXBContext.newInstance(CalcularPrimaAutoRequest.class); Unmarshaller
     * jaxbUnmarshaller = jaxbContext.createUnmarshaller(); mucresp =
     * (CalcularPrimaAutoRequest) jaxbUnmarshaller.unmarshal(new
     * StringReader(strCalcularPrimaAutoRequest)); System.out.println(); }
     */

    @Before
    public void initMocks() {
        domainImpl = new DomainImpl();
        ReflectionTestUtils.setField(domainImpl, "usuarioVp", "xdtpruebas");
        ReflectionTestUtils.setField(domainImpl, "passVp", "pruebas");
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = ExecutionError.class)
    public void getEmitirPortalPersonasNTest() throws DatatypeConfigurationException, ParseException,
            JsonParseException, JsonMappingException, IOException {
        Integer id = 3325223;
        List<PersonaNeg> personas = ObjetosPruebas.getListPersonaInc();
        when(conf.getUrlPersona()).thenReturn("http://wsp-persona-uat.oscpuat.gnp.com.mx/persona");
        String llamado = conf.getUrlPersona() + "/persona?tid=" + id.toString() + "&idCot=" + ObjetosPruebas.COT_NOW
                + "IUYUI";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
        HttpEntity<List<PersonaNeg>> requestEntity = new HttpEntity<>(personas, requestHeaders);
        when(restTemplate.exchange(llamado, HttpMethod.POST, requestEntity,
                new ParameterizedTypeReference<List<PersonaNeg>>() {
                })).thenReturn(ObjetosPruebas.getSrvPersonasNull());
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatosCPers(consulNegReq, personas,
                ObjetosPruebas.COT_NOW + "IUYUI");
        domainImpl.getEmitir(emDatos, id, "A2400LNEVO:00001");
    }

    @Test(expected = ExecutionError.class)
    public void getEmitirPortalPersonasErrTest() throws DatatypeConfigurationException, ParseException,
            JsonParseException, JsonMappingException, IOException {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        Integer id = 3325;
        when(conf.getUrlPersona()).thenReturn("http://wsp-persona-uat.oscpuat.gnp.com.mx/persona");
        String llamado = conf.getUrlPersona() + "/persona?tid=" + id.toString() + "&idCot=" + ObjetosPruebas.COT_NOW;
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
        HttpEntity<List<PersonaNeg>> requestEntity = new HttpEntity<>(personas, requestHeaders);
        when(restTemplate.exchange(llamado, HttpMethod.POST, requestEntity,
                new ParameterizedTypeReference<List<PersonaNeg>>() {
                })).thenReturn(ObjetosPruebas.getSrvPersonas());
        PersonaNeg personaneg = new PersonaNeg();
        personas = new ArrayList<>();
        personas.add(personaneg);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        domainImpl.getEmitir(emDatos, id, "A2400LNEVO:00001");
    }

    @Test(expected = NullPointerException.class)
    public void getEmitirPortalTidNullTest() throws DatatypeConfigurationException, ParseException, JsonParseException,
            JsonMappingException, IOException {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        Integer id = null;
        when(conf.getUrlPersona()).thenReturn("http://wsp-persona-uat.oscpuat.gnp.com.mx/persona");
        String llamado = conf.getUrlPersona() + "/persona?idCot=" + ObjetosPruebas.COT_NOW;
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
        HttpEntity<List<PersonaNeg>> requestEntity = new HttpEntity<>(personas, requestHeaders);
        when(restTemplate.exchange(llamado, HttpMethod.POST, requestEntity,
                new ParameterizedTypeReference<List<PersonaNeg>>() {
                })).thenReturn(ObjetosPruebas.getSrvPersonas());
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        domainImpl.getEmitir(emDatos, id, "A2400LNEVO");
    }

    @Test(expected = ExecutionError.class)
    public void getEmitirPortalTidRestTest() throws DatatypeConfigurationException, ParseException, JsonParseException,
            JsonMappingException, IOException {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        Integer id = 3325;
        when(conf.getUrlPersona()).thenReturn("http://wsp-persona-uat.oscpuat.gnp.com.mx/persona");
        String llamado = conf.getUrlPersona() + "/persona?tid=" + id.toString() + "&idCot=" + ObjetosPruebas.COT_NOW;
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
        HttpEntity<List<PersonaNeg>> requestEntity = new HttpEntity<>(personas, requestHeaders);
        when(restTemplate.exchange(llamado, HttpMethod.POST, requestEntity,
                new ParameterizedTypeReference<List<PersonaNeg>>() {
                })).thenReturn(ObjetosPruebas.getSrvPersonasNoContect());
        PersonaNeg personaneg = new PersonaNeg();
        personas = new ArrayList<>();
        personas.add(personaneg);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        domainImpl.getEmitir(emDatos, id, "A2400LNEVO");
    }

    @Test(expected = ExecutionError.class)
    public void getEmitirPortalPersonasNullTest() throws DatatypeConfigurationException, ParseException,
            JsonParseException, JsonMappingException, IOException {
        List<PersonaNeg> personas = null;
        Integer id = 0;
        when(conf.getUrlPersona()).thenReturn("http://wsp-persona-uat.oscpuat.gnp.com.mx/persona");
        String llamado = conf.getUrlPersona() + "/persona?tid=" + id.toString() + "&idCot=" + ObjetosPruebas.COT_NOW;
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
        HttpEntity<List<PersonaNeg>> requestEntity = new HttpEntity<>(personas, requestHeaders);
        when(restTemplate.exchange(llamado, HttpMethod.POST, requestEntity,
                new ParameterizedTypeReference<List<PersonaNeg>>() {
                })).thenReturn(ObjetosPruebas.getSrvPersonasNull());
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        domainImpl.getEmitir(emDatos, id, "A2400LNEVO");
    }

    @Test(expected = NullPointerException.class)
    public void getEmitirPortalExcTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        Integer id = 3325;
        when(conf.getUrlPersona()).thenReturn("http://wsp-persona-uat.oscpuat.gnp.com.mx/persona");
        String llamado = conf.getUrlPersona() + "/persona?tid=" + id.toString() + "&idCot=" + ObjetosPruebas.COT_NOW;
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
        HttpEntity<List<PersonaNeg>> requestEntity = new HttpEntity<>(personas, requestHeaders);
        when(restTemplate.exchange(llamado, HttpMethod.POST, requestEntity,
                new ParameterizedTypeReference<List<PersonaNeg>>() {
                })).thenReturn(ObjetosPruebas.getSrvPersonas());
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        // emDatos.getEmite().getMucPrimaAutoReq().getDATOSCOTIZACION().get(0).getDATOSPRODUCTO().get(0).getCOBERTURA();
        domainImpl.getEmitir(emDatos, id, "A2400LNEVO");
    }

    @Test(expected = ExecutionError.class)
    public void getEmitirPortalPersIncomTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersonaErr();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        Integer id = 3325;
        when(conf.getUrlPersona()).thenReturn("http://wsp-persona-uat.oscpuat.gnp.com.mx/persona");
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        domainImpl.getEmitir(emDatos, id, "A2400LNEVO");
    }

    @Test(expected = ExecutionError.class)
    public void getEmitirPortalUrlErrTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersonaErr();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        Integer id = 3325;
        when(conf.getUrlPersona()).thenReturn("http://wsp-persona-uat.oscp.mx/persona");
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        domainImpl.getEmitir(emDatos, id, "A2400LNEVO");
    }

    @Test(expected = ExecutionError.class)
    public void getEmitirPortalSinDescuentosTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        Integer id = 3325;
        when(conf.getUrlPersona()).thenReturn("http://wsp-persona-uat.oscpuat.gnp.com.mx/persona");
        String llamado = conf.getUrlPersona() + "/persona?tid=" + id.toString() + "&idCot=" + ObjetosPruebas.COT_NOW;
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
        HttpEntity<List<PersonaNeg>> requestEntity = new HttpEntity<>(personas, requestHeaders);
        when(restTemplate.exchange(llamado, HttpMethod.POST, requestEntity,
                new ParameterizedTypeReference<List<PersonaNeg>>() {
                })).thenReturn(ObjetosPruebas.getSrvPersonas());
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplementoSinDescuentos(emDatos);
        domainImpl.getEmitir(emDatos, id, "A2400LNEVO");
    }

    @Test(expected = ExecutionError.class)
    public void getEmitirPortalErrValidaReglasTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        Integer id = 3325;
        when(conf.getUrlPersona()).thenReturn("http://wsp-persona-uat.oscpuat.gnp.com.mx/persona");
        when(conf.getUrlValidas())
                .thenReturn("http://wsp-validareglas-uat.oscp.gnp.com.mx/validareglas/validareglas/json");
        String llamado = conf.getUrlPersona() + "/persona?tid=" + id.toString() + "&idCot=" + ObjetosPruebas.COT_NOW;
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
        HttpEntity<List<PersonaNeg>> requestEntity = new HttpEntity<>(personas, requestHeaders);
        when(restTemplate.exchange(llamado, HttpMethod.POST, requestEntity,
                new ParameterizedTypeReference<List<PersonaNeg>>() {
                })).thenReturn(ObjetosPruebas.getSrvPersonas());
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        domainImpl.getEmitir(emDatos, id, "A2400LNEVO");
    }

    @Test(expected = ExecutionError.class)
    public void getEmitirPortalSinTidTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        Integer id = 3325;
        when(conf.getUrlPersona()).thenReturn("http://wsp-persona-uat.oscpuat.gnp.com.mx/persona");
        when(conf.getUrlValidas())
                .thenReturn("http://wsp-validareglas-uat.oscp.gnp.com.mx/validareglas/validareglas/json");
        String llamadoPersonas = conf.getUrlPersona() + "/persona?tid=" + id.toString() + "&idCot="
                + ObjetosPruebas.COT_NOW;
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
        HttpEntity<List<PersonaNeg>> requestEntity = new HttpEntity<>(personas, requestHeaders);
        when(restTemplate.exchange(llamadoPersonas, HttpMethod.POST, requestEntity,
                new ParameterizedTypeReference<List<PersonaNeg>>() {
                })).thenReturn(ObjetosPruebas.getSrvPersonas());
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        domainImpl.getEmitir(emDatos, null, "A2400LNEVO");
    }

    @Test
    public void complementaBanderasNARTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        EmiteNegReq objReq = emDatos.getEmite();
        objReq.getBanAutoResp().getPoliza().getPoliza().getObjetoAsegurado().getValue().getVehiculo()
                .setBANALTORIESGO(false);
        domainImpl.complementaBanderas(objReq);
        assertNotNull(objReq);
    }

    @Test
    public void complementaBanderasSCPRespTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        EmiteNegReq objReq = emDatos.getEmite();
        objReq.getBanAutoResp().getPoliza().getPoliza().getObjetoAsegurado().getValue().getCoberturasBasicas().get(0)
                .setClave("0000001916");
        domainImpl.complementaBanderas(objReq);
        assertNotNull(objReq);
    }

    @Test
    public void complementaBanderasSCPReqTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        EmiteNegReq objReq = emDatos.getEmite();
        objReq.getMucPrimaAutoReq().getDATOSCOTIZACION().get(0).getDATOSPRODUCTO().get(0).getCOBERTURA().get(0)
                .setCLAVECOBERTURA("0000001916");
        objReq.getMucPrimaAutoReq().getDATOSCOTIZACION().get(0).getDATOSPRODUCTO().get(0).getCOBERTURA().get(1)
                .setCLAVECOBERTURA("0000002916");
        domainImpl.complementaBanderas(objReq);
        assertNotNull(objReq);
    }

    @Test
    public void complementaBanderasTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        EmiteNegReq objReq = emDatos.getEmite();
        domainImpl.complementaBanderas(objReq);
        assertNotNull(objReq);
    }

    @Test
    public void imprimeURLSinBanderaTest() {
        when(conf.getUrlImpresion()).thenReturn("http://wsp-impresion-uat.oscp.gnp.com.mx/impresion");
        EmiteNegResp emResponse = ObjetosPruebas.getEmiteNegResp();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        EmiteNegReq objReq = emDatos.getEmite();
        Integer id = 3325;
        objReq.setBanImpresion("0");
        domainImpl.imprimeURL(emResponse, objReq, id);
        assertNotNull(objReq);
    }

    @Test
    public void imprimeURLConTidTest() {
        when(conf.getUrlImpresion()).thenReturn("http://wsp-impresion-uat.oscp.gnp.com.mx/impresion");
        EmiteNegResp emResponse = ObjetosPruebas.getEmiteNegResp();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        EmiteNegReq objReq = emDatos.getEmite();
        Integer id = 3325;
        domainImpl.imprimeURL(emResponse, objReq, id);
        assertNotNull(objReq);
    }

    @Test
    public void imprimeURLSinTidTest() {
        when(conf.getUrlImpresion()).thenReturn("http://wsp-impresion-uat.oscp.gnp.com.mx/impresion");
        EmiteNegResp emResponse = ObjetosPruebas.getEmiteNegResp();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        EmiteNegReq objReq = emDatos.getEmite();
        domainImpl.imprimeURL(emResponse, objReq, null);
        assertNotNull(objReq);
    }

    @Test
    public void imprimeURLExcimpCltTest() {
        when(conf.getUrlImpresion()).thenReturn("http://wsp-impresion-uat.oscp.gnp.com.mx/impresionsdfasdf");
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        EmiteNegReq objReq = emDatos.getEmite();
        domainImpl.imprimeURL(null, objReq, null);
        assertNotNull(objReq);
    }

    @Test
    public void imprimeURLTest() {
        when(conf.getUrlImpresion()).thenReturn("http://wsp-impresion-uat.oscp.gnp.com.mx/impresion");
        EmiteNegResp emResponse = ObjetosPruebas.getEmiteNegResp();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        EmiteNegReq objReq = emDatos.getEmite();
        Integer tid = 213753;
        emResponse.setNumPoliza("00000376085213");
        String llamado = conf.getUrlImpresion() + "/impresion/buscarDoc" + "?tid=" + tid.toString();
        BuscaDocReq objBImp = new BuscaDocReq();
        objBImp.setNumPoliza(emResponse.getNumPoliza());
        when(restTemplate.postForEntity(llamado, objBImp, BuscaDocResp.class))
                .thenReturn(ObjetosPruebas.getImpresion());
        domainImpl.imprimeURL(emResponse, objReq, tid);
        assertNotNull(objReq);
    }

    @Test
    public void validaPagoTVCambiaPagoTrueTest() {
        when(conf.getValidaPago()).thenReturn("1");
        when(conf.getCambiaPago()).thenReturn("1");
        domainImpl.validaPagoTV("1", ObjetosPruebas.COT_NOW, "23452345234", "xdtpruebas", "pruebas");
        assertNotNull(ObjetosPruebas.COT_NOW);
    }

    @Test
    public void validaPagoTVCPVPCeroTrueTest() {
        when(conf.getValidaPago()).thenReturn("0");
        when(conf.getCambiaPago()).thenReturn("0");
        domainImpl.validaPagoTV("1", ObjetosPruebas.COT_NOW, "23452345234", "xdtpruebas", "pruebas");
        assertNotNull(ObjetosPruebas.COT_NOW);
    }

    @Test(expected = ExecutionError.class)
    public void validaPagoTVCambiaPagoFalseTest() {
        when(conf.getValidaPago()).thenReturn("1");
        when(conf.getCambiaPago()).thenReturn("0");
        domainImpl.validaPagoTV("1", ObjetosPruebas.COT_NOW, "23452345234", "xdtpruebas", "pruebas");
        assertNotNull(ObjetosPruebas.COT_NOW);
    }

    @Test(expected = ExecutionError.class)
    public void validaPagoTVTest() {
        when(conf.getValidaPago()).thenReturn("1");
        when(conf.getCambiaPago()).thenReturn("0");
        when(conf.getUrlWSPagoTV()).thenReturn("http://pruebas.gnpventamasiva.com.mx/wspagotv/wspagotv");
        domainImpl.validaPagoTV("1", ObjetosPruebas.COT_NOW, "23452345234", "xdtpruebas", "pruebas");
        assertNotNull(ObjetosPruebas.COT_NOW);
    }

    @Test
    public void regCuentaFinancieraBCTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        EmiteNegReq objReq = emDatos.getEmite();
        Integer tid = 3325;
        objReq.setViaPago("IN");
        domainImpl.regCuentaFinanciera(objReq, tid);
        assertNotNull(objReq);
    }

    @Test
    public void regCuentaFinancieraMCnullTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplementoSinMC(emDatos);
        EmiteNegReq objReq = emDatos.getEmite();
        Integer tid = 3325;
        objReq.setViaPago("BN");
        domainImpl.regCuentaFinanciera(objReq, tid);
        assertNotNull(objReq);
    }

    @Test
    public void regCuentaFinancieraCLTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplementoSinMC(emDatos);
        EmiteNegReq objReq = emDatos.getEmite();
        Integer tid = 3325;
        objReq.setViaPago("CL");
        domainImpl.regCuentaFinanciera(objReq, tid);
        assertNotNull(objReq);
    }

    @Test
    public void regCuentaFinancieraBC2Test() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplementoSinMC(emDatos);
        EmiteNegReq objReq = emDatos.getEmite();
        Integer tid = 3325;
        objReq.setViaPago("BC");
        domainImpl.regCuentaFinanciera(objReq, tid);
        assertNotNull(objReq);
    }

    @Test
    public void regCuentaFinancieraPersonasTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplementoSinMC(emDatos);
        EmiteNegReq objReq = emDatos.getEmite();
        Integer tid = 3325;
        objReq.setViaPago("BC");
        objReq.getPersonas().get(0).setTipo("CONDUCTOR");
        domainImpl.regCuentaFinanciera(objReq, tid);
        assertNotNull(objReq);
    }

    @Test
    public void regCuentaFinancieraMCNNTest() {
        when(conf.getValidaPago()).thenReturn("1");
        when(conf.getCambiaPago()).thenReturn("1");
        when(conf.getUrlLiquidacion()).thenReturn("http://wsp-liquidacion-uat.oscp.gnp.com.mx");
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplementoSinMC(emDatos);
        EmiteNegReq objReq = emDatos.getEmite();
        Integer tid = 3325;
        objReq.setViaPago("BC");
        MedioCobroNeg medioCobro = new MedioCobroNeg();
        medioCobro.setFchVencimiento("20501231");
        medioCobro.setNumCobro("23452345234");
        medioCobro.setCveTipoCuentaTarjeta("dfgdsfg354");
        medioCobro.setNumTarjeta("3045125863471500");
        objReq.getPersonas().get(0).setMedioCobro(medioCobro);
        domainImpl.regCuentaFinanciera(objReq, tid);
        assertNotNull(objReq);
    }

    @Test
    public void regCuentaFinancieraTPJTest() {
        when(conf.getValidaPago()).thenReturn("1");
        when(conf.getCambiaPago()).thenReturn("1");
        when(conf.getUrlLiquidacion()).thenReturn("http://wsp-liquidacion-uat.oscp.gnp.com.mx");
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplementoSinMC(emDatos);
        EmiteNegReq objReq = emDatos.getEmite();
        Integer tid = 3325;
        objReq.setViaPago("BC");
        MedioCobroNeg medioCobro = new MedioCobroNeg();
        medioCobro.setFchVencimiento("20501231");
        medioCobro.setNumCobro("23452345234");
        medioCobro.setCveTipoCuentaTarjeta("dfgdsfg354");
        medioCobro.setNumTarjeta("3045125863471500");
        objReq.getPersonas().get(0).setMedioCobro(medioCobro);
        objReq.getPersonas().get(0).setTipoPersona("J");
        domainImpl.regCuentaFinanciera(objReq, tid);
        assertNotNull(objReq);
    }

    @Test
    public void regCuentaFinancieraTPMTest() {
        when(conf.getValidaPago()).thenReturn("1");
        when(conf.getCambiaPago()).thenReturn("1");
        when(conf.getUrlLiquidacion()).thenReturn("http://wsp-liquidacion-uat.oscp.gnp.com.mx");
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplementoSinMC(emDatos);
        EmiteNegReq objReq = emDatos.getEmite();
        Integer tid = 3325;
        objReq.setViaPago("BC");
        MedioCobroNeg medioCobro = new MedioCobroNeg();
        medioCobro.setFchVencimiento("20501231");
        medioCobro.setNumCobro("23452345234");
        medioCobro.setCveTipoCuentaTarjeta("dfgdsfg354");
        medioCobro.setNumTarjeta("3045125863471500");
        objReq.getPersonas().get(0).setMedioCobro(medioCobro);
        objReq.getPersonas().get(0).setTipoPersona("M");
        domainImpl.regCuentaFinanciera(objReq, tid);
        assertNotNull(objReq);
    }

    @SuppressWarnings("static-access")
    @Test
    public void setTitularTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        EmiteNegReq objReq = emDatos.getEmite();
        Optional<PersonaNeg> contNeg = objReq.getPersonas().stream()
                .filter(p -> p.getTipo().equalsIgnoreCase("CONTRATANTE")).findFirst();
        String cveTipoCuentaTarjeta = contNeg.get().getMedioCobro().getCveTipoCuentaTarjeta();
        String cveTipoDatoBancario = contNeg.get().getMedioCobro().getCveTipoBancario();
        RegistrarCuentaFinancieraReq cFReq = new RegistrarCuentaFinancieraReq();
        cFReq.setBanCtaPrincipal(false);
        cFReq.setCodFiliacion(contNeg.get().getCodigoCliente());
        cFReq.setCveEntidadFinanciera(contNeg.get().getMedioCobro().getCveEntidadFinanciera());
        cFReq.setCveMoneda(objReq.getCveMoneda());
        cFReq.setCveTipoCuentaTarjeta(cveTipoCuentaTarjeta);
        cFReq.setCveTipoDatoBancario(cveTipoDatoBancario);
        cFReq.setDiaCobroPreferido((short) 0);
        cFReq.setFchVencimiento(contNeg.get().getMedioCobro().getFchVencimiento());
        cFReq.setIdParticipante(contNeg.get().getIdParticipante());
        cFReq.setIdTransaccion(objReq.getIdCotizacion());
        cFReq.setNumCuenta("12358479615453256");
        domainImpl.setTitular(cFReq, contNeg);
        assertNotNull(objReq);
    }

    @Test
    public void liquidaReciboErrTipoEmisionTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplementoSinMC(emDatos);
        EmiteNegReq objReq = emDatos.getEmite();
        Integer tid = 3325;
        EmiteNegResp emResponse = ObjetosPruebas.getEmiteNegResp();
        EmitirResponse objResp = ObjetosPruebas.getEmitirResponse();
        objResp.getPoliza().setINDTIPOEMISION("A");
        domainImpl.liquidaRecibo(objReq, emResponse, objResp, tid);
        assertNotNull(objReq);
    }

    @Test
    public void liquidaReciboCambiaPagoFalseTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        EmiteNegReq objReq = emDatos.getEmite();
        Integer tid = 3325;
        EmiteNegResp emResponse = ObjetosPruebas.getEmiteNegResp();
        EmitirResponse objResp = ObjetosPruebas.getEmitirResponse();
        objReq.setViaPago("CL");
        when(conf.getLiquidaOrigenDefault()).thenReturn("VM");
        when(conf.getCambiaPago()).thenReturn("NOVENTADIR:OTRO");
        domainImpl.liquidaRecibo(objReq, emResponse, objResp, tid);
        assertNotNull(objReq);
    }

    @Test
    public void liquidaReciboTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        EmiteNegReq objReq = emDatos.getEmite();
        Integer tid = 3325;
        EmiteNegResp emResponse = ObjetosPruebas.getEmiteNegResp();
        EmitirResponse objResp = ObjetosPruebas.getEmitirResponse();
        objReq.setViaPago("CL");
        when(conf.getLiquidaOrigenDefault()).thenReturn("VM");
        when(conf.getCambiaPago()).thenReturn("NOVENTADIR:OTRO");
        when(conf.getCabIdPerfil()).thenReturn("Intermediario");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlLiquidacion()).thenReturn("http://wsp-liquidacion-uat.oscp.gnp.com.mx");
        objReq.setIdUMO("NOVENTADIR");
        domainImpl.liquidaRecibo(objReq, emResponse, objResp, tid);
        assertNotNull(objReq);
    }

    @Test
    public void liquidaReciboLCLTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        EmiteNegReq objReq = emDatos.getEmite();
        Integer tid = 3325;
        EmiteNegResp emResponse = ObjetosPruebas.getEmiteNegResp();
        EmitirResponse objResp = ObjetosPruebas.getEmitirResponse();
        objReq.setViaPago("CL");
        when(conf.getLiquidaOrigenDefault()).thenReturn("VM");
        when(conf.getCambiaPago()).thenReturn("NOVENTADIR:OTRO");
        when(conf.getCabIdPerfil()).thenReturn("Intermediario");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlLiquidacion()).thenReturn("http://wsp-liquidacion-uat.oscp.gnp.com.mx");
        objReq.setIdUMO("NOVENTADIR");
        objResp.getPoliza().setINDTIPOEMISION("L");
        domainImpl.liquidaRecibo(objReq, emResponse, objResp, tid);
        assertNotNull(objReq);
    }

    @Test
    public void liquidaReciboLTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        EmiteNegReq objReq = emDatos.getEmite();
        Integer tid = 3325;
        EmiteNegResp emResponse = ObjetosPruebas.getEmiteNegResp();
        EmitirResponse objResp = ObjetosPruebas.getEmitirResponse();
        objReq.setViaPago("CA");
        when(conf.getLiquidaOrigenDefault()).thenReturn("VM");
        when(conf.getCambiaPago()).thenReturn("NOVENTADIR:OTRO");
        when(conf.getCabIdPerfil()).thenReturn("Intermediario");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlLiquidacion()).thenReturn("http://wsp-liquidacion-uat.oscp.gnp.com.mx");
        objReq.setIdUMO("NOVENTADIR");
        objResp.getPoliza().setINDTIPOEMISION("L");
        domainImpl.liquidaRecibo(objReq, emResponse, objResp, tid);
        assertNotNull(objReq);
    }

    @Test
    public void liquidaReciboCLTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        EmiteNegReq objReq = emDatos.getEmite();
        Integer tid = 3325;
        EmiteNegResp emResponse = ObjetosPruebas.getEmiteNegResp();
        EmitirResponse objResp = ObjetosPruebas.getEmitirResponse();
        objReq.setViaPago("CL");
        when(conf.getLiquidaOrigenDefault()).thenReturn("VM");
        when(conf.getCambiaPago()).thenReturn("NOVENTADIR:OTRO");
        when(conf.getCabIdPerfil()).thenReturn("Intermediario");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlLiquidacion()).thenReturn("http://wsp-liquidacion-uat.oscp.gnp.com.mx");
        objReq.setIdUMO("NOVENTADIR");
        objResp.getPoliza().setINDTIPOEMISION("A");
        domainImpl.liquidaRecibo(objReq, emResponse, objResp, tid);
        assertNotNull(objReq);
    }

    @Test
    public void liquidaReciboNoLCLTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        EmiteNegReq objReq = emDatos.getEmite();
        Integer tid = 3325;
        EmiteNegResp emResponse = ObjetosPruebas.getEmiteNegResp();
        EmitirResponse objResp = ObjetosPruebas.getEmitirResponse();
        objReq.setViaPago("CN");
        when(conf.getLiquidaOrigenDefault()).thenReturn("VM");
        when(conf.getCambiaPago()).thenReturn("NOVENTADIR:OTRO");
        when(conf.getCabIdPerfil()).thenReturn("Intermediario");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlLiquidacion()).thenReturn("http://wsp-liquidacion-uat.oscp.gnp.com.mx");
        objReq.setIdUMO("NOVENTADIR");
        objResp.getPoliza().setINDTIPOEMISION("A");
        domainImpl.liquidaRecibo(objReq, emResponse, objResp, tid);
        assertNotNull(objReq);
    }

    @Test
    public void liquidaReciboBeneficiarioTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        EmiteNegReq objReq = emDatos.getEmite();
        Integer tid = 3325;
        EmiteNegResp emResponse = ObjetosPruebas.getEmiteNegResp();
        EmitirResponse objResp = ObjetosPruebas.getEmitirResponse();
        objReq.setViaPago("CL");
        when(conf.getLiquidaOrigenDefault()).thenReturn("VM");
        when(conf.getCambiaPago()).thenReturn("NOVENTADIR:OTRO");
        when(conf.getCabIdPerfil()).thenReturn("Intermediario");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlLiquidacion()).thenReturn("http://wsp-liquidacion-uat.oscp.gnp.com.mx");
        objReq.setIdUMO("NOVENTADIR");
        objResp.getPoliza().setINDTIPOEMISION("L");
        objReq.getPersonas().get(0).setTipo("BENEFICIARIO");
        domainImpl.liquidaRecibo(objReq, emResponse, objResp, tid);
        assertNotNull(objReq);
    }

    @Test
    public void liquidaReciboNumTarjDTTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        EmiteNegReq objReq = emDatos.getEmite();
        Integer tid = 3325;
        EmiteNegResp emResponse = ObjetosPruebas.getEmiteNegResp();
        EmitirResponse objResp = ObjetosPruebas.getEmitirResponse();
        objReq.setViaPago("CL");
        when(conf.getLiquidaOrigenDefault()).thenReturn("VM");
        when(conf.getCambiaPago()).thenReturn("NOVENTADIR:OTRO");
        when(conf.getCabIdPerfil()).thenReturn("Intermediario");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlLiquidacion()).thenReturn("http://wsp-liquidacion-uat.oscp.gnp.com.mx");
        objReq.setIdUMO("NOVENTADIR");
        objResp.getPoliza().setINDTIPOEMISION("L");
        objReq.getPersonas().get(0).getMedioCobro().setNumTarjeta("1045125863471500");
        domainImpl.liquidaRecibo(objReq, emResponse, objResp, tid);
        assertNotNull(objReq);
    }

    @Test
    public void liquidaReciboExTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        EmiteNegReq objReq = emDatos.getEmite();
        Integer tid = 3325;
        EmiteNegResp emResponse = ObjetosPruebas.getEmiteNegResp();
        EmitirResponse objResp = ObjetosPruebas.getEmitirResponse();
        objReq.setViaPago("CL");
        when(conf.getLiquidaOrigenDefault()).thenReturn("VM");
        when(conf.getCambiaPago()).thenReturn("NOVENTADIR:OTRO");
        when(conf.getCabIdPerfil()).thenReturn("Intermediario");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlLiquidacion()).thenReturn("http://wsp-liquidacion-uat.oscp.gnp.com.mx");
        objReq.setIdUMO("NOVENTADIR");
        objResp.getPoliza().setINDTIPOEMISION("L");
        objReq.getPersonas().get(0).getMedioCobro().setNumTarjeta("1045125863471500");
        objReq.setPersonas(null);
        domainImpl.liquidaRecibo(objReq, emResponse, objResp, tid);
        assertNotNull(objReq);
    }

    @SuppressWarnings("static-access")
    @Test
    public void getRespNegTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        EmiteNegReq objReq = emDatos.getEmite();
        EmitirResponse objResp = ObjetosPruebas.getEmitirResponse();
        domainImpl.getRespNeg(objResp, objReq);
        assertNotNull(objReq);
    }

    @Test(expected = NullPointerException.class)
    public void obtenRequestSinTipoNominaTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplementoSinTipoNomina(emDatos);
        String prodTecCom = "A2400LNEVO";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        domainImpl.obtenRequest(emDatos, prodTecCom);
    }

    @Test(expected = NullPointerException.class)
    public void obtenRequestSinUbicacionTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplementoSinUbicacion(emDatos);
        String prodTecCom = "A2400LNEVO";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        domainImpl.obtenRequest(emDatos, prodTecCom);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void obtenRequestVigenciaIrregularTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos.getEmite().setBanAjusteIrregular("1");
        String prodTecCom = "A2400LNEVO";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        domainImpl.obtenRequest(emDatos, prodTecCom);
    }

    @Test(expected = ExecutionError.class)
    public void obtenRequestPaqueteNullTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        String prodTecCom = "A2400LNEVO";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        emDatos.getEmite().setPaquetes(null);
        List<PaqueteNeg> paq = new ArrayList<>();
        emDatos.getEmite().setPaquetes(paq);
        domainImpl.obtenRequest(emDatos, prodTecCom);
    }

    @Test(expected = ExecutionError.class)
    public void obtenRequestSinElemTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        domainImpl.obtenRequest(emDatos, prodTecCom);
    }

    @Test(expected = ExecutionError.class)
    public void obtenRequestSinContratanteTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        emDatos.getEmite().getPersonas().get(0).setTipo("BENEFICIARIO");
        domainImpl.obtenRequest(emDatos, prodTecCom);
    }

    @Test(expected = ExecutionError.class)
    public void obtenRequestRefExtTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        List<ElementoNeg> elementos = emDatos.getEmite().getElementos();
        ElementoNeg elemento = new ElementoNeg();
        elemento.setClave("refext");
        elemento.setNombre("REFERENCIA_EXTERNA");
        elemento.setValor("refext");
        elementos.add(elemento);
        emDatos.getEmite().setElementos(elementos);
        domainImpl.obtenRequest(emDatos, prodTecCom);
    }

    @SuppressWarnings("static-access")
    @Test
    public void obtenRequestRefExtElemTest() {
        domainImpl.getElementoValor(null, "REFERENCIA_EXTERNA");
        assertNotNull(domainImpl);
    }

    @Test(expected = ExecutionError.class)
    public void obtenRequestSMCTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        emDatos.getEmite().getPersonas().get(0).setMediosContacto(null);
        ElementoNeg elemento = new ElementoNeg();
        elemento.setClave("55");
        elemento.setNombre("CVE_LADA");
        elemento.setValor("55");
        List<ElementoNeg> elementos = new ArrayList<>();
        elementos.add(elemento);
        elemento = new ElementoNeg();
        elemento.setClave("55");
        elemento.setNombre("CVE_LADA_NACIONAL");
        elemento.setValor("55");
        elementos.add(elemento);
        elemento = new ElementoNeg();
        elemento.setClave("54238525");
        elemento.setNombre("NUMERO_TELEFONO");
        elemento.setValor("54238525");
        elementos.add(elemento);
        MedioContactoNeg medio = new MedioContactoNeg();
        medio.setElementos(elementos);
        List<MedioContactoNeg> mediosContacto = new ArrayList<>();
        mediosContacto.add(medio);
        emDatos.getEmite().getPersonas().get(0).setMediosContacto(mediosContacto);
        domainImpl.obtenRequest(emDatos, prodTecCom);
    }

    @Test(expected = ExecutionError.class)
    public void obtenRequestPMTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        emDatos.getEmite().getPersonas().get(0).setTipoPersona("M");
        domainImpl.obtenRequest(emDatos, prodTecCom);
    }

    @Test(expected = ExecutionError.class)
    public void obtenRequestSinContrtTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        //
        domainImpl.obtenRequest(emDatos, prodTecCom);
    }

    @Test(expected = NumberFormatException.class)
    public void obtenRequestBanUTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanU(emDatos);
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        domainImpl.obtenRequest(emDatos, prodTecCom);
    }

    @Test(expected = NullPointerException.class)
    public void obtenRequestBanCTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        domainImpl.obtenRequest(emDatos, prodTecCom);
    }

    @Test(expected = NullPointerException.class)
    public void obtenRequestPctBenNullTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanCNull(emDatos);
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        domainImpl.obtenRequest(emDatos, prodTecCom);
    }

    @Test(expected = NullPointerException.class)
    public void obtenRequestSinAgentesTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        domainImpl.obtenRequest(emDatos, prodTecCom);
    }

    @Test(expected = NullPointerException.class)
    public void obtenRequestBanIntSecTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentesBanInterSec(emDatos);
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        domainImpl.obtenRequest(emDatos, prodTecCom);
    }

    @Test(expected = NullPointerException.class)
    public void obtenRequestInterSecTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentesBanInterSec(emDatos);
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        domainImpl.obtenRequest(emDatos, prodTecCom);
    }

    @Test(expected = ExecutionError.class)
    public void obtenRequestInterNullTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentesBanNull(emDatos);
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        domainImpl.obtenRequest(emDatos, prodTecCom);
    }

    @Test(expected = ExecutionError.class)
    public void obtenRequestSinPaqRespTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().getMucPrimaAutoResp().getPETICION().get(0).getDATOSPRODUCTOS().get(0)
                .setIDPRODUCTO("PRP0000287");
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        domainImpl.obtenRequest(emDatos, prodTecCom);
    }

    @Test(expected = ExecutionError.class)
    public void obtenRequestSinPaqReqTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().getMucPrimaAutoReq().getDATOSCOTIZACION().get(0).getDATOSPRODUCTO().get(0)
                .setIDPRODUCTO("PRP0000287");
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        domainImpl.obtenRequest(emDatos, prodTecCom);
    }

    @Test(expected = NullPointerException.class)
    public void obtenRequestMucRespCobEmpTest() {
        // getCotizacionMucResp
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().getMucPrimaAutoResp().getPETICION().get(0).getDATOSPRODUCTOS().get(0).getDATOSCOTIZACION()
                .get(0).getCOBERTURAPRIMA().add(0, null);
        /*
         * emDatos.getEmite().getMucPrimaAutoResp().getPETICION().get(0).
         * getDATOSPRODUCTOS().get(0).getDATOSCOTIZACION().get(0).getCOBERTURAPRIMA()
         * .add(1, null); emDatos.getEmite().getMucPrimaAutoResp().getPETICION().get(0).
         * getDATOSPRODUCTOS().get(0).getDATOSCOTIZACION().get(0).getCOBERTURAPRIMA()
         * .add(2, null); // .getCOBERTURAPRIMA().get(0).setCLAVECOBERTURA(null);
         */
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        domainImpl.obtenRequest(emDatos, prodTecCom);
        /*
         * EmiteNegReq objReq) { CotizacionDto cotizaDto =
         * objReq.getMucPrimaAutoResp().getPETICION().get(0).getDATOSPRODUCTOS().get(0).
         * getDATOSCOTIZACION().get(0);
         * objDerechos.setMTODERECHOPOLIZA(getConcepto(cotizaDto.getTOTALPRIMA().
         * getCONCEPTOECONOMICO(), "DERECHOS_POLIZA")); getCotizacionMucResp() - 515
         * getProductoMucResp - 535 getPeticionMucResp-542 getMucPrimaAutoResp-548
         */
    }

    @Test(expected = WSPSimpleException.class)
    public void obtenRequestConductor2daPosTest() {
        // getCotizacionMucResp
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("0");
        emDatos.getEmite().getMucPrimaAutoReq().getDATOSCOTIZACION().get(0).getDATOSPRODUCTO().get(0).getCOBERTURA()
                .get(0).setCLAVECOBERTURA("0100001268");
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        domainImpl.obtenRequest(emDatos, prodTecCom);
    }

    @Test(expected = WSPSimpleException.class)
    public void obtenRequestFConductorTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        domainImpl.obtenRequest(emDatos, prodTecCom);
    }

    @Test(expected = WSPSimpleException.class)
    public void obtenRequestSinEdadTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        domainImpl.obtenRequest(emDatos, prodTecCom);
    }

    @Test(expected = WSPSimpleException.class)
    public void obtenRequestVPSTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().setViaPagoSucesivos("AA");
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        domainImpl.obtenRequest(emDatos, prodTecCom);
    }

    @Test(expected = WSPSimpleException.class)
    public void obtenRequestVPSVacTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().setViaPagoSucesivos("");
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        domainImpl.obtenRequest(emDatos, prodTecCom);
    }

    @Test(expected = WSPSimpleException.class)
    public void obtenRequestVPSNullTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().setViaPagoSucesivos(null);
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        domainImpl.obtenRequest(emDatos, prodTecCom);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void getEmitirHuecos1Test() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        Integer id = 3325;
        when(conf.getUrlPersona()).thenReturn("http://wsp-persona-uat.oscpuat.gnp.com.mx/persona");
        when(conf.getUrlValidas())
                .thenReturn("http://wsp-validareglas-uat.oscp.gnp.com.mx/validareglas/validareglas/json");
        String llamadoPersonas = conf.getUrlPersona() + "/persona?tid=" + id.toString() + "&idCot="
                + ObjetosPruebas.COT_NOW;
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
        HttpEntity<List<PersonaNeg>> requestEntity = new HttpEntity<>(personas, requestHeaders);
        when(restTemplate.exchange(llamadoPersonas, HttpMethod.POST, requestEntity,
                new ParameterizedTypeReference<List<PersonaNeg>>() {
                })).thenReturn(ObjetosPruebas.getSrvPersonas());
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos.getEmite().setCveHerramienta("PGN");
        emDatos.getEmite().setBanRenovacionAutomatica("0");
        emDatos.getEmite().setCodigoPromocion("");
        emDatos.getEmite().setBanRenovacionAutomatica("0");
        domainImpl.getEmitir(emDatos, id, "A2400LNEVO");
    }

    @Test(expected = WSPSimpleException.class)
    public void obtenRequestSinPSTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().setViaPagoSucesivos(null);
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        domainImpl.obtenRequest(emDatos, prodTecCom);
    }

    @Test(expected = WSPSimpleException.class)
    public void obtenRequestTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        domainImpl.obtenRequest(emDatos, prodTecCom);
    }

    @SuppressWarnings("static-access")
    @Test(expected = ExecutionError.class)
    public void manejaErrorIDTest() {
        PersonaClient pc = new PersonaClient(restTemplate);
        pc.manejaErrorID("Error", null);
    }

    @Test(expected = WSPSimpleException.class)
    public void obtenRequestImpCentCTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().setViaPagoSucesivos(null);
        emDatos.getEmite().setBanImpresionCentralizada("0");
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        domainImpl.obtenRequest(emDatos, prodTecCom);
    }

    @Test // (expected = ExecutionError.class)
    public void obtenRequest2Test() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    @Test(expected = NullPointerException.class)
    public void obtenRequestVpBnTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().setViaPago("BN");
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        domainImpl.obtenRequest(emDatos, prodTecCom);
    }

    @Test(expected = NullPointerException.class)
    public void obtenRequestVpDnTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().setViaPago("DN");
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        domainImpl.obtenRequest(emDatos, prodTecCom);
    }

    @Test // (expected = ExecutionError.class)
    public void obtenRequestVpInTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().setViaPago("IN");
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    @Test // (expected = ExecutionError.class)
    public void obtenRequestVpVacTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().setViaPago("");
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    @Test(expected = NullPointerException.class)
    public void obtenRequestVpNullTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().setViaPago(null);
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        domainImpl.obtenRequest(emDatos, prodTecCom);
    }

    @Test(expected = NullPointerException.class)
    public void obtenRequestSadeNullTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().setViaPago("DN");
        emDatos.getEmite().getTarificadordResp().getTARIFICADORPRODUCTO().getDATOSSOLICITUD().setCONTRATOSADE(null);
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        domainImpl.obtenRequest(emDatos, prodTecCom);
    }

    @Test(expected = NullPointerException.class)
    public void obtenRequestSadeVacioTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().setViaPago("DN");
        emDatos.getEmite().getTarificadordResp().getTARIFICADORPRODUCTO().getDATOSSOLICITUD().setCONTRATOSADE("");
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        domainImpl.obtenRequest(emDatos, prodTecCom);
    }

    @Test(expected = NullPointerException.class)
    public void obtenRequestSadeTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().setViaPago("DN");
        emDatos.getEmite().getTarificadordResp().getTARIFICADORPRODUCTO().getDATOSSOLICITUD().setCONTRATOSADE("234654");
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        domainImpl.obtenRequest(emDatos, prodTecCom);
    }

    @Test // (expected = ExecutionError.class)
    public void obtenRequestEst2Test() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().setIdVersionNegocio(null);
        emDatos.getEmite().setTipoCanalAcceso(null);
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    @Test // (expected = ExecutionError.class)
    public void obtenRequestRefNullTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().setElementos(null);
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    @Test // (expected = ExecutionError.class)
    public void obtenRequestRefExt2Test() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        List<ElementoNeg> elementos = emDatos.getEmite().getElementos();
        ElementoNeg elemento = new ElementoNeg();
        elemento.setClave("refext");
        elemento.setNombre("REFERENCIA");
        elemento.setValor("refext");
        elementos.add(elemento);
        emDatos.getEmite().setElementos(elementos);
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    @Test // (expected = ExecutionError.class)
    public void obtenRequestCAVacioTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().setTipoCanalAcceso("");
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    @Test // (expected = ExecutionError.class)
    public void obtenRequestTelefonoElemNullTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().getPersonas().add(0, ObjetosPruebas.getPersonas());
        emDatos.getEmite().setElementos(null);
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    @Test // (expected = ExecutionError.class)
    public void obtenRequestElemVaclTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().getPersonas().add(0, ObjetosPruebas.getPersonas());
        emDatos.getEmite().setElementos(new ArrayList<>());
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    @Test // (expected = ExecutionError.class)
    public void obtenRequestSinLLANTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().getElementos().get(2).setNombre("SIN_LLAVE");
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    @Test // (expected = ExecutionError.class)
    public void obtenRequestDirNINullTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().getPersonas().get(0).getDomicilio().setNumInterior(null);
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    @Test // (expected = ExecutionError.class)
    public void obtenRequestDirNIVacTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().getPersonas().get(0).getDomicilio().setNumInterior("");
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    @Test // (expected = ExecutionError.class)
    public void obtenRequestDirNITest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().getPersonas().get(0).getDomicilio().setNumInterior("10");
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    @Test // (expected = ExecutionError.class)
    public void obtenRequestDirVacTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().getPersonas().get(0).getDomicilio().setEstado("99");
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    @Test // (expected = ExecutionError.class)
    public void obtenRequestNumTarMCnullTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().getPersonas().get(0).setMedioCobro(null);
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    @Test // (expected = ExecutionError.class)
    public void obtenRequestNumTarNullTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().getPersonas().get(0).getMedioCobro().setNumTarjeta(null);
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    @Test // (expected = ExecutionError.class)
    public void obtenRequestNumTarIdCtaFinanNullTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().getPersonas().get(0).getMedioCobro().setIdCuentaFinanciera(null);
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    @Test // (expected = ExecutionError.class)
    public void obtenRequestNumTarIdCtaFinanVacTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().getPersonas().get(0).getMedioCobro().setIdCuentaFinanciera("");
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    @Test // (expected = ExecutionError.class)
    public void obtenRequestNumTarIdCtaFinanTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().getPersonas().get(0).getMedioCobro().setIdCuentaFinanciera("1");
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    @Test // (expected = ExecutionError.class)
    public void obtenRequestAdapBanCeroTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().getVehiculo().setAdaptaciones(new ArrayList<>());
        emDatos.getEmite().getVehiculo().getAdaptaciones().add(ObjetosPruebas.getAdaptaciones());
        emDatos.getEmite().getVehiculo().getAdaptaciones().get(0).setBanEquip("0");
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    @Test // (expected = ExecutionError.class)
    public void obtenRequestAdapTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().getVehiculo().setAdaptaciones(new ArrayList<>());
        emDatos.getEmite().getVehiculo().getAdaptaciones().add(ObjetosPruebas.getAdaptaciones());
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    @Test // (expected = ExecutionError.class)
    public void obtenRequestNSTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    @Test // (expected = ExecutionError.class)
    public void obtenRequestARNullTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().getVehiculo().setAdaptaciones(new ArrayList<>());
        emDatos.getEmite().getVehiculo().getAdaptaciones().add(ObjetosPruebas.getAdaptaciones());
        emDatos.getEmite().getVehiculo().setAltoRiesgo(null);
        emDatos.getEmite().getVehiculo().setTipoCarga(null);
        emDatos.getEmite().getVehiculo().setDescripcionFactura(null);
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    @Test // (expected = ExecutionError.class)
    public void obtenRequestARCeroTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().getVehiculo().setAdaptaciones(new ArrayList<>());
        emDatos.getEmite().getVehiculo().getAdaptaciones().add(ObjetosPruebas.getAdaptaciones());
        emDatos.getEmite().getVehiculo().setAltoRiesgo("0");
        emDatos.getEmite().getVehiculo().setTipoCarga("");
        emDatos.getEmite().getVehiculo().setDescripcionFactura("");
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    @Test // (expected = ExecutionError.class)
    public void obtenRequestARTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().getVehiculo().setAdaptaciones(new ArrayList<>());
        emDatos.getEmite().getVehiculo().getAdaptaciones().add(ObjetosPruebas.getAdaptaciones());
        emDatos.getEmite().getVehiculo().setAltoRiesgo("1");
        emDatos.getEmite().getVehiculo().setTipoCarga("A");
        emDatos.getEmite().getVehiculo().setDescripcionFactura("quemacocos");
        ElementoNeg elemento = new ElementoNeg();
        elemento.setClave("1");
        elemento.setNombre("VERSION");
        elemento.setValor("1");
        emDatos.getEmite().getElementos().add(elemento);
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    @Test // (expected = ExecutionError.class)
    public void obtenRequestPasSinSATest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().getVehiculo().setAdaptaciones(new ArrayList<>());
        emDatos.getEmite().getVehiculo().getAdaptaciones().add(ObjetosPruebas.getAdaptaciones());
        emDatos.getEmite().getVehiculo().setAltoRiesgo("1");
        emDatos.getEmite().getVehiculo().setTipoCarga("A");
        emDatos.getEmite().getVehiculo().setDescripcionFactura("quemacocos");
        emDatos.getEmite().getVehiculo().setNumPasajeros("5");
        emDatos.getEmite().getPersonas().get(0).setEdad("");
        emDatos.getEmite().getMucPrimaAutoReq().getDATOSCOTIZACION().get(0).getDATOSPRODUCTO().get(0).getCOBERTURA()
                .get(0).getMODIFICADOR().get(0).setCLAVEMODIFICADOR("CPASEGURNO");
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    @Test // (expected = ExecutionError.class)
    public void obtenRequestAutosSCEcobTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().getVehiculo().setAdaptaciones(new ArrayList<>());
        emDatos.getEmite().getVehiculo().getAdaptaciones().add(ObjetosPruebas.getAdaptaciones());
        emDatos.getEmite().getVehiculo().setAltoRiesgo("1");
        emDatos.getEmite().getVehiculo().setTipoCarga("A");
        emDatos.getEmite().getVehiculo().setDescripcionFactura("quemacocos");
        emDatos.getEmite().getVehiculo().setNumPasajeros("5");
        emDatos.getEmite().getPersonas().get(0).setEdad("");
        emDatos.getEmite().getMucPrimaAutoReq().getDATOSCOTIZACION().get(0).getDATOSPRODUCTO().get(0).getCOBERTURA()
                .get(0).getMODIFICADOR().get(0).setCLAVEMODIFICADOR("CPASEGURNO");
        emDatos.getEmite().getMucPrimaAutoReq().getDATOSCOTIZACION().get(0).getDATOSPRODUCTO().get(0).getCOBERTURA()
                .get(0).getMODIFICADOR().get(0).setVALORREQUERIDO(new Double("993999.0"));
        emDatos.getEmite().getPaquetes().get(0).getCoberturas().get(0).setCveCobertura("0000001268");// ok,no
        emDatos.getEmite().getPaquetes().get(0).getCoberturas().get(0).setSa("993999.0");
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    @Test // (expected = ExecutionError.class)
    public void obtenRequestAutosSCEnoampTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().getVehiculo().setAdaptaciones(new ArrayList<>());
        emDatos.getEmite().getVehiculo().getAdaptaciones().add(ObjetosPruebas.getAdaptaciones());
        emDatos.getEmite().getVehiculo().setAltoRiesgo("1");
        emDatos.getEmite().getVehiculo().setTipoCarga("A");
        emDatos.getEmite().getVehiculo().setDescripcionFactura("quemacocos");
        emDatos.getEmite().getVehiculo().setNumPasajeros("5");
        emDatos.getEmite().getPersonas().get(0).setEdad("");
        emDatos.getEmite().getMucPrimaAutoReq().getDATOSCOTIZACION().get(0).getDATOSPRODUCTO().get(0).getCOBERTURA()
                .get(0).getMODIFICADOR().get(0).setCLAVEMODIFICADOR("CPASEGURNO");
        emDatos.getEmite().getMucPrimaAutoReq().getDATOSCOTIZACION().get(0).getDATOSPRODUCTO().get(0).getCOBERTURA()
                .get(0).getMODIFICADOR().get(0).setVALORREQUERIDO(new Double("995999.0"));
        emDatos.getEmite().getPaquetes().get(0).getCoberturas().get(0).setCveCobertura("0000001268");// no,ok
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    @Test // (expected = ExecutionError.class)
    public void obtenRequestAutosSCEnonoTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().getVehiculo().setAdaptaciones(new ArrayList<>());
        emDatos.getEmite().getVehiculo().getAdaptaciones().add(ObjetosPruebas.getAdaptaciones());
        emDatos.getEmite().getVehiculo().setAltoRiesgo("1");
        emDatos.getEmite().getVehiculo().setTipoCarga("A");
        emDatos.getEmite().getVehiculo().setDescripcionFactura("quemacocos");
        emDatos.getEmite().getVehiculo().setNumPasajeros("5");
        emDatos.getEmite().getPersonas().get(0).setEdad("");
        emDatos.getEmite().getMucPrimaAutoReq().getDATOSCOTIZACION().get(0).getDATOSPRODUCTO().get(0).getCOBERTURA()
                .get(0).getMODIFICADOR().get(0).setCLAVEMODIFICADOR("CPASEGURNO");
        emDatos.getEmite().getMucPrimaAutoReq().getDATOSCOTIZACION().get(0).getDATOSPRODUCTO().get(0).getCOBERTURA()
                .get(0).getMODIFICADOR().get(0).setVALORREQUERIDO(new Double("995999.0"));
        emDatos.getEmite().getPaquetes().get(0).getCoberturas().get(0).setCveCobertura("0100001268");// no,no
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    @Test // (expected = ExecutionError.class)
    public void obtenRequestAutosSCEsaTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().getVehiculo().setAdaptaciones(new ArrayList<>());
        emDatos.getEmite().getVehiculo().getAdaptaciones().add(ObjetosPruebas.getAdaptaciones());
        emDatos.getEmite().getVehiculo().setAltoRiesgo("1");
        emDatos.getEmite().getVehiculo().setTipoCarga("A");
        emDatos.getEmite().getVehiculo().setDescripcionFactura("quemacocos");
        emDatos.getEmite().getVehiculo().setNumPasajeros("5");
        emDatos.getEmite().getPersonas().get(0).setEdad("");
        emDatos.getEmite().getMucPrimaAutoReq().getDATOSCOTIZACION().get(0).getDATOSPRODUCTO().get(0).getCOBERTURA()
                .get(0).getMODIFICADOR().get(0).setCLAVEMODIFICADOR("CPASEGURNO");
        emDatos.getEmite().getMucPrimaAutoReq().getDATOSCOTIZACION().get(0).getDATOSPRODUCTO().get(0).getCOBERTURA()
                .get(0).getMODIFICADOR().get(0).setVALORREQUERIDO(new Double("999999.0"));
        emDatos.getEmite().getPaquetes().get(0).getCoberturas().get(0).setCveCobertura("0000001268");// okok
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    @Test // (expected = ExecutionError.class)
    public void obtenRequestAutosSCETest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().getVehiculo().setAdaptaciones(new ArrayList<>());
        emDatos.getEmite().getVehiculo().getAdaptaciones().add(ObjetosPruebas.getAdaptaciones());
        emDatos.getEmite().getVehiculo().setAltoRiesgo("1");
        emDatos.getEmite().getVehiculo().setTipoCarga("A");
        emDatos.getEmite().getVehiculo().setDescripcionFactura("quemacocos");
        emDatos.getEmite().getVehiculo().setNumPasajeros("5");
        emDatos.getEmite().getPersonas().get(0).setEdad("");
        emDatos.getEmite().getMucPrimaAutoReq().getDATOSCOTIZACION().get(0).getDATOSPRODUCTO().get(0).getCOBERTURA()
                .get(0).getMODIFICADOR().get(0).setCLAVEMODIFICADOR("CPASEGURNO");
        emDatos.getEmite().getMucPrimaAutoReq().getDATOSCOTIZACION().get(0).getDATOSPRODUCTO().get(0).getCOBERTURA()
                .get(0).getMODIFICADOR().get(0).setVALORREQUERIDO(new Double("999999.0"));
        emDatos.getEmite().getPaquetes().get(0).getCoberturas().get(0).setCveCobertura("0000001268");
        emDatos.getEmite().getMucPrimaAutoReq().getDATOSCOTIZACION().get(0).getDATOSPRODUCTO().get(0).getCOBERTURA()
                .get(0).getMODIFICADOR().get(1).setCLAVEMODIFICADOR("CDDEDUCINO");
        ModificadorCoberturaDto modificador = new ModificadorCoberturaDto();
        modificador.setCLAVEMODIFICADOR("CDDEDUCIAR");
        modificador.setVALORREQUERIDO(new Double("5"));
        modificador.setUNIDADMEDIDA("UMAS");
        emDatos.getEmite().getMucPrimaAutoReq().getDATOSCOTIZACION().get(0).getDATOSPRODUCTO().get(0).getCOBERTURA()
                .get(0).getMODIFICADOR().add(modificador);
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    @Test // (expected = ExecutionError.class)
    public void obtenRequestAutosSCE2Test() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().getVehiculo().setAdaptaciones(new ArrayList<>());
        emDatos.getEmite().getVehiculo().getAdaptaciones().add(ObjetosPruebas.getAdaptaciones());
        emDatos.getEmite().getVehiculo().setAltoRiesgo("1");
        emDatos.getEmite().getVehiculo().setTipoCarga("A");
        emDatos.getEmite().getVehiculo().setDescripcionFactura("quemacocos");
        emDatos.getEmite().getVehiculo().setNumPasajeros("5");
        emDatos.getEmite().getPersonas().get(0).setEdad("");
        emDatos.getEmite().getMucPrimaAutoReq().getDATOSCOTIZACION().get(0).getDATOSPRODUCTO().get(0).getCOBERTURA()
                .get(0).getMODIFICADOR().get(0).setCLAVEMODIFICADOR("CPASEGURNO");
        emDatos.getEmite().getMucPrimaAutoReq().getDATOSCOTIZACION().get(0).getDATOSPRODUCTO().get(0).getCOBERTURA()
                .get(0).getMODIFICADOR().get(0).setVALORREQUERIDO(new Double("999999.0"));
        emDatos.getEmite().getPaquetes().get(0).getCoberturas().get(0).setCveCobertura("0000001268");
        emDatos.getEmite().getMucPrimaAutoReq().getDATOSCOTIZACION().get(0).getDATOSPRODUCTO().get(0).getCOBERTURA()
                .get(0).getMODIFICADOR().get(1).setCLAVEMODIFICADOR("CDDEDUCINO");
        ModificadorCoberturaDto modificador = new ModificadorCoberturaDto();
        modificador.setCLAVEMODIFICADOR("CDDEDUCIAR");
        modificador.setVALORREQUERIDO(new Double("5"));
        modificador.setUNIDADMEDIDA("UMAS");
        emDatos.getEmite().getMucPrimaAutoReq().getDATOSCOTIZACION().get(0).getDATOSPRODUCTO().get(0).getCOBERTURA()
                .get(0).getMODIFICADOR().add(modificador);
        emDatos.getEmite().getPersonas().get(0).setSecBeneficiario(null);
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    @Test // (expected = ExecutionError.class)
    public void obtenRequestAutosSCE3Test() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().getVehiculo().setAdaptaciones(new ArrayList<>());
        emDatos.getEmite().getVehiculo().getAdaptaciones().add(ObjetosPruebas.getAdaptaciones());
        emDatos.getEmite().getVehiculo().setAltoRiesgo("1");
        emDatos.getEmite().getVehiculo().setTipoCarga("A");
        emDatos.getEmite().getVehiculo().setDescripcionFactura("quemacocos");
        emDatos.getEmite().getVehiculo().setNumPasajeros("5");
        emDatos.getEmite().getPersonas().get(0).setEdad("");
        emDatos.getEmite().getMucPrimaAutoReq().getDATOSCOTIZACION().get(0).getDATOSPRODUCTO().get(0).getCOBERTURA()
                .get(0).getMODIFICADOR().get(0).setCLAVEMODIFICADOR("CPASEGURNO");
        emDatos.getEmite().getMucPrimaAutoReq().getDATOSCOTIZACION().get(0).getDATOSPRODUCTO().get(0).getCOBERTURA()
                .get(0).getMODIFICADOR().get(0).setVALORREQUERIDO(new Double("999999.0"));
        emDatos.getEmite().getPaquetes().get(0).getCoberturas().get(0).setCveCobertura("0000001268");
        emDatos.getEmite().getMucPrimaAutoReq().getDATOSCOTIZACION().get(0).getDATOSPRODUCTO().get(0).getCOBERTURA()
                .get(0).getMODIFICADOR().get(1).setCLAVEMODIFICADOR("CDDEDUCINO");
        ModificadorCoberturaDto modificador = new ModificadorCoberturaDto();
        modificador.setCLAVEMODIFICADOR("CDDEDUCIAR");
        modificador.setVALORREQUERIDO(new Double("5"));
        modificador.setUNIDADMEDIDA("UMAS");
        emDatos.getEmite().getMucPrimaAutoReq().getDATOSCOTIZACION().get(0).getDATOSPRODUCTO().get(0).getCOBERTURA()
                .get(0).getMODIFICADOR().add(modificador);
        emDatos.getEmite().getPersonas().get(0).setSecBeneficiario("0");
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    @Test(expected = ExecutionError.class)
    public void obtenRequestAutosSCEUrlTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().getVehiculo().setAdaptaciones(new ArrayList<>());
        emDatos.getEmite().getVehiculo().getAdaptaciones().add(ObjetosPruebas.getAdaptaciones());
        emDatos.getEmite().getVehiculo().setAltoRiesgo("1");
        emDatos.getEmite().getVehiculo().setTipoCarga("A");
        emDatos.getEmite().getVehiculo().setDescripcionFactura("quemacocos");
        emDatos.getEmite().getVehiculo().setNumPasajeros("5");
        emDatos.getEmite().getPersonas().get(0).setEdad("");
        emDatos.getEmite().getMucPrimaAutoReq().getDATOSCOTIZACION().get(0).getDATOSPRODUCTO().get(0).getCOBERTURA()
                .get(0).getMODIFICADOR().get(0).setCLAVEMODIFICADOR("CPASEGURNO");
        emDatos.getEmite().getMucPrimaAutoReq().getDATOSCOTIZACION().get(0).getDATOSPRODUCTO().get(0).getCOBERTURA()
                .get(0).getMODIFICADOR().get(0).setVALORREQUERIDO(new Double("999999.0"));
        emDatos.getEmite().getPaquetes().get(0).getCoberturas().get(0).setCveCobertura("0000001268");
        emDatos.getEmite().getMucPrimaAutoReq().getDATOSCOTIZACION().get(0).getDATOSPRODUCTO().get(0).getCOBERTURA()
                .get(0).getMODIFICADOR().get(1).setCLAVEMODIFICADOR("CDDEDUCINO");
        ModificadorCoberturaDto modificador = new ModificadorCoberturaDto();
        modificador.setCLAVEMODIFICADOR("CDDEDUCIAR");
        modificador.setVALORREQUERIDO(new Double("5"));
        modificador.setUNIDADMEDIDA("UMAS");
        emDatos.getEmite().getMucPrimaAutoReq().getDATOSCOTIZACION().get(0).getDATOSPRODUCTO().get(0).getCOBERTURA()
                .get(0).getMODIFICADOR().add(modificador);
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogos");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    @Test // (expected = ExecutionError.class)
    public void obtenRequestConDesc4tTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().getVehiculo().setAdaptaciones(new ArrayList<>());
        emDatos.getEmite().getVehiculo().getAdaptaciones().add(ObjetosPruebas.getAdaptaciones());
        emDatos.getEmite().getVehiculo().setAltoRiesgo("1");
        emDatos.getEmite().getVehiculo().setTipoCarga("A");
        emDatos.getEmite().getVehiculo().setDescripcionFactura("quemacocos");
        emDatos.getEmite().getVehiculo().setNumPasajeros("5");
        emDatos.getEmite().getPersonas().get(0).setEdad("");
        DescuentoDto descuento = new DescuentoDto();
        descuento.setCLAVEDESCUENTO("POVASEP");
        emDatos.getEmite().getMucPrimaAutoResp().getPETICION().get(0).getDATOSPRODUCTOS().get(0).getDESCUENTO()
                .add(descuento);
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    @Test // (expected = ExecutionError.class)
    public void obtenRequestAdNullTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().getVehiculo().setAdaptaciones(new ArrayList<>());
        emDatos.getEmite().getVehiculo().getAdaptaciones().add(ObjetosPruebas.getAdaptaciones());
        emDatos.getEmite().getVehiculo().setAltoRiesgo("1");
        emDatos.getEmite().getVehiculo().setTipoCarga("A");
        emDatos.getEmite().getVehiculo().setDescripcionFactura("quemacocos");
        emDatos.getEmite().getVehiculo().setNumPasajeros("5");
        emDatos.getEmite().getPersonas().get(0).setEdad("");
        DescuentoDto descuento = new DescuentoDto();
        descuento.setCLAVEDESCUENTO("VADESVOL");
        descuento.setCOEFICIENTE(new Double("10"));
        emDatos.getEmite().getMucPrimaAutoResp().getPETICION().get(0).getDATOSPRODUCTOS().get(0).getDESCUENTO()
                .add(descuento);
        emDatos.getEmite().setNotificacionAdicional(null);
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    @Test // (expected = ExecutionError.class)
    public void obtenRequestDescPresenteTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().getVehiculo().setAdaptaciones(new ArrayList<>());
        emDatos.getEmite().getVehiculo().getAdaptaciones().add(ObjetosPruebas.getAdaptaciones());
        emDatos.getEmite().getVehiculo().setAltoRiesgo("1");
        emDatos.getEmite().getVehiculo().setTipoCarga("A");
        emDatos.getEmite().getVehiculo().setDescripcionFactura("quemacocos");
        emDatos.getEmite().getVehiculo().setNumPasajeros("5");
        emDatos.getEmite().getPersonas().get(0).setEdad("");
        DescuentoDto descuento = new DescuentoDto();
        descuento.setCLAVEDESCUENTO("VADESVOL");
        descuento.setCOEFICIENTE(new Double("10"));
        emDatos.getEmite().getMucPrimaAutoResp().getPETICION().get(0).getDATOSPRODUCTOS().get(0).getDESCUENTO()
                .add(descuento);
        emDatos.getEmite().setNotificacionAdicional("hey@mail.com");
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    @Test // (expected = ExecutionError.class)
    public void obtenRequestConDescDFTest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().getVehiculo().setAdaptaciones(new ArrayList<>());
        emDatos.getEmite().getVehiculo().getAdaptaciones().add(ObjetosPruebas.getAdaptaciones());
        emDatos.getEmite().getVehiculo().setAltoRiesgo("1");
        emDatos.getEmite().getVehiculo().setTipoCarga("A");
        emDatos.getEmite().getVehiculo().setDescripcionFactura("quemacocos");
        emDatos.getEmite().getVehiculo().setNumPasajeros("5");
        emDatos.getEmite().getPersonas().get(0).setEdad("");
        DescuentoDto descuento = new DescuentoDto();
        descuento.setCLAVEDESCUENTO("POVASEP");
        emDatos.getEmite().getMucPrimaAutoResp().getPETICION().get(0).getDATOSPRODUCTOS().get(0).getDESCUENTO()
                .add(descuento);
        emDatos.getEmite().getMucPrimaAutoResp().getPETICION().get(0).getDATOSPRODUCTOS().get(0).getDATOSCOTIZACION()
                .get(0).setTOTALPRIMA(new TotalPrimaDto());
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    @Test // (expected = ExecutionError.class)
    public void obtenRequestAAATest() {
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        personabase.setEdad(null);
        personas.add(personabase);
        personabase = ObjetosPruebas.getPersona();
        personabase.setTipo("CONDUCTOR");
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        emDatos = ObjetosPruebas.getBeneficiariosBanC(emDatos);
        emDatos = ObjetosPruebas.getAgentes(emDatos);
        emDatos = ObjetosPruebas.getProductos(emDatos);
        emDatos.getEmite().setIndContConductor("1");
        emDatos.getEmite().getVehiculo().setModelo("2018");
        emDatos.getEmite().getVehiculo().setVersion("13");
        emDatos.getEmite().getVehiculo().setCarroceria("69");
        emDatos.getEmite().getVehiculo().setArmadora("GM");
        emDatos.getEmite().getVehiculo().setTipoVehiculo("AUT");
        emDatos.getEmite().getVehiculo().setAdaptaciones(new ArrayList<>());
        emDatos.getEmite().getVehiculo().getAdaptaciones().add(ObjetosPruebas.getAdaptaciones());
        emDatos.getEmite().getVehiculo().setAltoRiesgo("1");
        emDatos.getEmite().getVehiculo().setTipoCarga("A");
        emDatos.getEmite().getVehiculo().setDescripcionFactura("quemacocos");
        /*
         * emDatos.getEmite().getVehiculo().setNumPasajeros("5");
         * emDatos.getEmite().getPersonas().get(0).setEdad(""); DescuentoDto descuento =
         * new DescuentoDto(); descuento.setCLAVEDESCUENTO("POVASEP");
         * emDatos.getEmite().getMucPrimaAutoResp().getPETICION().get(0).
         * getDATOSPRODUCTOS().get(0).getDESCUENTO().add(descuento);
         * emDatos.getEmite().getMucPrimaAutoResp().getPETICION().get(0).
         * getDATOSPRODUCTOS().get(0).getDATOSCOTIZACION().get(0) .setTOTALPRIMA(new
         * TotalPrimaDto());
         */
        String prodTecCom = "A2400LNEVO:00001";
        when(conf.getUrlPersona()).thenReturn("5");
        when(conf.getCabIdPerfil()).thenReturn("INTERMEDIARIO");
        when(conf.getCabIdRol()).thenReturn("ROL");
        when(conf.getUrlCatalogo()).thenReturn("http://wsp-catalogos-uat.oscpuat.gnp.com.mx/catalogo");
        assertNotNull(domainImpl.obtenRequest(emDatos, prodTecCom));
    }

    ////////////////////
    @Test(expected = ExecutionError.class)
    public void regCuentaFinancieraTest() {
        // valiudaPago ok?
        when(conf.getValidaPago()).thenReturn("1");
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        EmiteNegReq objReq = emDatos.getEmite();
        Integer tid = 3325;
        objReq.setViaPago("CL");
        domainImpl.regCuentaFinanciera(objReq, tid);
    }

    @Test(expected = ExecutionError.class)
    public void getEmitirPortalTest() {
        PersonaNeg personabase = ObjetosPruebas.getPersona();
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(personabase);
        Integer id = 3325;
        // ConsultarPolizaPorNumPolizaRequest request = obtenerRequest();
        // when(client.getPoliza(request, 0)).thenReturn(obtenerConsultaReq());
        // when(tarificar.getCancelacion(reqCancelacion,
        // 0)).thenReturn(getTarificarCancelacion(req));
        when(conf.getUrlPersona()).thenReturn("http://wsp-persona-uat.oscpuat.gnp.com.mx/persona");
        when(conf.getUrlValidas())
                .thenReturn("http://wsp-validareglas-uat.oscp.gnp.com.mx/validareglas/validareglas/json");
        String llamadoPersonas = conf.getUrlPersona() + "/persona?tid=" + id.toString() + "&idCot="
                + ObjetosPruebas.COT_NOW;
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
        HttpEntity<List<PersonaNeg>> requestEntity = new HttpEntity<>(personas, requestHeaders);
        when(restTemplate.exchange(llamadoPersonas, HttpMethod.POST, requestEntity,
                new ParameterizedTypeReference<List<PersonaNeg>>() {
                })).thenReturn(ObjetosPruebas.getSrvPersonas());
        /*
         * String llamadoValida = conf.getUrlValidas() + "?tid=" + id.toString();
         * HttpEntity<List<PersonaNeg>> requestEntity = new HttpEntity<>(personas,
         * requestHeaders); when(restTemplate.exchange(llamadoPersonas, HttpMethod.POST,
         * requestEntity, new ParameterizedTypeReference<List<PersonaNeg>>() {
         * })).thenReturn(getSrvPersonas());
         */
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatos(consulNegReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplemento(emDatos);
        domainImpl.getEmitir(emDatos, id, "A2400LNEVO");
        // EmiteNegResp response = domainImpl.getEmitir(emDatos, 0, "A2400LNEVO");
        // assertNotNull(response);
    }
}