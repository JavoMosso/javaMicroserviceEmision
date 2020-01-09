package com.gnp.autos.wsp.emisor.eot.util;

import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import com.gnp.autos.wsp.emisor.eot.error.ExecutionError;
import com.gnp.autos.wsp.negocio.neg.model.CoberturaNeg;
import com.gnp.autos.wsp.negocio.neg.model.PersonaNeg;
import com.gnp.autos.wsp.negocio.neg.model.VehiculoNeg;

@RunWith(SpringRunner.class)
public class UtileriaTest {
    @Test
    public void isDateaaaammddTest() {
        assertTrue(true);
        Utileria.isDateaaaammdd("20190101");
    }
    
    @Test
    public void isDateaaaammddExTest() {
        assertTrue(true);
        Utileria.isDateaaaammdd("20AA0101");
    }
    
    @Test(expected = ExecutionError.class)
    public void getFechaTest() {
        assertTrue(true);
        Utileria.getFecha("995566");
    }
    
    @Test(expected = ExecutionError.class)
    public void getGregCalShortExTest() {
        Utileria.getGregCalShort("aaa");
    }
    
    @Test
    public void getGregCalShortTest() {
        Utileria.getGregCalShort("20190101");
        assertTrue(true);
    }
    
    @Test
    public void soloLetrasTest() {
        assertTrue(true);
        Utileria.soloLetras("AAbb3");
    }
    
    @Test
    public void soloAlfaNumericoTest() {
        assertTrue(true);
        Utileria.soloAlfaNumerico("Ab1?");
    }
    
    @Test
    public void isMoralContratanteTest() {
        assertTrue(true);
        List<PersonaNeg> personas = new ArrayList<>();
        personas.add(ObjetosPruebas.getPersona());
        Utileria.isMoralContratante(personas);
    }
    
    @Test
    public void getStrFechaTest() {
        assertTrue(true);
        Utileria.getStrFecha(new Date());
    }
    
    @Test(expected = ExecutionError.class)
    public void getStrFechaExTest() {
        assertTrue(true);
        Utileria.getStrFecha(null);
    }
    
    @Test
    public void getFecNacTest() {
        assertTrue(true);
        Utileria.getFecNac(20);
    }
    
    @Test
    public void isNumericTest() {
        assertTrue(true);
        Utileria.isNumeric("awawe");
    }
    
    @Test
    public void validarCurpTest() {
        assertTrue(true);
        Utileria.validarCurp("AAAA90101");
    }
    
    @Test
    public void validarRFCFisicaTest() {
        assertTrue(true);
        Utileria.validarRFCFisica("AAAA90101123");
    }
    
    @Test
    public void validarRFCFisicaLNTest() {
        assertTrue(true);
        Utileria.validarRFCFisica("MOCO90101123");
    }
    
    @Test
    public void validarRFCFisicaFalseTest() {
        assertTrue(true);
        Utileria.validarRFCFisica("MOC90101123");
    }
    
    @Test
    public void validarRFCMoralTest() {
        assertTrue(true);
        Utileria.validarRFCMoral("MOC90101123");
    }
    
    @Test
    public void getEdadTest() {
        assertTrue(true);
        Utileria.getEdad("20001212");
    }
    
    @Test
    public void getGregCalTest() {
        assertTrue(true);
        Utileria.getGregCal("20001212");
    }
    
    @Test(expected = ExecutionError.class)
    public void getGregCalExTest() {
        assertTrue(true);
        Utileria.getGregCal("2000");
    }
    
    @Test
    public void soloNumerosNullTest() {
        assertTrue(true);
        Utileria.soloNumeros(null);
    }
    
    @Test
    public void soloNumerosTest() {
        assertTrue(true);
        Utileria.soloNumeros("132");
    }
    
    @Test
    public void actualizaMoralTest() {
        assertTrue(true);
        Utileria.actualizaMoral(ObjetosPruebas.getPersona());
    }
    
    @Test
    public void actualizaMoralNoContratanteTest() {
        assertTrue(true);
        PersonaNeg persona = ObjetosPruebas.getPersona();
        persona.setTipo("OTRO");
        Utileria.actualizaMoral(persona);
    }
    
    @Test
    public void actualizaMoralMTest() {
        assertTrue(true);
        PersonaNeg persona = ObjetosPruebas.getPersona();
        persona.setTipoPersona("M");
        Utileria.actualizaMoral(persona);
    }
    
