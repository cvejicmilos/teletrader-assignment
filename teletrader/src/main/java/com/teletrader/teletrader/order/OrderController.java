package com.teletrader.teletrader.order;

import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/latest")
    public ResponseEntity<List<Order>> getLast10OrdersByType(@RequestParam Type type) {
        List<Order> orders = orderService.getLast10OrdersByType(type);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyOrders() {
        try {
            List<Order> myOrders = orderService.getCurrentUserOrders();
            return ResponseEntity.ok(myOrders);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
