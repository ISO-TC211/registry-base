package de.geoinfoffm.registry.api.ex;

import java.io.Serializable;

public class ResetPasswordException extends Exception implements Serializable {

    private static final long serialVersionUID = 874039453225884928L;

    public ResetPasswordException(String message) {
        super(message);
    }
}
