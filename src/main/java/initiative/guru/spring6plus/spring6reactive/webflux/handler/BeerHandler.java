package initiative.guru.spring6plus.spring6reactive.webflux.handler;

import initiative.guru.spring6plus.spring6reactive.domain.dto.BeerDTO;
import initiative.guru.spring6plus.spring6reactive.service.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Component
@RequiredArgsConstructor
public class BeerHandler {

    private final static String BEER_ID = "beerId";

    private final BeerService beerService;

    public Mono<ServerResponse> getBeers(ServerRequest request) {
        return ok()
            .contentType(MediaType.APPLICATION_JSON)
            .location(URI.create(request.path())).build()
            .flatMap(response -> beerService.getBeers()
                // If no records found, just return an empty OK.
                .as(beerDTOs -> ServerResponse.from(response).body(beerDTOs, BeerDTO.class))
            );
    }

    public Mono<ServerResponse> getBeerById(ServerRequest request) {
        return ok()
            .contentType(MediaType.APPLICATION_JSON)
            .location(URI.create(request.path())).build()
            .flatMap(response -> beerService.getBeerById(Integer.valueOf(request.pathVariable(BEER_ID)))
                // If no record was found, just return an empty OK.
                .as(beerDto -> ServerResponse.from(response).body(beerDto, BeerDTO.class))
            );
    }

    public Mono<ServerResponse> createNewBeer(ServerRequest request) {
        return request.bodyToMono(BeerDTO.class)
            .flatMap(beerService::saveNewBeer)
            .flatMap(savedBeer ->
                created(URI.create(request.path() + "/" + savedBeer.getId()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(savedBeer)));
    }

    public Mono<ServerResponse> updateExistingBeer(ServerRequest request) {
        return request.bodyToMono(BeerDTO.class)
            .flatMap(beerDto -> beerService.updateBeer(Integer.valueOf(request.pathVariable(BEER_ID)), beerDto))
            .flatMap(updatedBeer -> ok()
                .contentType(MediaType.APPLICATION_JSON)
                .location(URI.create(request.path()))
                .body(BodyInserters.fromValue(updatedBeer))
            )
            .switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> patchExistingBeer(ServerRequest request) {
        return request.bodyToMono(BeerDTO.class)
            .flatMap(beerDto -> beerService.patchBeer(Integer.valueOf(request.pathVariable(BEER_ID)), beerDto))
            .flatMap(updatedBeer -> ok()
                .contentType(MediaType.APPLICATION_JSON)
                .location(URI.create(request.path()))
                .body(BodyInserters.fromValue(updatedBeer))
            )
            .switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> deleteById(ServerRequest request) {
        int id = Integer.parseInt(request.pathVariable(BEER_ID));
        return ok()
            .contentType(MediaType.APPLICATION_JSON)
            .location(URI.create(request.path()))
            .build()
            .flatMap(response -> beerService.deleteBeerById(id)
                .then(ServerResponse.from(response).body(BodyInserters.fromValue("{\"message\": \"Record "+ id +" deleted.\"}")))
            )
            .switchIfEmpty(notFound().build());
    }
}
