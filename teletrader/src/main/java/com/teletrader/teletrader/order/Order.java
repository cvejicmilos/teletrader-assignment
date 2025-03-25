package com.teletrader.teletrader.order;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "creator_id", nullable = false)
    private Integer creatorId;

    @Column(name = "acceptor_id")
    private Integer acceptorId;

    private Float stockPrice;
    private Integer stockAmount;
    private String stockSymbol;

    @Builder.Default
    private Boolean isActive = true;

    @Enumerated(EnumType.STRING)
    private Type type;
}
