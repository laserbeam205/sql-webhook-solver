package com.bajaj.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sql_solutions")
public class SqlSolution {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "reg_no")
    private String regNo;
    
    @Column(name = "question_type")
    private String questionType;
    
    @Column(name = "final_query", columnDefinition = "TEXT")
    private String finalQuery;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    public SqlSolution() {
        this.createdAt = LocalDateTime.now();
    }
    
    public SqlSolution(String regNo, String questionType, String finalQuery) {
        this();
        this.regNo = regNo;
        this.questionType = questionType;
        this.finalQuery = finalQuery;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getRegNo() {
        return regNo;
    }
    
    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }
    
    public String getQuestionType() {
        return questionType;
    }
    
    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }
    
    public String getFinalQuery() {
        return finalQuery;
    }
    
    public void setFinalQuery(String finalQuery) {
        this.finalQuery = finalQuery;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
