package com.teletrader.teletrader.order;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody @Valid CreateOrderRequest createOrderRequest) {
        try {
            Order newOrder = orderService.createOrder(createOrderRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PatchMapping("/cancel/{id}")
    public ResponseEntity<Order> cancelOrder(@PathVariable Integer id) {
        try {
            Order cancelledOrder = orderService.cancelOrder(id);
            return ResponseEntity.status(HttpStatus.OK).body(cancelledOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
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

    @GetMapping("/top")
    public ResponseEntity<?> getTopOrders(@RequestParam Type type, @RequestParam String stockSymbol) {
        try {
            List<Order> topOrders = orderService.getTopOrders(type, stockSymbol);
            return ResponseEntity.ok(topOrders);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }
}
