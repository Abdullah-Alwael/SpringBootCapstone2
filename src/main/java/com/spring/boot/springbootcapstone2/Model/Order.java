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
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "date should not be empty")
    @Column(columnDefinition = "datetime not null")
    @CreationTimestamp
    private LocalDateTime date;

    @NotEmpty(message = "status should not be empty")
    @Column(columnDefinition = "varchar(30) not null")
    @Pattern(regexp = "^(pending)$",
            message = "must be initially pending")
    // "^(pending|confirmed|delivered|canceled)$"
    private String status;

    @NotNull(message = "buyerId should not be empty")
    @Column(columnDefinition = "int not null")
    private Integer buyerId;

    @NotNull(message = "farmerId should not be empty")
    @Column(columnDefinition = "int not null")
    private Integer farmerId;

    @NotNull(message = "totalPrice should not be empty")
    @PositiveOrZero(message = "totalPrice must be positive or zero")
    @Column(columnDefinition = "double not null")
    private Double totalPrice;


}
