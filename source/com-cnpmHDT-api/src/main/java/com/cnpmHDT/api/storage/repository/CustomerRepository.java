package com.cnpmHDT.api.storage.repository;

import com.cnpmHDT.api.storage.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
    public Customer findCustomerByAccountId(Long id);
}
