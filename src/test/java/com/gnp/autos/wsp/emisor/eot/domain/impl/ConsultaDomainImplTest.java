package com.gnp.autos.wsp.emisor.eot.domain.impl;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.text.ParseException;

import javax.xml.datatype.DatatypeConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import com.gnp.autos.wsp.emisor.eot.consulta.wsdl.ConsultarPolizaPorNumPolizaRequest;
import com.gnp.autos.wsp.emisor.eot.consulta.wsdl.ConsultarPolizaPorNumPolizaResponse;
import com.gnp.autos.wsp.emisor.eot.soap.service.ConsultaClient;

@RunWith(SpringRunner.class)
public class ConsultaDomainImplTest {
    @InjectMocks
    private ConsultaDomainImpl consultadomainimpl;
    @Mock
    private ConsultaClient client;
    
    @Before
    public void initMocks() {
        consultadomainimpl = new ConsultaDomainImpl();
        MockitoAnnotations.initMocks(this);
    }
    
    public ConsultarPolizaPorNumPolizaRequest obtenerRequest() {
        ConsultarPolizaPorNumPolizaRequest request = new ConsultarPolizaPorNumPolizaRequest();
        return request;
    }
    
    public ConsultarPolizaPorNumPolizaResponse obtenerConsultaReq() {
        ConsultarPolizaPorNumPolizaResponse response = new ConsultarPolizaPorNumPolizaResponse();
        return response;
    }
    
    @Test
    public void cancelacionNTUPolizaCancelacionTest() throws DatatypeConfigurationException, ParseException {
        ConsultarPolizaPorNumPolizaRequest request = obtenerRequest();
        when(client.getPoliza(request, 0)).thenReturn(obtenerConsultaReq());
        assertNull(consultadomainimpl.consultarPoliza("00001404597534", "5", 0));
    }
}
