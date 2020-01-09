package com.gnp.autos.wsp.emisor.eot.domain;

/**
 * The Interface ProdTecComDomain.
 */
@FunctionalInterface
public interface ProdTecComDomain {
    /**
     * Gets the prod tec com.
     *
     * @param urlTInter the url T inter
     * @param urlProdTC the url prod TC
     * @param tid the tid
     * @param idUMO the id UMO
     * @return the prod tec com
     */
    String getProdTecCom(String urlTInter, String urlProdTC, Integer tid, String idUMO);
}