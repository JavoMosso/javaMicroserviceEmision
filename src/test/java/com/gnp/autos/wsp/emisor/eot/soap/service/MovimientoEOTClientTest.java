package com.gnp.autos.wsp.emisor.eot.soap.service;

import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import com.gnp.autos.wsp.emisor.eot.config.ConfWSP;
import com.gnp.autos.wsp.emisor.eot.util.ObjetosPruebas;

@RunWith(SpringRunner.class)
public class MovimientoEOTClientTest {
    /* Clase Test. */
    @InjectMocks
    private MovimientoEOTClient MovimientoEOTClient;
    /* Atributo Autowired. */
    @Mock
    private ConfWSP conf;
    
    @Before
    public void initMocks() {
        MovimientoEOTClient = new MovimientoEOTClient();
        MockitoAnnotations.initMocks(this);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void getPolizaTest() {
        assertTrue(true);
        MovimientoEOTClient.getConsulta(ObjetosPruebas.getConsultaNegocio(), "url", 0);
    }
}
