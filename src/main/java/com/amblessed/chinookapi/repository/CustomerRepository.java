package com.amblessed.chinookapi.repository;

/*
 * @Project Name: ChinookAPI
 * @Author: Okechukwu Bright Onwumere
 * @Created: 01-Sep-25
 */

import com.amblessed.chinookapi.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findCustomersByCountryIgnoreCase(String country);

    List<Customer> findCustomersByCityIgnoreCase(String city);

    List<Customer> findCustomersByStateIgnoreCase(String state);
}
