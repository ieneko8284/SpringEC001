package com.springec001.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table("items") // MySQLの「items」テーブルと紐付け
public class Item {
    @Id
    private Long id;
    private String name;
    private Integer price;
    private String description;
    private Integer stock;
}