FROM iraz/archlinux-jdk11-base:opencv
ENV HOST_HOSTNAME local_network
EXPOSE 20001
COPY GaussianFilterServer.jar /home/
RUN chmod -R 7777 /home/
