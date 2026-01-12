FROM jenkins/jenkins:lts-jdk21

USER jenkins

# Pre-install required Jenkins plugins
RUN jenkins-plugin-cli --plugins \
    job-dsl \
    git \
    timestamper \
    build-timeout \
    workflow-aggregator \
    maven-plugin