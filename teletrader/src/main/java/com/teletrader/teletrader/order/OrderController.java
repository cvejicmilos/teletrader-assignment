package com.teletrader.teletrader.order;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // Returns a list of all ACTIVE orders
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllActiveOrders() {
        List<OrderResponse> orders = orderService.getAllActiveOrders();
        return ResponseEntity.ok(orders);
    }

    // Creates a new order
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody @Valid CreateOrderRequest createOrderRequest) {
        try {
            OrderResponse newOrder = orderService.createOrder(createOrderRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Cancels an order with that id
    @PatchMapping("/cancel/{id}")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Integer id) {
        try {
            OrderResponse cancelledOrder = orderService.cancelOrder(id);
            return ResponseEntity.status(HttpStatus.OK).body(cancelledOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Returns a list of 10 last active orders
    @GetMapping("/latest")
    public ResponseEntity<List<OrderResponse>> getLast10OrdersByType(@RequestParam Type type) {
        List<OrderResponse> orders = orderService.getLast10OrdersByType(type);
        return ResponseEntity.ok(orders);
    }

    // Returns all (active and not) orders made by user
    @GetMapping("/my")
    public ResponseEntity<?> getMyOrders() {
        try {
            List<OrderResponse> myOrders = orderService.getCurrentUserOrders();
            return ResponseEntity.ok(myOrders);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Returns a list of top 10 active orders
    @GetMapping("/top")
    public ResponseEntity<?> getTopOrders(@RequestParam Type type, @RequestParam String stockSymbol) {
        try {
            List<OrderResponse> topOrders = orderService.getTopOrders(type, stockSymbol);
            return ResponseEntity.ok(topOrders);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    // Returns a list of all orders
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @PatchMapping("/accept/{id}")
    public ResponseEntity<OrderResponse> acceptOrder(@PathVariable Integer id) {
        try {
            OrderResponse acceptedOrder = orderService.acceptOrder(id);
            return ResponseEntity.status(HttpStatus.OK).body(acceptedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
