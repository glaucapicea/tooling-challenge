package org.example;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public class TestHealthCLI {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: java -jar test-health-cli.jar <result.json>");
            System.exit(1);
        }

        // TODO: Check if file exists and is readable. Throw proper exception.

        File filePath = new File(args[0]);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        List<TestResult> results = mapper.readValue(
                filePath,
                new TypeReference<List<TestResult>>() {}
        );

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
