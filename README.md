# ClearSky Spring MVC and Spring Boot API	
A weather information web application made in Spring MVC and Spring Boot with Docker.	

## directory structure:	
**`rest`** [*module-rest*]: contains SpringMVC based REST API	
**`container`** [*module-container*]: contains SpringBoot version of the same API with Docker configuration

### For the final module submission, update following urls for your app:    
**`EC2 Jenkins URL`**: `http://ec2-54-193-96-158.us-west-1.compute.amazonaws.com:8090/`  
**`Swagger URL for Spring Boot API`**: `http://ec2-54-193-96-158.us-west-1.compute.amazonaws.com/clearsky-boot/api/swagger-ui.html`  
**`Swagger URL for Spring MVC REST API`**: `http://ec2-54-193-96-158.us-west-1.compute.amazonaws.com/clearsky/api/swagger-ui.html`  

`NOTE: THESE URLS MAY CHANGE. AS I'VE USED 2 EC2 INSTANCES AND ALMOST FINISHED UP MY FREE INSTANCE HOURS, I'LL HAVE TO TERMINATE THE INSTANCES AS OF NOW. I HAVE AMI IMAGES USING WHICH I WOULD LAUNCH INSTANCES AGAIN. DOING SO WILL CHANGE THE HOSTNAMES AND IPS. PLEASE MESSAGE ME ON SLACK JUST 15 TO 20 MINUTES EARLY. I'LL LAUNCH INSTANCES, CHANGE URLS IN NGINX AND JENKINS AND TEST THE END-POINTS AT ONCE.`

## Architecture:
We have 2 EC2 instances (free tier) from 2 separate accounts. On one EC2 instance, Nginx, Jenkins and Docker have been installed and on the other instance tomcat (for module 1) and Docker (again) have been installed.

Module 1: 
For Module 1, upon jenkins build, GitHub code from rest/ClearSky will be taken from the module-rest branch, war will be build and through ssh, war will be deployed in 2nd EC2 instance in /usr/local/apache-tomcat-8.5.11/webapps/ folder.

Module 2: 
For Module 2, upon jenkins build, GiHub code from container/ClearSky will be taken from module-container branch, jar will be build and using Docker Step plugin, a docker image will be created in the same instance (1st instance), and pushed to shrikantkakaraparthi/clearskyboot private repo. Then, docker run command (through remote ssh) will pull the image from the repo and create a container in the 2nd EC2 instance and start the application.

Nginx will proxy_pass requests to 2 separate apps based on clearsky.conf and clearsky-boot.conf files towards the 2nd instance. Instance 1 will communicate to instance 2 using its public IP. The Swagger URLs (1st instance) for both the app are mentioned above. 