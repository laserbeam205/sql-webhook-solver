package com.bajaj.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SqlProblemSolverService {
    
    private static final Logger logger = LoggerFactory.getLogger(SqlProblemSolverService.class);
    
    public String determineQuestionType(String regNo) {
        if (regNo == null || regNo.length() < 2) {
            throw new IllegalArgumentException("Invalid registration number");
        }
        
        String lastTwoDigits = regNo.substring(regNo.length() - 2);
        int lastDigit = Integer.parseInt(lastTwoDigits.substring(1));
        
        return (lastDigit % 2 == 0) ? "Question 2" : "Question 1";
    }
    
    public String solveSqlProblem(String questionType) {
        logger.info("Solving SQL problem for: {}", questionType);
        
        if ("Question 2".equals(questionType)) {
            return solveQuestion2();
        } else {
            return solveQuestion2();
        }
    }
    
    
    private String solveQuestion2() {
        return """
            WITH employee_performance AS (
                SELECT 
                    e.employee_id,
                    e.first_name,
                    e.last_name,
                    e.email,
                    e.hire_date,
                    d.department_name,
                    d.location,
                    m.first_name as manager_first_name,
                    m.last_name as manager_last_name,
                    COUNT(DISTINCT p.project_id) as total_projects,
                    COUNT(DISTINCT CASE WHEN p.status = 'COMPLETED' THEN p.project_id END) as completed_projects,
                    SUM(CASE WHEN p.status = 'COMPLETED' THEN p.budget ELSE 0 END) as total_budget_managed,
                    AVG(CASE WHEN p.status = 'COMPLETED' THEN p.budget ELSE NULL END) as avg_project_budget,
                    COUNT(DISTINCT t.task_id) as total_tasks_assigned,
                    COUNT(DISTINCT CASE WHEN t.status = 'COMPLETED' THEN t.task_id END) as completed_tasks,
                    DATEDIFF(YEAR, e.hire_date, GETDATE()) as years_of_experience
                FROM employees e
                INNER JOIN departments d ON e.department_id = d.department_id
                LEFT JOIN employees m ON e.manager_id = m.employee_id
                LEFT JOIN projects p ON e.employee_id = p.project_manager_id
                LEFT JOIN project_tasks pt ON p.project_id = pt.project_id
                LEFT JOIN tasks t ON pt.task_id = t.task_id AND t.assigned_to = e.employee_id
                WHERE e.status = 'ACTIVE'
                GROUP BY 
                    e.employee_id, e.first_name, e.last_name, e.email, e.hire_date,
                    d.department_name, d.location, m.first_name, m.last_name
            ),
            performance_metrics AS (
                SELECT 
                    *,
                    CASE 
                        WHEN completed_projects = 0 THEN 0
                        ELSE (completed_projects * 100.0 / total_projects)
                    END as project_completion_rate,
                    CASE 
                        WHEN completed_tasks = 0 THEN 0
                        ELSE (completed_tasks * 100.0 / total_tasks_assigned)
                    END as task_completion_rate,
                    CASE 
                        WHEN years_of_experience >= 5 THEN 'SENIOR'
                        WHEN years_of_experience >= 2 THEN 'MID_LEVEL'
                        ELSE 'JUNIOR'
                    END as experience_level
                FROM employee_performance
            )
            SELECT 
                employee_id,
                first_name,
                last_name,
                email,
                department_name,
                location,
                manager_first_name + ' ' + manager_last_name as manager_name,
                years_of_experience,
                experience_level,
                total_projects,
                completed_projects,
                ROUND(project_completion_rate, 2) as project_completion_rate,
                total_tasks_assigned,
                completed_tasks,
                ROUND(task_completion_rate, 2) as task_completion_rate,
                total_budget_managed,
                ROUND(avg_project_budget, 2) as avg_project_budget,
                CASE 
                    WHEN project_completion_rate >= 80 AND task_completion_rate >= 85 AND years_of_experience >= 3 THEN 'HIGH_PERFORMER'
                    WHEN project_completion_rate >= 60 AND task_completion_rate >= 70 THEN 'GOOD_PERFORMER'
                    WHEN project_completion_rate >= 40 AND task_completion_rate >= 50 THEN 'AVERAGE_PERFORMER'
                    ELSE 'NEEDS_IMPROVEMENT'
                END as performance_rating
            FROM performance_metrics
            WHERE total_projects > 0 OR total_tasks_assigned > 0
            ORDER BY 
                performance_rating DESC,
                project_completion_rate DESC,
                task_completion_rate DESC,
                total_budget_managed DESC,
                years_of_experience DESC
            """;
    }
}
