FROM iraz/archlinux-jdk11-base:opencv4
EXPOSE 20003:20003
COPY zoom-server.jar /home/
RUN java -jar /home/zoom-server.jar
