package ev3dev.tools.firststepsjava;

import ev3dev.tools.firststepsjava.handlers.ApiHandler;
import ev3dev.tools.firststepsjava.handlers.ErrorHandler;
import ev3dev.tools.firststepsjava.routers.MainRouter;
import ev3dev.tools.firststepsjava.services.HelloServiceImpl;
import ev3dev.tools.firststepsjava.services.HelloService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;

@Configuration
@EnableWebFlux
public class ApplicationConfig {

    @Bean
    HelloService helloService() {
        return new HelloServiceImpl();
    }

    @Bean
    ApiHandler apiHandler(final HelloService helloService,
                          final ErrorHandler errorHandler) {
        return new ApiHandler(helloService, errorHandler);
    }

    @Bean
    ErrorHandler errorHandler() {
        return new ErrorHandler();
    }

    @Bean
    RouterFunction<?> mainRouterFunction(final ApiHandler apiHandler, final ErrorHandler errorHandler) {
        return MainRouter.doRoute(apiHandler, errorHandler);
    }
}
