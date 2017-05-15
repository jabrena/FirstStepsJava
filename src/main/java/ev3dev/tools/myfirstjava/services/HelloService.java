package ev3dev.tools.myfirstjava.services;

import reactor.core.publisher.Mono;

import java.util.function.Function;

public interface HelloService {
    Function<Mono<String>, Mono<String>> getGreetings();

    Function<Mono<String>, Mono<String>> decode();

    Function<Mono<String>, Mono<Boolean>> compile();

    Function<Mono<Boolean>, Mono<String>> run();
}
