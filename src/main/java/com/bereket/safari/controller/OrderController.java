package com.bereket.safari.controller;

import com.bereket.safari.dto.OrderDto;
import com.bereket.safari.model.Customer;
import com.bereket.safari.model.Order;
import com.bereket.safari.repository.CustomerRepository;
import com.bereket.safari.repository.OrderRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderRepository orderRepo;
    private final CustomerRepository customerRepo;

    public OrderController(OrderRepository orderRepo, CustomerRepository customerRepo) {
        this.orderRepo = orderRepo;
        this.customerRepo = customerRepo;
    }

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

    // Create
    @PostMapping("/customer/{customerId}")
    public ResponseEntity<OrderDto> createOrder(
            @PathVariable Long customerId,
            @Valid @RequestBody OrderDto dto) {

        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Order order = new Order();
        order.setProductName(dto.getProductName());
        order.setQuantity(dto.getQuantity());
        order.setPrice(dto.getPrice());
        order.setCustomer(customer);

        Order saved = orderRepo.save(order);
        return ResponseEntity
                .created(URI.create("/api/v1/orders/" + saved.getId()))
                .body(mapToDto(saved));
    }

    // Read all
    @GetMapping
    public List<OrderDto> getAll() {
        return orderRepo.findAll().stream().map(this::mapToDto).toList();
    }

    // Read by ID
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getById(@PathVariable Long id) {
        return orderRepo.findById(id)
                .map(order -> ResponseEntity.ok(mapToDto(order)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Read by Customer
    @GetMapping("/customer/{customerId}")
    public List<OrderDto> getByCustomer(@PathVariable Long customerId) {
        return orderRepo.findByCustomerId(customerId).stream().map(this::mapToDto).toList();
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<OrderDto> updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody OrderDto dto) {

        return orderRepo.findById(id)
                .map(existing -> {
                    existing.setProductName(dto.getProductName());
                    existing.setQuantity(dto.getQuantity());
                    existing.setPrice(dto.getPrice());
                    Order updated = orderRepo.save(existing);
                    return ResponseEntity.ok(mapToDto(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        if (!orderRepo.existsById(id))
            return ResponseEntity.notFound().build();
        orderRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    public List<OrderDto> filterOrders(@RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Integer minQuantity) {
        List<Order> orders;
        if (minPrice != null) {
            orders = orderRepo.findByPriceGreaterThanEqual(minPrice);
        } else if (minQuantity != null) {
            orders = orderRepo.findByQuantityGreaterThanEqual(minQuantity);
        } else {
            orders = orderRepo.findAll();
        }
        return orders.stream().map(this::mapToDto).toList();
    }

}
