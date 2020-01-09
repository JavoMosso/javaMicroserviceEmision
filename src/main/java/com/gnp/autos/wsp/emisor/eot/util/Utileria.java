package com.gnp.autos.wsp.emisor.eot.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.Source;

import org.apache.log4j.Logger;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.SoapFaultDetailElement;
import org.springframework.ws.soap.client.SoapFaultClientException;

import com.gnp.autos.wsp.emisor.eot.error.ExecutionError;
import com.gnp.autos.wsp.negocio.neg.model.CoberturaNeg;
import com.gnp.autos.wsp.negocio.neg.model.PersonaNeg;
import com.gnp.autos.wsp.negocio.neg.model.VehiculoNeg;
import com.gnp.autos.wsp.negocio.util.Utils;

/**
 * The Class Utileria.
 */
public final class Utileria {
    /** The Constant TRES. */
    public static final int TRES = 3;

    /** The Constant CUATRO. */
    public static final int CUATRO = 4;

    /** The Constant CINCO. */
    public static final int CINCO = 5;

    /** The Constant SEIS. */
    public static final int SEIS = 6;

    /** The Constant SIETE. */
    public static final int SIETE = 7;

    /** The Constant NUEVE. */
    public static final int NUEVE = 9;

    /** The Constant OCHO. */
    public static final int OCHO = 8;

    /** The Constant DIEZ. */
    public static final int DIEZ = 10;

    /** The Constant DOCE. */
    public static final int DOCE = 12;

    /** The Constant TRECE. */
    public static final int TRECE = 13;

    /** The Constant MENOS_CIEN. */
    public static final int MENOS_CIEN = -100;

    /**
     * Instantiates a new utileria.
     */
    private Utileria() {
    }

