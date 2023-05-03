package initiative.guru.spring6plus.spring6reactive.webflux.router;

import initiative.guru.spring6plus.spring6reactive.webflux.handler.BeerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration(proxyBeanMethods = false)
public class BeerRouter {

    public static final String BEER_BASE_URL = "/api/v2/beer";
    public static final String BEER_PATH_ID = BEER_BASE_URL + "/{beerId}";

    @Bean
    public RouterFunction<ServerResponse> routes(BeerHandler beerHandler) {

        return RouterFunctions
                .route(GET(BEER_BASE_URL).and(accept(MediaType.TEXT_PLAIN)), beerHandler::getBeers)
                .andRoute(GET(BEER_PATH_ID).and(accept(MediaType.TEXT_PLAIN)), beerHandler::getBeerById)
                .andRoute(POST(BEER_BASE_URL).and(accept(MediaType.APPLICATION_JSON)), beerHandler::createNewBeer)
                .andRoute(PUT(BEER_PATH_ID).and(accept(MediaType.APPLICATION_JSON)), beerHandler::updateExistingBeer)
                .andRoute(PATCH(BEER_PATH_ID).and(accept(MediaType.APPLICATION_JSON)), beerHandler::patchExistingBeer)
                .andRoute(DELETE(BEER_PATH_ID).and(accept(MediaType.TEXT_PLAIN)), beerHandler::deleteById);
    }
}
