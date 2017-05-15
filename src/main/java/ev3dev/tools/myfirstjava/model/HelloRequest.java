package ev3dev.tools.myfirstjava.model;

public class HelloRequest {

    private String name;

    public HelloRequest() {
        this.name = "";
    }

    public HelloRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
