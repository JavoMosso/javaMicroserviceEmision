package com.gnp.autos.wsp.emisor.eot.services;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;

import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.gnp.autos.wsp.emisor.eot.cancelacion.CancelacionReq;
import com.gnp.autos.wsp.emisor.eot.cancelacion.CancelacionResp;
import com.gnp.autos.wsp.emisor.eot.domain.CancelacionDomain;
import com.gnp.autos.wsp.errors.xml.ErrorXML;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * The Class CancelacionService.
 */
@Api(tags = "Cancelacion", value = "/cancelacioneot", produces = MediaType.APPLICATION_XML,
        consumes = MediaType.APPLICATION_XML)
@RestController
@RequestMapping("/cancelacioneot")
public class CancelacionService {
    /** The domain. */
    @Autowired
    private CancelacionDomain domain;
    
    /**
     * Gets the cancelacion EOT.
     *
     * @param cancelaReq the cancela req
     * @return the cancelacion EOT
     */
    @ApiOperation(code = SC_OK, value = "cancelacion emision de EOT", response = CancelacionReq.class, 
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = SC_OK, message = "OK", response = CancelacionResp.class, responseContainer = "List"),
            @ApiResponse(code = SC_BAD_REQUEST, message = "Bad Request", response = ErrorXML.class)
    })
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_XML,
            consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public CancelacionResp getCancelacionEOT(@RequestBody final CancelacionReq cancelaReq) {
        return domain.cancelacionNTU(cancelaReq);
    }
}