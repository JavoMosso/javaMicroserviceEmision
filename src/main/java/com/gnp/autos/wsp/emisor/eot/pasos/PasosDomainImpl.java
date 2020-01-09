package com.gnp.autos.wsp.emisor.eot.pasos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gnp.autos.wsp.emisor.eot.config.ConfWSP;
import com.gnp.autos.wsp.emisor.eot.pasos.wsdl.ConsultaPasosConfigurador;
import com.gnp.autos.wsp.emisor.eot.pasos.wsdl.ConsultaPasosConfiguradorResponse;
import com.gnp.autos.wsp.emisor.eot.pasos.wsdl.PasoElementoConsultaDto;
import com.gnp.autos.wsp.emisor.eot.pasos.wsdl.PasosConsultaConfiguradorDto;
import com.gnp.autos.wsp.emisor.eot.soap.service.PasosClient;
import com.gnp.autos.wsp.emisor.eot.util.Constantes;
import com.gnp.autos.wsp.negocio.neg.model.EmiteNegReq;

/**
 * The Class PasosDomainImpl.
 */
@Service
public class PasosDomainImpl implements PasosDomain {
    /** The Constant CONDUCTOR. */
    public static final String CONDUCTOR = "CONDUCTOR";
    
    /** The Constant CONTRATANTE. */
    public static final String CONTRATANTE = "CONTRATANTE";
    
    /** The client. */
    @Autowired
    private PasosClient client;
    
    /** The conf. */
    @Autowired
    private ConfWSP conf;
    
    /**
     * Gets the tipo carga.
     *
     * @param negReq the neg req
     * @param tid the tid
     * @param urlTInter the url T inter
     * @return the tipo carga
     */
    @Override
    public ConsultaPasosConfiguradorResponse getTipoCarga(final EmiteNegReq negReq, final Integer tid,
            final String urlTInter) {
        ConsultaPasosConfigurador requestDatos = obtenRequestDatos(negReq, Constantes.ERROR_6);
        client.setDefaultUri(conf.getUrlPasos());
        ConsultaPasosConfiguradorResponse respSOAP;
        respSOAP = client.getPasos(requestDatos, tid, urlTInter);
        return respSOAP;
    }
    
    /**
     * Obten request datos.
     *
     * @param objReq the obj req
     * @param numPaso the num paso
     * @return the consulta pasos configurador
     */
    private static ConsultaPasosConfigurador obtenRequestDatos(final EmiteNegReq objReq, final Integer numPaso) {
        ConsultaPasosConfigurador requestDatos = new ConsultaPasosConfigurador();
        PasosConsultaConfiguradorDto pasosC = new PasosConsultaConfiguradorDto();
        pasosC.setPASO(numPaso);
        pasosC.setCOMPONENTE("PS_COTIZADOR_EXPRESS_AUTOS");
        pasosC.getELEMENTOS().add(addElemento("ID_NEGOCIO_OPERABLE", objReq.getIdUMO()));
        pasosC.getELEMENTOS().add(addElemento("CVE_MODELO", objReq.getVehiculo().getModelo()));
        pasosC.getELEMENTOS().add(addElemento("TCTIPVEH", objReq.getVehiculo().getTipoVehiculo()));
        pasosC.getELEMENTOS().add(addElemento("TCSRAMO", objReq.getVehiculo().getSubRamo()));
        pasosC.getELEMENTOS().add(addElemento("CRITERIO_BUSQUEDA", "DESCRIPCION_VEHICULO"));
        pasosC.getELEMENTOS().add(addElemento("CODIGO_PROMOCION", objReq.getCodigoPromocion()));
        pasosC.getELEMENTOS().add(addElemento("TIPO_USO", objReq.getVehiculo().getUso()));
        pasosC.getELEMENTOS().add(addElemento("DESCRIPCION_VEHICULO", objReq.getVehiculo().getDescArmadora()));
        requestDatos.setPASOCONSULTACONFIGURADOR(pasosC);
        return requestDatos;
    }
    
    /**
     * Adds the elemento.
     *
     * @param nombre the nombre
     * @param valor the valor
     * @return the paso elemento consulta dto
     */
    private static PasoElementoConsultaDto addElemento(final String nombre, final String valor) {
        PasoElementoConsultaDto nombreEle = new PasoElementoConsultaDto();
        nombreEle.setELEMENTO(nombre);
        PasoElementoConsultaDto valorEle = new PasoElementoConsultaDto();
        valorEle.setELEMENTO(valor);
        nombreEle.getVALORESELEMENTO().add(valorEle);
        return nombreEle;
    }
}