package com.gnp.autos.wsp.emisor.eot.domain.impl;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ProdTecComDomainImplTest {
    @InjectMocks
    private ProdTecComDomainImpl prodteccomdomainimpl;

    @Before
    public void initMocks() {
        prodteccomdomainimpl = new ProdTecComDomainImpl();
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = NullPointerException.class)
    public void getProdTecComTest() {
        assertTrue(true);
        prodteccomdomainimpl.getProdTecCom("", "", 0, "");
    }
}
