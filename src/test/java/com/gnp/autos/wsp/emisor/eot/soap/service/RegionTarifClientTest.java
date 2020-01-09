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
import com.gnp.autos.wsp.errors.exceptions.WSPXmlExceptionWrapper;

@RunWith(SpringRunner.class)
public class RegionTarifClientTest {
    /* Clase Test. */
    @InjectMocks
    private RegionTarifClient RegionTarifClient;
    /* Atributo Autowired. */
    @Mock
    private ConfWSP conf;

    @Before
    public void initMocks() {
        RegionTarifClient = new RegionTarifClient();
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = WSPXmlExceptionWrapper.class)
    public void getPolizaTest() {
        assertTrue(true);
        RegionTarifClient.getRegTarif(ObjetosPruebas.getConsultaRegionTarificacionPorCp(), 0, "utl");
    }
}
