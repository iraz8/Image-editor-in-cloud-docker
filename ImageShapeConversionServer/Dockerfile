FROM iraz/archlinux-jdk11-base:opencv4
EXPOSE 20002:20002
COPY image-shape-conversion-server.jar /home/
RUN java -jar /home/image-shape-conversion-server.jar
