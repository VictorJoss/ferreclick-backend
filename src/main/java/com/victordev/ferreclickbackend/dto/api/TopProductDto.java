package com.victordev.ferreclickbackend.dto.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TopProductDto {
    private String productName;
    private Long totalSold;
}
