package com.gnp.autos.wsp.emisor.eot.domain.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gnp.autos.wsp.emisor.eot.config.ConfWSP;
import com.gnp.autos.wsp.emisor.eot.domain.MovimientoDomain;
import com.gnp.autos.wsp.emisor.eot.error.ExecutionError;
import com.gnp.autos.wsp.emisor.eot.movimiento.wsdl.ConsultaNegocio;
import com.gnp.autos.wsp.emisor.eot.movimiento.wsdl.ConsultaNegocioResponse;
import com.gnp.autos.wsp.emisor.eot.movimiento.wsdl.DatosSolicitudMovimientosDto;
import com.gnp.autos.wsp.emisor.eot.movimiento.wsdl.MovimientoNegocioComercialDto;
import com.gnp.autos.wsp.emisor.eot.movimiento.wsdl.MovimientoPorcentajeDto;
import com.gnp.autos.wsp.emisor.eot.movimiento.wsdl.MovimientoPorcentajeIntermediarioDto;
import com.gnp.autos.wsp.emisor.eot.soap.service.MovimientoEOTClient;
import com.gnp.autos.wsp.negocio.neg.model.AgenteNeg;
import com.gnp.autos.wsp.negocio.neg.model.ElementoNeg;
import com.gnp.autos.wsp.negocio.neg.model.EmiteNegReq;

/**
 * The Class MovimientoDomainImp.
 */
@Service
public class MovimientoDomainImp implements MovimientoDomain {
    /** The Constant STRUDI. */
    private static final String STRUDI = "UDI";

    /** The Constant CVE_TIPO_NOMINA. */
    private static final String CVE_TIPO_NOMINA = "CVE_TIPO_NOMINA";

    /** The Constant PUNTO_VENTA. */
    private static final String PUNTO_VENTA = "PUNTO_VENTA";

    /** The client. */
    @Autowired
    private MovimientoEOTClient client;

    /** The conf. */
    @Autowired
    private ConfWSP conf;

    /**
     * Gets the movimiento NC.
     *
     * @param objEmiNeg the obj emi neg
     * @param tid       the tid
     * @return the movimiento NC
     */
    @Override
    public EmiteNegReq getMovimientoNC(final EmiteNegReq objEmiNeg, final Integer tid) {
        ConsultaNegocio consultaNeg = getRequestNegocio(objEmiNeg);
        client.setDefaultUri(conf.getUrlMovimientoEOT());
        ConsultaNegocioResponse consultaResul = client.getConsulta(consultaNeg,
                conf.getUrlTransacciones() + "/logintermedia", tid);
        List<AgenteNeg> agentes = new ArrayList<>();
        List<MovimientoPorcentajeDto> compensaciones = consultaResul.getMOVIMIENTOSNEGOCIO()
                .getPORCENTAJECOMPENSACION();
        if (objEmiNeg.getElementos() != null) {
            List<String> validCves = new ArrayList<>();
            Optional<ElementoNeg> elemTN = objEmiNeg.getElementos().stream()
                    .filter(p -> p.getNombre().equalsIgnoreCase(CVE_TIPO_NOMINA)).findFirst();
            if (elemTN.isPresent()) {
                consultaResul.getMOVIMIENTOSNEGOCIO().getNEGOCIOTIPONOMINA()
                        .forEach(cve -> validCves.add(cve.getCVETIPONOMINA()));
                areMatchsCveTN(validCves, elemTN.get().getClave());
            }
            objEmiNeg
                    .setTipoCanalAcceso(consultaResul.getMOVIMIENTOSNEGOCIO().getDATOSSOLICITUD().getTIPOCANALACCESO());

            objEmiNeg.getElementos().stream().filter(p -> p.getNombre().equalsIgnoreCase(PUNTO_VENTA)).findFirst()
                    .ifPresent(p -> {
                        if (consultaResul.getMOVIMIENTOSNEGOCIO().getDEPENDENCIANEGOCIO().stream()
                                .noneMatch(t -> t.getIDDEPENDENCIANEGOCIO().trim().equalsIgnoreCase(p.getValor()))) {
                            throw new ExecutionError(2, "El punto de venta es incorrecto");
                        }
                    });

        }
        if (compensaciones != null && !compensaciones.isEmpty()) {
            compensaciones.stream()
                    .forEach(p -> p.getPORCENTAJEINTERMEDIARIO().stream().forEach(q -> agentes.add(getAgenteNeg(q,
                            p.getCVETIPOCOMPENSACION(), p.getPORCENTAJECOMISION().toString(), p.getCVEBASECALCULO()))));
            if (!agentes.isEmpty()) {
                objEmiNeg.setAgentes(agentes);
            }
        }

        return objEmiNeg;
    }

