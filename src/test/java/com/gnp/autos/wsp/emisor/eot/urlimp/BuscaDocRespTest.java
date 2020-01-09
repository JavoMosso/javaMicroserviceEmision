package com.gnp.autos.wsp.emisor.eot.urlimp;

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
public class BuscaDocRespTest {
    /* Clase Test. */
    @InjectMocks
    private BuscaDocResp base;
    @InjectMocks
    private BuscaDocResp equal;
    @InjectMocks
    private BuscaDocResp notEqual;
    @InjectMocks
    private BuscaDocResp nulo;
    @InjectMocks
    private BuscaDocResp object3;
    
    @Before
    public void initMocksBase() {
        Resultado resultado = new Resultado();
        resultado.setUrlDocumento("url");
        base = new BuscaDocResp();
        ReflectionTestUtils.setField(base, "resultado", resultado);
        equal = new BuscaDocResp();
        ReflectionTestUtils.setField(equal, "resultado", base.getResultado());
        notEqual = new BuscaDocResp();
        ReflectionTestUtils.setField(notEqual, "resultado", new Resultado());
        nulo = null;
        object3 = new BuscaDocResp();
        ReflectionTestUtils.setField(object3, "resultado", base.getResultado());
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test() {
        assertNotNull(base.getResultado());
    }
    
    @Test
    public void testEquals() {
        assertEquals(equal.getResultado(), base.getResultado());
        assertEquals(base, equal);
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(base.hashCode(), equal.hashCode());
    }
    
    @Test
    public void testDifferent() {
        assertNotEquals(notEqual.getResultado(), base.getResultado());
        assertNotEquals(base, notEqual);
        assertNotEquals(base.hashCode(), notEqual.hashCode());
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
        assertFalse(base.equals(notEqual));
        assertFalse(base.equals(null));
    }
    
    @Test
    public void testNull() {
        assertNull(nulo.getResultado());
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
