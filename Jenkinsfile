pipeline {
    agent any

    options {
        buildDiscarder(logRotator(numToKeepStr: '5', artifactNumToKeepStr: '5'))
    }

    tools {
        maven 'maven_3.9.4'
    }

    stages {

        stage('Code Compilation') {
            steps {
                echo 'Code Compilation is In Progress!'
                sh 'mvn clean compile'
                echo 'Code Compilation Completed!'
            }
        }

        stage('Code QA Execution') {
            steps {
                echo 'Running Test Cases...'
                sh 'mvn test'
                echo 'Test Cases Completed!'
            }
        }

        stage('Code Package') {
            steps {
                echo 'Packaging Application...'
                sh 'mvn package'
                echo 'Packaging Completed!'
            }
        }

    }
}