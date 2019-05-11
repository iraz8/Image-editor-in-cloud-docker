docker build -t main-server main-server/
docker build -t gaussian-filter-server GaussianFilterServer/
docker build -t client-simulation ClientSimulation/
docker container run --network-alias local_network_main_server --network local_network -d -v /home/razvan/Workspace/Image-editor-in-cloud-docker/Images/:/home/Images/ main-server java -jar /home/main-server.jar
docker container run --network-alias local_network_gaussian_filter_server --network local_network -d -v /home/razvan/Workspace/Image-editor-in-cloud-docker/Images/:/home/Images/ gaussian-filter-server java -jar /home/gaussian-filter-server.jar
docker container run --network local_network -d -v /home/razvan/Workspace/Image-editor-in-cloud-docker/Images/:/home/Images/ client-simulation java -jar /home/client-simulation.jar

