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
public class VehiculoValNegTest {
    /* Clase Test. */
    @InjectMocks
    private VehiculoValNeg base;
    @InjectMocks
    private VehiculoValNeg equal;
    @InjectMocks
    private VehiculoValNeg notEqual;
    @InjectMocks
    private VehiculoValNeg nulo;
    @InjectMocks
    private VehiculoValNeg object3;
    
    @Before
    public void initMocksBase() {
        List<AdaptacionVehValNeg> adaptaciones = new ArrayList<>();
        List<AdaptacionVehValNeg> adaptacionesn = new ArrayList<>();
        adaptacionesn.add(new AdaptacionVehValNeg());
        base = new VehiculoValNeg();
        ReflectionTestUtils.setField(base, "subRamo", "subRamo");
        ReflectionTestUtils.setField(base, "adaptaciones", adaptaciones);
        equal = new VehiculoValNeg();
        ReflectionTestUtils.setField(equal, "subRamo", base.getSubRamo());
        ReflectionTestUtils.setField(equal, "adaptaciones", base.getAdaptaciones());
        notEqual = new VehiculoValNeg();
        ReflectionTestUtils.setField(notEqual, "subRamo", "subRamon");
        ReflectionTestUtils.setField(notEqual, "adaptaciones", adaptacionesn);
        nulo = null;
        object3 = new VehiculoValNeg();
        ReflectionTestUtils.setField(object3, "subRamo", base.getSubRamo());
        ReflectionTestUtils.setField(object3, "adaptaciones", base.getAdaptaciones());
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test() {
        base.setSubRamo("subRamo");
        assertNotNull(base.getSubRamo());
        base.setTipoVehiculo("tipoVehiculo");
        assertNotNull(base.getTipoVehiculo());
        base.setClaveMarca("claveMarca");
        assertNotNull(base.getClaveMarca());
        base.setModelo("modelo");
        assertNotNull(base.getModelo());
        base.setArmadora("armadora");
        assertNotNull(base.getArmadora());
        base.setCarroceria("carroceria");
        assertNotNull(base.getCarroceria());
        base.setUso("uso");
        assertNotNull(base.getUso());
        base.setFormaIndemnizacion("formaIndemnizacion");
        assertNotNull(base.getFormaIndemnizacion());
        base.setValorVehiculo("valorVehiculo");
        assertNotNull(base.getValorVehiculo());
        base.setPlacas("placas");
        assertNotNull(base.getPlacas());
        base.setAltoRiesgo("altoRiesgo");
        assertNotNull(base.getAltoRiesgo());
        base.setTipoValorVehiculo("tipoValorVehiculo");
        assertNotNull(base.getTipoValorVehiculo());
        base.setTipoCarga("tipoCarga");
        assertNotNull(base.getTipoCarga());
        base.setEstadoCirculacion("estadoCirculacion");
        assertNotNull(base.getEstadoCirculacion());
        base.setSerie("serie");
        assertNotNull(base.getSerie());
        base.setAdaptaciones(new ArrayList<>());
        assertNotNull(base.getAdaptaciones());
    }
    
    @Test
    public void testEquals() {
        assertEquals(equal.getSubRamo(), base.getSubRamo());
        assertEquals(base, equal);
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(base.hashCode(), equal.hashCode());
    }
    
    @Test
    public void testDifferent() {
        assertNotEquals(notEqual.getSubRamo(), base.getSubRamo());
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
