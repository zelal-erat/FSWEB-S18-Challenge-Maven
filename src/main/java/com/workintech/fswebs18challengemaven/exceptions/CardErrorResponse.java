package com.workintech.fswebs18challengemaven.exceptions;



public class CardErrorResponse {
    private int status;
    private String message;
    private long timestamp;

    public CardErrorResponse() {
    }

    public CardErrorResponse(String message) {
        this.message = message;
    }

    public CardErrorResponse(int status, String message, long timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
