package com.bereket.safari.service.impl;

import com.bereket.safari.dto.CustomerDto;
import com.bereket.safari.exception.DuplicateResourceException;
import com.bereket.safari.exception.ResourceNotFoundException;
import com.bereket.safari.model.Customer;
import com.bereket.safari.repository.CustomerRepository;
import com.bereket.safari.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repo;

    public CustomerServiceImpl(CustomerRepository repo) {
        this.repo = repo;
    }

    @Override
    public CustomerDto createCustomer(CustomerDto dto) {
        repo.findByEmail(dto.getEmail()).ifPresent(c -> {
            throw new DuplicateResourceException("Email already exists: " + dto.getEmail());
        });

        Customer c = new Customer();
        c.setName(dto.getName());
        c.setEmail(dto.getEmail());
        c.setPhone(dto.getPhone());
        Customer saved = repo.save(c);

        return new CustomerDto(saved.getId(), saved.getName(), saved.getEmail(), saved.getPhone());
    }

    @Override
    public CustomerDto getCustomerById(Long id) {
        Customer c = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));
        return new CustomerDto(c.getId(), c.getName(), c.getEmail(), c.getPhone());
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        return repo.findAll().stream()
                .map(c -> new CustomerDto(c.getId(), c.getName(), c.getEmail(), c.getPhone()))
                .toList();
    }

    @Override
    public CustomerDto updateCustomer(Long id, CustomerDto dto) {
        Customer c = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));
        c.setName(dto.getName());
        c.setEmail(dto.getEmail());
        c.setPhone(dto.getPhone());
        Customer updated = repo.save(c);
        return new CustomerDto(updated.getId(), updated.getName(), updated.getEmail(), updated.getPhone());
    }

    @Override
    public void deleteCustomer(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found with id " + id);
        }
        repo.deleteById(id);
    }
}
