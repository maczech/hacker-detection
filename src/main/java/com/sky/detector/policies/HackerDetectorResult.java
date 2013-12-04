package com.sky.detector.policies;

public class HackerDetectorResult {

    private Boolean sucessfull;
    private String message = "";

    public Boolean isSucessfull() {
        return sucessfull;
    }
    public void setSucessfull(Boolean sucessfull) {
        this.sucessfull = sucessfull;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }


}
