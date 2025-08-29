pipeline {
    agent { label 'docker-agent' }

    parameters {
        string(
            name: 'VERSION',
            defaultValue: 'latest',
            description: 'Enter the version tag from DockerHub (example: latest, v1, v2)'
        )
    }

    environment {
        DOCKER_IMAGE = "orvencasido/resume-project"
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

        stage('Test') {
            steps {
                script {
                    echo "Testing image: ${DOCKER_IMAGE}:${params.VERSION}"
                    sh """
                        docker run -d --name test-${BUILD_NUMBER} -p 90:90 ${DOCKER_IMAGE}:${params.VERSION}
                        sleep 5

                        # Just check if curl gets a response
                        if curl -s http://54.169.51.227:90 > /dev/null; then
                          echo "✅ Container responded successfully!"
                        else
                          echo "❌ Container did not respond!"
                          docker logs test-${BUILD_NUMBER}
                          docker rm -f test-${BUILD_NUMBER} || true
                          exit 1
                        fi

                        docker rm -f test-${BUILD_NUMBER} || true
                    """
                }
            }
        }
    }
}
