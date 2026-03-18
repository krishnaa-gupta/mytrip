pipeline {
    agent any

    tools {
        maven 'maven 3.9.14'
    }

    stages {

        stage('Code Compilation') {
            steps {
                echo 'Code Compilation is In Progress!'
                sh 'mvn clean compile'
                echo 'Code Compilation is Completed Successfully!'
            }
        }

        stage('Code QA Execution') {
            steps {
                echo 'JUnit Test case check in Progress!'
                sh 'mvn clean test'
                echo 'JUnit Test case check Completed!'
            }
        }

        stage('Code Package') {
            steps {
                echo 'Creating War Artifact'
                sh 'mvn clean package'
                echo 'Creating War Artifact Completed'
            }
        }
        stage('Building & Tag Docker Image') {
            steps {
                script {
                    def imageName = "krihnaa0401/mytrip-v.1.${BUILD_NUMBER}"
                    echo "Starting Building Docker Image: ${imageName}"
                    sh "docker build -t ${imageName} ."
                    echo 'Completed Building Docker Image'
                }
            }
        }

        stage('Docker Image Scanning') {
            steps {
                echo 'Docker Image Scanning Started'
                sh 'docker --version'
                echo 'Docker Image Scanning Completed'
            }
        }

        stage('Docker push to Docker Hub') {
            steps {
                script {
                    withDockerRegistry([credentialsId: 'dockerhub-creds', url: 'https://index.docker.io/v1/']) {
                        echo "Push Docker Image to DockerHub: In Progress"
                        sh "docker push ${imageName}"
                        echo "Push Docker Image to DockerHub: Completed"
                    }                       }
                }
            }
        }
    }
}