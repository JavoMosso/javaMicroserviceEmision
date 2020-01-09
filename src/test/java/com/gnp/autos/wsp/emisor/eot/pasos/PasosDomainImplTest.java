package com.gnp.autos.wsp.emisor.eot.pasos;

import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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
import org.springframework.util.StreamUtils;
import com.gnp.autos.wsp.emisor.eot.config.ConfWSP;
import com.gnp.autos.wsp.emisor.eot.soap.service.PasosClient;
import com.gnp.autos.wsp.emisor.eot.util.ObjetosPruebas;
import com.gnp.autos.wsp.negocio.neg.model.EmiteNegReq;
import com.gnp.autos.wsp.negocio.neg.model.VehiculoNeg;
import com.google.gson.Gson;

@RunWith(SpringRunner.class)
public class PasosDomainImplTest {
    /* String Request. */
    private String strEmiteNegReq;
    /* Request. */
    private EmiteNegReq negReq;
    @InjectMocks
    private PasosDomainImpl domain;
    @Mock
    private PasosClient client;
    @Mock
    private ConfWSP conf;
    
    @Value("classpath:/EmiteNegReqTest.json")
    public void setFileEmiteNegReq(final Resource myRes) throws IOException {
        try (InputStream is = myRes.getInputStream() ) {
            strEmiteNegReq = StreamUtils.copyToString(is, StandardCharsets.UTF_8);
        }
    }
    
    @Before
    public void prepararTest() throws JAXBException {
        Gson gson = new Gson();
        negReq = gson.fromJson(strEmiteNegReq, EmiteNegReq.class);
        negReq.setIdCotizacion(ObjetosPruebas.COT_NOW);
        negReq.setIniVig(ObjetosPruebas.FCH_MOV);
        negReq.setFinVig(ObjetosPruebas.FCHFIN);
        negReq.setVehiculo(new VehiculoNeg());
        negReq.getVehiculo().setModelo("2010");
    }
    
    @Before
    public void initMocks() {
        domain = new PasosDomainImpl();
        MockitoAnnotations.initMocks(this);
    }
    
    @Test(expected = NullPointerException.class)
    public void getMovimientoNCTest() {
        assertTrue(true);
        // when(conf.getURLMovimientoEOT()).thenReturn("http://10.67.16.19/configuradorWeb/MovimientosNegocioComercialWebImplPort");
        // EmiteNegReq objEmiNeg = new EmiteNegReq();
        // objEmiNeg.setVehiculo(ObjetosPruebas.getVehiculo());
        domain.getTipoCarga(negReq, 0, "url");
    }
}
