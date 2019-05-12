FROM iraz/archlinux-jdk11-base:intellij-idea
ENV HOST_HOSTNAME local_network
EXPOSE 20000:20000
COPY main-server.jar /home/
RUN chmod -R 7777 /home/
