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
                sh "docker build -t ${IMAGE_NAME} ."
            }
        }

        stage('Docker Login') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                sh "docker push ${IMAGE_NAME}"
            }
        }

        stage('Upload Docker Image to Nexus') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'nexus-credentials',
                    usernameVariable: 'USERNAME',
                    passwordVariable: 'PASSWORD'
                )]) {

                    sh 'echo $PASSWORD | docker login 172.31.12.56:8085 -u $USERNAME --password-stdin'

                    script {
                        def nexusImage = "172.31.12.56:8085/mytrip-ms:${BUILD_NUMBER}"

                        echo "Pushing to Nexus..."
                        sh "docker tag ${IMAGE_NAME} ${nexusImage}"
                        sh "docker push ${nexusImage}"
                        echo "Push to Nexus completed"
                    }
                }
            }
        }

        stage('Push Docker Image to Amazon ECR') {
            steps {
                script {
                    def ecrImageName = "524140443570.dkr.ecr.ap-south-1.amazonaws.com/mytrip.ecr/mytrip/mytrip:dev-booking-v.1.${BUILD_NUMBER}"

                    echo "Tagging image for ECR..."
                    sh "docker tag ${IMAGE_NAME} ${ecrImageName}"

                    withDockerRegistry([
                        credentialsId: 'ecr:ap-south-1:ecr-credentials',
                        url: 'https://524140443570.dkr.ecr.ap-south-1.amazonaws.com'
                    ]) {
                        echo "Pushing to ECR..."
                        sh "docker push ${ecrImageName}"
                    }

                    env.ECR_IMAGE_NAME = ecrImageName
                }
            }
        }

        stage('Delete Local Docker Images') {
            steps {
                echo "Cleaning up local images..."
                sh "docker rmi ${IMAGE_NAME} || true"
                sh "docker rmi ${env.ECR_IMAGE_NAME} || true"
                echo "Cleanup completed"
            }
        }
    }
}