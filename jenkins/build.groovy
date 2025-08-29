pipeline {
    agent { label 'docker-agent' }

    parameters {
        string(
            name: 'GIT_COMMIT',
            defaultValue: 'main',
            description: 'Specify Git commit hash or branch (default: main)'
        )
    }

    environment {
        DOCKER_IMAGE = "orvencasido/resume-project"
        VERSION = "${params.GIT_COMMIT.take(7)}"   // shorten commit hash for tag
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/orvencasido/resume-project.git'
                script {
                    // Checkout specific commit chosen in parameter
                    sh "git checkout ${params.GIT_COMMIT}"
                }
            }
        }

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
    }
}
