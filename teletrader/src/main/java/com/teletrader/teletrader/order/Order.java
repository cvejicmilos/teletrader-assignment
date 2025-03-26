package com.teletrader.teletrader.order;

import com.teletrader.teletrader.user.User;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Column(name = "acceptor_id")
    private Integer acceptorId;

    @Column(nullable = false)
    private Float stockPrice;
    @Column(name = "stock_amount", nullable = false)
    private Integer stockAmount;
    @Column(name = "stock_symbol", nullable = false)
    private String stockSymbol;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;
}
