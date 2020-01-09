package com.gnp.autos.wsp.emisor.eot.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(SpringRunner.class)
public class ErrorDBSolverTest {
    /* Clase Test. */
    @InjectMocks
    private ErrorDBSolver base;
    @InjectMocks
    private ErrorDBSolver equal;
    @InjectMocks
    private ErrorDBSolver notEqual;
    @InjectMocks
    private ErrorDBSolver nulo;
    @InjectMocks
    private ErrorDBSolver object3;
    
    @Before
    public void initMocks() {
        base = new ErrorDBSolver();
        ReflectionTestUtils.setField(base, "urlErrores", "http://wsp-erroresdb-uat.oscp.gnp.com.mx/erroresdb");
        equal = new ErrorDBSolver();
        ReflectionTestUtils.setField(equal, "urlErrores", base.getURL());
        notEqual = new ErrorDBSolver();
        ReflectionTestUtils.setField(notEqual, "urlErrores", "notEqual");
        nulo = null;
        object3 = new ErrorDBSolver();
        ReflectionTestUtils.setField(object3, "urlErrores", base.getURL());
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testEquals() {
        assertEquals(equal.getURL(), base.getURL());
    }
    
    @Test
    public void testDifferent() {
        assertNotEquals(notEqual.getURL(), base.getURL());
        assertNotEquals(base, notEqual);
        assertNotEquals(base.hashCode(), notEqual.hashCode());
        assertNotEquals(base.hashCode(), nulo);
    }
    
    @Test
    public void testAssert() {
        assertTrue(base.equals(base));
    }
    
    @Test
    public void testDeny() {
        assertFalse(base.equals(notEqual));
        assertFalse(base.equals(null));
    }
    
    @Test
    public void testNull() {
        assertNull(nulo.getURL());
    }
    
    @Test
    public void testNotNull() {
        assertNotNull(base);
        assertNotNull(base.toString());
    }
    
    @Test
    public void testReflexive() {
        assertEquals(base, base);
    }
}
