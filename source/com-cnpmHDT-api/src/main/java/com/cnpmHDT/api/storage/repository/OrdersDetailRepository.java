<<<<<<< Updated upstream
package com.cnpmHDT.api.storage.repository;public interface OrdersDetailRepository {
=======
package com.cnpmHDT.api.storage.repository;

import com.cnpmHDT.api.storage.model.OrdersDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrdersDetailRepository extends JpaRepository<OrdersDetail, Long>, JpaSpecificationExecutor<OrdersDetail> {
>>>>>>> Stashed changes
}