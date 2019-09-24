package exceptions;


import org.eclipse.jetty.server.Response;

public class BadRequestException extends Throwable {
    private int code;

    public BadRequestException() {
        this(Response.SC_BAD_REQUEST);
    }

    public BadRequestException(int code) {
        this(code, "Error while processing the request", null);
    }

    public BadRequestException(int code, String message) {
        this(code, message, null);
    }

    public BadRequestException(int code, String message, Throwable throwable) {
        super(message, throwable);
        this.code = code;
    }

    public BadRequestException(String message) {
        this(Response.SC_BAD_REQUEST, message);
    }

    public int getCode() {
        return code;
    }
}