package initiative.guru.spring6plus.spring6reactive.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "Beer name is required")
    @Size(min = 2, max = 255)
    private String beerName;

    @NotBlank(message = "Beer style is required")
    @Size(min = 1, max = 255)
    private String beerStyle;

    @NotBlank(message = "UPC is required")
    @Size(min = 5, max = 25)
    private String upc;

    private Integer quantityOnHand;
    private BigDecimal price;

    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
