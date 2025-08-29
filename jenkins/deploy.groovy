pipeline {
    agent { label 'docker-agent' }

    parameters {
        choice(
            name: 'VERSION',
            choices: getDockerTags(),
            description: 'Select the version of the image from DockerHub'
        )
    }

    environment {
        DOCKER_IMAGE = "orvencasido/resume-project"
        DOCKER_CONTAINER = "resume"
    }

    stages {
        stage('Pull') {
            steps {
                script {
                    echo "Pulling image: ${DOCKER_IMAGE}:${params.VERSION}"
                    sh "docker pull ${DOCKER_IMAGE}:${params.VERSION}"
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    echo "Deploying container: ${DOCKER_CONTAINER} with image ${DOCKER_IMAGE}:${params.VERSION}"
                    sh """
                        docker rm -f ${DOCKER_CONTAINER} || true
                        docker run -d --name ${DOCKER_CONTAINER} -p 80:80 ${DOCKER_IMAGE}:${params.VERSION}
                    """
                }
            }
        }
    }
}

