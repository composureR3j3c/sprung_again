package com.bereket.safari.service.impl;

import com.bereket.safari.dto.OrderDto;
import com.bereket.safari.exception.ResourceNotFoundException;
import com.bereket.safari.model.Customer;
import com.bereket.safari.model.Order;
import com.bereket.safari.repository.CustomerRepository;
import com.bereket.safari.repository.OrderRepository;
import com.bereket.safari.service.OrderService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepo;
    private final CustomerRepository customerRepo;

    public OrderServiceImpl(OrderRepository orderRepo, CustomerRepository customerRepo) {
        this.orderRepo = orderRepo;
        this.customerRepo = customerRepo;
    }

    private OrderDto mapToDto(Order o) {
    return OrderDto.builder()
            .id(o.getId())
            .productName(o.getProductName())
            .quantity(o.getQuantity())
            .price(o.getPrice())
            .createdAt(o.getCreatedAt())
            .customerId(o.getCustomer().getId())
            .customerName(o.getCustomer().getName())
            .build();
}

    @Override
    public OrderDto createOrder(Long customerId, OrderDto dto) {
        Customer c = customerRepo.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + customerId));

        Order o = new Order();
        o.setCustomer(c);
        o.setProductName(dto.getProductName());
        o.setQuantity(dto.getQuantity());
        o.setPrice(dto.getPrice());

        Order saved = orderRepo.save(o);
        return mapToDto(saved);
    }

    @Override
    public List<OrderDto> getOrdersByCustomer(Long customerId) {
        List<Order> orders = orderRepo.findByCustomerId(customerId);
        return orders.stream().map(this::mapToDto).toList();
    }

    @Override
    public List<OrderDto> getAllOrders() {
        return orderRepo.findAll().stream().map(this::mapToDto).toList();
    }
}
