package com.bk.automation.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Utility class for reading and parsing JSON test data files.
 * Uses Jackson ObjectMapper for deserialization.
 */
public final class JsonUtil {

    private static final Logger logger = LogManager.getLogger(JsonUtil.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private JsonUtil() {
        // Utility class
    }

    /**
     * Reads a JSON file and returns its content as a JsonNode tree.
     *
     * @param filePath path to the JSON file
     * @return JsonNode representation
     */
    public static JsonNode readJsonFile(String filePath) {
        try {
            JsonNode jsonNode = objectMapper.readTree(new File(filePath));
            logger.info("Successfully read JSON file: {}", filePath);
            return jsonNode;
        } catch (IOException e) {
            logger.error("Error reading JSON file: {}", filePath, e);
            throw new RuntimeException("Failed to read JSON file: " + filePath, e);
        }
    }

    /**
     * Reads a JSON file and deserializes it to the specified class type.
     *
     * @param filePath path to the JSON file
     * @param clazz    the target class type
     * @return deserialized object
     */
    public static <T> T readJsonFile(String filePath, Class<T> clazz) {
        try {
            T result = objectMapper.readValue(new File(filePath), clazz);
            logger.info("Successfully deserialized JSON file: {} to {}", filePath, clazz.getSimpleName());
            return result;
        } catch (IOException e) {
            logger.error("Error deserializing JSON file: {} to {}", filePath, clazz.getSimpleName(), e);
            throw new RuntimeException("Failed to deserialize JSON file: " + filePath, e);
        }
    }

    /**
     * Reads a JSON file and returns a list of maps.
     * Useful for reading JSON arrays of objects.
     *
     * @param filePath path to the JSON file
     * @return list of maps
     */
    public static List<Map<String, String>> readJsonAsListOfMaps(String filePath) {
        try {
            List<Map<String, String>> data = objectMapper.readValue(
                    new File(filePath),
                    new TypeReference<List<Map<String, String>>>() {}
            );
            logger.info("Read {} records from JSON file: {}", data.size(), filePath);
            return data;
        } catch (IOException e) {
            logger.error("Error reading JSON file as list of maps: {}", filePath, e);
            throw new RuntimeException("Failed to read JSON file: " + filePath, e);
        }
    }

    /**
     * Reads a JSON file and returns a single map.
     *
     * @param filePath path to the JSON file
     * @return map of key-value pairs
     */
    public static Map<String, String> readJsonAsMap(String filePath) {
        try {
            Map<String, String> data = objectMapper.readValue(
                    new File(filePath),
                    new TypeReference<Map<String, String>>() {}
            );
            logger.info("Read JSON file as map: {}", filePath);
            return data;
        } catch (IOException e) {
            logger.error("Error reading JSON file as map: {}", filePath, e);
            throw new RuntimeException("Failed to read JSON file: " + filePath, e);
        }
    }

    /**
     * Gets a specific value from a JSON file by key (top-level only).
     *
     * @param filePath path to the JSON file
     * @param key      the JSON key
     * @return value as string
     */
    public static String getJsonValue(String filePath, String key) {
        JsonNode rootNode = readJsonFile(filePath);
        JsonNode valueNode = rootNode.get(key);
        if (valueNode == null) {
            logger.warn("Key '{}' not found in JSON file: {}", key, filePath);
            return null;
        }
        return valueNode.asText();
    }

    /**
     * Converts an object to its JSON string representation.
     *
     * @param obj the object to serialize
     * @return JSON string
     */
    public static String toJson(Object obj) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (IOException e) {
            logger.error("Error converting object to JSON", e);
            throw new RuntimeException("Failed to serialize object to JSON", e);
        }
    }
}
