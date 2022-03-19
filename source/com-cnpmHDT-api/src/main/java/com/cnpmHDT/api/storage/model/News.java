package com.cnpmHDT.api.storage.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = TablePrefix.PREFIX_TABLE+"news")
public class News extends Auditable<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;

    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;
    private String avatar;
    private String banner;
    private String description;
}