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
                        export TEST_CONTAINER=test-${BUILD_NUMBER}
                        docker run -d --name \$TEST_CONTAINER -p 8080:80 ${DOCKER_IMAGE}:${params.VERSION}

                        sleep 5

                        # Grab the <title> from the HTML
                        TITLE=\$(curl -s http://localhost:8080 | grep -oP '(?<=<title>).*?(?=</title>)')

                        if [ -z "\$TITLE" ]; then
                          echo "❌ No <title> found in response!"
                          docker logs \$TEST_CONTAINER
                          docker rm -f \$TEST_CONTAINER || true
                          exit 1
                        fi

                        echo "✅ Test passed! Found page title: '\$TITLE'"
                        docker rm -f \$TEST_CONTAINER || true
                    """
                }
            }
        }
    }
}
