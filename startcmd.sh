docker build -t main-server main-server/
docker build -t gaussian-filter-server GaussianFilterServer/
docker build -t image-shape-conversion-server ImageShapeConversionServer/
docker build -t zoom-server ZoomServer/
docker build -t client-simulation ClientSimulation/
docker container run --network-alias local_network_main_server --network local_network -d -v /home/razvan/Workspace/Image-editor-in-cloud-docker/Images/:/home/Images/ main-server java -jar /home/main-server.jar
docker container run --network-alias local_network_gaussian_filter_server --network local_network -d -v /home/razvan/Workspace/Image-editor-in-cloud-docker/Images/:/home/Images/ gaussian-filter-server java -jar /home/GaussianFilterServer.jar
docker container run --network-alias local_network_image_shape_conversion_server --network local_network -d -v /home/razvan/Workspace/Image-editor-in-cloud-docker/Images/:/home/Images/ image-shape-conversion-server java -jar /home/ImageShapeConversionServer.jar
docker container run --network-alias local_network_zoom_server --network local_network -d -v /home/razvan/Workspace/Image-editor-in-cloud-docker/Images/:/home/Images/ zoom-server java -jar /home/ZoomServer.jar
docker container run --network local_network -it -v /home/razvan/Workspace/Image-editor-in-cloud-docker/Images/:/home/Images/ client-simulation java -jar /home/ClientSimulationG.jar
docker container run --network local_network -d -v /home/razvan/Workspace/Image-editor-in-cloud-docker/Images/:/home/Images/ client-simulation java -jar /home/ClientSimulationS.jar
docker container run --network local_network -d -v /home/razvan/Workspace/Image-editor-in-cloud-docker/Images/:/home/Images/ client-simulation java -jar /home/ClientSimulationZ.jar



