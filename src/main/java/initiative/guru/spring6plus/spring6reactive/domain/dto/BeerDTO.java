package initiative.guru.spring6plus.spring6reactive.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerDTO {

    private Integer id;

    private String beerName;
    private String beerStyle;
    private String upc;

    private Integer quantityOnHand;
    private BigDecimal price;

    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
