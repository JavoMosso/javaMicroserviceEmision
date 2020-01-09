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
public class EmisionEOTClientTest {
    /* Clase Test. */
    @InjectMocks
    private EmisionEOTClient EmisionEOTClient;
    /* Atributo Autowired. */
    @Mock
    private ConfWSP conf;
    
    @Before
    public void initMocks() {
        EmisionEOTClient = new EmisionEOTClient();
        MockitoAnnotations.initMocks(this);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void getPolizaTest() {
        assertTrue(true);
        EmisionEOTClient.getEmision(ObjetosPruebas.getRegistrarEmisionRequest(), 0);
    }
}
