package com.example.equations;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
@Getter
@Setter
public class Equation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "equation_string", nullable = false)
    private String equationString;

    @OneToMany(mappedBy = "equation", cascade = CascadeType.ALL)
    private List<Root> roots;

    public Equation(String equationString) {
        this.equationString = equationString;
    }
     public Equation() {
    }
}
