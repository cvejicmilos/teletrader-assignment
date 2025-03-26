package com.teletrader.teletrader.order;

import org.antlr.v4.runtime.atn.SemanticContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByCreatorId(Integer creatorId);
    List<Order> findTop10ByTypeOrderByIdDesc(Type type);
    @Query("SELECT o FROM Order o JOIN FETCH o.creator WHERE o.creator.username = :username")
    List<Order> findByCreatorUsername(@Param("username") String username);
}