package br.com.bortolo.products.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductDTO {

    private String name;
    private BigDecimal value;

}
