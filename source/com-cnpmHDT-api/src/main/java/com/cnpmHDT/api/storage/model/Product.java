package com.cnpmHDT.api.storage.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Getter
@Setter
@Table(name = TablePrefix.PREFIX_TABLE+"product")
public class Product extends  Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Double price;
    private String image;

    @Column(name = "content", columnDefinition = "TEXT", length=10485760)
    private String description;

    @Column(name = "short_description", columnDefinition = "TEXT", length=10485760)
    private String shortDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Min(0)
    @Max(100)
    private Integer saleoff = 0;

}
