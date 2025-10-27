package com.bereket.safari.service;

import com.bereket.safari.model.Customer;
import com.bereket.safari.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository repo;

    public CustomerService(CustomerRepository repo) {
        this.repo = repo;
    }

    public List<Customer> getAll() {
        return repo.findAll();
    }

    public Customer getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public Customer create(Customer customer) {
        return repo.save(customer);
    }

    public Customer update(Long id, Customer newData) {
        Customer existing = getById(id);
        existing.setName(newData.getName());
        existing.setEmail(newData.getEmail());
        existing.setPhone(newData.getPhone());
        return repo.save(existing);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
