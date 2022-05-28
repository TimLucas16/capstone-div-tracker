package de.neuefische.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Portfolio {

    private int pfValue;
    private int pfTotalReturnAbsolute;
    private int pfTotalReturnPercent;
}
