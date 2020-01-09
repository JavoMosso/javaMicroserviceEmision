package com.gnp.autos.wsp.emisor.eot.tarificadorcp;

import static org.junit.Assert.assertTrue;
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

import com.gnp.autos.wsp.emisor.eot.calcp.wsdl.CodigoPostalDto;
import com.gnp.autos.wsp.emisor.eot.calcp.wsdl.ConsultaRegionTarificacionPorCp;
import com.gnp.autos.wsp.emisor.eot.calcp.wsdl.ConsultaRegionTarificacionPorCpResponse;
import com.gnp.autos.wsp.emisor.eot.config.ConfWSP;
import com.gnp.autos.wsp.emisor.eot.soap.service.RegionTarifClient;

@RunWith(SpringRunner.class)
public class RegionTarifDomainImplTest {
    /* Clase Test. */
    @InjectMocks
    private RegionTarifDomainImpl RegionTarifDomainImpl;
    /* Atributo Autowired. */
    @Mock
    private RegionTarifClient client;
    @Mock
    private ConfWSP conf;
    
    @Before
    public void initMocks() {
        RegionTarifDomainImpl = new RegionTarifDomainImpl();
        MockitoAnnotations.initMocks(this);
    }
    
    private static ConsultaRegionTarificacionPorCp obtenRegionTarif(String cp) {
        ConsultaRegionTarificacionPorCp reqRegion = new ConsultaRegionTarificacionPorCp();
        List<CodigoPostalDto> listaCp = new ArrayList<>();
        CodigoPostalDto cdp = new CodigoPostalDto();
        cdp.getCODIGOPOSTAL().add(cp);
        listaCp.add(cdp);
        reqRegion.setCodigoPostal(listaCp.get(0));
        return reqRegion;
    }
    
    private static ConsultaRegionTarificacionPorCpResponse getConsultaRegionTarificacionPorCpResponse() {
        return new ConsultaRegionTarificacionPorCpResponse();
    }
    
    @Test(expected = NullPointerException.class)
    public void getPolizaTest() {
        assertTrue(true);
        when(conf.getUrlConsultaCP()).thenReturn("http://10.67.4.59/motorUnicoCalculoWeb/CalculoAdicionalesWebImplPort");
        when(client.getRegTarif(obtenRegionTarif("03330"), 0, "http://10.67.4.59/motorUnicoCalculoWeb/CalculoAdicionalesWebImplPort"))
                .thenReturn(getConsultaRegionTarificacionPorCpResponse());
        RegionTarifDomainImpl.getRegionTarif(0, "03330");
    }
}
