package com.gnp.autos.wsp.emisor.eot.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.gnp.autos.wsp.emisor.eot.soap.service.EmisionEOTClient;

@RunWith(SpringRunner.class)
public class EmisionEOTConfigurationTest {
    /** In. */
    private Jaxb2Marshaller marshaller;
    /* Clase Test. */
    @InjectMocks
    private EmisionEOTConfiguration emisioneotconfiguration;

    @Before
    public void initMocks() {
        emisioneotconfiguration = new EmisionEOTConfiguration();
        MockitoAnnotations.initMocks(this);
    }

    @Before
    public void prepararTest() {
        marshaller = new Jaxb2Marshaller();
    }

    @Test
    public void getRegistrarEmisionTest() {
        EmisionEOTClient out = emisioneotconfiguration.getRegistrarEmision(marshaller);
        assertNotNull(out);
    }

    @Test
    public void restTemplateTest() {
        RestTemplate out = emisioneotconfiguration.restTemplate();
        assertNotNull(out);
    }

//    @Test
//    public void getCancelarEOTTest() {
//        CancelacionClient out = emisioneotconfiguration.getCancelarEOT(marshaller);
//        assertNotNull(out);
//    }

//    @Test
//    public void getConsultarEOTTest() {
//        ConsultaClient out = consultarconfiguration.getConsultarEOT(marshaller);
//        assertNotNull(out);
//    }

//    @Test
//    public void getRegistrarEmisionTest() {
//        Jaxb2Marshaller out = emitireotconfiguration.marshaller();
//        assertNotNull(out);
//    }
//    
//    @Test
//    public void restTemplateTest() {
//        EmitirEOTClient out = emitireotconfiguration.getRegistrar(marshaller);
//        assertNotNull(out);
//    }

//    @Test
//    public void getFoliadorEOTTest() {
//        FoliadorClient out = foliadoreotconfigurarion.getFoliadorEOT(marshaller);
//        assertNotNull(out);
//    }

//    @Test
//    public void getEmisionTest() {
//        MovimientoEOTClient out = movimientoeotconfiguration.getEmision(marshaller);
//        assertNotNull(out);
//    }

//    @Test
//    public void getPasosEOTTest() {
//        PasosClient out = pasoseotconfiguration.getPasosEOT(marshaller);
//        assertNotNull(out);
//    }

//    @Test
//    public void getProdTecComTest() {
//        ProdTecComClient out = prodteccomconfiguration.getProdTecCom(marshaller);
//        assertNotNull(out);
//    }
//
//    @Test
//    public void getRegionTarificacionTest() {
//        RegionTarifClient out = regiontarifconfiguration.getRegionTarificacion(marshaller);
//        assertNotNull(out);
//    }
//    
}
