package com.example.equations;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquationRepository extends JpaRepository<Equation, Long> {
    List<Equation> findEquationsByRootsValue(Double root);
}