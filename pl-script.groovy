// Assuming gv represents a simple map or another data structure holding method references
def gv = [:]

gv.checkoutCode = {
    git branch: 'main', url: 'https://github.com/gornication/hrs_lab.git'
}

gv.syntaxCheck = {
    sh 'python -m py_compile $(find . -name "*.py")'
}

gv.buildDockerImage = {
    def dockerImage = docker.build("${env.DOCKER_HUB_REPO}:${env.BUILD_NUMBER}")
    echo "Docker Image built: ${env.DOCKER_HUB_REPO}:${env.BUILD_NUMBER}"
    return dockerImage
}

gv.pushDockerImage = { dockerImage ->
    docker.withRegistry('https://index.docker.io/v1/', 'gornication-pipeline-lab-1') {
        dockerImage.push("${env.BUILD_NUMBER}")
        dockerImage.push("latest")
        echo "Docker Image pushed: ${env.DOCKER_HUB_REPO}:${env.BUILD_NUMBER}"
    }
}

gv.deployToKubernetes = { dockerImage ->
    sh """
    kubectl set image deployment/your-deployment-name your-container-name=${env.DOCKER_HUB_REPO}:${env.BUILD_NUMBER}
    kubectl rollout status deployment/your-deployment-name
    """
    echo "Deployment to Kubernetes completed"
}

return gv
