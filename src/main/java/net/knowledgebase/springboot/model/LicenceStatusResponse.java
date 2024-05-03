package net.knowledgebase.springboot.model;

public class LicenceStatusResponse {
    private boolean exists;
    private boolean active;
    private String errorMessage;

    public LicenceStatusResponse(boolean exists, boolean active, String errorMessage) {
        this.exists = exists;
        this.active = active;
        this.errorMessage = errorMessage;
    }

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
