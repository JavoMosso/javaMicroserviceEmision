package com.gnp.autos.wsp.emisor.eot.emision.req;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import com.gnp.autos.wsp.negocio.neg.model.EmiteNegReq;

@RunWith(SpringRunner.class)
public class EmisionReqTest {
    @InjectMocks
    private EmisionReq emisionReq;
    
    @Before
    public void initMocks() {
        emisionReq = new EmisionReq();
        // ReflectionTestUtils.setField(emisionReq, "STRCONDUCTOR", "CONDUCTOR");
        // ReflectionTestUtils.setField(emisionReq, "STRCONTRATANTE", "CONTRATANTE");
        // ReflectionTestUtils.setField(emisionReq, "STRBENEFICIARIO", "BENEFICIARIO");
        // ReflectionTestUtils.setField(emisionReq, "STRCOD1", "MRAMIR988358");
        // ReflectionTestUtils.setField(emisionReq, "STRCOD2", "0018499321");
        MockitoAnnotations.initMocks(this);
    }
    
    @Test(expected = NullPointerException.class)
    public void getEmisionTest() {
        EmiteNegReq emisionNeg = new EmiteNegReq();
        emisionReq.getEmision(emisionNeg);
    }
}
