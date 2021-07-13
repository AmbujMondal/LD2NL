###### _To deploy the api in Linux, we are using Docker._

First step is to create a docker image. For that we have added the Dockerfile.
It defines the steps to create a docker image.

Once docker image is created, it shall be containerized. For that we need a server.
We are using our linux server provided by university.

**steps:** 

_First create the environment in the server._ 

1) Install docker cli - may need root privileges- then Use `sudo su` to login
    
   `apt install docker.io`
   
2)  verify that docker is installed by running
    
    `docker --version`
3) pull docker image from dockerhub. Login to dockerHub first from cli
   
   `docker pull hub_account/image_name:tag`
   
   Or Create Docker Image from dockerfile -
   
   `docker build -t hub_account/image_name:tag .`
   
4) create container
   
   `docker run -d --name container_name hub_account/image_name`
5) run the container
   
   `docker start container_name`

6) Now the application shall be running. Test by running curl command-
    
        curl --url http://localhost:8081/hello

7) To access the application over internet, we need to expose the container port.
   for that we have to bind the container port to the tcp port of the host.
   By default, 8080 is the host port. so we have to bind 8081 to 8080.
   
***Note*** - _use -p option while container creation to open the link of the docker with host port
    else, it will not be accessible over the internet, but locally._
    
example- 

    docker run -d -p 8081:8080 --name container_name hub_account/image_name

**-d**  : _to run the container in the detached mode. else it will be attached to the terminal lifecycle._
