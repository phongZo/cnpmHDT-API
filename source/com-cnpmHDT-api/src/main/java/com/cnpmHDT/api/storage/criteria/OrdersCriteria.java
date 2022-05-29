package com.cnpmHDT.api.storage.criteria;

import com.cnpmHDT.api.storage.model.Customer;
import com.cnpmHDT.api.storage.model.Orders;
import com.cnpmHDT.api.storage.model.Permission;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrdersCriteria {
    private Long id;
    private Long customerId;
    private String code;
    private String receiverName; // Ten nguoi nhan
    private String receiverPhone; // Sdt nguoi nhan

    public Specification<Orders> getSpecification() {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Orders> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if(getId() != null) {
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if(getCustomerId() != null) {
                    Join<Customer, Orders> joinCustomer = root.join("customer", JoinType.INNER);
                    predicates.add(cb.equal(joinCustomer.get("id"), getCustomerId()));
                }
                if(!StringUtils.isEmpty(getCode())){
                    predicates.add(cb.like(cb.lower(root.get("code")), "%"+getCode().toLowerCase()+"%"));
                }
                if(!StringUtils.isEmpty(getReceiverName())){
                    predicates.add(cb.like(cb.lower(root.get("receiverName")), "%"+getReceiverName().toLowerCase()+"%"));
                }
                if(!StringUtils.isEmpty(getReceiverPhone())){
                    predicates.add(cb.like(cb.lower(root.get("receiverPhone")), "%"+getReceiverPhone().toLowerCase()+"%"));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
