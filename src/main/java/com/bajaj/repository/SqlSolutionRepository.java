package com.bajaj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bajaj.model.SqlSolution;

@Repository
public interface SqlSolutionRepository extends JpaRepository<SqlSolution, Long> {
}
