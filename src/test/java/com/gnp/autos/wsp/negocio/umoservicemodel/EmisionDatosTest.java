package com.gnp.autos.wsp.negocio.umoservicemodel;

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
import com.gnp.autos.wsp.negocio.neg.model.EmiteNegReq;

@RunWith(SpringRunner.class)
public class EmisionDatosTest {
    /* Clase Test. */
    @InjectMocks
    private static EmisionDatos base;
    @InjectMocks
    private static EmisionDatos equal;
    @InjectMocks
    private static EmisionDatos notEqual;
    @InjectMocks
    private static EmisionDatos nulo;
    @InjectMocks
    private static EmisionDatos object3;
    
    @Before
    public void initMocks() {
        EmiteNegReq emite = new EmiteNegReq();
        emite.setAccion("accion");
        EmiteNegReq emiteNot = new EmiteNegReq();
        emiteNot.setAccion("notEqual");
        base = new EmisionDatos();
        ReflectionTestUtils.setField(base, "emite", emite);
        ReflectionTestUtils.setField(base, "umo", new UmoServiceResp());
        equal = new EmisionDatos();
        ReflectionTestUtils.setField(equal, "emite", base.getEmite());
        ReflectionTestUtils.setField(equal, "umo", base.getUmo());
        notEqual = new EmisionDatos();
        ReflectionTestUtils.setField(notEqual, "emite", emiteNot);
        ReflectionTestUtils.setField(notEqual, "umo", base.getUmo());
        nulo = null;
        object3 = new EmisionDatos();
        ReflectionTestUtils.setField(object3, "emite", base.getEmite());
        ReflectionTestUtils.setField(object3, "umo", base.getUmo());
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test() {
        base.setEmite(new EmiteNegReq());
        assertNotNull(base.getEmite());
        base.setUmo(new UmoServiceResp());
        assertNotNull(base.getUmo());
    }
    
    @Test
    public void testEquals() {
        assertEquals(equal.getEmite().getAccion(), base.getEmite().getAccion());
        assertEquals(base, equal);
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(base.hashCode(), equal.hashCode());
    }
    
    @Test
    public void testDifferent() {
        assertNotEquals("accionNot", base.getEmite().getAccion());
        assertNotEquals(base.getEmite().getAccion(), notEqual.getEmite().getAccion());
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
        assertFalse(base.getEmite().getAccion().equals(notEqual.getEmite().getAccion()));
        assertFalse(base.getEmite().getAccion().equals(null));
    }
    
    @Test
    public void testNull() {
        assertNull(nulo.getEmite());
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
        assertEquals(base.getEmite().getAccion(), equal.getEmite().getAccion());
        assertEquals(equal.getEmite().getAccion(), object3.getEmite().getAccion());
        assertEquals(base.getEmite().getAccion(), object3.getEmite().getAccion());
    }
}
