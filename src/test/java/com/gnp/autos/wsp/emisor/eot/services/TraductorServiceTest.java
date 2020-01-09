package com.gnp.autos.wsp.emisor.eot.services;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
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
import com.gnp.autos.wsp.emisor.eot.domain.EmisionDomain;
import com.gnp.autos.wsp.emisor.eot.domain.EmisorDomain;
import com.gnp.autos.wsp.emisor.eot.domain.MovimientoDomain;
import com.gnp.autos.wsp.emisor.eot.emision.wsdl.RegistrarEmisionResponse;
import com.gnp.autos.wsp.negocio.emision.model.req.TraductorEmitirReq;
import com.gnp.autos.wsp.negocio.emision.model.resp.TraductorEmitirResp;
import com.gnp.autos.wsp.negocio.neg.model.EmiteNegReq;
import com.google.gson.Gson;

@RunWith(SpringRunner.class)
public class TraductorServiceTest {
    /* String. */
    private String strTraductorEmitirReq;
    /* String. */
    private String strEmiteNegReq;
    /* Request. */
    private TraductorEmitirReq traductorReq;
    /* Request. */
    private EmiteNegReq consulNegReq;
    /* Clase Test. */
    @InjectMocks
    private TraductorService traductorservice;
    /* Atributo Autowired. */
    @Mock
    private EmisorDomain domain;
    /* Atributo Autowired. */
    @Mock
    private MovimientoDomain domainC;
    /* Atributo Autowired. */
    @Mock
    private EmisionDomain domainE;
    
    @Value("classpath:/TraductorEmitirReqTest.xml")
    public void setFileTraductorEmitirReq(final Resource myRes) throws IOException {
        try (InputStream is = myRes.getInputStream() ) {
            strTraductorEmitirReq = StreamUtils.copyToString(is, StandardCharsets.UTF_8);
        }
    }
    
    @Value("classpath:/EmiteNegReqTest.json")
    public void setFileEmiteNegReq(final Resource myRes) throws IOException {
        try (InputStream is = myRes.getInputStream() ) {
            strEmiteNegReq = StreamUtils.copyToString(is, StandardCharsets.UTF_8);
        }
    }
    
    @Before
    public void initMocks() {
        traductorservice = new TraductorService();
        MockitoAnnotations.initMocks(this);
    }
    
    @Before
    public void prepararTest() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(TraductorEmitirReq.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        traductorReq = (TraductorEmitirReq) jaxbUnmarshaller.unmarshal(new StringReader(strTraductorEmitirReq));
        Gson gson = new Gson();
        consulNegReq = gson.fromJson(strEmiteNegReq, EmiteNegReq.class);
    }
    
    public TraductorEmitirResp getTraductorEmitirResp() {
        TraductorEmitirResp response = new TraductorEmitirResp();
        return response;
    }
    
    public EmiteNegReq getEmiteNegReq() {
        EmiteNegReq response = new EmiteNegReq();
        return response;
    }
    
    public RegistrarEmisionResponse getRegistrarEmisionResponse() {
        RegistrarEmisionResponse response = new RegistrarEmisionResponse();
        return response;
    }
    
    @Test
    public void getEmisorEOTTest() {
        when(domain.getEmitir(traductorReq, 0)).thenReturn(getTraductorEmitirResp());
        TraductorEmitirResp response = traductorservice.getEmisorEOT(traductorReq, 0);
        assertNotNull(response);
    }
    
    @Test
    public void getConsultaTest() {
        when(domainC.getMovimientoNC(consulNegReq, 0)).thenReturn(getEmiteNegReq());
        EmiteNegReq response = traductorservice.getConsulta(consulNegReq, 0);
        assertNotNull(response);
    }
    
    @Test
    public void getEmisionEOTTest() {
        when(domainE.getEmision(traductorReq, 0)).thenReturn(getRegistrarEmisionResponse());
        RegistrarEmisionResponse response = traductorservice.getEmisionEOT(traductorReq, 0);
        assertNotNull(response);
    }
}
