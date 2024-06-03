// Assuming gv represents a simple map or another data structure holding method references
def gv = [:]

gv.checkoutCode = {
    git branch: 'main', url: 'https://github.com/gornication/hrs_lab.git'
}

gv.syntaxCheck = {
    sh 'python3 -m py_compile $(find . -name "*.py")'
}

gv.buildDockerImage = {
    def dockerImage = sh(script: "/usr/local/bin/docker build -t ${env.DOCKER_HUB_REPO}:${env.BUILD_NUMBER} .", returnStdout: true).trim()
    echo "Docker Image built: ${env.DOCKER_HUB_REPO}:${env.BUILD_NUMBER}"
    return dockerImage
}

gv.pushDockerImage = { dockerImage ->
    // Extract Docker Hub username and password from credentials
    def credentials = credentials('gornication-pipeline-lab-1')
    def username = credentials?.username
    def password = credentials?.password

    // Perform Docker login using credentials
    withDockerRegistry([credentialsId: 'gornication-pipeline-lab-1', url: 'https://index.docker.io/v1/']) {
        sh "echo ${password} | docker login -u ${username} --password-stdin"
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
