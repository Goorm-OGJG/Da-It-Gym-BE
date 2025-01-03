#!/bin/bash

REPOSITORY=/home/ec2-user/guen3
CURRENT_PORT=$(cat $REPOSITORY/service_url.inc | grep -Po '[0-9]+' | tail -1)
TARGET_PORT=0
STANDBY_CONTAINER="none"

# 새로 사용하려는 포트(TARGET_PORT) 찾기
echo "> Current port of running container is ${CURRENT_PORT}."

if [ ${CURRENT_PORT} -eq 8081 ]; then
  TARGET_PORT=8082
  STANDBY_CONTAINER="app-v2"
elif [ ${CURRENT_PORT} -eq 8082 ]; then
  TARGET_PORT=8081
  STANDBY_CONTAINER="app-v1"
else
  echo "> No container is connected to nginx"
  exit 1
fi

# 새로 사용하려는 포트를 리스닝하는 컨테이너가 있는지 확인
TARGET_CONTAINER_ID=$(docker ps --filter "publish=${TARGET_PORT}" --format "{{.ID}}")

if [ ! -z ${TARGET_CONTAINER_ID} ]; then
  echo "> Stopping and removing container listening on port ${TARGET_PORT}."
  docker stop ${TARGET_CONTAINER_ID}
  docker rm ${TARGET_CONTAINER_ID}
else
  echo "> No container is currently listening on port ${TARGET_PORT}."
fi

# 새로운 컨테이너 실행
echo "Starting new container service : $STANDBY_CONTAINER"
docker-compose -f $REPOSITORY/docker-compose.yml up --build -d $STANDBY_CONTAINER

sleep 5