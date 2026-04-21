package org.sento.platform.webclient.exception;

public class InternalWebClientHttpException extends InternalWebClientException {

    private final int statusCode;
    private final String responseBody;

    public InternalWebClientHttpException(int statusCode, String responseBody) {
        super("HTTP " + statusCode + " from downstream service");
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }
}
