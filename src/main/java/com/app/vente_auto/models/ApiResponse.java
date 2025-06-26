package com.app.vente_auto.models;

public class ApiResponse {

    private int errorCode;
    private String errorMessage;
    private String referenceNumber;
    private Object data;
    private String token;
    private String refreshToken;

    public ApiResponse(int errorCode, String errorMessage, String referenceNumber,Object data, String token, String refreshToken) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.referenceNumber = referenceNumber;
        this.data = data;
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public ApiResponse() {
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) { 
        this.data = data;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
