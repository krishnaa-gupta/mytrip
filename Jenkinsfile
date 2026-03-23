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

        stage('Docker Image Push to Amazon ECR') {
            steps {
                script {
                    echo "Tagging the Docker Image: In Progress"

                    def ecrImageName = "524104443570.dkr.ecr.ap-south-1.amazonaws.com/mytrip/mytrip:dev-booking-v.1.${BUILD_NUMBER}"

                    sh "docker tag ${IMAGE_NAME} ${ecrImageName}"

                    echo "Tagging the Docker Image: Completed"

                    withDockerRegistry([credentialsId: 'ecr:ap-south-1:ecr-credentials', url: 'https://524104443570.dkr.ecr.ap-south-1.amazonaws.com']) {
                        echo "Push Docker Image to ECR: In Progress"
                        sh "docker push ${ecrImageName}"
                        echo "Push Docker Image to ECR: Completed"
                    }

                    // Save for later stage
                    env.ECR_IMAGE_NAME = ecrImageName
                }
            }
        }

        stage('Upload the Docker Image to Nexus') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {

                        sh "docker login 13.203.155.223:8085 -u $USERNAME -p $PASSWORD"

                        echo "Push Docker Image to Nexus: In Progress"

                        def nexusImage = "13.203.155.223:8085/mytrip-ms:${BUILD_NUMBER}"

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