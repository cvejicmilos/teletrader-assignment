package com.teletrader.teletrader.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByIsActiveTrue();
    @Query("SELECT o FROM Order o JOIN FETCH o.creator WHERE o.creator.username = :username")
    List<Order> findByCreatorUsername(@Param("username") String username);
    List<Order> findTop10ByStockSymbolAndTypeOrderByStockPriceDesc(String stockSymbol, Type type);
    List<Order> findTop10ByStockSymbolAndTypeOrderByStockPriceAsc(String stockSymbol, Type type);
    List<Order> findTop10ByTypeAndIsActiveTrueOrderByIdDesc(Type type);
}