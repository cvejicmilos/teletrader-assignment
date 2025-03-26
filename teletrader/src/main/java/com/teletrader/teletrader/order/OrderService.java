package com.teletrader.teletrader.order;

import com.teletrader.teletrader.user.User;
import com.teletrader.teletrader.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public List<OrderResponse> getAllActiveOrders() {
        List<Order> orders = orderRepository.findByIsActiveTrue();
        return orders
                .stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> getLast10OrdersByType(Type type) {
        List<Order> orders = orderRepository.findTop10ByTypeAndIsActiveTrueOrderByIdDesc(type);
        return orders
                .stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> getCurrentUserOrders () {
        String username = getAuthenticatedUsername();
        List<Order> orders = orderRepository.findByCreatorUsername(username);
        return orders
                .stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }

    private String getAuthenticatedUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        throw new IllegalStateException("User not authenticated");
    }

    public OrderResponse createOrder(CreateOrderRequest createOrderRequest) {
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

        Order savedOrder = orderRepository.save(newOrder);
        return mapToOrderResponse(savedOrder);
    }

    public OrderResponse cancelOrder(Integer id) {
        String username = getAuthenticatedUsername();

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Order not found"));

        if (!order.getCreator().getUsername().equals(username)) {
            throw new IllegalStateException("You can only cancel your own orders");
        }

        order.setIsActive(false);

        Order cancelledOrder = orderRepository.save(order);
        return mapToOrderResponse(cancelledOrder);
    }

    public List<OrderResponse> getTopOrders(Type type, String stockSymbol) {
        if (stockSymbol == null || stockSymbol.isBlank()) {
            throw new IllegalArgumentException("Stock symbol cannot be null or empty");
        }

        if (type == Type.BUY) {
            List<Order> orders = orderRepository.findTop10ByStockSymbolAndTypeOrderByStockPriceDesc(stockSymbol, type);
            return orders
                    .stream()
                    .map(this::mapToOrderResponse)
                    .collect(Collectors.toList());
        } else if (type == Type.SELL) {
            List<Order> orders = orderRepository.findTop10ByStockSymbolAndTypeOrderByStockPriceAsc(stockSymbol, type);
            return orders
                    .stream()
                    .map(this::mapToOrderResponse)
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Order type must be BUY or SELL");
        }
    }

    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders
                .stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse acceptOrder(Integer id) {

        String username = getAuthenticatedUsername();

        User acceptor = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Order not found"));

        if (!order.getIsActive()) {
            throw new IllegalStateException("Order is no longer active");
        }

        if (order.getCreator().getUsername().equals(username)) {
            throw new IllegalStateException("You cannot accept your own order");
        }

        if (order.getAcceptor() != null) {
            throw new IllegalStateException("Order is already accepted");
        }

        order.setAcceptor(acceptor);
        order.setIsActive(false);
        Order acceptedOrder = orderRepository.save(order);
        return mapToOrderResponse(acceptedOrder);
    }

    public OrderResponse mapToOrderResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .creatorId(order.getCreator().getId())
                .acceptorId(order.getAcceptor() != null ? order.getAcceptor().getId() : null)
                .stockPrice(order.getStockPrice())
                .stockAmount(order.getStockAmount())
                .stockSymbol(order.getStockSymbol())
                .isActive(order.getIsActive())
                .type(order.getType())
                .build();
    }
}