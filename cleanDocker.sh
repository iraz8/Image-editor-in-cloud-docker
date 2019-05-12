    #!/bin/bash
docker system prune --all --force --volumes
    docker container stop $(docker container ls -aq)
    # Delete all containers
    docker rm $(docker ps -a -q)
    # Delete all images
#    docker rmi $(docker images -q)
#    docker system prune
docker system prune --all --force --volumes
