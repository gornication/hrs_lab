def gv

pipeline {
    agent any

    environment {
        DOCKER_HUB_CREDENTIALS = credentials('gornication-pipeline-lab-1')
        DOCKER_HUB_REPO = 'gornication/pipeline-lab-1'
    }

    stages {
        stage("init") {
            steps {
                script {
                    gv = load "pl-script.groovy"
                }
            }
        }
        stage("Checkout") {
            steps {
                script {
                    gv.checkoutCode()
                }
            }
        }

        stage("Syntax Check") {
            steps {
                script {
                    gv.syntaxCheck()
                }
            }
        }

        stage("Build Docker Image") {
            steps {
                script {
                    gv.buildDockerImage()
                }
            }
        }

        stage("Push Docker Image") {
            steps {
                script {
                    gv.pushDockerImage()
                }
            }
        }

        stage("Deploy to Kubernetes") {
            steps {
                script {
                    gv.deployToKubernetes()
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
