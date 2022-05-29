
package com.cnpmHDT.api.storage.repository;

import com.cnpmHDT.api.storage.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrdersRepository extends JpaRepository<Orders, Long>, JpaSpecificationExecutor<Orders> {
    public Orders findOrdersByIdAndCustomerId(Long ordersId, Long customerId);
}
