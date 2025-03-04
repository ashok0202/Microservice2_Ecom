package com.teekworks.Order_Service.external.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.teekworks.Order_Service.execption.CustomException;
import com.teekworks.Order_Service.external.response.ErrorResponse;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CustomErrorDecoder implements ErrorDecoder {

    private final Logger LOGGER = LoggerFactory.getLogger(CustomErrorDecoder.class);

    @Override
    public Exception decode(String s, Response response) {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Fix for LocalDateTime

        LOGGER.info("Error URL {}",response.request().url());
        LOGGER.info( "Error Headers {}",response.request().headers());
        //LOGGER.error("Error Response Code : {}",response.status());

        try {
            ErrorResponse errorResponse = objectMapper.readValue(response.body().asInputStream(), ErrorResponse.class);
            return new CustomException( errorResponse.getErrorMessage(),errorResponse.getErrorCode(),response.status());
        } catch (IOException e) {
            LOGGER.error("Error while decoding the error response {}",e.getMessage());
            throw new CustomException("Internal_Server _Error", "INTERNAL_SERVER_ERROR",500);

        }
    }
}
