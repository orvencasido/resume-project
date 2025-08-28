#!/bin/bash

docker network create jenkins-network

docker run -d \
--name jenkins-master \
--network jenkins-network \
-p 30000:8080 -p 50000:50000 \
-v $HOME/jenkins_home:/var/jenkins_home \
-u root \
jenkins/jenkins

docker run -d \
--name docker-agent \
--network jenkins-network \
--restart=always \
-v /var/run/docker.sock:/var/run/docker.sock \
-v $(which docker):/usr/bin/docker \
-u root \
-e JENKINS_AGENT_NAME=docker-agent \
-e JENKINS_URL=http://13.213.12.183:30000/ \
-e JENKINS_SECRET=2afa3ac684d5083526870231f38aa34b32619be0cd07444ee0c19e35b9a5a9c0 \
jenkins/inbound-agent