package com.fahim.newapp.holder;

import java.util.List;

public class ResponseBookHolder {

    private boolean status;
    private String message;
    private List<BookHolder> response;

    public ResponseBookHolder() {

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

    public List<BookHolder> getResponse() {
        return response;
    }

    public void setResponse(List<BookHolder> response) {
        this.response = response;
    }
}
