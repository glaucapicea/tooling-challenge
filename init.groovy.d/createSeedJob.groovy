import jenkins.model.Jenkins
import javaposse.jobdsl.plugin.ExecuteDslScripts
import javaposse.jobdsl.plugin.LookupStrategy
import javaposse.jobdsl.plugin.RemovedJobAction
import javaposse.jobdsl.plugin.RemovedViewAction
import hudson.model.FreeStyleProject

def jobName = 'seed-job'
def jenkins = Jenkins.instance

// Check if seed job already exists
if (jenkins.getItem(jobName) == null) {
    println "Creating seed job: ${jobName}"

    // Create the seed job
    def seedJob = jenkins.createProject(FreeStyleProject, jobName)
    seedJob.setDescription('Job DSL seed job that creates the test-health-reporter job')

    // Add the Job DSL build step
    def jobDslBuildStep = new ExecuteDslScripts()
    jobDslBuildStep.setScriptText(new File('/usr/share/jenkins/ref/seedJob.groovy').text)
    jobDslBuildStep.removedJobAction = RemovedJobAction.DELETE
    jobDslBuildStep.removedViewAction = RemovedViewAction.DELETE
    jobDslBuildStep.lookupStrategy = LookupStrategy.JENKINS_ROOT

    seedJob.buildersList.add(jobDslBuildStep)
    seedJob.save()

    // Trigger the seed job to create test-health-reporter
    println "Triggering seed job to create test-health-reporter..."
    seedJob.scheduleBuild2(0)

    println "Seed job created and triggered successfully"
} else {
    println "Seed job already exists, skipping creation"
}