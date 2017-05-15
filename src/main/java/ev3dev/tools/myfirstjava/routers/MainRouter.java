package ev3dev.tools.myfirstjava.routers;

import ev3dev.tools.myfirstjava.handlers.ApiHandler;
import ev3dev.tools.myfirstjava.handlers.ErrorHandler;
import org.springframework.web.reactive.function.server.RouterFunction;

public class MainRouter {

    public static RouterFunction<?> doRoute(final ApiHandler handler, final ErrorHandler errorHandler) {
        return ApiRouter.doRoute(handler, errorHandler)
                .andOther(StaticRouter.doRoute());
    }
}
