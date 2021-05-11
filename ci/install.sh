#!/bin/bash


echo "Downloading certificate from private repository..."
git clone https://${GH_TOKEN}:x-oauth-basic@github.com/Engineering-Research-and-Development/private-files-repo.git
cp -rf private-files-repo/*.p12 ./ci/docker/ecc-cert
echo "Certificate from private repository downloaded"
BRANCH_DATA_APP=master


echo "Installing Newman CLI..."
sudo npm install -g newman@5.2.2
newman --version
echo "Newman installed, READY TO TEST..."

echo "Downloading and Installing docker-compose..."
sudo rm /usr/local/bin/docker-compose
sudo curl -L "https://github.com/docker/compose/releases/download/1.29.1/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
echo "docker-compose correctly installed"

sudo docker system prune --volumes -f

mkdir -p  $HOME/.m2/repository/de/fraunhofer/aisec
mkdir -p  $HOME/.m2/repository/de/fraunhofer/dataspaces
mkdir -p  $HOME/.m2/repository/de/fraunhofer/iais/eis
de.fraunhofer.iais.eis.ids.infomodel:java:ja
cp -rf ./ci/.m2/repository/de/fraunhofer/aisec/ids  $HOME/.m2/repository/de/fraunhofer/aisec
cp -rf ./ci/.m2/repository/de/fraunhofer/dataspaces/iese  $HOME/.m2/repository/de/fraunhofer/dataspaces
cp -rf ./ci/.m2/repository/de/fraunhofer/iais/eis  $HOME/.m2/repository/de/fraunhofer/iais


echo "Installing Multipart Message Lib..."
git clone https://github.com/Engineering-Research-and-Development/market4.0-ids_multipart_message_processor
cd market4.0-ids_multipart_message_processor
mvn clean install
cd ..
echo "Installed  Multipart Message Lib"

echo "Installing websocket-message-streamer-lib..."
git clone https://github.com/Engineering-Research-and-Development/market4.0-websocket_message_streamer.git
cd market4.0-websocket_message_streamer
mvn clean install
cd ..
echo "Installed websocket-message-streamer-lib"

echo "Cloning and Creating Docker Container from Data-App repo..."
git clone https://github.com/Engineering-Research-and-Development/market4.0-data_app_test_BE.git
cd market4.0-data_app_test_BE
git checkout ${BRANCH_DATA_APP}
mvn clean package
docker build -f Dockerfile -t rdlabengpa/data-app .
cd ..
echo "Data-App is ready to start"

echo "Downloading and installing Clearing-House Model..."
git clone https://github.com/Engineering-Research-and-Development/market4.0-clearing_house.git
cd market4.0-clearing_house
mvn install -DskipTests
cd ..
echo "Clearing-House Model installed!"

echo "Creating Docker Container for ECCs..."
mvn clean package -DskipTests
docker build -f Dockerfile -t rdlabengpa/execution_core_container_bl .

#echo "Starting services..."
#cd ci/docker
#ls

#sudo docker-compose up