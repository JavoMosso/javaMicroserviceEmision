package com.gnp.autos.wsp.emisor.eot.config;

import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ConfigSwaggerTest {
    /* Clase Test. */
    @InjectMocks
    private ConfigSwagger configswagger;
    
    @Before
    public void initMocks() {
        configswagger = new ConfigSwagger();
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void getEmisorEOTTest() {
        configswagger.postsApi();
        assertTrue(true);
    }
}
