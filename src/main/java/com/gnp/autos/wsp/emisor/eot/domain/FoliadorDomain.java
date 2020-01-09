package com.gnp.autos.wsp.emisor.eot.domain;

/**
 * The Interface FoliadorDomain.
 */
public interface FoliadorDomain {
    /**
     * Gets the folio.
     *
     * @param tid the tid
     * @param clave the clave
     * @return the folio
     */
    String getFolio(Integer tid, String clave);
}