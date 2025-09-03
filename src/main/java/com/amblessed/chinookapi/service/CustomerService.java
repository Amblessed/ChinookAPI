package com.amblessed.chinookapi.service;



/*
 * @Project Name: ChinookAPI
 * @Author: Okechukwu Bright Onwumere
 * @Created: 01-Sep-25
 */


import com.amblessed.chinookapi.entity.Customer;
import com.amblessed.chinookapi.exception.ResourceNotFoundException;
import com.amblessed.chinookapi.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public List<Customer> getCustomersByCity(String city) {
        return customerRepository.findCustomersByCityIgnoreCase(city);
    }

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public List<Customer> getCustomersByCountry(String country) {
        return customerRepository.findCustomersByCountryIgnoreCase(country);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("Customer with id: %d not found", id)));
    }



}
