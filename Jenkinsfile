pipeline {
    agent { label 'docker-agent' }

    environment {
        DOCKER_IMAGE = "orvencasido/resume-project"
        DOCKER_CONTAINER = "resume"
        VERSION = "${env.BUILD_NUMBER}"
    }

    stages {
        stage('Build') {
            steps {
                script {
                    sh "docker build -t ${DOCKER_IMAGE}:${VERSION} ."
                }
            }
        }

        stage('Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-cred', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PW')]) {
                    script {
                        sh "echo ${DOCKER_PW} | docker login -u ${DOCKER_USER} --password-stdin"
                        sh "docker push ${DOCKER_IMAGE}:${VERSION}"
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    sh """
                        docker rm -f ${DOCKER_CONTAINER} || true
                        docker run -d --name ${DOCKER_CONTAINER} -p 80:80 ${DOCKER_IMAGE}:${VERSION}
                    """
                }
            }
        }
    }
}

