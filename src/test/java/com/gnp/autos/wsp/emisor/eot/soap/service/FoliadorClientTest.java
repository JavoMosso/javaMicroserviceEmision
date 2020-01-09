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
public class FoliadorClientTest {
    /* Clase Test. */
    @InjectMocks
    private FoliadorClient FoliadorClient;
    /* Atributo Autowired. */
    @Mock
    private ConfWSP conf;
    
    @Before
    public void initMocks() {
        FoliadorClient = new FoliadorClient();
        MockitoAnnotations.initMocks(this);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void getPolizaTest() {
        assertTrue(true);
        FoliadorClient.getFolio(ObjetosPruebas.getFoliador(), 0, "url");
    }
}
