FROM jenkins/jenkins:lts-jdk21

USER root
# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

USER jenkins

# Pre-install required Jenkins plugins
RUN jenkins-plugin-cli --plugins \
    job-dsl \
    git \
    timestamper \
    build-timeout \
    workflow-aggregator \
    maven-plugin

# Copy seed job DSL script
COPY seedJob.groovy /usr/share/jenkins/ref/seedJob.groovy

# Copy init script to auto-create the seed job
COPY init.groovy.d/createSeedJob.groovy /usr/share/jenkins/ref/init.groovy.d/createSeedJob.groovy