    /**
     * Checks if is dateaaaammdd.
     *
     * @param strFecha the str fecha
     * @return true, if is dateaaaammdd
     */
    public static boolean isDateaaaammdd(final String strFecha) {
        try {
            new SimpleDateFormat(Constantes.STRFORMATOFCH).parse(strFecha);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Gets the fecha.
     *
     * @param strFecha the str fecha
     * @return the fecha
     */
    public static Date getFecha(final String strFecha) {
        Date fecha = null;
        try {
            SimpleDateFormat d = new SimpleDateFormat(Constantes.STRFORMATOFCH);
            fecha = d.parse(strFecha);
        } catch (ParseException ex) {
            throw new ExecutionError(Constantes.ERROR_4, ex);
        }
        return fecha;
    }

    /**
     * Gets the str fecha.
     *
     * @param fecha the fecha
     * @return the str fecha
     */
    public static String getStrFecha(final Date fecha) {
        String strFecha;
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
            strFecha = format1.format(fecha);
        } catch (Exception ex) {
            throw new ExecutionError(Constantes.ERROR_4, ex);
        }
        return strFecha;
    }

    /**
     * Gets the fec nac.
     *
     * @param edad the edad
     * @return the fec nac
     */
    public static String getFecNac(final int edad) {
        String strFecNac = "";
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.YEAR, edad * -1);
            calendar.set(Calendar.MONTH, 0);
            calendar.set(Calendar.DATE, 0);
            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
            strFecNac = format1.format(calendar.getTime());
        } catch (Exception ex) {
            throw new ExecutionError(Constantes.ERROR_4, ex);
        }
        return strFecNac;
    }

    /**
     * Checks if is numeric.
     *
     * @param s the s
     * @return true, if is numeric
     */
    public static boolean isNumeric(final String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }

    /**
     * Validar curp.
     *
     * @param curp the curp
     * @return true, if successful
     */
    public static boolean validarCurp(final String curp) {
        return curp.toUpperCase().trim().matches("[A-Z]{4}[0-9]{6}[H,M][A-Z]{5}[0-9]{2}");
    }

    /**
     * Validar RFC fisica.
     *
     * @param rfc the rfc
     * @return true, if successful
     */
    public static boolean validarRFCFisica(final String rfc) {
        boolean result;
        result = validarRfcExp(rfc, "F");
        if (result) {
            List<String> listaNegra = Arrays.asList("BUEI", "BUEY", "CACA", "CACO", "CAGA", "CAGO", "CAKA", "CAKO",
                    "COGE", "COJA", "COJE", "COJI", "COJO", "CULO", "FETO", "GUEY", "JOTO", "KACA", "KACO", "KAGA",
                    "KAGO", "KAKA", "KOGE", "KOJO", "KULO", "MAME", "MAMO", "MEAR", "MEAS", "MEON", "MION", "MOCO",
                    "MULA", "PEDA", "PEDO", "PENE", "PUTA", "PUTO", "QULO", "RATA", "RUIN");
            return !listaNegra.stream().anyMatch(p -> p.equals(rfc.substring(0, CUATRO)));
        }
        return result;
    }

    /**
     * Validar RFC moral.
     *
     * @param rfc the rfc
     * @return true, if successful
     */
    public static boolean validarRFCMoral(final String rfc) {
        boolean result;
        result = validarRfcExp(rfc, "M");
        return result;
    }

    /**
     * Validar rfc exp.
     *
     * @param rfc  the rfc
     * @param tipo the tipo
     * @return true, if successful
     */
    public static boolean validarRfcExp(final String rfc, final String tipo) {
        String tipoPersonaDefault = "F";
        return tipo.equals(tipoPersonaDefault)
                ? rfc.toUpperCase().trim()
                        .matches("[A-Z,,&]{4}[0-9]{2}[0-1][0-9][0-3][0-9][A-Z,0-9]?[A-Z,0-9]?[0-9,A-Z]?")
                : rfc.toUpperCase().trim()
                        .matches("[A-Z,,&]{3}[0-9]{2}[0-1][0-9][0-3][0-9][A-Z,0-9]?[A-Z,0-9]?[0-9,A-Z]?");
    }

    /**
     * Gets the edad.
     *
     * @param strFecNac the str fec nac
     * @return the edad
     */
    public static int getEdad(final String strFecNac) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constantes.STRFORMATOFCH);
        LocalDate fecNac = LocalDate.parse(strFecNac, formatter);
        LocalDate ahora = LocalDate.now();
        Period periodo = Period.between(fecNac, ahora);
        return periodo.getYears();
    }

    /**
     * Gets the greg cal.
     *
     * @param strFecNac the str fec nac
     * @return the greg cal
     */
    public static XMLGregorianCalendar getGregCal(final String strFecNac) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(getFecha(strFecNac));
        XMLGregorianCalendar calXML;
        try {
            calXML = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        } catch (DatatypeConfigurationException e) {
            throw new ExecutionError(Constantes.ERROR_3, e);
        }
        return calXML;
    }

    /**
     * Gets the greg cal short.
     *
     * @param strFecNac the str fec nac
     * @return the greg cal short
     */
    public static XMLGregorianCalendar getGregCalShort(final String strFecNac) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(getFecha(strFecNac));
        XMLGregorianCalendar calXML;
        try {
            calXML = DatatypeFactory.newInstance().newXMLGregorianCalendarDate(cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), DatatypeConstants.FIELD_UNDEFINED);
        } catch (DatatypeConfigurationException e) {
            throw new ExecutionError(Constantes.ERROR_3, e);
        }
        return calXML;
    }

    /**
     * Solo numeros.
     *
     * @param referencia the referencia
     * @return the string
     */
    public static String soloNumeros(final String referencia) {
        if (referencia == null) {
            return null;
        }
        return referencia.replaceAll("[^0-9.]", "");
    }

    /**
     * Solo letras.
     *
     * @param texto the texto
     * @return the string
     */
    public static String soloLetras(final String texto) {
        return texto.replaceAll("[^A-Za-z ]+", "");
    }

    /**
     * Solo alfa numerico.
     *
     * @param texto the texto
     * @return the string
     */
    public static String soloAlfaNumerico(final String texto) {
        return texto.replaceAll("[^A-Za-z0-9]+", "");
    }

    /**
     * Checks if is moral contratante.
     *
     * @param personas the personas
     * @return the list
     */
    public static List<PersonaNeg> isMoralContratante(final List<PersonaNeg> personas) {
        personas.stream().forEach(Utileria::actualizaMoral);
        return personas;
    }

    /**
     * Actualiza moral.
     *
     * @param persona the persona
     * @return the persona neg
     */
    public static PersonaNeg actualizaMoral(final PersonaNeg persona) {
        String strCONTRATANTE = "CONTRATANTE";
        String strM = "M";
        String strJ = "J";
        if (persona.getTipo().equalsIgnoreCase(strCONTRATANTE) && (persona.getTipoPersona().equalsIgnoreCase(strM)
                || persona.getTipoPersona().equalsIgnoreCase(strJ))) {
            persona.setRazonSocial(persona.getNombre());
            persona.setTipoDocumento("R");
            persona.setNumeroIdentificacion(persona.getRfc());
            persona.setNombre("");
        }
        return persona;
    }

    /**
     * Checks if is fisica.
     *
     * @param personas the personas
     * @return the list
     */
    public static List<PersonaNeg> isFisica(final List<PersonaNeg> personas) {
        personas.stream().forEach(Utileria::actualizaFisica);
        return personas;
    }

    /**
     * Actualiza fisica.
     *
     * @param persona the persona
     * @return the persona neg
     */
    public static PersonaNeg actualizaFisica(final PersonaNeg persona) {
        String strF = "F";
        if (persona.getTipoPersona().equalsIgnoreCase(strF)) {
            persona.setNombre(soloLetras(persona.getNombre()));
            persona.setAPaterno(soloLetras(persona.getAPaterno()));
            persona.setAMaterno(soloLetras(persona.getAMaterno()));
        }
        return persona;
    }

    /**
     * Format calle.
     *
     * @param calle the calle
     * @return the string
     */
    public static String formatCalle(final String calle) {
        return calle.replaceAll("NO. EXTERIOR:", "").replaceAll("NO. INTERIOR:", "");
    }

    /**
     * Format no exterior.
     *
     * @param noExt the no ext
     * @return the string
     */
    public static String formatNoExterior(final String noExt) {
        return noExt.replaceAll("-", "").replaceAll(" ", "");
    }

    /**
     * Separa nombre.
     *
     * @param persona the persona
     * @return the persona neg
     */
    public static PersonaNeg separaNombre(final PersonaNeg persona) {
        String nombreCompleto = persona.getNombre();
        String[] arrNombre = nombreCompleto.split(" ");
        if (arrNombre.length < 2) {
            return persona;
        }
        persona.setAMaterno(arrNombre[arrNombre.length - 1].trim());
        persona.setAPaterno(arrNombre[arrNombre.length - 2].trim());
        persona.setNombre(nombreCompleto.split(persona.getAPaterno())[0].trim());
        return persona;
    }

    /**
     * Gets the forma pago.
     *
     * @param formaPago  the forma pago
     * @param frecPago   the frec pago
     * @param numRecibos the num recibos
     * @return the forma pago
     */
    public static String getformaPago(final String formaPago, final String frecPago, final int numRecibos) {
        String str999 = "999";
        if (frecPago != null && frecPago.equals(str999) && numRecibos >= 1) {
            return "A";
        }
        return formaPago;
    }

    /**
     * Gets the fecha nacimiento RFC.
     *
     * @param rfc the rfc
     * @return the fecha nacimiento RFC
     */
    public static String getFechaNacimientoRFC(final String rfc) {
        String strFecNac = "";
        if ((rfc.length() == DIEZ || rfc.length() == TRECE) && validarRFCFisica(rfc)) {
            int anio = Integer.parseInt(rfc.substring(CUATRO, SEIS));
            int mes = Integer.parseInt(rfc.substring(SEIS, OCHO));
            int dia = Integer.parseInt(rfc.substring(OCHO, DIEZ));
            Calendar calendarH = Calendar.getInstance();
            Calendar calendar = Calendar.getInstance();
            calendarH.setTime(new Date());
            SimpleDateFormat format1 = new SimpleDateFormat(Constantes.STRFORMATOFCH);
            String strFecHoy = format1.format(calendarH.getTime());
            Integer siglo = Integer.parseInt(strFecHoy.substring(0, 2) + "00");
            anio = siglo + anio;
            calendar.set(anio, mes - 1, dia);
            if (calendar.after(calendarH)) {
                calendar.add(Calendar.YEAR, MENOS_CIEN);
            }
            strFecNac = format1.format(calendar.getTime());
        }
        return strFecNac;
    }

    /**
     * Gets the fecha constitucion RFC.
     *
     * @param rfc the rfc
     * @return the fecha constitucion RFC
     */
    public static String getFechaConstitucionRFC(final String rfc) {
        String strFecCon = "";
        if ((rfc.length() == DOCE) && validarRFCMoral(rfc)) {
            int aniof = Integer.parseInt(rfc.substring(TRES, CINCO));
            int mesf = Integer.parseInt(rfc.substring(CINCO, SIETE));
            int diaf = Integer.parseInt(rfc.substring(SIETE, NUEVE));
            Calendar calendarH = Calendar.getInstance();
            Calendar calendar = Calendar.getInstance();
            calendarH.setTime(new Date());
            SimpleDateFormat format1 = new SimpleDateFormat(Constantes.STRFORMATOFCH);
            String strFecHoy = format1.format(calendarH.getTime());
            Integer siglo = Integer.parseInt(strFecHoy.substring(0, 2) + "00");
            aniof = siglo + aniof;
            calendar.set(aniof, mesf - 1, diaf);
            if (calendar.after(calendarH)) {
                calendar.add(Calendar.YEAR, MENOS_CIEN);
            }
            strFecCon = format1.format(calendar.getTime());
        }
        return strFecCon;
    }

    /**
     * Rellenar cero.
     *
     * @param cadena   the cadena
     * @param longitud the longitud
     * @return the string
     */
    public static String rellenarCero(final String cadena, final String longitud) {
        String patron = "%0" + longitud + "d";
        String result;
        try {
            result = String.format(patron, new BigInteger(cadena));
        } catch (NumberFormatException ex) {
            throw new ExecutionError(Constantes.ERROR_3, "Dato incorrecto");
        } catch (Exception ex) {
            Logger.getRootLogger().info(ex);
            throw new ExecutionError(Constantes.ERROR_3, "Dato incorrecto");
        }
        return result;
    }

    /**
     * Adds the tipo V clave marca.
     *
     * @param objVeh the obj veh
     * @return the vehiculo neg
     */
    public static VehiculoNeg addTipoVClaveMarca(final VehiculoNeg objVeh) {
        objVeh.setClaveMarca(objVeh.getTipoVehiculo() + objVeh.getClaveMarca());
        return objVeh;
    }

    /**
     * Gets the dat vehfrom cve marca.
     *
     * @param objVeh the obj veh
     * @return the dat vehfrom cve marca
     */
    public static VehiculoNeg getDatVehfromCveMarca(final VehiculoNeg objVeh) {
        String cveMarca = objVeh.getClaveMarca();
        if (cveMarca == null || cveMarca.length() < NUEVE || cveMarca.isEmpty()) {
            throw new ExecutionError(Constantes.ERROR_3, "La clave de marca debe ser de 9 caracteres");
        }
        objVeh.setArmadora(cveMarca.substring(TRES, CINCO));
        objVeh.setCarroceria(cveMarca.substring(CINCO, SIETE));
        objVeh.setVersion(cveMarca.substring(SIETE, NUEVE));
        return objVeh;
    }

    /**
     * Gets the cve marcafrom dat veh.
     *
     * @param objVeh the obj veh
     * @return the cve marcafrom dat veh
     */
    public static VehiculoNeg getCveMarcafromDatVeh(final VehiculoNeg objVeh) {
        String cveMarca = objVeh.getTipoVehiculo() + objVeh.getArmadora() + objVeh.getCarroceria()
                + objVeh.getVersion();
        objVeh.setClaveMarca(cveMarca);
        return objVeh;
    }

    /**
     * Gets the tipo persona.
     *
     * @param persona the persona
     * @return the tipo persona
     */
    public static PersonaNeg getTipoPersona(final PersonaNeg persona) {
        if (persona.getTipoPersona().isEmpty()) {
            persona.setTipoPersona("F");
            if (persona.getRfc().length() == DOCE || persona.getRfc().length() == NUEVE) {
                persona.setTipoPersona("M");
            }
            if (persona.getRfc().length() == DIEZ || persona.getRfc().length() == TRECE) {
                persona.setTipoPersona("F");
            }
        }
        return persona;
    }

    /**
     * Aplanar deducibles.
     *
     * @param c the c
     */
    public static void aplanarDeducibles(final CoberturaNeg c) {
        if (c.getDeducible() == null || c.getDeducible().isEmpty()) {
            return;
        }
        Pattern p = Pattern.compile("\\+?\\-?([0-9]+).?[0-9]*");
        Matcher match = p.matcher(c.getDeducible());
        if (match.matches()) {
            c.setDeducible(match.group(1));
        }
    }

    /**
     * Checks if is valid email address.
     *
     * @param email the email
     * @return true, if is valid email address
     */
    public static boolean isValidEmailAddress(final String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}"
                + "\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * Gets the registra log time.
     *
     * @param registro the registro
     */
    public static void getRegistraLogTime(final String registro) {
        Timestamp tsReq = new Timestamp(new Date().getTime());
        Logger.getRootLogger().info(registro + ":" + tsReq);
    }

    /**
     * Gets the decimal format.
     *
     * @param val   the val
     * @param scale the scale
     * @return the decimal format
     */
    public static BigDecimal getDecimalFormat(final Double val, final Integer scale) {
        BigDecimal bd = new BigDecimal(val);
        if (bd.scale() > scale) {
            bd = bd.setScale(scale, RoundingMode.HALF_UP);
        }
        return bd;
    }

    /**
     * Gets the XML date of str.
     *
     * @param dateToBeParsed the date to be parsed
     * @return the XML date of str
     */
    public static XMLGregorianCalendar getXMLDateOfStr(final String dateToBeParsed) {
        XMLGregorianCalendar date2 = null;
        try {
            String format = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            Date date = simpleDateFormat.parse(dateToBeParsed);
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(date);
            date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
            date2.setTimezone(0);
        } catch (ParseException | DatatypeConfigurationException e) {
            Logger.getRootLogger().info(e);
            throw new ExecutionError(2, "Fecha invalida.");
        }
        return date2;
    }

    /**
     * Gets the XML date now.
     *
     * @return the XML date now
     */
    public static XMLGregorianCalendar getXMLDateNow() {
        XMLGregorianCalendar date2 = null;
        try {
            Date fecha = new Date();
            GregorianCalendar gregoCalen = new GregorianCalendar();
            gregoCalen.setTime(fecha);
            date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregoCalen);
        } catch (DatatypeConfigurationException e) {
            Logger.getRootLogger().info(e);
            throw new ExecutionError(2, "Fecha invalida.");
        }
        return date2;
    }

    /**
     * Gets the str now.
     *
     * @return the str now
     */
    public static String getStrNow() {
        // Utileria.getStrNow()
        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("yyyyMMdd");
        return formateador.format(ahora);
    }

    /**
     * String to boolean.
     *
     * @param obj the obj
     * @return the boolean
     */
    public static Boolean stringToBoolean(final String obj) {
        return Optional.ofNullable(obj).isPresent() && obj.equalsIgnoreCase(Constantes.UNO);

    }

    /**
     * Existe valor.
     *
     * @param obj the obj
     * @return true, if successful
     */
    public static boolean existeValor(final Object obj) {
        if (obj instanceof String) {
            return !((String) obj).isEmpty();
        }
        if (obj instanceof List) {
            return !((List<?>) obj).isEmpty();
        }
        return Optional.ofNullable(obj).isPresent();
    }

    /**
     * Error interp.
     *
     * @param <T>     the generic type
     * @param sf      the sf
     * @param gateway the gateway
     * @return the t
     */
    public static <T> T errorInterp(final SoapFaultClientException sf, final WebServiceGatewaySupport gateway) {
        Logger.getRootLogger().info(sf);
        try {
            SoapFaultDetail dtls = sf.getSoapFault().getFaultDetail();
            SoapFaultDetailElement detailElementChld = dtls.getDetailEntries().next();
            Source detailSrc = detailElementChld.getSource();
            @SuppressWarnings("unchecked")
            JAXBElement<T> detail = (JAXBElement<T>) gateway.getWebServiceTemplate().getUnmarshaller()
                    .unmarshal(detailSrc);
            return detail.getValue();
        } catch (Exception ex1) {
            Logger.getRootLogger().info(ex1);
            throw new ExecutionError(1, ex1.getMessage());
        }

    }

    /**
     * Checks if is tarjeta encriptada.
     *
     * @param numTarjeta the num tarjeta
     * @return true, if is tarjeta encriptada
     */
    public static boolean isTarjetaEncriptada(final String numTarjeta) {
        return !Utils.isEmpty(soloLetras(numTarjeta));
    }
}