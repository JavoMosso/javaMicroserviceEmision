package com.gnp.autos.wsp.emisor.eot.services;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;

import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gnp.autos.wsp.emisor.eot.domain.EmisionDomain;
import com.gnp.autos.wsp.emisor.eot.domain.EmisorDomain;
import com.gnp.autos.wsp.emisor.eot.domain.MovimientoDomain;
import com.gnp.autos.wsp.emisor.eot.emision.wsdl.RegistrarEmisionResponse;
import com.gnp.autos.wsp.emisor.eot.movimiento.wsdl.ConsultaNegocioResponse;
import com.gnp.autos.wsp.errors.xml.ErrorXML;
import com.gnp.autos.wsp.negocio.emision.model.req.TraductorEmitirReq;
import com.gnp.autos.wsp.negocio.emision.model.resp.TraductorEmitirResp;
import com.gnp.autos.wsp.negocio.neg.model.EmiteNegReq;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * The Class TraductorService.
 */
@Api(tags = "Emisor", value = "/emisoreot", produces = MediaType.APPLICATION_XML, consumes = MediaType.APPLICATION_XML)
@RestController
@RequestMapping("/emisoreot")
public class TraductorService {
    /** The domain. */
    @Autowired
    private EmisorDomain domain;
    
    /** The domain C. */
    @Autowired
    private MovimientoDomain domainC;
    
    /** The domain E. */
    @Autowired
    private EmisionDomain domainE;
    
    /**
     * Gets the emisor EOT.
     *
     * @param traductorReq the traductor req
     * @param tid the tid
     * @return the emisor EOT
     */
    @ApiOperation(code = SC_OK, value = "Regresa la emision de EOT", response = TraductorEmitirResp.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = SC_OK, message = "OK", response = TraductorEmitirResp.class,
                    responseContainer = "List"),
            @ApiResponse(code = SC_BAD_REQUEST, message = "Bad Request", response = ErrorXML.class)
    })
    @RequestMapping(method = RequestMethod.POST, value = "", produces = { MediaType.APPLICATION_XML},
            consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public TraductorEmitirResp getEmisorEOT(@RequestBody final TraductorEmitirReq traductorReq,
            @RequestParam(value = "tid", required = false) final Integer tid) {
        return domain.getEmitir(traductorReq, tid);
    }
    
    /**
     * Gets the consulta.
     *
     * @param consulNegReq the consul neg req
     * @param tid the tid
     * @return the consulta
     */
    @ApiOperation(code = SC_OK, value = "Regresa la emision", response = ConsultaNegocioResponse.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = SC_OK, message = "OK", response = ConsultaNegocioResponse.class,
                    responseContainer = "List"),
            @ApiResponse(code = SC_BAD_REQUEST, message = "Bad Request", response = ErrorXML.class)
    })
    @RequestMapping(method = RequestMethod.POST, value = "consultaNeg", produces = { MediaType.APPLICATION_XML},
            consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public EmiteNegReq getConsulta(@RequestBody final EmiteNegReq consulNegReq,
            @RequestParam(value = "tid", required = false) final Integer tid) {
        return domainC.getMovimientoNC(consulNegReq, tid);
    }
    
    /**
     * Gets the emision EOT.
     *
     * @param traductorReq the traductor req
     * @param tid the tid
     * @return the emision EOT
     */
    @ApiOperation(code = SC_OK, value = "Regresa la emision", response = RegistrarEmisionResponse.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = SC_OK, message = "OK", response = RegistrarEmisionResponse.class,
                    responseContainer = "List"),
            @ApiResponse(code = SC_BAD_REQUEST, message = "Bad Request", response = ErrorXML.class)
    })
    @RequestMapping(method = RequestMethod.POST, value = "Emision", produces = { MediaType.APPLICATION_XML},
            consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public RegistrarEmisionResponse getEmisionEOT(@RequestBody final TraductorEmitirReq traductorReq,
            @RequestParam(value = "tid", required = false) final Integer tid) {
        return domainE.getEmision(traductorReq, tid);
    }
}