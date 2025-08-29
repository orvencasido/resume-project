def getDockerTags() {
    def tags = sh(
        script: "curl -s https://hub.docker.com/repository/docker/orvencasido/resume-project/tags | jq -r '.results[].name'",
        returnStdout: true
    ).trim().split('\n')
    return tags
}

pipeline {
    agent { label 'docker-agent' }

    parameters {
        choice(
            name: 'VERSION',
            choices: getDockerTags(),
            description: 'Select the version of the image'
        )
    }

    environment {
        DOCKER_IMAGE = "orvencasido/resume-project"
    }

    stages {
        stage('Build') {
            steps {
                script {
                    sh "docker build -t ${DOCKER_IMAGE}:${params.VERSION} ."
                    sh "docker push ${DOCKER_IMAGE}:${params.VERSION}"
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    sh """
                        docker rm -f ${DOCKER_CONTAINER} || true
                        docker run -d --name ${DOCKER_CONTAINER} -p 80:80 ${DOCKER_IMAGE}:${params.VERSION}
                    """
                }
            }
        }
    }
}

