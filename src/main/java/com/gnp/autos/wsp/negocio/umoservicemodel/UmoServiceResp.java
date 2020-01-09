package com.gnp.autos.wsp.negocio.umoservicemodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.gnp.autos.wsp.negocio.umoservice.model.Dominios;
import com.gnp.autos.wsp.negocio.umoservice.model.Negocio;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Instantiates a new umo service resp. */
@Data
@XmlRootElement(name = "UmoServiceResp")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType
@NoArgsConstructor
public class UmoServiceResp {
    /** The id. */
    private float id;
    /** The nombre. */
    private String nombre;
    /** The codigo promocion. */
    private String codigoPromocion;
    /** The es umo. */
    private boolean esUmo;
    /** The negocio. */
    private Negocio negocio;
    /** The dominios. */
    private Dominios dominios;
}
