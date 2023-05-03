package initiative.guru.spring6plus.spring6reactive.webflux.handler;

import initiative.guru.spring6plus.spring6reactive.domain.dto.BeerDTO;
import initiative.guru.spring6plus.spring6reactive.service.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RequiredArgsConstructor
public class BeerHandler {

    private final static String beerId = "beerId";
    private final static String msgNoRecords = "{\"message\": \"No records found.\"}";

    private final BeerService beerService;

    public Mono<ServerResponse> getBeers(ServerRequest request) {
        return ok()
            .contentType(MediaType.APPLICATION_JSON)
            .location(URI.create(request.path())).build()
            .flatMap(response -> {
                Flux<BeerDTO> beerDTOs = beerService.getBeers();
                return beerDTOs
                    .hasElements()
                    .flatMap(found -> {
                        if (found) {
                            return ServerResponse.from(response).body(beerDTOs, BeerDTO.class);
                        }
                        return ServerResponse.from(response).body(BodyInserters.fromValue(msgNoRecords));
                    });
            });
    }

    public Mono<ServerResponse> getBeerById(ServerRequest request) {
        return ok()
            .contentType(MediaType.APPLICATION_JSON)
            .location(URI.create(request.path())).build()
            .flatMap(response -> {
                Mono<BeerDTO> beerDto = beerService.getBeerById(Integer.valueOf(request.pathVariable(beerId)));
                return beerDto
                    .hasElement()
                    .flatMap(found -> {
                        if (found) {
                            return ServerResponse.from(response).body(beerDto, BeerDTO.class);
                        }
                        return ServerResponse.from(response).body(BodyInserters.fromValue(msgNoRecords));
                    });
            });
    }

    public Mono<ServerResponse> createNewBeer(ServerRequest request) {
        return request.bodyToMono(BeerDTO.class)
            .flatMap(beerService::saveNewBeer)
            .flatMap(savedBeer ->
                ServerResponse.created(URI.create(request.path() + "/" + savedBeer.getId()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(savedBeer)));
    }

    public Mono<ServerResponse> updateExistingBeer(ServerRequest request) {
        return request.bodyToMono(BeerDTO.class)
            .flatMap(beerDto -> beerService.updateBeer(Integer.valueOf(request.pathVariable(beerId)), beerDto))
            .flatMap(updatedBeer ->
                ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .location(URI.create(request.path()))
                    .body(BodyInserters.fromValue(updatedBeer)));
    }

    public Mono<ServerResponse> patchExistingBeer(ServerRequest request) {
        return request.bodyToMono(BeerDTO.class)
            .flatMap(beerDto -> beerService.patchBeer(Integer.valueOf(request.pathVariable(beerId)), beerDto))
            .flatMap(updatedBeer ->
                ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .location(URI.create(request.path()))
                    .body(BodyInserters.fromValue(updatedBeer)));
    }

    public Mono<ServerResponse> deleteById(ServerRequest request) {
        int id = Integer.parseInt(request.pathVariable(beerId));
        return ok()
            .contentType(MediaType.APPLICATION_JSON)
            .location(URI.create(request.path()))
            .build(beerService.deleteBeerById(Integer.valueOf(request.pathVariable(beerId))))
            .switchIfEmpty(ServerResponse.notFound().build())
            .flatMap(response -> ServerResponse.from(response)
                .body(BodyInserters.fromValue("{\"message\": \"Record "+ id +" deleted.\"}")));
    }
}
