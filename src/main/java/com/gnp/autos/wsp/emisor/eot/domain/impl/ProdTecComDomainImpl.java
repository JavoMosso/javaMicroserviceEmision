package com.gnp.autos.wsp.emisor.eot.domain.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gnp.autos.wsp.emisor.eot.TecCom.wsdl.CatalogoTallerGenericoDto;
import com.gnp.autos.wsp.emisor.eot.TecCom.wsdl.CatalogosTallerGenericoDto;
import com.gnp.autos.wsp.emisor.eot.TecCom.wsdl.ConsultarTallerGenericoRequest;
import com.gnp.autos.wsp.emisor.eot.TecCom.wsdl.ConsultarTallerGenericoResponse;
import com.gnp.autos.wsp.emisor.eot.TecCom.wsdl.ParametroDto;
import com.gnp.autos.wsp.emisor.eot.TecCom.wsdl.ParametrosRequestMdDto;
import com.gnp.autos.wsp.emisor.eot.domain.ProdTecComDomain;
import com.gnp.autos.wsp.emisor.eot.error.ExecutionError;
import com.gnp.autos.wsp.emisor.eot.soap.service.ProdTecComClient;
import com.gnp.autos.wsp.emisor.eot.util.Utileria;

/**
 * The Class ProdTecComDomainImpl.
 */
@Service
public class ProdTecComDomainImpl implements ProdTecComDomain {
    /** The client. */
    @Autowired
    private ProdTecComClient client;

    /**
     * Gets the prod tec com.
     *
     * @param urlTInter the url T inter
     * @param urlProdTC the url prod TC
     * @param tid the tid
     * @param idUMO the id UMO
     * @return the prod tec com
     */
    @Override
    public String getProdTecCom(final String urlTInter, final String urlProdTC, final Integer tid, final String idUMO) {

        ConsultarTallerGenericoRequest requestDatos = new ConsultarTallerGenericoRequest();
        CatalogosTallerGenericoDto ctg = new CatalogosTallerGenericoDto();
        List<CatalogosTallerGenericoDto> lctg = obtenRequestDatos(idUMO);
        ctg.getCATALOGO().add(lctg.get(0).getCATALOGO().get(0));
        requestDatos.setCATALOGOS(ctg);
        client.setDefaultUri(urlProdTC);
        ConsultarTallerGenericoResponse resSOAP;
        resSOAP = client.getProdTecCom(requestDatos, urlTInter, tid);
        if (!Utileria.existeValor(resSOAP.getCATALOGOS().getCATALOGO().get(0).getVALORESELEMENTO())) {
            throw new ExecutionError(2, "No se encontro producto tecnico");
        }

        return resSOAP.getCATALOGOS().getCATALOGO().get(0).getVALORESELEMENTO().get(0).getDESCRIPCIONVALOR();
    }

    /**
     * Obten request datos.
     *
     * @param idUMO the id UMO
     * @return the list
     */
    private static List<CatalogosTallerGenericoDto> obtenRequestDatos(final String idUMO) {
        List<CatalogosTallerGenericoDto> cstg = new ArrayList<>();
        CatalogosTallerGenericoDto cats = new CatalogosTallerGenericoDto();
        CatalogoTallerGenericoDto ctg = new CatalogoTallerGenericoDto();
        ctg.setIDELEMENTO("PROD_TECCOME");
        ParametroDto pd = new ParametroDto();
        pd.setNOMBREPARAMETRO("CDN_NEGOPERA");
        pd.setVALORPARAMETRO(idUMO);
        ParametrosRequestMdDto param = new ParametrosRequestMdDto();
        param.getPARAMETRO().add(pd);
        ctg.setPARAMETROS(param);
        cats.getCATALOGO().add(ctg);
        cstg.add(cats);
        return cstg;
    }
}