#!/bin/bash
echo "Deploy to Docker Hub for developer"
sudo docker login -u ${DOCKER_USER} -p ${DOCKER_PASSWORD}
sudo docker tag rdlabengpa/execution_core_container_bl rdlabengpa/execution_core_container_bl:develop
sudo docker push rdlabengpa/execution_core_container_bl