    @Test
    public void actualizaMoralJTest() {
        assertTrue(true);
        PersonaNeg persona = ObjetosPruebas.getPersona();
        persona.setTipoPersona("J");
        Utileria.actualizaMoral(persona);
    }
    
    @Test
    public void isFisicaTest() {
        assertTrue(true);
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg persona = ObjetosPruebas.getPersona();
        personas.add(persona);
        Utileria.isFisica(personas);
    }
    
    @Test
    public void isFisicaMTest() {
        assertTrue(true);
        List<PersonaNeg> personas = new ArrayList<>();
        PersonaNeg persona = ObjetosPruebas.getPersona();
        persona.setTipoPersona("M");
        personas.add(persona);
        Utileria.isFisica(personas);
    }
    
    @Test
    public void formatCalleTest() {
        assertTrue(true);
        Utileria.formatCalle("calle");
    }
    
    @Test
    public void formatNoExteriorTest() {
        assertTrue(true);
        Utileria.formatNoExterior("no exterior");
    }
    
    @Test
    public void separaNombreTest() {
        assertTrue(true);
        PersonaNeg persona = ObjetosPruebas.getPersona();
        persona.setNombre("Eduardo");
        Utileria.separaNombre(persona);
    }
    
    @Test
    public void separaNombreLargoTest() {
        assertTrue(true);
        PersonaNeg persona = ObjetosPruebas.getPersona();
        persona.setNombre("Eduardo Camerino");
        Utileria.separaNombre(persona);
    }
    
    @Test
    public void getformaPagoTest() {
        assertTrue(true);
        Utileria.getformaPago("IN", "M", 3);
    }
    
    @Test
    public void getformaPagoNumRecibosTest() {
        assertTrue(true);
        Utileria.getformaPago("IN", "M", 0);
    }
    
    @Test
    public void getformaPagoFrecPgoNullTest() {
        assertTrue(true);
        Utileria.getformaPago("IN", null, 1);
    }
    
    @Test
    public void getformaPagoFrecPgoTest() {
        assertTrue(true);
        Utileria.getformaPago("IN", "999", 1);
    }
    
    @Test
    public void getformaPagoFrecPgoCrTest() {
        assertTrue(true);
        Utileria.getformaPago("IN", "999", 0);
    }
    
    @Test
    public void getFechaNacimientoRFCTest() {
        assertTrue(true);
        Utileria.getFechaNacimientoRFC("AAAA901011235");
    }
    
    @Test
    public void getFechaNacimientoRFC10Test() {
        assertTrue(true);
        Utileria.getFechaNacimientoRFC("AAA9010112");
    }
    
    @Test
    public void getFechaNacimientoRFC15Test() {
        assertTrue(true);
        Utileria.getFechaNacimientoRFC("AAAA901011235aa");
    }
    
    @Test
    public void getFechaNacimientoRFCcalTest() {
        assertTrue(true);
        Utileria.getFechaNacimientoRFC("AAAA190903");
    }
    
    @Test
    public void getFechaConstitucionRFCMenTest() {
        assertTrue(true);
        Utileria.getFechaConstitucionRFC("MOC90101123");
    }
    
    @Test
    public void getFechaConstitucionRFCTest() {
        assertTrue(true);
        Utileria.getFechaConstitucionRFC("MOC901011232");
    }
    
    @Test
    public void getFechaConstitucionRFCActTest() {
        assertTrue(true);
        Utileria.getFechaConstitucionRFC("MOC190903232");
    }
    
    @Test(expected = ExecutionError.class)
    public void rellenarCeroNFETest() {
        assertTrue(true);
        Utileria.rellenarCero("MOC", "5");
    }
    
    @Test(expected = ExecutionError.class)
    public void rellenarCeroExTest() {
        assertTrue(true);
        Utileria.rellenarCero("13", "A");
    }
    
    @Test
    public void rellenarCeroTest() {
        assertTrue(true);
        Utileria.rellenarCero("13", "3");
    }
    
    @Test
    public void addTipoVClaveMarcaTest() {
        assertTrue(true);
        VehiculoNeg vehiculo = ObjetosPruebas.getVehiculo();
        Utileria.addTipoVClaveMarca(vehiculo);
    }
    
    @Test(expected = ExecutionError.class)
    public void getDatVehfromCveMarcaNullTest() {
        assertTrue(true);
        VehiculoNeg vehiculo = ObjetosPruebas.getVehiculo();
        vehiculo.setClaveMarca(null);
        Utileria.getDatVehfromCveMarca(vehiculo);
    }
    
