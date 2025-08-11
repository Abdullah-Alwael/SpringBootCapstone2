package com.spring.boot.springbootcapstone2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FarmOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "datetime")
    @CreationTimestamp
    private LocalDateTime date;

    // "^(pending|confirmed|delivered|canceled)$"
    // managed by the system
    @Column(columnDefinition = "varchar(30) not null")
    private String status;

    @NotNull(message = "buyerId should not be empty")
    @Column(columnDefinition = "int not null")
    private Integer buyerId;

    @NotNull(message = "farmerId should not be empty")
    @Column(columnDefinition = "int not null")
    private Integer farmerId;

    // handled by the system
    @Column(columnDefinition = "double not null default 0.0")
    private Double totalPrice;

}
