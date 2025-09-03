package com.bajaj.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bajaj.model.SolutionRequest;

@Service
public class SolutionSubmissionService {
    
    private static final Logger logger = LoggerFactory.getLogger(SolutionSubmissionService.class);
    private static final String SOLUTION_SUBMISSION_URL = "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA";
    
    @Autowired
    private RestTemplate restTemplate;
    
    public boolean submitSolution(String finalQuery, String accessToken) {
        try {
            logger.info("Preparing to submit SQL solution...");
            
            SolutionRequest request = new SolutionRequest(finalQuery);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);
            
            HttpEntity<SolutionRequest> entity = new HttpEntity<>(request, headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                SOLUTION_SUBMISSION_URL,
                HttpMethod.POST,
                entity,
                String.class
            );
            
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Solution submitted successfully. Response: {}", response.getBody());
                return true;
            } else {
                logger.error("Failed to submit solution. Status: {}, Response: {}", 
                    response.getStatusCode(), response.getBody());
                return false;
            }
            
        } catch (org.springframework.web.client.RestClientException e) {
            logger.error("Network error submitting solution", e);
            return false;
        } catch (Exception e) {
            logger.error("Unexpected error submitting solution", e);
            return false;
        }
    }
}