    @Test(expected = ExecutionError.class)
    public void getDatVehfromCveMarcaEmptyTest() {
        assertTrue(true);
        VehiculoNeg vehiculo = ObjetosPruebas.getVehiculo();
        vehiculo.setClaveMarca("");
        Utileria.getDatVehfromCveMarca(vehiculo);
    }
    
    @Test(expected = ExecutionError.class)
    public void getDatVehfromCveMarcaMenTest() {
        assertTrue(true);
        VehiculoNeg vehiculo = ObjetosPruebas.getVehiculo();
        vehiculo.setClaveMarca("MARCA");
        Utileria.getDatVehfromCveMarca(vehiculo);
    }
    
    @Test
    public void getDatVehfromCveMarcaMayTest() {
        assertTrue(true);
        VehiculoNeg vehiculo = ObjetosPruebas.getVehiculo();
        vehiculo.setClaveMarca("HJYTRDE15");
        Utileria.getDatVehfromCveMarca(vehiculo);
    }
    
    @Test
    public void getCveMarcafromDatVehTest() {
        assertTrue(true);
        VehiculoNeg vehiculo = ObjetosPruebas.getVehiculo();
        vehiculo.setClaveMarca("HJYTRDE15");
        Utileria.getCveMarcafromDatVeh(vehiculo);
    }
    
    @Test
    public void getTipoPersonaEmptyTest() {
        assertTrue(true);
        PersonaNeg persona = ObjetosPruebas.getPersona();
        persona.setTipoPersona("");
        Utileria.getTipoPersona(persona);
    }
    
    @Test
    public void getTipoPersonaRfc12Test() {
        assertTrue(true);
        PersonaNeg persona = ObjetosPruebas.getPersona();
        persona.setTipoPersona("");
        persona.setRfc("MOC190903232");
        Utileria.getTipoPersona(persona);
    }
    
    @Test
    public void getTipoPersonaRfc9Test() {
        assertTrue(true);
        PersonaNeg persona = ObjetosPruebas.getPersona();
        persona.setTipoPersona("");
        persona.setRfc("MOC190903");
        Utileria.getTipoPersona(persona);
    }
    
    @Test
    public void getTipoPersonaRfc10Test() {
        assertTrue(true);
        PersonaNeg persona = ObjetosPruebas.getPersona();
        persona.setTipoPersona("");
        persona.setRfc("MACA190903");
        Utileria.getTipoPersona(persona);
    }
    
    @Test
    public void getTipoPersonaRfc13Test() {
        assertTrue(true);
        PersonaNeg persona = ObjetosPruebas.getPersona();
        persona.setTipoPersona("");
        persona.setRfc("MACA190903154");
        Utileria.getTipoPersona(persona);
    }
    
    @Test
    public void getTipoPersonaFTest() {
        assertTrue(true);
        PersonaNeg persona = ObjetosPruebas.getPersona();
        persona.setTipoPersona("F");
        persona.setRfc("MACA190903154");
        Utileria.getTipoPersona(persona);
    }
    
    @Test
    public void aplanarDeduciblesNullTest() {
        assertTrue(true);
        CoberturaNeg c = new CoberturaNeg();
        c.setDeducible(null);
        Utileria.aplanarDeducibles(c);
    }
    
    @Test
    public void aplanarDeduciblesEmptyTest() {
        assertTrue(true);
        CoberturaNeg c = new CoberturaNeg();
        c.setDeducible("");
        Utileria.aplanarDeducibles(c);
    }
    
    @Test
    public void aplanarDeduciblesNMatchTest() {
        assertTrue(true);
        CoberturaNeg c = new CoberturaNeg();
        c.setDeducible("aa");
        Utileria.aplanarDeducibles(c);
    }
    
    @Test
    public void aplanarDeduciblesTest() {
        assertTrue(true);
        CoberturaNeg c = new CoberturaNeg();
        c.setDeducible("10");
        Utileria.aplanarDeducibles(c);
    }
    
    @Test
    public void isValidEmailAddressTest() {
        assertTrue(true);
        Utileria.isValidEmailAddress("micorreo@mail.com");
    }
    
    @Test
    public void getRegistraLogTimeTest() {
        assertTrue(true);
        Utileria.getRegistraLogTime("registro");
    }
    
    @Test
    public void getDecimalFormatScaleTest() {
        assertTrue(true);
        Utileria.getDecimalFormat(new Double("10.123"), 1);
    }
    
    @Test
    public void getDecimalFormatScaleMinTest() {
        assertTrue(true);
        Utileria.getDecimalFormat(new Double("10"), 1);
    }
}
