// src/main/java/com/bereket/safari/controller/CustomerController.java
package com.bereket.safari.controller;

import com.bereket.safari.dto.CustomerDto;
import com.bereket.safari.model.Customer;
import com.bereket.safari.repository.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerRepository repo;
    public CustomerController(CustomerRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Customer> all() {
        return repo.findAll();
    }

    @PostMapping
    public ResponseEntity<Customer> create(@Valid @RequestBody CustomerDto dto) {
        Customer customer = new Customer(dto.getName(), dto.getEmail(), dto.getPhone());
        Customer saved = repo.save(customer);
        return ResponseEntity.created(URI.create("/api/v1/customers/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> update(@PathVariable Long id, @Valid @RequestBody CustomerDto dto) {
        return repo.findById(id)
            .map(existing -> {
                existing.setName(dto.getName());
                existing.setEmail(dto.getEmail());
                existing.setPhone(dto.getPhone());
                Customer updated = repo.save(existing);
                return ResponseEntity.ok(updated);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> get(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/search")
public List<Customer> searchCustomers(@RequestParam(required = false) String name,
                                      @RequestParam(required = false) String email) {
    if (name != null) return repo.findByNameContainingIgnoreCase(name);
    if (email != null) return repo.findByEmailContainingIgnoreCase(email);
    return repo.findAll();
}


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
