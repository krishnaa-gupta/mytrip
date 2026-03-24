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
                    def accountId = "524140443570"
                    def region = "ap-south-1"
                    def repoName = "mytrip.ecr"
                    def ecrImage = "${accountId}.dkr.ecr.${region}.amazonaws.com/${repoName}:${BUILD_NUMBER}"

                    echo "Logging into AWS ECR..."

                    withCredentials([[
                        $class: 'AmazonWebServicesCredentialsBinding',
                        credentialsId: 'ecr-credentials',
                        accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                        secretKeyVariable: 'AWS_SECRET_ACCESS_KEY'
                    ]]) {

                        sh """
                        aws configure set aws_access_key_id $AWS_ACCESS_KEY_ID
                        aws configure set aws_secret_access_key $AWS_SECRET_ACCESS_KEY
                        aws configure set default.region ${region}

                        aws ecr get-login-password --region ${region} | \
                        docker login --username AWS --password-stdin ${accountId}.dkr.ecr.${region}.amazonaws.com
                        """
                    }

                    echo "Tagging Docker image..."
                    sh "docker tag ${IMAGE_NAME} ${ecrImage}"

                    echo "Pushing Docker image to ECR..."
                    sh "docker push ${ecrImage}"

                    env.ECR_IMAGE_NAME = ecrImage
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