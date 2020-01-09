package com.gnp.autos.wsp.emisor.eot.error;

import com.gnp.autos.wsp.errors.exceptions.WSPSimpleException;

/** The Class ExecutionError. */
@SuppressWarnings("serial")
public class ExecutionError extends WSPSimpleException {
    /** The Constant TIPO. */
    private static final String TIPO = "emisor-eot";
    
    /**
     * Instantiates a new execution error.
     *
     * @param id
     *               the id
     */
    public ExecutionError(final int id) {
        super(TIPO, id);
    }
    
    /**
     * Instantiates a new execution error.
     *
     * @param id
     *               the id
     * @param ex
     *               the ex
     */
    public ExecutionError(final int id, final Throwable ex) {
        super(TIPO, id);
        initCause(ex);
    }
    
    /**
     * Instantiates a new execution error.
     *
     * @param id
     *                 the id
     * @param args
     *                 the args
     */
    public ExecutionError(final int id, final String...args) {
        super(TIPO, id, args);
    }
}