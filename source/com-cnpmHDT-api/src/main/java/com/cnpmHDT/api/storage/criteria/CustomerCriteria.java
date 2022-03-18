package com.cnpmHDT.api.storage.criteria;

import com.cnpmHDT.api.storage.model.Customer;
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
public class CustomerCriteria {
    private Long id;
    private String name;
    private Integer gender;
    private Integer status;     //=1? true

    public Specification<Customer> getSpecification() {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
//                Join<Category, Category> joinCategory = root.join("parentCategory", JoinType.INNER);

                if(getId() != null) {
                    predicates.add(cb.equal(root.get("id"), getId()));
                }

                if(!StringUtils.isEmpty(getName())) {
                    predicates.add(cb.like(cb.lower(root.get("name")), "%" + getName().toLowerCase() + "%"));
                }

                if(getGender()!=null){
                    predicates.add(cb.equal(root.get("gender"), getGender()));
                }

                if(getStatus() != null) {
                    predicates.add(cb.equal(root.get("status"), getStatus()));
                }

                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
