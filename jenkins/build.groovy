pipeline {
    agent { label 'docker-agent' }

    environment {
        DOCKER_IMAGE = "orvencasido/resume-project"
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
    }

}