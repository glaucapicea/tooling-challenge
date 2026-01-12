A tool to generate reports on the health of an automated test suite

# Setup instructions

---
```bash
# Clone the repository
git clone https://github.com/glaucapicea/tooling-challenge.git
cd tooling-challenge

# Start Jenkins
docker compose up -d
```

1. Wait a few seconds for Jenkins to set up
2. Access Jenkins in your browser at http://localhost:8080
3. Click on the job titled `test-health-reporter`
4. On the left sidebar, select the `Build with Parameters` options
5. If the default value of `result.json` is not entered, do so manually and select the green `Build` button

## Running the tool locally
```bash
# Build the JAR file
mvn clean package

# Run the tool
java -jar target/test-health-cli.jar result.json
```

## Cleanup
```bash
docker compose down -v
```

# Notes on project structure

---
- The folder init.groovy.d contains a script to automatically create the main job of this assignment
- The results of running the test suite should be placed in a file called `results.json` at the root of the repository

# Possible improvements

---
* A pass/fail percentage doesn't actually address test flakiness. Its more helpful to store longitudinal data to analyze performance issues (such as execution time increases over time), catch intermittently failing tests, or perform simple statistical analysis to ensure results are not outliers or to correlate them to metadata about tests.
* This could be done via an InfluxDB database, which is purposely built for analyzing time series data. It can be run either locally or on the cloud depending on how important data retention is and the size of the test suite. It also has automatic downsampling and retention policies, and a schema for handling metadata to store additional test information like environments and execution time.
* Grafana is a good option as a visualizer for the InfluxDB database. It provides a dashboard which would be convenient to easily display testing issues during standups or for general monitoring.