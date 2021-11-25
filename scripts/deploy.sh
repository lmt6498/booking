#!/usr/bin/env bash

mvn clean install
docker build -f ./docker/Dockerfile.local -t ms-booking:latest .

docker stop $(docker ps --filter "name=ms-booking" -q)
docker rm $(docker ps --filter "name=ms-booking" -aq)
docker run -m 256M --network ms-net --name ms-booking --rm -p 8002:8002 -d  ms-booking:latest 
sleep 3
docker logs -f $(docker ps -q --filter "name=ms-booking*")
