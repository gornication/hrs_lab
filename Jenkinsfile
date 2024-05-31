pipeline {
    agent any

    environment {
        DOCKER_HUB_CREDENTIALS = credentials('gornication-pipeline-lab-1')
        DOCKER_HUB_REPO = 'gornication/pipeline-lab-1'
    }

    stages {
        stage('Load Script') {
            steps {
                script {
                    echo 'Loading script...'
                    load 'pl-script.groovy'
                    echo 'Script loaded successfully!'
                }
            }
        }
        stage('Checkout') {
            steps {
                script {
                    checkoutCode()
                }
            }
        }

        stage('Syntax Check') {
            steps {
                script {
                    syntaxCheck()
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    buildDockerImage()
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    pushDockerImage()
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    deployToKubernetes()
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}
