package com.gnp.autos.wsp.emisor.eot.rest.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
public class PersonaClientTest {
    /* Clase Test. */
    @InjectMocks
    private static PersonaClient base;
    @InjectMocks
    private static PersonaClient equal;
    @InjectMocks
    private static PersonaClient notEqual;
    @InjectMocks
    private static PersonaClient nulo;
    @InjectMocks
    private static PersonaClient object3;
    @Mock
    private RestTemplate restTemplate;
    
    @Before
    public void initMocks() {
        restTemplate = new RestTemplate();
        base = new PersonaClient(restTemplate);
        equal = new PersonaClient(restTemplate);
        notEqual = new PersonaClient(restTemplate);
        nulo = null;
        object3 = new PersonaClient(restTemplate);
        MockitoAnnotations.initMocks(this);
    }
    
    @SuppressWarnings("static-access")
    @Test
    public void test() {
        assertNotNull(base.getClass());
    }
    
    @Test
    public void testEquals() {
        assertEquals(base, equal);
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(base.hashCode(), equal.hashCode());
    }
    
    @Test
    public void testDifferent() {
        assertEquals(base, notEqual);
        assertEquals(base.hashCode(), notEqual.hashCode());
        assertNotEquals(base.hashCode(), nulo);
    }
    
    @Test
    public void testAssert() {
        assertTrue(base.canEqual(base));
        assertTrue(base.equals(base));
        assertTrue(base.canEqual(equal));
        assertTrue(base.equals(equal));
    }
    
    @Test
    public void testDeny() {
        assertTrue(base.equals(notEqual));
        assertFalse(base.equals(null));
    }
    
    @SuppressWarnings("static-access")
    @Test
    public void testNull() {
        assertNotNull(nulo);
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
    
    @Test
    public void testSymmetric() {
        assertEquals(base, equal);
        assertEquals(equal, base);
    }
    
    @Test
    public void testTransitive() {
        assertEquals(base, equal);
        assertEquals(equal, object3);
        assertEquals(base, object3);
    }
}
