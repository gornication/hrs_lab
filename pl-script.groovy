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
    withCredentials([usernamePassword(credentialsId: 'gornication-pipeline-lab-1', usernameVariable: 'DOCKER_HUB_USERNAME', passwordVariable: 'DOCKER_HUB_PASSWORD')]) {
        sh "echo ${DOCKER_HUB_PASSWORD} | /usr/local/bin/docker login -u ${DOCKER_HUB_USERNAME} --password-stdin"
        sh "/usr/local/bin/docker push ${env.DOCKER_HUB_REPO}:${env.BUILD_NUMBER}"
        echo "Docker Image pushed: ${env.DOCKER_HUB_REPO}:${env.BUILD_NUMBER}"
    }
}

gv.deployToKubernetes = { dockerImage ->
    sh """
    /usr/local/bin/kubectl set image deployment/web-app web-app=${env.DOCKER_HUB_REPO}:${env.BUILD_NUMBER}
    /usr/local/bin/kubectl rollout status deployment/web-app
    """
    echo "Deployment to Kubernetes completed"
}

return gv
