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
    public ResponseEntity<ResponseWrapper<List<OrderResponse>>> getAllActiveOrders() {
        ResponseWrapper<List<OrderResponse>> response = orderService.getAllActiveOrders();
        return ResponseEntity.ok(response);
    }

    // Creates a new order
    @PostMapping
    public ResponseEntity<ResponseWrapper<OrderResponse>> createOrder(@RequestBody @Valid CreateOrderRequest createOrderRequest) {
        try {
            ResponseWrapper<OrderResponse> response = orderService.createOrder(createOrderRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            ResponseWrapper<OrderResponse> errorResponse = new ResponseWrapper<>(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    // Cancels an order with that id
    @PatchMapping("/cancel/{id}")
    public ResponseEntity<ResponseWrapper<OrderResponse>> cancelOrder(@PathVariable Integer id) {
        try {
            ResponseWrapper<OrderResponse> response = orderService.cancelOrder(id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            ResponseWrapper<OrderResponse> errorResponse = new ResponseWrapper<>(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    // Returns a list of 10 last active orders
    @GetMapping("/latest")
    public ResponseEntity<ResponseWrapper<List<OrderResponse>>> getLast10OrdersByType(@RequestParam Type type) {
        ResponseWrapper<List<OrderResponse>> response = orderService.getLast10OrdersByType(type);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Returns all (active and not) orders made by user
    @GetMapping("/my")
    public ResponseEntity<?> getMyOrders() {
        try {
            ResponseWrapper<List<OrderResponse>> response = orderService.getCurrentUserOrders();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            ResponseWrapper<OrderResponse> errorResponse = new ResponseWrapper<>(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    // Returns a list of top 10 active orders
    @GetMapping("/top")
    public ResponseEntity<?> getTopOrders(@RequestParam Type type, @RequestParam String stockSymbol) {
        try {
            ResponseWrapper<List<OrderResponse>> response = orderService.getTopOrders(type, stockSymbol);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            ResponseWrapper<OrderResponse> errorResponse = new ResponseWrapper<>(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Returns a list of all orders
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseWrapper<List<OrderResponse>>> getAllOrders() {
        ResponseWrapper<List<OrderResponse>> response = orderService.getAllOrders();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/accept/{id}")
    public ResponseEntity<ResponseWrapper<OrderResponse>> acceptOrder(@PathVariable Integer id) {
        try {
            ResponseWrapper<OrderResponse> response = orderService.acceptOrder(id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            ResponseWrapper<OrderResponse> errorResponse = new ResponseWrapper<>(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
