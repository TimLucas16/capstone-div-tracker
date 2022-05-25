package de.neuefische.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection ="stockCol")
public class Stock {

    @Id
    private String id;
    private String symbol;
    private String companyName;
    private double shares;
    private double costPrice;
    private double value;
    private double price;
    private String website;
    private String image;

}
