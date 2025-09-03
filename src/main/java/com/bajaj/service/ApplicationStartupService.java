package com.bajaj.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.bajaj.model.SqlSolution;
import com.bajaj.model.WebhookResponse;
import com.bajaj.repository.SqlSolutionRepository;

@Service
public class ApplicationStartupService {
    
    private static final Logger logger = LoggerFactory.getLogger(ApplicationStartupService.class);
    private static final String REG_NO = "REG12347";
    
    @Autowired
    private WebhookService webhookService;
    
    @Autowired
    private SqlProblemSolverService sqlProblemSolverService;
    
    @Autowired
    private SolutionSubmissionService solutionSubmissionService;
    
    @Autowired
    private SqlSolutionRepository sqlSolutionRepository;
    
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        logger.info("Application startup complete. Initiating SQL problem solving workflow...");
        
        try {
            WebhookResponse webhookResponse = webhookService.generateWebhook();
            
            String questionType = sqlProblemSolverService.determineQuestionType(REG_NO);
            logger.info("Question type identified: {}", questionType);
            
            String finalQuery = sqlProblemSolverService.solveSqlProblem(questionType);
            logger.info("SQL solution generated successfully");
            
            SqlSolution solution = new SqlSolution(REG_NO, questionType, finalQuery);
            sqlSolutionRepository.save(solution);
            logger.info("Solution saved to database with ID: {}", solution.getId());
            
            boolean submissionSuccess = solutionSubmissionService.submitSolution(
                finalQuery, 
                webhookResponse.getAccessToken()
            );
            
            if (submissionSuccess) {
                logger.info("All done! Workflow completed successfully.");
            } else {
                logger.error("Workflow finished but submission failed!");
            }
            
        } catch (RuntimeException e) {
            logger.error("Something went wrong during the workflow", e);
        } catch (Exception e) {
            logger.error("Unexpected error occurred", e);
        }
    }
}
