package com.spring.boot.springbootcapstone2.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Plants {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "name should not be empty")
    @Column(columnDefinition = "varchar(30)")
    private String name;

    @NotEmpty(message = "description should not be empty")
    @Column(columnDefinition = "varchar(255)")
    private String description;

}
