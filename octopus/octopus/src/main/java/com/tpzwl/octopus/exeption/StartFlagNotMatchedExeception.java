package com.tpzwl.octopus.exeption;

import io.netty.handler.codec.DecoderException;

public class StartFlagNotMatchedExeception extends DecoderException {

	private static final long serialVersionUID = -4688252635899040807L;
	
    /**
     * Creates a new instance.
     */
    public StartFlagNotMatchedExeception() {
    }

    /**
     * Creates a new instance.
     */
    public StartFlagNotMatchedExeception(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new instance.
     */
    public StartFlagNotMatchedExeception(String message) {
        super(message);
    }

    /**
     * Creates a new instance.
     */
    public StartFlagNotMatchedExeception(Throwable cause) {
        super(cause);
    }

}
