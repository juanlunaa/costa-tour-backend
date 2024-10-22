package application.costa_tour.dto.payment;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderRequest {
    private String title;
    private BigDecimal price;
    private Integer quantity;
    private String currencyId;
}
