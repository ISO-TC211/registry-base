package de.geoinfoffm.registry.api.ex;

import java.io.Serializable;

public class EmptyPasswordException extends Exception implements Serializable {

    private static final long serialVersionUID = -3540864857361222477L;

    public EmptyPasswordException(String message) {
        super(message);
    }
}
