pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'building app...'
            }
        }

        stage('Test') {
            steps {
                echo 'testing app...'
            }
        }

        stage('Deploy') {
            steps {
                echo 'deploying app...'
            }
        }
    }

    post {
        success {
            echo '*******SUCCESS********'
        }
        failure {
            echo '!!!!!!!!FAILED!!!!!!!'
        }
    }
}
