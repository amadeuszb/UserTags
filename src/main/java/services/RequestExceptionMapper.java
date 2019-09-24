package services;


import exceptions.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public
class RequestExceptionMapper implements ExceptionMapper<BadRequestException> {
    @Override
    public Response toResponse(BadRequestException exception) {
        return Response.status(exception.getCode())
                .entity(new Error(exception.getCode(), exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @Data
    @AllArgsConstructor
    public class Error {
        private int code;
        private String message;
    }
}