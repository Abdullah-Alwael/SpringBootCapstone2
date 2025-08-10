package com.spring.boot.springbootcapstone2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Items {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "plantId should not be empty")
    @Column(columnDefinition = "int not null")
    private Integer plantId;

    @NotNull(message = "orderId should not be empty")
    @Column(columnDefinition = "int not null")
    private Integer orderId;

    @NotNull(message = "quantity should not be empty")
    @Positive(message = "quantity must be positive")
    @Column(columnDefinition = "int not null")
    private Integer quantity;


}
