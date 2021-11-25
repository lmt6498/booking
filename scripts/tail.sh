#!/bin/bash

docker logs -f $(docker ps -q --filter "name=ms-booking*")
