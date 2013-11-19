package org.schors.lopds.json;

public class Response {
    private String result;
    private String details;

    public Response() {
    }

    public Response(String result) {
        this.result = result;
    }

    public Response(String result, String details) {
        this.result = result;
        this.details = details;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "Response{" +
                "result='" + result + '\'' +
                ", details='" + details + '\'' +
                '}';
    }
}
