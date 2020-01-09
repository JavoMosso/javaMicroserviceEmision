package com.gnp.autos.wsp.emisor.eot.pasos;

import com.gnp.autos.wsp.emisor.eot.pasos.wsdl.ConsultaPasosConfiguradorResponse;
import com.gnp.autos.wsp.negocio.neg.model.EmiteNegReq;

/**
 * The Interface PasosDomain.
 */
public interface PasosDomain {
    /**
     * Gets the tipo carga.
     *
     * @param negReq the neg req
     * @param tid the tid
     * @param urlTInter the url T inter
     * @return the tipo carga
     */
    ConsultaPasosConfiguradorResponse getTipoCarga(EmiteNegReq negReq, Integer tid, String urlTInter);
}