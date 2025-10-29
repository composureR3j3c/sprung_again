package com.bereket.safari.service;

import com.bereket.safari.dto.OrderDto;
import java.util.List;

public interface OrderService {
    OrderDto createOrder(Long customerId, OrderDto dto);
    List<OrderDto> getOrdersByCustomer(Long customerId);
    List<OrderDto> getAllOrders();
}
