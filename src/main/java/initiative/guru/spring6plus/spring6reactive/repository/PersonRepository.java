package initiative.guru.spring6plus.spring6reactive.repository;

import initiative.guru.spring6plus.spring6reactive.domain.model.Person;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

public interface PersonRepository {

    Mono<Person> getById(Integer id);

    Flux<Person> findAll();
}