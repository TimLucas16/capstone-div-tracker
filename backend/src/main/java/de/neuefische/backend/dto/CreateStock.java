package de.neuefische.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateStock {

    private String symbol;
    private String name;
    private double amountOfShares;
}
