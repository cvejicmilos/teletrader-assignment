package com.teletrader.teletrader.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Integer id;
    private Integer creatorId;
    private Integer acceptorId;
    private Float stockPrice;
    private Integer stockAmount;
    private String stockSymbol;
    private Boolean isActive;
    private Type type;
}
