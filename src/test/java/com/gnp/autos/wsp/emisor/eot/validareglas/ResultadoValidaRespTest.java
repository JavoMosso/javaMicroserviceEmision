package com.gnp.autos.wsp.emisor.eot.validareglas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(SpringRunner.class)
public class ResultadoValidaRespTest {
    /* Clase Test. */
    @InjectMocks
    private ResultadoValidaResp base;
    @InjectMocks
    private ResultadoValidaResp equal;
    @InjectMocks
    private ResultadoValidaResp notEqual;
    @InjectMocks
    private ResultadoValidaResp nulo;
    @InjectMocks
    private ResultadoValidaResp object3;
    
    @Before
    public void initMocksBase() {
        List<String> listaCausas = new ArrayList<>();
        List<String> listaCausasn = new ArrayList<>();
        listaCausasn.add("");
        base = new ResultadoValidaResp();
        ReflectionTestUtils.setField(base, "resultadoValida", "resultadoValida");
        ReflectionTestUtils.setField(base, "listaCausas", listaCausas);
        equal = new ResultadoValidaResp();
        ReflectionTestUtils.setField(equal, "resultadoValida", base.getResultadoValida());
        ReflectionTestUtils.setField(equal, "listaCausas", base.getListaCausas());
        notEqual = new ResultadoValidaResp();
        ReflectionTestUtils.setField(notEqual, "resultadoValida", "resultadoValidan");
        ReflectionTestUtils.setField(notEqual, "listaCausas", listaCausasn);
        nulo = null;
        object3 = new ResultadoValidaResp();
        ReflectionTestUtils.setField(object3, "resultadoValida", base.getResultadoValida());
        ReflectionTestUtils.setField(object3, "listaCausas", base.getListaCausas());
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test() {
        base.setResultadoValida("resultadoValida");
        assertNotNull(base.getResultadoValida());
        base.setListaCausas(new ArrayList<>());
        assertNotNull(base.getListaCausas());
    }
    
    @Test
    public void testEquals() {
        assertEquals(equal.getResultadoValida(), base.getResultadoValida());
        assertEquals(base, equal);
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(base.hashCode(), equal.hashCode());
    }
    
    @Test
    public void testDifferent() {
        assertNotEquals(notEqual.getResultadoValida(), base.getResultadoValida());
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
        assertNull(nulo.getResultadoValida());
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
