package com.gnp.autos.wsp.emisor.eot.liquidacion;

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
public class RegistrarCuentaFinancieraReqTest {
    /* Clase Test. */
    @InjectMocks
    private static RegistrarCuentaFinancieraReq base;
    @InjectMocks
    private static RegistrarCuentaFinancieraReq equal;
    @InjectMocks
    private static RegistrarCuentaFinancieraReq notEqual;
    @InjectMocks
    private static RegistrarCuentaFinancieraReq nulo;
    @InjectMocks
    private static RegistrarCuentaFinancieraReq object3;
    
    @Before
    public void initMocks() {
        base = new RegistrarCuentaFinancieraReq();
        ReflectionTestUtils.setField(base, "idTransaccion", "177657256");
        ReflectionTestUtils.setField(base, "idParticipante", "00132456");
        ReflectionTestUtils.setField(base, "codFiliacion", "1943656");
        ReflectionTestUtils.setField(base, "cveEntidadFinanciera", "02");
        ReflectionTestUtils.setField(base, "cveMoneda", "MXN");
        ReflectionTestUtils.setField(base, "cveTipoDatoBancario", "AX");
        ReflectionTestUtils.setField(base, "cveTipoCuentaTarjeta", "DS");
        ReflectionTestUtils.setField(base, "numCuenta", "577894420");
        ReflectionTestUtils.setField(base, "fchVencimiento", "200101");
        ReflectionTestUtils.setField(base, "banCtaPrincipal", true);
        ReflectionTestUtils.setField(base, "diaCobroPreferido", (short) 1);
        ReflectionTestUtils.setField(base, "titular", "titular");
        equal = new RegistrarCuentaFinancieraReq();
        ReflectionTestUtils.setField(equal, "idTransaccion", base.getIdTransaccion());
        ReflectionTestUtils.setField(equal, "idParticipante", base.getIdParticipante());
        ReflectionTestUtils.setField(equal, "codFiliacion", base.getCodFiliacion());
        ReflectionTestUtils.setField(equal, "cveEntidadFinanciera", base.getCveEntidadFinanciera());
        ReflectionTestUtils.setField(equal, "cveMoneda", base.getCveMoneda());
        ReflectionTestUtils.setField(equal, "cveTipoDatoBancario", base.getCveTipoDatoBancario());
        ReflectionTestUtils.setField(equal, "cveTipoCuentaTarjeta", base.getCveTipoCuentaTarjeta());
        ReflectionTestUtils.setField(equal, "numCuenta", base.getNumCuenta());
        ReflectionTestUtils.setField(equal, "fchVencimiento", base.getFchVencimiento());
        ReflectionTestUtils.setField(equal, "banCtaPrincipal", base.getBanCtaPrincipal());
        ReflectionTestUtils.setField(equal, "diaCobroPreferido", base.getDiaCobroPreferido());
        ReflectionTestUtils.setField(equal, "titular", base.getTitular());
        notEqual = new RegistrarCuentaFinancieraReq();
        ReflectionTestUtils.setField(notEqual, "idTransaccion", "1776572560");
        ReflectionTestUtils.setField(notEqual, "idParticipante", "001324560");
        ReflectionTestUtils.setField(notEqual, "codFiliacion", "19436560");
        ReflectionTestUtils.setField(notEqual, "cveEntidadFinanciera", "020");
        ReflectionTestUtils.setField(notEqual, "cveMoneda", "MXNnot");
        ReflectionTestUtils.setField(notEqual, "cveTipoDatoBancario", "AXnot");
        ReflectionTestUtils.setField(notEqual, "cveTipoCuentaTarjeta", "DSnot");
        ReflectionTestUtils.setField(notEqual, "numCuenta", "5778944200");
        ReflectionTestUtils.setField(notEqual, "fchVencimiento", "2001010");
        ReflectionTestUtils.setField(notEqual, "banCtaPrincipal", false);
        ReflectionTestUtils.setField(notEqual, "diaCobroPreferido", (short) 2);
        ReflectionTestUtils.setField(notEqual, "titular", "titularnot");
        nulo = null;
        object3 = new RegistrarCuentaFinancieraReq();
        ReflectionTestUtils.setField(object3, "idTransaccion", base.getIdTransaccion());
        ReflectionTestUtils.setField(object3, "idParticipante", base.getIdParticipante());
        ReflectionTestUtils.setField(object3, "codFiliacion", base.getCodFiliacion());
        ReflectionTestUtils.setField(object3, "cveEntidadFinanciera", base.getCveEntidadFinanciera());
        ReflectionTestUtils.setField(object3, "cveMoneda", base.getCveMoneda());
        ReflectionTestUtils.setField(object3, "cveTipoDatoBancario", base.getCveTipoDatoBancario());
        ReflectionTestUtils.setField(object3, "cveTipoCuentaTarjeta", base.getCveTipoCuentaTarjeta());
        ReflectionTestUtils.setField(object3, "numCuenta", base.getNumCuenta());
        ReflectionTestUtils.setField(object3, "fchVencimiento", base.getFchVencimiento());
        ReflectionTestUtils.setField(object3, "banCtaPrincipal", base.getBanCtaPrincipal());
        ReflectionTestUtils.setField(object3, "diaCobroPreferido", base.getDiaCobroPreferido());
        ReflectionTestUtils.setField(object3, "titular", base.getTitular());
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test() {
        base.setIdTransaccion("idTransaccion");
        assertNotNull(base.getIdTransaccion());
        base.setIdParticipante("idParticipante");
        assertNotNull(base.getIdParticipante());
        base.setCodFiliacion("codFiliacion");
        assertNotNull(base.getCodFiliacion());
        base.setCveEntidadFinanciera("cveEntidadFinanciera");
        assertNotNull(base.getCveEntidadFinanciera());
        base.setCveMoneda("cveMoneda");
        assertNotNull(base.getCveMoneda());
        base.setCveTipoDatoBancario("cveTipoDatoBancario");
        assertNotNull(base.getCveTipoDatoBancario());
        base.setCveTipoCuentaTarjeta("cveTipoCuentaTarjeta");
        assertNotNull(base.getCveTipoCuentaTarjeta());
        base.setNumCuenta("numCuenta");
        assertNotNull(base.getNumCuenta());
        base.setFchVencimiento("fchVencimiento");
        assertNotNull(base.getFchVencimiento());
        base.setBanCtaPrincipal(true);
        assertNotNull(base.getBanCtaPrincipal());
        base.setDiaCobroPreferido((short) 1);
        assertNotNull(base.getDiaCobroPreferido());
        base.setTitular("titular");
        assertNotNull(base.getTitular());
    }
    
    @Test
    public void testEquals() {
        assertEquals("titular", base.getTitular());
        assertEquals(base, equal);
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(base.hashCode(), equal.hashCode());
    }
    
    @Test
    public void testDifferent() {
        assertNotEquals("titularnot", base.getTitular());
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
        nulo.setTitular(null);
        assertNull(nulo.getTitular());
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
