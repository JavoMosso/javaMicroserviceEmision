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
import com.gnp.autos.wsp.emisor.eot.cancelacion.CancelacionReq;
import com.gnp.autos.wsp.emisor.eot.cancelacion.CancelacionResp;
import com.gnp.autos.wsp.emisor.eot.domain.CancelacionDomain;

@RunWith(SpringRunner.class)
public class CancelacionServiceTest {
    /* String. */
    private String strCancelacionReq;
    /* Request. */
    private CancelacionReq cancelaReq;
    /* Clase Test. */
    @InjectMocks
    private CancelacionService cancelacionservice;
    /* Atributo Autowired. */
    @Mock
    private CancelacionDomain domain;
    
    @Value("classpath:/CancelacionReqTest.xml")
    public void setFileCancelacionReq(final Resource myRes) throws IOException {
        try (InputStream is = myRes.getInputStream() ) {
            strCancelacionReq = StreamUtils.copyToString(is, StandardCharsets.UTF_8);
        }
    }
    
    @Before
    public void initMocks() {
        cancelacionservice = new CancelacionService();
        MockitoAnnotations.initMocks(this);
    }
    
    @Before
    public void prepararTest() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(CancelacionReq.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        cancelaReq = (CancelacionReq) jaxbUnmarshaller.unmarshal(new StringReader(strCancelacionReq));
        // Gson gson = new Gson();
        // consulNegReq = gson.fromJson(strEmiteNegReq, EmiteNegReq.class);
    }
    
    public CancelacionResp getCancelacionResp() {
        CancelacionResp response = new CancelacionResp();
        return response;
    }
    
    @Test// (expected = NullPointerException.class)
    public void getEmisorEOTTest() {
        when(domain.cancelacionNTU(cancelaReq)).thenReturn(getCancelacionResp());
        CancelacionResp response = cancelacionservice.getCancelacionEOT(cancelaReq);
        assertNotNull(response);
    }
}
