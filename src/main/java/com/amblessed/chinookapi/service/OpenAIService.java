package com.amblessed.chinookapi.service;

/*
 * @Project Name: ChinookAPI
 * @Author: Okechukwu Bright Onwumere
 * @Created: 02-Sep-25
 */

import com.amblessed.chinookapi.entity.OpenAIModel;
import com.amblessed.chinookapi.exception.OpenAiSqlException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OpenAIService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    private final ObjectMapper mapper = new ObjectMapper();

    public String generateSQL(String userInput) throws IOException {
        String result = executeOpenAiRequest(userInput);
        JsonNode firstChoice = extractFirstChoice(result);
        String sql = extractSqlFromChoice(firstChoice);
        return sanitizeSql(sql);
    }

    private String executeOpenAiRequest(String userInput) throws IOException {
        HttpPost post = getHttpPost(userInput);
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            return client.execute(post, response -> {
                int status = response.getCode();
                if (status >= 200 && status < 300) {
                    return response.getEntity() != null ? EntityUtils.toString(response.getEntity()) : null;
                }
                else {
                    throw new IOException("Unexpected response status: " + status);
                }
            });
        }
    }

    private HttpPost getHttpPost (String userInput) throws IOException {
        ObjectNode messageNode = mapper.createObjectNode();
        messageNode.put("role", "user");
        messageNode.put("content", "Convert this request to SQL for Chinook PostgreSQL DB: \""
                + userInput + "\". Return ONLY the SQL query.");


        ArrayNode messagesArray = mapper.createArrayNode();
        messagesArray.add(messageNode);

        ObjectNode requestBody = mapper.createObjectNode();
        requestBody.put("model", OpenAIModel.GPT_4O_MINI.getModelName());
        requestBody.set("messages", messagesArray);
        String jsonBody =  mapper.writeValueAsString(requestBody);

        HttpPost post = new HttpPost(apiUrl);
        post.setHeader("Authorization", "Bearer " + apiKey);
        post.setHeader("Content-Type", "application/json");
        post.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));
        return post;
    }

    private String sanitizeSql(String sql) {
        int selectIndex = sql.toLowerCase().indexOf("select");
        if (selectIndex >= 0) {
            sql = sql.substring(selectIndex);
        }
        if (!sql.toLowerCase().startsWith("select")) {
            throw new OpenAiSqlException("Only SELECT queries are allowed.");
        }
        return sql;
    }

    private String extractSqlFromChoice(JsonNode firstChoice) {
        JsonNode message = firstChoice.path("message");
        if (message.isMissingNode() || !message.has("content")) {
            if (firstChoice.has("text")) {
                return firstChoice.get("text").asText().trim();
            } else {
                throw new OpenAiSqlException("No SQL content returned from OpenAI API.");
            }
        } else {
            return message.get("content").asText().trim();
        }
    }

    private JsonNode extractFirstChoice(String result) throws IOException {
        if (result == null) {
            throw new OpenAiSqlException("Empty response from OpenAI API");
        }
        JsonNode root = mapper.readTree(result);
        JsonNode choices = root.path("choices");

        if (!choices.isArray() || choices.isEmpty() || choices.get(0) == null) {
            throw new OpenAiSqlException("No valid choice returned from OpenAI API.");
        }
        return choices.get(0);
    }
}


