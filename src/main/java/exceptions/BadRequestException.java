package exceptions;

public class BadRequestException extends Throwable {
    private int code;

    public BadRequestException() {
        this(500);
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

    public int getCode() {
        return code;
    }
}