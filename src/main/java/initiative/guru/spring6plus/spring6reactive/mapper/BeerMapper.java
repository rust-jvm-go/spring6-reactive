package initiative.guru.spring6plus.spring6reactive.mapper;

import initiative.guru.spring6plus.spring6reactive.domain.model.Beer;
import initiative.guru.spring6plus.spring6reactive.domain.dto.BeerDTO;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Mapper
@Component
public interface BeerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @interface BeerMapperUnmappedPolicy {}

    Beer beerDtoToBeer(BeerDTO dto);

    BeerDTO beerToBeerDto(Beer beer);

    // Mapped fields are set to null if null in DTO.
    @BeerMapperUnmappedPolicy
    Beer updateBeer(@MappingTarget Beer beer, BeerDTO beerDTO);

    // Mapped fields are ignored if null in DTO.
    @BeerMapperUnmappedPolicy
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Beer patchBeer(@MappingTarget Beer beer, BeerDTO beerDTO);

    @Condition
    default boolean stringIsNotEmpty(String value) {
        return value != null && !value.isEmpty();
    }

    @Condition
    default boolean integerIsNotEmpty(Integer value) {
        return value != null;
    }

    @Condition
    default boolean bigDecimalIsNotEmpty(BigDecimal value) {
        return value != null;
    }
}
