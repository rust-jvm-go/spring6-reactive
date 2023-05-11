package initiative.guru.spring6plus.spring6reactive.service;

import initiative.guru.spring6plus.spring6reactive.domain.dto.BeerDTO;
import initiative.guru.spring6plus.spring6reactive.mapper.BeerMapper;
import initiative.guru.spring6plus.spring6reactive.repository.BeerRepository;
import initiative.guru.spring6plus.spring6reactive.service.validator.CustomValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    private final CustomValidator customValidator;

    @Override
    public Mono<Void> deleteBeerById(Integer beerId) {
        return beerRepository.deleteById(beerId)
            .switchIfEmpty(Mono.empty());
    }

    @Override
    public Mono<BeerDTO> patchBeer(Integer beerId, BeerDTO beerDTO) {

        return beerRepository.findById(beerId)
            .switchIfEmpty(Mono.empty())
            .map(foundBeer -> beerMapper.patchBeer(foundBeer, beerDTO))  // Uses conditional mappings
            .doOnNext(customValidator::validate)
            .flatMap(beerRepository::save)
            .map(beerMapper::beerToBeerDto);
    }

    @Override
    public Mono<BeerDTO> updateBeer(Integer beerId, BeerDTO beerDTO) {

        return beerRepository.findById(beerId)
            .switchIfEmpty(Mono.empty())
            .map(foundBeer -> beerMapper.updateBeer(foundBeer, beerDTO))  // Uses conditional mappings
            .doOnNext(customValidator::validate)
            .flatMap(beerRepository::save)
            .map(beerMapper::beerToBeerDto);
    }

    @Override
    public Mono<BeerDTO> saveNewBeer(BeerDTO beerDTO) {
        return beerRepository.save(beerMapper.beerDtoToBeer(beerDTO))
            .map(beerMapper::beerToBeerDto);
    }

    @Override
    public Mono<BeerDTO> getBeerById(Integer beerId) {
        return beerRepository.findById(beerId)
            .map(beerMapper::beerToBeerDto);
    }

    @Override
    public Flux<BeerDTO> getBeers() {
        return beerRepository.findAll()
            .map(beerMapper::beerToBeerDto);
    }
}
