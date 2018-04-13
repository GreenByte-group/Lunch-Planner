package group.greenbyte.lunchplanner.exceptions;

public class HttpRequestException extends Exception {

    private int statusCode;
    private String errorMessage;

    public HttpRequestException(int statusCode, String errorMessage) {
        super();
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public int getErrorMessage() {
        return errorMessage;
    }
}
