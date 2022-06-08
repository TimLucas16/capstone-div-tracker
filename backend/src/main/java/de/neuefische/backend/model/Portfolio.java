package de.neuefische.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Portfolio {

    private BigDecimal pfValue;
    private BigDecimal pfTotalReturnAbsolute;
    private BigDecimal pfTotalReturnPercent;
}
