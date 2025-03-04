package com.teekworks.ProductService.model;

import java.time.LocalDateTime;

public class ErrorResponse {

    private LocalDateTime time;
    private String errorCode;
    private String errorMessage;

    public ErrorResponse() {
    }

    public ErrorResponse(LocalDateTime time, String errorCode, String errorMessage) {
        this.time = time;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
