package com.bereket.safari.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CustomerDto {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be at most {max} characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    // Example phone rule: international format or simple digits; adjust pattern to your needs
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\+?[0-9 .-]{7,20}$", message = "Phone number is invalid")
    private String phone;

    // Constructors, getters, setters
    public CustomerDto() {}

    public CustomerDto(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
