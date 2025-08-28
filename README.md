# 🚀 CI/CD Pipeline with Docker Commands  
**Author:** Orven Casido  

---

## NOTE
❗Add config to these Variable: `JENKINS_AGENT_NAME` `JENKINS_URL` `JENKINS_SECRET`

## 🔄 Continuous Integration  
I push code → Git repository updates → Jenkins pulls from Git → Jenkins builds the Docker image

## 🔵 Continuous Deployment  
Jenkins pushes the image to Docker Hub → Jenkins pulls the image from Docker Hub → Jenkins deploys the container

---

## 🛠️ How Did I Do It?

1. SSH into the VM.  
2. Run updates, and install **Git** and **Docker**.  
3. Install **Jenkins via Docker** (get the container from the official Docker Hub repository).  
4. Configure Jenkins setup and access it via the VM’s **public IP** and configured port.  
5. After setting up Jenkins Master, create a container for **Jenkins Agent** to run Docker commands.  
6. Configure the Jenkins Agent, enable Docker mount to use Docker commands and Docker API.  
7. Code the frontend, located at `/src`. Resume contents are configurable since they are written in a **JSON markup file**.  
8. Create a `Dockerfile` to dockerize the application.  
9. *(Optional)* Build the Dockerfile locally to verify if the build works correctly.  
10. Create a `Jenkinsfile` for the CI/CD pipeline (include all the stages).  
11. In the same folder, initialize Git and connect the repository inside the Jenkins UI.  
12. Build the pipeline job in Jenkins.  

---