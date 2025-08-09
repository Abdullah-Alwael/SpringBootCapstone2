package com.spring.boot.springbootcapstone2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "date should not be empty")
    @Column(columnDefinition = "datetime not null")
    private LocalDateTime date;

    @NotEmpty(message = "status should not be empty")
    @Column(columnDefinition = "varchar(30) not null")
    private String status;

    @NotNull(message = "buyerId should not be empty")
    @Column(columnDefinition = "int not null")
    private Integer buyerId;

    @NotNull(message = "farmerId should not be empty")
    @Column(columnDefinition = "int not null")
    private Integer farmerId;

    @NotNull(message = "totalAmount should not be empty")
    @Column(columnDefinition = "double not null")
    private Double totalAmount;


}
