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
        stage('Docker Image Push to Amazon ECR') {
            steps {
                script {
                    echo "Tagging the Docker Image: In Progress"
                    def ecrImageName = "524104443570.dkr.ecr.ap-south-1.amazonaws.com/mytrip/mytrip:dev-booking-v.1.${BUILD_NUMBER}"
                    sh "docker tag ${imageName} ${ecrImageName}"
                    echo "Tagging the Docker Image: Completed"

                    withDockerRegistry([credentialsId: 'ecr:ap-south-1:ecr-credentials', url: 'https://524104443570.dkr.ecr.ap-south-1.amazonaws.com']) {
                    echo "Push Docker Image to ECR: In Progress"
                    sh "docker push ${ecrImageName}"
                    echo "Push Docker Image to ECR: Completed"
                    }
                }
            }
            stage('Upload the Docker Image to Nexus') {
                steps {
                    script {
                        withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {

                            sh 'docker login http://13.203.155.223:8085/repository/mytrip-ms/ -u admin -p ${PASSWORD}'
                            echo "Push Docker Image to Nexus: In Progress"
                            sh "docker tag ${env.IMAGE_NAME} 13.203.155.223:8085/mytrip-ms:${ecrImageName}"
                            sh "docker push 13.203.155.223:8085/mytrip-ms-${ecrImageName}"
                            echo "Push Docker Image to Nexus: Completed"
                        }
                    }
                }
            }
            stage('Delete Local Docker Images') {
                steps {
                    echo "Deleting Local Docker Images: ${env.IMAGE_NAME} and ${env.ECR_IMAGE_NAME}"
                    sh "docker rmi ${env.IMAGE_NAME} ${env.ECR_IMAGE_NAME}"
                    echo "Local Docker Images Deletion Completed"
                }
            }
        }
    }
}