package initiative.guru.spring6plus.spring6reactive.domain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Beer {

    @Id
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

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
}
