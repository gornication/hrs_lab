def dockerImage

def checkoutCode() {
    git 'https://github.com/gornication/hrs_lab.git'
}

def syntaxCheck() {
    sh 'python -m py_compile $(find . -name "*.py")'
}

def buildDockerImage() {
    dockerImage = docker.build("${env.DOCKER_HUB_REPO}:${env.BUILD_NUMBER}")
    echo "Docker Image built: ${env.DOCKER_HUB_REPO}:${env.BUILD_NUMBER}"
}

def pushDockerImage() {
    docker.withRegistry('https://index.docker.io/v1/', 'gornication-pipeline-lab-1') {
        dockerImage.push("${env.BUILD_NUMBER}")
        dockerImage.push("latest")
        echo "Docker Image pushed: ${env.DOCKER_HUB_REPO}:${env.BUILD_NUMBER}"
    }
}

def deployToKubernetes() {
    // Deploy to Kubernetes
    sh """
    kubectl set image deployment/your-deployment-name your-container-name=${env.DOCKER_HUB_REPO}:${env.BUILD_NUMBER}
    kubectl rollout status deployment/your-deployment-name
    """
    echo "Deployment to Kubernetes completed"
}
