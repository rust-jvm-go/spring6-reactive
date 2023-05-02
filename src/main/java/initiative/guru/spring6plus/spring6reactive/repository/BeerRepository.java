package initiative.guru.spring6plus.spring6reactive.repository;

import initiative.guru.spring6plus.spring6reactive.domain.model.Beer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BeerRepository extends ReactiveCrudRepository<Beer, Integer> {
}
