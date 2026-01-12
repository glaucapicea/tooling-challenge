job('test-health-reporter') {

    description('Daily test suite health report generator')

    parameters {
        stringParam(
                'RESULT_FILE',
                'result.json',
                'Name of the test result JSON file'
        )
    }

    // Scheduled trigger for 9AM PST every day
    triggers {
        cron('0 9 * * *')
    }

    // Absolute timeout of 10 minutes
    wrappers {
        timestamps()
        timeout {
            absolute(10)
        }
    }

    scm {
        git {
            remote {
                url('https://github.com/glaucapicea/tooling-challenge.git')
            }
            branch('master')
        }
    }

    steps {
        shell('''
            echo "Building Test Health CLI..."
            mvn clean package

            echo "Running Test Health Report..."
            java -jar target/test-health-cli.jar $RESULT_FILE
        ''')
    }
}
