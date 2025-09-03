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

import com.bajaj.model.WebhookRequest;
import com.bajaj.model.WebhookResponse;

@Service
public class WebhookService {
    
    private static final Logger logger = LoggerFactory.getLogger(WebhookService.class);
    private static final String WEBHOOK_GENERATION_URL = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
    
    @Autowired
    private RestTemplate restTemplate;
    
    public WebhookResponse generateWebhook() {
        try {
            logger.info("Starting webhook generation process...");
            
            WebhookRequest request = new WebhookRequest(
                "John Doe",
                "REG12347", 
                "john@example.com"
            );
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<WebhookRequest> entity = new HttpEntity<>(request, headers);
            
            ResponseEntity<WebhookResponse> response = restTemplate.exchange(
                WEBHOOK_GENERATION_URL,
                HttpMethod.POST,
                entity,
                WebhookResponse.class
            );
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                WebhookResponse webhookResponse = response.getBody();
                if (webhookResponse != null) {
                    logger.info("Webhook generated successfully. URL: {}", webhookResponse.getWebhook());
                    return webhookResponse;
                }
            }
            
            logger.error("Failed to generate webhook. HTTP Status: {}", response.getStatusCode());
            throw new RuntimeException("Failed to generate webhook");
            
        } catch (org.springframework.web.client.RestClientException e) {
            logger.error("Network error occurred while generating webhook", e);
            throw new RuntimeException("Network error occurred while generating webhook", e);
        }
    }
}
