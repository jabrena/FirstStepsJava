package ev3dev.tools.myfirstjava.model;


public class ErrorResponse {
    private String error;

    public ErrorResponse() {
        this.error = "";
    }

    public ErrorResponse(final String hello) {
        this.error = hello;
    }

    public String getError() {
        return error;
    }

    public void setError(final String error) {
        this.error = error;
    }
}
