package com.gnp.autos.wsp.emisor.eot.rest.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;
import com.gnp.autos.wsp.emisor.eot.error.ExecutionError;
import com.gnp.autos.wsp.emisor.eot.util.ObjetosPruebas;
import com.gnp.autos.wsp.emisor.eot.validareglas.EmiteNegValReq;
import com.gnp.autos.wsp.negocio.muc.soap.CoberturaDto;
import com.gnp.autos.wsp.negocio.neg.model.AdaptacionVehNeg;
import com.gnp.autos.wsp.negocio.neg.model.CoberturaNeg;
import com.gnp.autos.wsp.negocio.neg.model.EmiteNegReq;
import com.gnp.autos.wsp.negocio.neg.model.PersonaNeg;
import com.gnp.autos.wsp.negocio.tarificadordatos.soap.ConvenioEspecialDto;
import com.gnp.autos.wsp.negocio.umoservice.model.Normatividades;
import com.gnp.autos.wsp.negocio.umoservicemodel.EmisionDatos;
import com.google.gson.Gson;

@RunWith(SpringRunner.class)
public class ValidaEOTClientTest {
    /* String Request. */
    private String strEmiteNegReq;
    /* Request. */
    private EmiteNegReq objReq;
    private ValidaEOTClient valida;
    @Mock
    private RestTemplate restTemplate;
    
    @Value("classpath:/EmiteNegReqTest.json")
    public void setFileEmiteNegReq(final Resource myRes) throws IOException {
        try (InputStream is = myRes.getInputStream() ) {
            strEmiteNegReq = StreamUtils.copyToString(is, StandardCharsets.UTF_8);
        }
    }
    
    @Before
    public void initMocks() throws JAXBException {
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
        restTemplate = new RestTemplate();
        MockitoAnnotations.initMocks(this);
    }
    
