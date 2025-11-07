package com.sportshop.sports_shop.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "mon_the_thao")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MonTheThao {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_mon_the_thao")
    private Integer maMonTheThao;


    @Column(name = "ten_mon_the_thao", length = 100, nullable = false)
    private String tenMonTheThao;
}





