package com.fahim.newapp.holder;

import java.util.List;

public class ResponseStandardHolder {

    private boolean status;
    private String message;
    private List<StandardHolder> response;

    public ResponseStandardHolder() {

    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<StandardHolder> getResponse() {
        return response;
    }

    public void setResponse(List<StandardHolder> response) {
        this.response = response;
    }
}
