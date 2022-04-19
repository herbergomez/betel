#!/bin/sh
mvn clean package && docker build -t com.mycompany/betel-backend .
docker rm -f betel-backend || true && docker run -d -p 9080:9080 -p 9443:9443 --name betel-backend com.mycompany/betel-backend