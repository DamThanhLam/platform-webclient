package org.sento.platform.webclient.exception;

public class InternalWebClientException extends RuntimeException {

    public InternalWebClientException(String message) {
        super(message);
    }

    public InternalWebClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
