package com.amblessed.chinookapi.controller;



/*
 * @Project Name: ChinookAPI
 * @Author: Okechukwu Bright Onwumere
 * @Created: 01-Sep-25
 */

import com.amblessed.chinookapi.entity.Customer;
import com.amblessed.chinookapi.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/city")
    public ResponseEntity<List<Customer>> getCustomersByCity(@RequestParam(name = "city") String city) {
        return ResponseEntity.ok(customerService.getCustomersByCity(city));
    }

    @GetMapping("/country")
    public ResponseEntity<List<Customer>> getCustomersByCountry(@RequestParam(name = "country") String country) {
        return ResponseEntity.ok(customerService.getCustomersByCountry(country));
    }


    @GetMapping()
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    // POST endpoint to add a new customer
    @PostMapping("/create")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        if (customer.getFirstName() == null || customer.getLastName() == null) {
            return ResponseEntity.badRequest().build();
        }
        Customer savedCustomer = customerService.save(customer);
        return ResponseEntity.ok(savedCustomer);
    }
}

