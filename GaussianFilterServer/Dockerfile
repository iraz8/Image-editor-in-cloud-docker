FROM iraz/manjaro-jdk11-opencv4-base
ENV HOST_HOSTNAME local_network
EXPOSE 20001
COPY META-INF/ /home/META-INF/
COPY gaussian-filter-server.jar /home/
RUN chmod -R 7777 /home/