    @Test// (expected = NullPointerException.class)
    public void constructorSinNormativasTest() {
        List<PersonaNeg> personas = ObjetosPruebas.getListPersonaInc();
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatosCPers(objReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplementoSinMC(emDatos);
        emDatos.getUmo().getDominios().setNormatividades(new Normatividades());
        valida = new ValidaEOTClient(emDatos, restTemplate);
        assertNotNull(valida);
    }
    
    @Test(expected = ExecutionError.class)
    public void getValidaReglasTidNullTest() {
        List<PersonaNeg> personas = ObjetosPruebas.getListPersonaInc();
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatosCPers(objReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplementoSinMC(emDatos);
        valida = new ValidaEOTClient(emDatos, restTemplate);
        valida.getValidaReglas("url", null);
    }
    
    @Test(expected = ExecutionError.class)
    public void getValidaReglasTidCrTest() {
        List<PersonaNeg> personas = ObjetosPruebas.getListPersonaInc();
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatosCPers(objReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplementoSinMC(emDatos);
        valida = new ValidaEOTClient(emDatos, restTemplate);
        valida.getValidaReglas("url", 0);
    }
    
    @Test
    public void getConveniosTest() {
        List<PersonaNeg> personas = ObjetosPruebas.getListPersonaInc();
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatosCPers(objReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplementoSinMC(emDatos);
        valida = new ValidaEOTClient(emDatos, restTemplate);
        ConvenioEspecialDto convDto = new ConvenioEspecialDto();
        convDto.setCONVENIOESPECIAL("convenio");
        convDto.setCVEUNIDADMEDIDA("unidad");
        convDto.setIDCONVENIOESPECIAL("idConvenio");
        convDto.setVALOR(new BigDecimal("10"));
        valida.getConvenios(convDto);
        assertNotNull(valida);
    }
    
    @Test
    public void getAgentesValNullTest() {
        List<PersonaNeg> personas = ObjetosPruebas.getListPersonaInc();
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatosCPers(objReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplementoSinMC(emDatos);
        valida = new ValidaEOTClient(emDatos, restTemplate);
        valida.getAgentesVal(null);
        assertNotNull(valida);
    }
    
    @Test
    public void getAgentesValEmptyTest() {
        List<PersonaNeg> personas = ObjetosPruebas.getListPersonaInc();
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatosCPers(objReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplementoSinMC(emDatos);
        valida = new ValidaEOTClient(emDatos, restTemplate);
        valida.getAgentesVal(new ArrayList<>());
        assertNotNull(valida);
    }
    
    @Test
    public void getCoberturasValEmptyTest() {
        List<PersonaNeg> personas = ObjetosPruebas.getListPersonaInc();
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatosCPers(objReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplementoSinMC(emDatos);
        valida = new ValidaEOTClient(emDatos, restTemplate);
        valida.getCoberturasVal(new ArrayList<>());
        assertNotNull(valida);
    }
    
    @Test
    public void getCoberturasValNullTest() {
        List<PersonaNeg> personas = ObjetosPruebas.getListPersonaInc();
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatosCPers(objReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplementoSinMC(emDatos);
        valida = new ValidaEOTClient(emDatos, restTemplate);
        valida.getCoberturasVal(null);
        assertNotNull(valida);
    }
    
    @Test
    public void getCoberturasValTest() {
        List<PersonaNeg> personas = ObjetosPruebas.getListPersonaInc();
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatosCPers(objReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplementoSinMC(emDatos);
        valida = new ValidaEOTClient(emDatos, restTemplate);
        List<CoberturaNeg> coberturasNeg = new ArrayList<>();
        CoberturaNeg obj = new CoberturaNeg();
        obj.setCveCobertura("cveCobertura");
        obj.setDeducible("10");
        obj.setSa("100000");
        coberturasNeg.add(obj);
        valida.getCoberturasVal(coberturasNeg);
        assertNotNull(valida);
    }
    
    @Test
    public void getCoberturaDtoEmptyTest() {
        List<PersonaNeg> personas = ObjetosPruebas.getListPersonaInc();
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatosCPers(objReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplementoSinMC(emDatos);
        valida = new ValidaEOTClient(emDatos, restTemplate);
        CoberturaDto coberturaDto = new CoberturaDto();
        coberturaDto.setCLAVECOBERTURA("cobertura");
        valida.getCoberturaDto(coberturaDto);
        assertNotNull(valida);
    }
    
    @Test
    public void getDescuentoValNullTest() {
        List<PersonaNeg> personas = ObjetosPruebas.getListPersonaInc();
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatosCPers(objReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplementoSinMC(emDatos);
        valida = new ValidaEOTClient(emDatos, restTemplate);
        valida.getDescuentoVal(null);
        assertNotNull(valida);
    }
    
    @Test
    public void getDescuentoValEmptyTest() {
        List<PersonaNeg> personas = ObjetosPruebas.getListPersonaInc();
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatosCPers(objReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplementoSinMC(emDatos);
        valida = new ValidaEOTClient(emDatos, restTemplate);
        valida.getDescuentoVal(new ArrayList<>());
        assertNotNull(valida);
    }
    
    @Test
    public void getPersonasValNullTest() {
        List<PersonaNeg> personas = ObjetosPruebas.getListPersonaInc();
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatosCPers(objReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplementoSinMC(emDatos);
        valida = new ValidaEOTClient(emDatos, restTemplate);
        valida.getPersonasVal(null);
        assertNotNull(valida);
    }
    
    @Test
    public void setEqualHashTest() {
        List<PersonaNeg> personas = ObjetosPruebas.getListPersonaInc();
        EmisionDatos emDatos = ObjetosPruebas.getEmisionDatosCPers(objReq, personas, ObjetosPruebas.COT_NOW);
        emDatos = ObjetosPruebas.setComplementoSinMC(emDatos);
        valida = new ValidaEOTClient(emDatos, restTemplate);
        valida.setObjReqVal(new EmiteNegValReq());
        ValidaEOTClient valida2 = new ValidaEOTClient(emDatos, restTemplate);
        assertEquals(valida.hashCode(), valida.hashCode());
        assertNotEquals(valida.hashCode(), valida2.hashCode());
        assertTrue(valida.canEqual(valida));
        assertFalse(valida.equals(valida2));
        assertNotNull(valida.toString());
    }
}
