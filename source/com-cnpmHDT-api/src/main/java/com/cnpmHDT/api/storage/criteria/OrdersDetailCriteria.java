package com.cnpmHDT.api.storage.criteria;

import com.cnpmHDT.api.storage.model.OrdersDetail;
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
public class OrdersDetailCriteria {
    private Long id;
    private Double price;
    private Integer amount;

    public Specification<OrdersDetail> getSpecification() {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<OrdersDetail> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if(getId() != null) {
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if(getPrice() != null) {
                    predicates.add(cb.equal(root.get("price"), getPrice()));
                }
                if(getAmount() != null){
                    predicates.add(cb.equal(root.get("amount"), getAmount()));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
