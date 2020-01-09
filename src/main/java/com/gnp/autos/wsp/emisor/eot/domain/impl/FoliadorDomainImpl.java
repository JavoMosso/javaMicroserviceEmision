package com.gnp.autos.wsp.emisor.eot.domain.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gnp.autos.wsp.emisor.eot.config.ConfWSP;
import com.gnp.autos.wsp.emisor.eot.domain.FoliadorDomain;
import com.gnp.autos.wsp.emisor.eot.foliador.wsdl.Foliador;
import com.gnp.autos.wsp.emisor.eot.foliador.wsdl.FoliadorResponse;
import com.gnp.autos.wsp.emisor.eot.soap.service.FoliadorClient;

/**
 * The Class FoliadorDomainImpl.
 */
@Service
public class FoliadorDomainImpl implements FoliadorDomain {
    /** The client. */
    @Autowired
    private FoliadorClient client;
    
    /** The conf. */
    @Autowired
    private ConfWSP conf;
    
    /**
     * Gets the folio.
     *
     * @param tid the tid
     * @param clave the clave
     * @return the folio
     */
    @Override
    public String getFolio(final Integer tid, final String clave) {
        client.setDefaultUri(conf.getUrlFoliador());
        FoliadorResponse response = client.getFolio(obtenerRequest(clave), tid, conf.getUrlTransacciones());
        return response.getIDTRANSACCION();
    }
    
    /**
     * Obtener request.
     *
     * @param clave the clave
     * @return the foliador
     */
    protected static Foliador obtenerRequest(final String clave) {
        Foliador req = new Foliador();
        req.setCVETIPOTRANSACCION(clave);
        return req;
    }
}