package com.teletrader.teletrader.order;

import com.teletrader.teletrader.user.User;
import com.teletrader.teletrader.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public List<Order> getAllActiveOrders() {
        return orderRepository.findByIsActiveTrue();
    }

    public List<Order> getLast10OrdersByType(Type type) {
        return orderRepository.findTop10ByTypeAndIsActiveTrueOrderByIdDesc(type);
    }

    public List<Order> getCurrentUserOrders () {
        String username = getAuthenticatedUsername();
        return orderRepository.findByCreatorUsername(username);
    }

    private String getAuthenticatedUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        throw new IllegalStateException("User not authenticated");
    }

    public Integer getCurrentUserId() {
        String username = getAuthenticatedUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found"))
                .getId();
    }

    public Order createOrder(CreateOrderRequest createOrderRequest) {
        if (createOrderRequest.getStockPrice() <= 0 || createOrderRequest.getStockAmount() <= 0) {
            throw new IllegalArgumentException("Stock price and amount must be greater than zero");
        }

        String username = getAuthenticatedUsername();
        User creator = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        Order newOrder = Order.builder()
                .stockPrice(createOrderRequest.getStockPrice())
                .stockAmount(createOrderRequest.getStockAmount())
                .stockSymbol(createOrderRequest.getStockSymbol())
                .type(createOrderRequest.getType())
                .creator(creator)
                .isActive(true)
                .build();

        return orderRepository.save(newOrder);
    }

    public Order cancelOrder(Integer id) {
        String username = getAuthenticatedUsername();

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Order not found"));

        if (!order.getCreator().getUsername().equals(username)) {
            throw new IllegalStateException("You can only cancel your own orders");
        }

        order.setIsActive(false);

        return orderRepository.save(order);
    }

    public List<Order> getTopOrders(Type type, String stockSymbol) {
        if (stockSymbol == null || stockSymbol.isBlank()) {
            throw new IllegalArgumentException("Stock symbol cannot be null or empty");
        }

        if (type == Type.BUY) {
            return orderRepository.findTop10ByStockSymbolAndTypeOrderByStockPriceDesc(stockSymbol, type);
        } else if (type == Type.SELL) {
            return orderRepository.findTop10ByStockSymbolAndTypeOrderByStockPriceAsc(stockSymbol, type);
        } else {
            throw new IllegalArgumentException("Order type must be BUY or SELL");
        }
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}