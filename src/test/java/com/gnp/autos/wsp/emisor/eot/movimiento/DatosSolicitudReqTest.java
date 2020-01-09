package com.gnp.autos.wsp.emisor.eot.movimiento;

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
public class DatosSolicitudReqTest {
    /* Clase Test. */
    @InjectMocks
    private static DatosSolicitudReq base;
    @InjectMocks
    private static DatosSolicitudReq equal;
    @InjectMocks
    private static DatosSolicitudReq notEqual;
    @InjectMocks
    private static DatosSolicitudReq nulo;
    @InjectMocks
    private static DatosSolicitudReq object3;
    
    @Before
    public void initMocks() {
        base = new DatosSolicitudReq();
        ReflectionTestUtils.setField(base, "subRamo", "subRamo");
        ReflectionTestUtils.setField(base, "tipoVehiculo", "tipoVehiculo");
        ReflectionTestUtils.setField(base, "cveTipouso", "cveTipouso");
        ReflectionTestUtils.setField(base, "codigoPromocion", "codigoPromocion");
        equal = new DatosSolicitudReq();
        ReflectionTestUtils.setField(equal, "subRamo", base.getSubRamo());
        ReflectionTestUtils.setField(equal, "tipoVehiculo", base.getTipoVehiculo());
        ReflectionTestUtils.setField(equal, "cveTipouso", base.getCveTipouso());
        ReflectionTestUtils.setField(equal, "codigoPromocion", base.getCodigoPromocion());
        notEqual = new DatosSolicitudReq();
        ReflectionTestUtils.setField(notEqual, "subRamo", "subRamon");
        ReflectionTestUtils.setField(notEqual, "tipoVehiculo", "tipoVehiculon");
        ReflectionTestUtils.setField(notEqual, "cveTipouso", "cveTipouson");
        ReflectionTestUtils.setField(notEqual, "codigoPromocion", "codigoPromocionn");
        nulo = null;
        object3 = new DatosSolicitudReq();
        ReflectionTestUtils.setField(object3, "subRamo", base.getSubRamo());
        ReflectionTestUtils.setField(object3, "tipoVehiculo", base.getTipoVehiculo());
        ReflectionTestUtils.setField(object3, "cveTipouso", base.getCveTipouso());
        ReflectionTestUtils.setField(object3, "codigoPromocion", base.getCodigoPromocion());
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test() {
        base.setSubRamo("subRamo");
        assertNotNull(base.getSubRamo());
        base.setTipoVehiculo("tipoVehiculo");
        assertNotNull(base.getTipoVehiculo());
        base.setCveTipouso("cveTipouso");
        assertNotNull(base.getCveTipouso());
        base.setCodigoPromocion("codigoPromocion");
        assertNotNull(base.getCodigoPromocion());
    }
    
    @Test
    public void testEquals() {
        assertEquals("subRamo", base.getSubRamo());
        assertEquals(base, equal);
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(base.hashCode(), equal.hashCode());
    }
    
    @Test
    public void testDifferent() {
        assertNotEquals("subRamon", base.getSubRamo());
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
        nulo.setSubRamo(null);
        assertNull(nulo.getSubRamo());
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
