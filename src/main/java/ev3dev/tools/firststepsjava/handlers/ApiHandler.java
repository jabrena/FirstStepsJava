package ev3dev.tools.firststepsjava.handlers;

import ev3dev.tools.firststepsjava.model.HelloRequest;
import ev3dev.tools.firststepsjava.model.HelloResponse;
import ev3dev.tools.firststepsjava.services.HelloService;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class ApiHandler {

    private static final String NAME = "name";

    private final ErrorHandler errorHandler;
    private final HelloService helloService;

    private static final Mono<String> DEFAULT_NAME = Mono.just("world");

    public ApiHandler(final HelloService helloService, final ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        this.helloService = helloService;
    }

    public Mono<ServerResponse> defaultHello(final ServerRequest request) {
        return DEFAULT_NAME
                .publish(getServerResponse())
                .onErrorResume(errorHandler::throwableError);
    }

    public Mono<ServerResponse> postHello(final ServerRequest request) {

        return request.bodyToMono(HelloRequest.class)
                .flatMap(helloRequest -> Mono.just(helloRequest.getName()))
                .publish(helloService.decode())
                .log()
                .publish(helloService.compile())
                .publish(helloService.run())
                .publish(getServerResponse())
                .onErrorResume(errorHandler::throwableError);
    }

    Function<Mono<String>, Mono<ServerResponse>> getServerResponse() {
        return (name) -> name
                .publish(createHelloResponse())
                .publish(convertToServerResponse());
    }

    Function<Mono<String>, Mono<HelloResponse>> createHelloResponse() {
        return name ->
                name.publish(helloService.getGreetings()).flatMap(
                        greetings -> getQuote2().flatMap(
                                title -> Mono.just(new HelloResponse(greetings, title))));
    }

    Mono<String> getQuote2() {
        return Mono.just("Hello World");
    }

    Function<Mono<HelloResponse>, Mono<ServerResponse>> convertToServerResponse() {
        return value -> value.flatMap(helloResponse -> {
            return ServerResponse.ok().body(Mono.just(helloResponse), HelloResponse.class);
        });
    }
}