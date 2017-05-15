package ev3dev.tools.myfirstjava.routers;

import ev3dev.tools.myfirstjava.handlers.ApiHandler;
import ev3dev.tools.myfirstjava.handlers.ErrorHandler;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

class ApiRouter {
    private static final String API_PATH = "/api";
    private static final String HELLO_PATH = "/hello";
    private static final String NAME_ARG = "/{name}";

    static RouterFunction<?> doRoute(final ApiHandler apiHandler, final ErrorHandler errorHandler) {
        return
            nest(path(API_PATH),
                nest(accept(APPLICATION_JSON),
                    route(POST(HELLO_PATH), apiHandler::postHello)
                ).andOther(route(RequestPredicates.all(), errorHandler::notFound))
            );
    }
}