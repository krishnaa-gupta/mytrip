pipeline {
    agent any

    stages {

        stage('Clone Code') {
            steps {
                git branch: 'dev', url: 'https://github.com/krishnaa-gupta/mytrip.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    def imageName = "krishnaa0401/mytomcatimage:latest"
                    sh "docker build -t ${imageName} ."
                }
            }
        }

        stage('Docker Login') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh "docker login -u $DOCKER_USER -p $DOCKER_PASS"
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    def imageName = "krishnaa0401/mytomcatimage:latest"
                    sh "docker push ${imageName}"
                }
            }
        }
    }
}