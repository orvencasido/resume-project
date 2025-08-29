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

        stage('Test') {
            steps {
                script {
                    echo "Testing image: ${DOCKER_IMAGE}:${params.VERSION}"
                    sh """
                        TEST_CONTAINER=test-${DOCKER_IMAGE}:${params.VERSION}
                        docker run -d --name $TEST_CONTAINER -p 80:80 ${DOCKER_IMAGE}:${params.VERSION}

                        sleep 5

                        # Check if container responds on port 8080
                        if ! curl -s -o /dev/null -w "%{http_code}" http://localhost:8080 | grep -q "200"; then
                          echo "❌ Container failed health check"
                          docker logs $TEST_CONTAINER
                          docker rm -f $TEST_CONTAINER || true
                          exit 1
                        fi

                        echo "✅ Container passed health check"
                        docker rm -f $TEST_CONTAINER || true
                    """
                }
            }
        }
    }
}
