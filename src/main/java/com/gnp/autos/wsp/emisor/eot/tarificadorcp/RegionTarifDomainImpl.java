package com.gnp.autos.wsp.emisor.eot.tarificadorcp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gnp.autos.wsp.emisor.eot.calcp.wsdl.CodigoPostalDto;
import com.gnp.autos.wsp.emisor.eot.calcp.wsdl.ConsultaRegionTarificacionPorCp;
import com.gnp.autos.wsp.emisor.eot.calcp.wsdl.ConsultaRegionTarificacionPorCpResponse;
import com.gnp.autos.wsp.emisor.eot.config.ConfWSP;
import com.gnp.autos.wsp.emisor.eot.soap.service.RegionTarifClient;

/**
 * The Class RegionTarifDomainImpl.
 */
@Service
public class RegionTarifDomainImpl implements RegionTarifDomain {
    /** The client. */
    @Autowired
    private RegionTarifClient client;
    
    /** The conf. */
    @Autowired
    private ConfWSP conf;
    
    /**
     * Gets the region tarif.
     *
     * @param tid the tid
     * @param cp the cp
     * @return the region tarif
     */
    @Override
    public String getRegionTarif(final Integer tid, final String cp) {
        ConsultaRegionTarificacionPorCp reqDatos = obtenRegionTarif(cp);
        client.setDefaultUri(conf.getUrlConsultaCP());
        ConsultaRegionTarificacionPorCpResponse resSOAP;
        resSOAP = client.getRegTarif(reqDatos, tid, conf.getUrlTransacciones());
        return resSOAP.getRegionesCp().get(0).getREGIONTARIFICACION();
    }
    
    /**
     * Obten region tarif.
     *
     * @param cp the cp
     * @return the consulta region tarificacion por cp
     */
    private static ConsultaRegionTarificacionPorCp obtenRegionTarif(final String cp) {
        ConsultaRegionTarificacionPorCp reqRegion = new ConsultaRegionTarificacionPorCp();
        List<CodigoPostalDto> listaCp = new ArrayList<>();
        CodigoPostalDto cdp = new CodigoPostalDto();
        cdp.getCODIGOPOSTAL().add(cp);
        listaCp.add(cdp);
        reqRegion.setCodigoPostal(listaCp.get(0));
        return reqRegion;
    }
}