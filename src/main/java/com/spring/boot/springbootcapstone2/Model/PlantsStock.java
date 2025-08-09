package com.spring.boot.springbootcapstone2.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PlantsStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "farmerId should not be null")
    @Column(columnDefinition = "int not null")
    private Integer farmerId;

    @NotNull(message = "plantId should not be null")
    @Column(columnDefinition = "int not null")
    private Integer plantId;

    @NotNull(message = "price should not be null")
    @Column(columnDefinition = "double not null")
    private Double price;

    @NotNull(message = "stockQuantity should not be null")
    @Column(columnDefinition = "int not null")
    private Integer stockQuantity;


}
