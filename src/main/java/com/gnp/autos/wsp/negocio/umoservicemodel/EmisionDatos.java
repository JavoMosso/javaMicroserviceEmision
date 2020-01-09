package com.gnp.autos.wsp.negocio.umoservicemodel;

import com.gnp.autos.wsp.negocio.neg.model.EmiteNegReq;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Clase. */
@Data
@NoArgsConstructor
public class EmisionDatos {
    /** Attribute. */
    private EmiteNegReq emite;
    /** Attribute. */
    private UmoServiceResp umo;
}
