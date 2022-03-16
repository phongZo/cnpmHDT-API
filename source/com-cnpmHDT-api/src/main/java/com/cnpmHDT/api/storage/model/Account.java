package com.cnpmHDT.api.storage.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = TablePrefix.PREFIX_TABLE+"account")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Account extends  Auditable<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer kind;
    private String username;
    private String email;
    @JsonIgnore
    private String password;
    @Column(name = "full_name")
    private String fullName;
    private String phone;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(name = "last_login")
    private Date lastLogin;

    @Column(name="verify_code")
    private String verifyCode;
}
