package initiative.guru.spring6plus.spring6reactive.mapper;

import initiative.guru.spring6plus.spring6reactive.domain.Beer;
import initiative.guru.spring6plus.spring6reactive.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    Beer beerDtoToBeer(BeerDTO dto);

    BeerDTO beerToBeerDto(Beer beer);
}
