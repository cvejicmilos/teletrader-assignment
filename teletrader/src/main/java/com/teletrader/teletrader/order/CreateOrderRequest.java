package com.teletrader.teletrader.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
    private Float stockPrice;
    private Integer stockAmount;
    private String stockSymbol;
    private Type type;
}
