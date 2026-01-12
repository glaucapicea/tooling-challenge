package org.example;

import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public class TestHealthCLI {
    public static void main(String[] args) throws Exception {
        // Validate input file
        if (args.length != 1) {
            System.err.println("Usage: java -jar test-health-cli.jar <result.json>");
            System.exit(1);
        }
        Path filePath = Paths.get(args[0]);
        if (!Files.exists(filePath) || !Files.isRegularFile(filePath) || !Files.isReadable(filePath)) {
            throw new IOException("Input file '" + filePath + "' is invalid");
        }

        // Parse input JSON
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<TestResult> results = mapper.readValue(
                filePath.toFile(),
                new TypeReference<List<TestResult>>() {}
        );

        // Calculate outputs
        int totalTests = results.size();
        long passedTestCount = results.stream()
                .filter(r -> "pass".equalsIgnoreCase(r.status))
                .count();
        List<String> failedTestNames = results.stream()
                .filter(r -> !"pass".equalsIgnoreCase(r.status))
                .map(r -> r.test_name)
                .distinct()
                .toList();
        double passPercentage = (passedTestCount * 100.0) / totalTests;
        double failPercentage = 100.0 - passPercentage;

        // Print outputs
        System.out.println("===== TEST HEALTH REPORT =====");
        System.out.println("Total Tests Run: " + totalTests);
        System.out.printf("Pass Percentage: %.2f%%\n", passPercentage);
        System.out.printf("Fail Percentage: %.2f%%\n", failPercentage);
        if (!failedTestNames.isEmpty()) {
            System.out.println("\nNames of failed tests:");
            for (String test : failedTestNames) {
                System.out.println(" - " + test);
            }
        }
    }
}
