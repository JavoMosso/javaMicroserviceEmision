package com.gnp.autos.wsp.emisor.eot.util;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ConstantesTest {
    @Test
    public void testCtes() {
        assertNotNull(Constantes.CVE_TRANSACCION);
        assertNotNull(Constantes.UNO);
        assertNotNull(Constantes.CANCELANEG);
        assertNotNull(Constantes.AUTOS);
        assertNotNull(Constantes.VACIO);
        assertNotNull(Constantes.SISTEMA);
        assertNotNull(Constantes.EDOPOLIZA);
        assertNotNull(Constantes.VERSIONPOL);
        assertNotNull(Constantes.STRFORMATOFCH);
        assertNotNull(Constantes.REFERENCIA_EXTERNA);
    }
}
