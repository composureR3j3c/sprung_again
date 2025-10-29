// src/main/java/com/bereket/safari/controller/CustomerController.java
package com.bereket.safari.controller;

import com.bereket.safari.dto.CustomerDto;
import com.bereket.safari.exception.DuplicateResourceException;
import com.bereket.safari.model.Customer;
import com.bereket.safari.repository.CustomerRepository;
import com.bereket.safari.service.CustomerService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CustomerDto> create(@Valid @RequestBody CustomerDto dto) {
        CustomerDto saved = service.createCustomer(dto);
        return ResponseEntity
                .created(URI.create("/api/v1/customers/" + saved.getId()))
                .body(saved);
    }

    @GetMapping("/{id}")
    public CustomerDto getById(@PathVariable Long id) {
        return service.getCustomerById(id);
    }

    @GetMapping
    public List<CustomerDto> getAll() {
        return service.getAllCustomers();
    }

    @PutMapping("/{id}")
    public CustomerDto update(@PathVariable Long id, @Valid @RequestBody CustomerDto dto) {
        return service.updateCustomer(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
