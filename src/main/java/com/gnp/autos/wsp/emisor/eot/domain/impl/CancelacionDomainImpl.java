package com.gnp.autos.wsp.emisor.eot.domain.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.datatype.DatatypeConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.gnp.autos.wsp.emisor.eot.cancelacion.CancelacionReq;
import com.gnp.autos.wsp.emisor.eot.cancelacion.CancelacionResp;
import com.gnp.autos.wsp.emisor.eot.cancelacion.wsdl.Cabecera;
import com.gnp.autos.wsp.emisor.eot.cancelacion.wsdl.CancelaPolizaDto;
import com.gnp.autos.wsp.emisor.eot.cancelacion.wsdl.CancelarRequestData;
import com.gnp.autos.wsp.emisor.eot.cancelacion.wsdl.EmitirCancelacionRequest;
import com.gnp.autos.wsp.emisor.eot.cancelacion.wsdl.EmitirCancelacionResponse2;
import com.gnp.autos.wsp.emisor.eot.cancelacion.wsdl.TarificarCancelacionRequest;
import com.gnp.autos.wsp.emisor.eot.cancelacion.wsdl.TarificarCancelacionResponse2;
import com.gnp.autos.wsp.emisor.eot.config.ConfWSP;
import com.gnp.autos.wsp.emisor.eot.consulta.wsdl.ConsultarPolizaPorNumPolizaResponse;
import com.gnp.autos.wsp.emisor.eot.domain.CancelacionDomain;
import com.gnp.autos.wsp.emisor.eot.domain.ConsultaDomain;
import com.gnp.autos.wsp.emisor.eot.domain.FoliadorDomain;
import com.gnp.autos.wsp.emisor.eot.error.ExecutionError;
import com.gnp.autos.wsp.emisor.eot.rest.service.TransaccionesClient;
import com.gnp.autos.wsp.emisor.eot.soap.service.CancelacionClient;
import com.gnp.autos.wsp.emisor.eot.util.Constantes;
import com.gnp.autos.wsp.emisor.eot.util.Utileria;

/**
 * The Class CancelacionDomainImpl.
 */
@Service
public class CancelacionDomainImpl implements CancelacionDomain {
    /** The foliador. */
    @Autowired
    private FoliadorDomain foliador;
    
    /** The tarificar. */
    @Autowired
    private CancelacionClient tarificar;
    
    /** The conf. */
    @Autowired
    private ConfWSP conf;
    
    /** The consulta. */
    @Autowired
    private ConsultaDomain consulta;
    
    /** The rest template. */
    @Autowired
    private static RestTemplate restTemplate = new RestTemplate();
    
    /**
     * Cancelacion NTU.
     *
     * @param req the req
     * @return the cancelacion resp
     */
    @Override
    public CancelacionResp cancelacionNTU(final CancelacionReq req) {
        EmitirCancelacionResponse2 resCancela = null;
        try {
            validar(req);
            Integer tid = new TransaccionesClient(restTemplate).getTransInterId(conf.getUrlTransacciones(), 
                    req.getNumPoliza()).getFolioWSP();
            TarificarCancelacionRequest contratoReq = obtenerContrato(req, tid);
            TarificarCancelacionResponse2 resTarifica = tarificar.getTarificar(contratoReq, tid);
            if (resTarifica == null) {
                resTarifica = new TarificarCancelacionResponse2();
            }
            EmitirCancelacionRequest reqCancelacion = cancelacionConfirmada(obtenerResp(resTarifica, req), tid);
            resCancela = tarificar.getCancelacion(reqCancelacion, tid);
            if (resCancela == null) {
                throw new ExecutionError(1, "No se cancelo poliza");
            }
        } catch (Exception e) {
            throw new ExecutionError(1, "No se encontro poliza");
        }
        return obtenerResp(resCancela);
    }
    
