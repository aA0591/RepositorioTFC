package com.webratio.units.store.social;

public class DoodleServiceException extends Exception {

    private static final long serialVersionUID = 1874563981752708331L;

    public DoodleServiceException() {
    }

    public DoodleServiceException(String message) {
        super(message);
    }

    public DoodleServiceException(Throwable cause) {
        super(cause);
    }

    public DoodleServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
