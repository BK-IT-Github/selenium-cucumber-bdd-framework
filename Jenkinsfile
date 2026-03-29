pipeline {
    agent any

    tools {
        maven 'Maven-3.9'
        jdk 'JDK-17'
    }

    environment {
        MAVEN_OPTS = '-Xmx1024m'
        BROWSER = 'chrome'
        ENVIRONMENT = 'qa'
    }

    parameters {
        choice(
            name: 'TEST_SUITE',
            choices: ['smoke', 'regression', 'sanity'],
            description: 'Select the test suite to execute'
        )
        choice(
            name: 'BROWSER',
            choices: ['chrome', 'firefox', 'edge'],
            description: 'Select the browser for test execution'
        )
        choice(
            name: 'ENVIRONMENT',
            choices: ['qa', 'staging', 'prod'],
            description: 'Select the target environment'
        )
        string(
            name: 'CUSTOM_TAGS',
            defaultValue: '',
            description: 'Custom Cucumber tags (e.g., @Smoke and @Login)'
        )
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                dir('Bk.Automation.UI.WebApp') {
                    bat 'mvn clean compile -DskipTests'
                }
            }
        }

        stage('Run Tests') {
            steps {
                dir('Bk.Automation.UI.WebApp') {
                    script {
                        def testProfile = params.TEST_SUITE ?: 'smoke'
                        def browser = params.BROWSER ?: 'chrome'
                        def env = params.ENVIRONMENT ?: 'qa'
                        def customTags = params.CUSTOM_TAGS ?: ''

                        def mvnCmd = "mvn test -P${testProfile} -Dbrowser=${browser} -Denvironment=${env} -Dbrowser.headless=true"

                        if (customTags?.trim()) {
                            mvnCmd += " -Dcucumber.filter.tags=\"${customTags}\""
                        }

                        bat mvnCmd
                    }
                }
            }
        }

        stage('Rerun Failed Scenarios') {
            when {
                expression {
                    return fileExists('Bk.Automation.UI.WebApp/target/rerun/failed_smoke_scenarios.txt') ||
                           fileExists('Bk.Automation.UI.WebApp/target/rerun/failed_regression_scenarios.txt')
                }
            }
            steps {
                dir('Bk.Automation.UI.WebApp') {
                    bat 'mvn test -Dtest=FailedScenariosRerunRunner -Dbrowser.headless=true'
                }
            }
        }
    }

    post {
        always {
            // Archive test reports
            archiveArtifacts artifacts: 'Bk.Automation.UI.WebApp/target/reports/**/*', allowEmptyArchive: true

            // Publish Cucumber reports
            cucumber(
                buildStatus: 'UNSTABLE',
                fileIncludePattern: '**/cucumber/*.json',
                jsonReportDirectory: 'Bk.Automation.UI.WebApp/target/reports/cucumber'
            )

            // Publish HTML Extent Report
            publishHTML(target: [
                allowMissing: true,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'Bk.Automation.UI.WebApp/target/reports/extent',
                reportFiles: 'SparkReport.html',
                reportName: 'Extent Report'
            ])

            // Archive screenshots
            archiveArtifacts artifacts: 'Bk.Automation.UI.WebApp/target/screenshots/**/*', allowEmptyArchive: true

            // Cleanup workspace
            cleanWs()
        }

        success {
            echo '✅ Test execution completed successfully!'
        }

        failure {
            echo '❌ Test execution failed. Check the reports for details.'
        }

        unstable {
            echo '⚠️ Test execution completed with some failures.'
        }
    }
}
