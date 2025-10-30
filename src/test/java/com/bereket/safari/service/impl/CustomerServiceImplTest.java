package com.bereket.safari.service.impl;

import com.bereket.safari.dto.CustomerDto;
import com.bereket.safari.exception.DuplicateResourceException;
import com.bereket.safari.exception.ResourceNotFoundException;
import com.bereket.safari.model.Customer;
import com.bereket.safari.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

    @Mock
    private CustomerRepository repo;

    @InjectMocks
    private CustomerServiceImpl service;

    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customer = new Customer();
        customer.setId(1L);
        customer.setName("Bereket");
        customer.setEmail("bereket@example.com");
        customer.setPhone("+251911234567");
    }

    @Test
    void createCustomer_Success() {
        CustomerDto dto = new CustomerDto(null, "Bereket", "bereket@example.com", "+251911234567");

        when(repo.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(repo.save(any(Customer.class))).thenReturn(customer);

        CustomerDto result = service.createCustomer(dto);

        assertNotNull(result);
        assertEquals("Bereket", result.getName());
        verify(repo, times(1)).save(any(Customer.class));
    }

    @Test
    void createCustomer_DuplicateEmail_ThrowsException() {
        CustomerDto dto = new CustomerDto(null, "Bereket", "bereket@example.com", "+251911234567");

        when(repo.findByEmail(dto.getEmail())).thenReturn(Optional.of(customer));

        assertThrows(DuplicateResourceException.class, () -> service.createCustomer(dto));
    }

    @Test
    void getCustomerById_Success() {
        when(repo.findById(1L)).thenReturn(Optional.of(customer));

        CustomerDto result = service.getCustomerById(1L);

        assertEquals("Bereket", result.getName());
    }

    @Test
    void getCustomerById_NotFound_ThrowsException() {
        when(repo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getCustomerById(1L));
    }

    @Test
    void getAllCustomers_ReturnsList() {
        when(repo.findAll()).thenReturn(List.of(customer));

        List<CustomerDto> result = service.getAllCustomers();

        assertEquals(1, result.size());
        assertEquals("Bereket", result.get(0).getName());
    }

    @Test
    void updateCustomer_Success() {
        CustomerDto dto = new CustomerDto(null, "Updated", "updated@example.com", "+251999999999");

        when(repo.findById(1L)).thenReturn(Optional.of(customer));
        when(repo.save(any(Customer.class))).thenReturn(customer);

        CustomerDto result = service.updateCustomer(1L, dto);

        assertEquals("Updated", result.getName());
        verify(repo, times(1)).save(any(Customer.class));
    }

    @Test
    void deleteCustomer_Success() {
        when(repo.existsById(1L)).thenReturn(true);

        service.deleteCustomer(1L);

        verify(repo, times(1)).deleteById(1L);
    }

    @Test
    void deleteCustomer_NotFound_ThrowsException() {
        when(repo.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.deleteCustomer(1L));
    }
}