    /**
     * Validacion del contrato de entrada.
     *
     * @param req the req
     */
    protected void validar(final CancelacionReq req) {
        req.setNumPoliza(Utileria.rellenarCero(req.getNumPoliza(), "14"));
        ConsultarPolizaPorNumPolizaResponse cResp = consulta.consultarPoliza(req.getNumPoliza(), "1", null);
        if (cResp == null || cResp.getPolizaDto().isEmpty()) {
            throw new ExecutionError(1, "No es posible cancelar la poliza debido a que no se encontro");
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String strFecNow = simpleDateFormat.format(new Date());
        Date fecIniPol = Utileria.getFecha(cResp.getPolizaDto().get(0).getFchInicioPoliza());
        Date fecNow = Utileria.getFecha(strFecNow);
        if (!fecIniPol.equals(fecNow)) {
            throw new ExecutionError(1, 
                    "No es posible cancelar la poliza debido a que la fecha de vigencia es diferente a la cancelacion");
        }
        if ("CANCELADA".equalsIgnoreCase(cResp.getPolizaDto().get(0).getEstPoliza())) {
            throw new ExecutionError(1, "La poliza ya se encuentra cancelada");
        }
    }
    
    /**
     * Traduce al contrato requerido de tarificacion.
     *
     * @param req the req
     * @param tid the tid
     * @return the tarificar cancelacion request
     * @throws DatatypeConfigurationException the datatype configuration exception
     */
    public TarificarCancelacionRequest obtenerContrato(final CancelacionReq req, final Integer tid)
            throws DatatypeConfigurationException {
        TarificarCancelacionRequest contratoReq = new TarificarCancelacionRequest();
        contratoReq.setCabecera(obtenerCabecera(tid));
        contratoReq.setDataTarificarCancelacion(obtCancelaPol(req.getNumPoliza()));
        return contratoReq;
    }
    
    /**
     * Obtener cabecera.
     *
     * @param tid the tid
     * @return the cabecera
     */
    private Cabecera obtenerCabecera(final Integer tid) {
        Cabecera cabecera = new Cabecera();
        cabecera.setIDTRANSACCION(foliador.getFolio(tid, Constantes.CVE_TRANSACCION));
        cabecera.setCVETRANSACCION(Constantes.CVE_TRANSACCION);
        cabecera.setIDACTOR(Constantes.UNO);
        cabecera.setIDROL(Constantes.UNO);
        cabecera.setIDPERFIL(Constantes.UNO);
        return cabecera;
    }
    
    /**
     * Obt cancela pol.
     *
     * @param numPoliza the num poliza
     * @return the cancela poliza dto
     * @throws DatatypeConfigurationException the datatype configuration exception
     */
    public CancelaPolizaDto obtCancelaPol(final String numPoliza) throws DatatypeConfigurationException {
        CancelaPolizaDto data = new CancelaPolizaDto();
        data.setNUMPOLIZA(numPoliza);
        data.setNUMVERSION(Constantes.VERSIONPOL);
        data.setFCHEFECTOMOVIMIENTO(Utileria.getXMLDateNow());
        data.setCVECANCELACIONNEGOCIO(Constantes.CANCELANEG);
        data.setCVERAMO(Constantes.AUTOS);
        return data;
    }
    
    /**
     * Obtener resp.
     *
     * @param resTarifica the res tarifica
     * @param req the req
     * @return the tarificar cancelacion response 2
     */
    public TarificarCancelacionResponse2 obtenerResp(final TarificarCancelacionResponse2 resTarifica,
            final CancelacionReq req) {
        resTarifica.setNUMPOLIZA(req.getNumPoliza());
        resTarifica.setNUMVERSION(Constantes.UNO);
        return resTarifica;
    }
    
    /**
     * Obtener el contrato de emitir cancelacion.
     *
     * @param resTarifica the res tarifica
     * @param tid the tid
     * @return the emitir cancelacion request
     * @throws DatatypeConfigurationException the datatype configuration exception
     */
    public EmitirCancelacionRequest cancelacionConfirmada(final TarificarCancelacionResponse2 resTarifica,
            final Integer tid)
            throws DatatypeConfigurationException {
        EmitirCancelacionRequest contratoReq = new EmitirCancelacionRequest();
        contratoReq.setCabecera(obtenerCabecera(tid));
        contratoReq.setData(obtCancelacion(resTarifica));
        return contratoReq;
    }
    
    /**
     * Obt cancelacion.
     *
     * @param resTarifica the res tarifica
     * @return the cancelar request data
     * @throws DatatypeConfigurationException the datatype configuration exception
     */
    public CancelarRequestData obtCancelacion(final TarificarCancelacionResponse2 resTarifica)
            throws DatatypeConfigurationException {
        CancelarRequestData reqData = new CancelarRequestData();
        reqData.setDatosPoliza(obtCancelaPol(resTarifica.getNUMPOLIZA()));
        reqData.getListaObjetosAsegurados().addAll(resTarifica.getListaObjetosAsegurados());
        return reqData;
    }
    
    /**
     * Obtener resp.
     *
     * @param resCancela the res cancela
     * @return the cancelacion resp
     */
    public CancelacionResp obtenerResp(final EmitirCancelacionResponse2 resCancela) {
        CancelacionResp response = new CancelacionResp();
        response.setNumPoliza(resCancela.getNUMPOLIZA());
        response.setNumVersion(resCancela.getNUMVERSION());
        response.setEstPoliza(resCancela.getESTADOPOLIZA());
        return response;
    }
}
