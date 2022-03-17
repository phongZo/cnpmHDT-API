package com.cnpmHDT.api.storage.model;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = TablePrefix.PREFIX_TABLE+"customer")
public class Customer extends Auditable<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // This property is auto increased
    private Long id;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "account_id") // name of this property in database
    @MapsId
    private Account account;   // 1 Customer have 1 account (Map OneToOne)


    @Column(name = "address")
    private String address;

    private Date birthday;
    private Integer gender;

    private Boolean isLoyalty = false;
    private Integer loyaltyLevel;    // type of customer (Diamond , Silver , Gold...)
    private Integer saleOff = 0;
}
