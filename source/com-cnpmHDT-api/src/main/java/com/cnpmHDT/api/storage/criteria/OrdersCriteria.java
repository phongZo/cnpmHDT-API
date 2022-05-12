package com.cnpmHDT.api.storage.criteria;

import com.cnpmHDT.api.storage.model.Customer;
import com.cnpmHDT.api.storage.model.Permission;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrdersCriteria {
    private Long id;
    private String customer;
    private String code;
    private String receiverName; // Ten nguoi nhan
    private String receiverPhone; // Sdt nguoi nhan

    public Specification<Permission> getSpecification() {
        return new Specification<Permission>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Permission> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if(getId() != null) {
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if(!StringUtils.isEmpty(getCustomer())){
                    predicates.add(cb.like(cb.lower(root.get("customer")), "%"+ getCustomer().toLowerCase()+"%"));
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