    /**
     * Are matchs cve TN.
     *
     * @param validCves the valid cves
     * @param cve       the cve
     */
    protected static void areMatchsCveTN(final List<String> validCves, final String cve) {
        for (String cveTN : validCves) {
            if (cveTN.equals(cve)) {
                return;
            }
        }
        throw new ExecutionError(2, "la clave tipo nomina no es válida para éste negocio");
    }

    /**
     * Gets the agente neg.
     *
     * @param agentDto      the agent dto
     * @param claveFuncion  the clave funcion
     * @param comisionPrima the comision prima
     * @param tipoBase      the tipo base
     * @return the agente neg
     */
    public AgenteNeg getAgenteNeg(final MovimientoPorcentajeIntermediarioDto agentDto, final String claveFuncion,
            final String comisionPrima, final String tipoBase) {
        AgenteNeg agenteNeg = new AgenteNeg();
        agenteNeg.setBanIntermediarioPrincipal(agentDto.getBANPRINCIPAL().toString());
        agenteNeg.setCodIntermediario(agentDto.getCODIGOINTERMEDIARIO());
        agenteNeg.setCveClaseIntermediarioInfo(agentDto.getCLASETIPOINTERMEDIARIO());
        agenteNeg.setCveOficinaDireccionAgencia(agentDto.getCVEOFICINADIRECCIONAGENCIA().trim());
        agenteNeg.setCveTipoFuncion(claveFuncion);
        agenteNeg.setFolio(agentDto.getFOLIOAGENTE());
        agenteNeg.setIdParticipante(agentDto.getIDPARTICIPANTEAGENTE());
        agenteNeg.setIdTipoBaseComision(tipoBase);
        agenteNeg.setPctCesionComision(agentDto.getPORCENTAJECESIONCOMISION().toString());
        agenteNeg.setPctComisionPrima(comisionPrima);
        agenteNeg.setPctParticipComision(agentDto.getPORCENTAJEDISTRIBUCION().toString());
        if (claveFuncion.equalsIgnoreCase(STRUDI)) {
            agenteNeg.setPctComisionPrima(agentDto.getPORCENTAJECOMISION().toString());
        }
        return agenteNeg;
    }

    /**
     * Gets the request negocio.
     *
     * @param objEmiNeg the obj emi neg
     * @return the request negocio
     */
    private static ConsultaNegocio getRequestNegocio(final EmiteNegReq objEmiNeg) {
        ConsultaNegocio consulta = new ConsultaNegocio();
        MovimientoNegocioComercialDto movNeg = new MovimientoNegocioComercialDto();
        movNeg.setIDNEGOCIOOPERABLE(objEmiNeg.getIdUMO());
        DatosSolicitudMovimientosDto datosSolicitud = new DatosSolicitudMovimientosDto();
        datosSolicitud.setSUBRAMO(objEmiNeg.getVehiculo().getSubRamo());
        datosSolicitud.setTIPOVEHICULO(objEmiNeg.getVehiculo().getTipoVehiculo());
        datosSolicitud.setCVETIPOUSO(objEmiNeg.getVehiculo().getUso());
        datosSolicitud.setIDCODIGOPROMOCION(objEmiNeg.getCodigoPromocion());
        ConsultaNegocio data = new ConsultaNegocio();
        data.setMOVIMIENTOSNEGOCIO(movNeg);
        MovimientoNegocioComercialDto movDto = new MovimientoNegocioComercialDto();
        movDto.setDATOSSOLICITUD(datosSolicitud);
        movDto.setIDNEGOCIOOPERABLE(objEmiNeg.getIdUMO());
        movDto.setVERSIONNEGOCIO(objEmiNeg.getIdVersionNegocio());
        consulta.setMOVIMIENTOSNEGOCIO(movDto);
        return consulta;
    }
}
