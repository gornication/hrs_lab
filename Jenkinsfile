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
                    gv = load 'pl-script.groovy'
                }
            }
        }
        stage('Checkout') {
            steps {
                script {
                    gv.checkoutCode()
                }
            }
        }

        stage('Syntax Check') {
            steps {
                script {
                    gv.syntaxCheck()
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    dockerImage = gv.buildDockerImage().call()
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    gv.pushDockerImage(dockerImage).call()
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    gv.deployToKubernetes().call()
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
