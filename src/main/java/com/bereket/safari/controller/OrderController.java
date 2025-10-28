package com.bereket.safari.controller;

import com.bereket.safari.dto.OrderDto;
import com.bereket.safari.model.Customer;
import com.bereket.safari.model.Order;
import com.bereket.safari.repository.CustomerRepository;
import com.bereket.safari.repository.OrderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderRepository orderRepo;
    private final CustomerRepository customerRepo;

    private OrderDto mapToDto(Order order) {
        return new OrderDto(
                order.getId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getCreatedAt(),
                order.getCustomer().getId(),
                order.getCustomer().getName());
    }

    public OrderController(OrderRepository orderRepo, CustomerRepository customerRepo) {
        this.orderRepo = orderRepo;
        this.customerRepo = customerRepo;
    }

    // Create order
    @PostMapping("/customer/{customerId}")
    public ResponseEntity<OrderDto> createOrder(@PathVariable Long customerId, @RequestBody Order order) {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        order.setCustomer(customer);
        Order saved = orderRepo.save(order);

        return ResponseEntity
                .created(URI.create("/api/v1/orders/" + saved.getId()))
                .body(mapToDto(saved));
    }

    // Get orders by customer
    @GetMapping("/customer/{customerId}")
    public List<OrderDto> getByCustomer(@PathVariable Long customerId) {
        List<Order> orders = orderRepo.findByCustomerId(customerId);
        return orders.stream().map(this::mapToDto).toList();
    }

    // Get all orders
    @GetMapping
    public List<OrderDto> getAll() {
        return orderRepo.findAll().stream().map(this::mapToDto).toList();
    }

}
