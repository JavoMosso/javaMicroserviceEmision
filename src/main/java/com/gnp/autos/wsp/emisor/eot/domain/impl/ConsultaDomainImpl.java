package com.gnp.autos.wsp.emisor.eot.domain.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gnp.autos.wsp.emisor.eot.consulta.wsdl.Cabecera;
import com.gnp.autos.wsp.emisor.eot.consulta.wsdl.ConsultarPolizaPorNumPoliza;
import com.gnp.autos.wsp.emisor.eot.consulta.wsdl.ConsultarPolizaPorNumPolizaRequest;
import com.gnp.autos.wsp.emisor.eot.consulta.wsdl.ConsultarPolizaPorNumPolizaResponse;
import com.gnp.autos.wsp.emisor.eot.domain.ConsultaDomain;
import com.gnp.autos.wsp.emisor.eot.soap.service.ConsultaClient;
import com.gnp.autos.wsp.emisor.eot.util.Constantes;

/**
 * The Class ConsultaDomainImpl.
 */
@Service
public class ConsultaDomainImpl implements ConsultaDomain {
    /** The client. */
    @Autowired
    private ConsultaClient client;
    
    /**
     * Se consulta poliza, este servicio no sera necesario cuando se pueda leer
     * el response de cancelacion ya que el estado lo regresa como null.
     *
     * @param numPoliza the num poliza
     * @param idTransaccion the id transaccion
     * @param tid the tid
     * @return the consultar poliza por num poliza response
     */
    @Override
    public ConsultarPolizaPorNumPolizaResponse consultarPoliza(final String numPoliza, final String idTransaccion,
            final Integer tid) {
        ConsultarPolizaPorNumPolizaRequest request = obtenerConsultaReq(numPoliza, idTransaccion);
        return client.getPoliza(request, tid);
    }
    
    /**
     * Obtener consulta req.
     *
     * @param numPoliza the num poliza
     * @param idTransaccion the id transaccion
     * @return the consultar poliza por num poliza request
     */
    private static ConsultarPolizaPorNumPolizaRequest obtenerConsultaReq(final String numPoliza,
            final String idTransaccion) {
        ConsultarPolizaPorNumPolizaRequest consult = new ConsultarPolizaPorNumPolizaRequest();
        consult.setCabecera(obtCabeceraConsul(idTransaccion));
        consult.setData(obtenerData(numPoliza));
        return consult;
    }
    
    /**
     * Obt cabecera consul.
     *
     * @param idTransaccion the id transaccion
     * @return the cabecera
     */
    private static Cabecera obtCabeceraConsul(final String idTransaccion) {
        Cabecera cabecera = new Cabecera();
        cabecera.setIDTRANSACCION(idTransaccion);
        cabecera.setCVETRANSACCION(Constantes.CVE_TRANSACCION);
        cabecera.setIDACTOR(Constantes.UNO);
        cabecera.setIDPERFIL(Constantes.UNO);
        cabecera.setIDROL(Constantes.UNO);
        return cabecera;
    }
    
    /**
     * Obtener data.
     *
     * @param numPoliza the num poliza
     * @return the consultar poliza por num poliza
     */
    private static ConsultarPolizaPorNumPoliza obtenerData(final String numPoliza) {
        ConsultarPolizaPorNumPoliza consultar = new ConsultarPolizaPorNumPoliza();
        consultar.setNumPoliza(numPoliza);
        return consultar;
    }
}