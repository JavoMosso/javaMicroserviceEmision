package com.gnp.autos.wsp.emisor.eot.tarificadorcp;

/**
 * The Interface RegionTarifDomain.
 */
public interface RegionTarifDomain {
    /**
     * Gets the region tarif.
     *
     * @param tid the tid
     * @param cp the cp
     * @return the region tarif
     */
    String getRegionTarif(Integer tid, String cp);
}