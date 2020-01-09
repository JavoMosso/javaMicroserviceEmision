package com.gnp.autos.wsp.emisor.eot.domain.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import com.gnp.autos.wsp.emisor.eot.config.ConfWSP;
import com.gnp.autos.wsp.emisor.eot.soap.service.FoliadorClient;
import com.gnp.autos.wsp.negocio.catalogo.model.CatalogoResp;
import com.gnp.autos.wsp.negocio.neg.model.ElementoReq;

@RunWith(SpringRunner.class)
public class FoliadorDomainImplTest {
    @InjectMocks
    private FoliadorDomainImpl foliadordomainimpl;
    @Mock
    private FoliadorClient client;
    @Mock
    private ConfWSP conf;
    
    @Before
    public void initMocks() {
        foliadordomainimpl = new FoliadorDomainImpl();
        MockitoAnnotations.initMocks(this);
    }
    
    public void getCatalogoResp() {
        ElementoReq elemento = new ElementoReq();
        elemento.setClave("MONEDA");
        List<ElementoReq> elementos = new ArrayList<>();
        CatalogoResp catResp = new CatalogoResp();
        catResp.setElementos(elementos);
    }
    
    @Test(expected = NullPointerException.class)
    public void getEmisionTest() {
        when(conf.getUrlFoliador()).thenReturn("http://10.67.16.19/odsWeb/FoliadorWebImplPort");
        // when(client.setDefaultUri("http://10.67.16.19/odsWeb/FoliadorWebImplPort")).thenReturn(getCatalogoResp());
        getCatalogoResp();
        foliadordomainimpl.getFolio(0, "");
    }
    
    @SuppressWarnings("static-access")
    @Test
    public void obtenerRequestTest() {
        assertNotNull(foliadordomainimpl.obtenerRequest("clave"));
    }
}
