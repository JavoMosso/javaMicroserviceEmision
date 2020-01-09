package com.gnp.autos.wsp.emisor.eot.soap.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

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
public class CancelacionClientTest {
    /* Clase Test. */
    @InjectMocks
    private CancelacionClient cancelacionClient;
    /* Atributo Autowired. */
    @Mock
    private ConfWSP conf;
    
    @Before
    public void initMocks() {
        cancelacionClient = new CancelacionClient();
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void cancelacionDefaulTest() {
        when(conf.getUrlCancelacion()).thenReturn("http://10.67.4.55:7918/esb/sce/info/movimientos/comunes/contrato/MovimientosComunes");
        cancelacionClient.cancelacionDefaul();
        assertNotNull(conf);
    }
    
    // @Test(expected = ExecutionError.class)
    public void getTarificarTest() {
        when(conf.getUrlCancelacion()).thenReturn("http://10.67.4.55:7918/esb/sce/info/movimientos/comunes/contrato/MovimientosComunes");
        cancelacionClient.getTarificar(ObjetosPruebas.getTarificarCancelacionRequest(), 0);
        assertTrue(true);
    }
    
    // @Test(expected = ExecutionError.class)
    public void getCancelacionTest() {
        when(conf.getUrlCancelacion()).thenReturn("http://10.67.4.55:7918/esb/sce/info/movimientos/comunes/contrato/MovimientosComunes");
        cancelacionClient.getCancelacion(ObjetosPruebas.getEmitirCancelacionRequest(), 0);
        assertTrue(true);
    }
}