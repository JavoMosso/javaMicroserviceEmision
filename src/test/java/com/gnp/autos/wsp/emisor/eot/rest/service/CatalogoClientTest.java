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
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import com.gnp.autos.wsp.negocio.catalogo.model.CatalogoReq;

@RunWith(SpringRunner.class)
public class CatalogoClientTest {
    /* Clase Test. */
    @InjectMocks
    private static CatalogoClient base;
    @InjectMocks
    private static CatalogoClient equal;
    @InjectMocks
    private static CatalogoClient notEqual;
    @InjectMocks
    private static CatalogoClient nulo;
    @InjectMocks
    private static CatalogoClient object3;
    
    @Before
    public void initMocks() {
        base = new CatalogoClient();
        ReflectionTestUtils.setField(base, "requestCat", new CatalogoReq());
        ReflectionTestUtils.setField(base, "restTemplate", new RestTemplate());
        equal = new CatalogoClient();
        ReflectionTestUtils.setField(equal, "requestCat", new CatalogoReq());
        ReflectionTestUtils.setField(equal, "restTemplate", new RestTemplate());
        notEqual = new CatalogoClient();
        ReflectionTestUtils.setField(notEqual, "requestCat", new CatalogoReq());
        ReflectionTestUtils.setField(notEqual, "restTemplate", new RestTemplate());
        nulo = null;
        object3 = new CatalogoClient();
        ReflectionTestUtils.setField(object3, "requestCat", new CatalogoReq());
        ReflectionTestUtils.setField(object3, "restTemplate", new RestTemplate());
        MockitoAnnotations.initMocks(this);
    }
    
    @SuppressWarnings("static-access")
    @Test
    public void test() {
        assertNotNull(base.getInstancia(null));
    }
    
    @Test
    public void testEquals() {
        assertEquals(base, equal);
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(base.hashCode(), equal.hashCode());
    }
    
    @Test// (expected = AssertionError.class)
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
    
    @Test// (expected = AssertionError.class)
    public void testDeny() {
        assertTrue(base.equals(notEqual));
        assertFalse(base.equals(null));
    }
    
    @SuppressWarnings("static-access")
    @Test// (expected = AssertionError.class)
    public void testNull() {
        assertNotNull(nulo.getInstancia(null));
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
