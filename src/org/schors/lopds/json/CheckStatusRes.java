package org.schors.lopds.json;

public class CheckStatusRes extends Response {

    private String status;

    public CheckStatusRes() {
    }

    public CheckStatusRes(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CheckStatusRes{" +
                "status='" + status + '\'' +
                '}';
    }
}
