package com.gnp.autos.wsp.emisor.eot.error;

import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ExecutionErrorTest {
    // @InjectMocks
    // private ExecutionError executionError;
    @Before
    public void initMocks() {
        // executionError = new ExecutionError(0);
        // ReflectionTestUtils.setField(executionError, "TIPO", "emisor-eot");
        // MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void ExecutionErrorITest() {
        ExecutionError executionError = new ExecutionError(1);
        assertNotNull(executionError);
    }
    
    @Test
    public void ExecutionErrorITTest() {
        // throw new ExecutionError(0, "error");
        Throwable ex = null;
        ExecutionError executionError = new ExecutionError(1, ex);
        assertNotNull(executionError);
    }
}
