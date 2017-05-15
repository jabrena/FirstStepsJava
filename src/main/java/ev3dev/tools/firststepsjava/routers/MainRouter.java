package ev3dev.tools.firststepsjava.routers;

import ev3dev.tools.firststepsjava.handlers.ApiHandler;
import ev3dev.tools.firststepsjava.handlers.ErrorHandler;
import org.springframework.web.reactive.function.server.RouterFunction;

public class MainRouter {

    public static RouterFunction<?> doRoute(final ApiHandler handler, final ErrorHandler errorHandler) {
        return ApiRouter.doRoute(handler, errorHandler)
                .andOther(StaticRouter.doRoute());
    }
}
