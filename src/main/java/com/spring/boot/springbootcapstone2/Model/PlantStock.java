package com.spring.boot.springbootcapstone2.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PlantStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "farmerId should not be empty")
    @Column(columnDefinition = "int not null")
    private Integer farmerId;

    @NotNull(message = "plantId should not be empty")
    @Column(columnDefinition = "int not null")
    private Integer plantId;

    @NotNull(message = "price should not be empty")
    @PositiveOrZero(message = "price must be positive or zero")
    @Column(columnDefinition = "double not null")
    private Double price;

    @NotNull(message = "stockQuantity should not be empty")
    @PositiveOrZero(message = "stockQuantity must be positive or zero")
    @Column(columnDefinition = "int not null")
    private Integer stockQuantity;


}
