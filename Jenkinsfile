pipeline {
    agent any

    environment {
        IMAGE_NAME = "krishnaa0401/mytomcatimage:latest"
    }

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
                    sh "docker build -t ${IMAGE_NAME} ."
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
                    sh "docker push ${IMAGE_NAME}"
                }
            }
        }

        stage('Upload the Docker Image to Nexus') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {

                        sh "docker login 3.109.158.202:8085 -u $USERNAME -p $PASSWORD"

                        echo "Push Docker Image to Nexus: In Progress"

                        def nexusImage = "3.109.158.202:8085/mytrip-ms:${BUILD_NUMBER}"

                        sh "docker tag ${IMAGE_NAME} ${nexusImage}"
                        sh "docker push ${nexusImage}"

                        echo "Push Docker Image to Nexus: Completed"
                    }
                }
            }
        }

        stage('Delete Local Docker Images') {
            steps {
                echo "Deleting Local Docker Images"

                sh "docker rmi ${IMAGE_NAME} || true"
                sh "docker rmi ${env.ECR_IMAGE_NAME} || true"

                echo "Local Docker Images Deletion Completed"
            }
        }
    }
}