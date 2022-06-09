package de.neuefische.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "stockCol")
public class Stock {

    @Id
    private String id;
    private String symbol;
    private String companyName;
    private BigDecimal shares;
    private BigDecimal costPrice;
    private BigDecimal value;
    private BigDecimal price;
    private BigDecimal totalReturn;
    private BigDecimal totalReturnPercent;
    private String website;
    private String image;
    private String isin;

}
