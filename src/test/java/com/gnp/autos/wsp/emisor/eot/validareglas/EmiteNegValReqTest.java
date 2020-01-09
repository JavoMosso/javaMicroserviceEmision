package com.gnp.autos.wsp.emisor.eot.validareglas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(SpringRunner.class)
public class EmiteNegValReqTest {
    /* Clase Test. */
    @InjectMocks
    private EmiteNegValReq base;
    @InjectMocks
    private EmiteNegValReq equal;
    @InjectMocks
    private EmiteNegValReq notEqual;
    @InjectMocks
    private EmiteNegValReq nulo;
    @InjectMocks
    private EmiteNegValReq object3;
    
    @Before
    public void initMocksBase() {
        base = new EmiteNegValReq();
        ReflectionTestUtils.setField(base, "idCotizacion", "idCotizacion");
        ReflectionTestUtils.setField(base, "codigoPromocion", "codigoPromocion");
        ReflectionTestUtils.setField(base, "iniVig", "iniVig");
        ReflectionTestUtils.setField(base, "finVig", "finVig");
        ReflectionTestUtils.setField(base, "fchCotizacion", "fchCotizacion");
        ReflectionTestUtils.setField(base, "fchExpedicion", "fchExpedicion");
        ReflectionTestUtils.setField(base, "formaPago", "formaPago");
        ReflectionTestUtils.setField(base, "canalCobro", "canalCobro");
        ReflectionTestUtils.setField(base, "canalCobroSub", "canalCobroSub");
        ReflectionTestUtils.setField(base, "primaNeta", "primaNeta");
        ReflectionTestUtils.setField(base, "idModeloNegocio", "idModeloNegocio");
        ReflectionTestUtils.setField(base, "idNegocioOperable", "idNegocioOperable");
        ReflectionTestUtils.setField(base, "idNegocioComercial", "idNegocioComercial");
        equal = new EmiteNegValReq();
        ReflectionTestUtils.setField(equal, "idCotizacion", base.getIdCotizacion());
        ReflectionTestUtils.setField(equal, "codigoPromocion", base.getCodigoPromocion());
        ReflectionTestUtils.setField(equal, "iniVig", base.getIniVig());
        ReflectionTestUtils.setField(equal, "finVig", base.getFinVig());
        ReflectionTestUtils.setField(equal, "fchCotizacion", base.getFchCotizacion());
        ReflectionTestUtils.setField(equal, "fchExpedicion", base.getFchExpedicion());
        ReflectionTestUtils.setField(equal, "formaPago", base.getFormaPago());
        ReflectionTestUtils.setField(equal, "canalCobro", base.getCanalCobro());
        ReflectionTestUtils.setField(equal, "canalCobroSub", base.getCanalCobroSub());
        ReflectionTestUtils.setField(equal, "primaNeta", base.getPrimaNeta());
        ReflectionTestUtils.setField(equal, "idModeloNegocio", base.getIdModeloNegocio());
        ReflectionTestUtils.setField(equal, "idNegocioOperable", base.getIdNegocioOperable());
        ReflectionTestUtils.setField(equal, "idNegocioComercial", base.getIdNegocioComercial());
        notEqual = new EmiteNegValReq();
        ReflectionTestUtils.setField(notEqual, "idCotizacion", "idCotizacionn");
        ReflectionTestUtils.setField(notEqual, "codigoPromocion", "codigoPromocionn");
        ReflectionTestUtils.setField(notEqual, "iniVig", "iniVign");
        ReflectionTestUtils.setField(notEqual, "finVig", "finVign");
        ReflectionTestUtils.setField(notEqual, "fchCotizacion", "fchCotizacionn");
        ReflectionTestUtils.setField(notEqual, "fchExpedicion", "fchExpedicionn");
        ReflectionTestUtils.setField(notEqual, "formaPago", "formaPagon");
        ReflectionTestUtils.setField(notEqual, "canalCobro", "canalCobron");
        ReflectionTestUtils.setField(notEqual, "canalCobroSub", "canalCobroSubn");
        ReflectionTestUtils.setField(notEqual, "primaNeta", "primaNetan");
        ReflectionTestUtils.setField(notEqual, "idModeloNegocio", "idModeloNegocion");
        ReflectionTestUtils.setField(notEqual, "idNegocioOperable", "idNegocioOperablne");
        ReflectionTestUtils.setField(notEqual, "idNegocioComercial", "idNegocioComercialn");
        nulo = null;
        object3 = new EmiteNegValReq();
        ReflectionTestUtils.setField(object3, "idCotizacion", base.getIdCotizacion());
        ReflectionTestUtils.setField(object3, "codigoPromocion", base.getCodigoPromocion());
        ReflectionTestUtils.setField(object3, "iniVig", base.getIniVig());
        ReflectionTestUtils.setField(object3, "finVig", base.getFinVig());
        ReflectionTestUtils.setField(object3, "fchCotizacion", base.getFchCotizacion());
        ReflectionTestUtils.setField(object3, "fchExpedicion", base.getFchExpedicion());
        ReflectionTestUtils.setField(object3, "formaPago", base.getFormaPago());
        ReflectionTestUtils.setField(object3, "canalCobro", base.getCanalCobro());
        ReflectionTestUtils.setField(object3, "canalCobroSub", base.getCanalCobroSub());
        ReflectionTestUtils.setField(object3, "primaNeta", base.getPrimaNeta());
        ReflectionTestUtils.setField(object3, "idModeloNegocio", base.getIdModeloNegocio());
        ReflectionTestUtils.setField(object3, "idNegocioOperable", base.getIdNegocioOperable());
        ReflectionTestUtils.setField(object3, "idNegocioComercial", base.getIdNegocioComercial());
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test() {
        base.setIdCotizacion("idCotizacion");
        assertNotNull(base.getIdCotizacion());
        base.setCodigoPromocion("codigoPromocion");
        assertNotNull(base.getCodigoPromocion());
        base.setIniVig("iniVig");
        assertNotNull(base.getIniVig());
        base.setFinVig("finVig");
        assertNotNull(base.getFinVig());
        base.setFchCotizacion("fchCotizacion");
        assertNotNull(base.getFchCotizacion());
        base.setFchExpedicion("fchExpedicion");
        assertNotNull(base.getFchExpedicion());
        base.setFormaPago("formaPago");
        assertNotNull(base.getFormaPago());
        base.setCanalCobro("canalCobro");
        assertNotNull(base.getCanalCobro());
        base.setCanalCobroSub("canalCobroSub");
        assertNotNull(base.getCanalCobroSub());
        base.setPrimaNeta("primaNeta");
        assertNotNull(base.getPrimaNeta());
        base.setIdModeloNegocio("idModeloNegocio");
        assertNotNull(base.getIdModeloNegocio());
        base.setIdNegocioOperable("idNegocioOperable");
        assertNotNull(base.getIdNegocioOperable());
        base.setIdNegocioComercial("idNegocioComercial");
        assertNotNull(base.getIdNegocioComercial());
        base.setVehiculo(new VehiculoValNeg());
        assertNotNull(base.getVehiculo());
        base.setConvenios(new ArrayList<>());
        assertNotNull(base.getConvenios());
        base.setPersonas(new ArrayList<>());
        assertNotNull(base.getPersonas());
        base.setAgentes(new ArrayList<>());
        assertNotNull(base.getAgentes());
        base.setDescuentos(new ArrayList<>());
        assertNotNull(base.getDescuentos());
        base.setDatosProducto(new DatosProductoValNeg());
        assertNotNull(base.getDatosProducto());
    }
    
    @Test
    public void testEquals() {
        assertEquals(equal.getIdCotizacion(), base.getIdCotizacion());
        assertEquals(base, equal);
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(base.hashCode(), equal.hashCode());
    }
    
    @Test
    public void testDifferent() {
        assertNotEquals(notEqual.getIdCotizacion(), base.getIdCotizacion());
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
        assertNull(nulo.getIdCotizacion());
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
