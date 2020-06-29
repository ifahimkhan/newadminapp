package com.fahim.newapp.holder;

import java.util.List;

public class ResponseSubjectHolder {

    private boolean status;
    private String message;
    private List<SubjectHolder> response;

    public ResponseSubjectHolder() {

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

    public List<SubjectHolder> getResponse() {
        return response;
    }

    public void setResponse(List<SubjectHolder> response) {
        this.response = response;
    }
}
