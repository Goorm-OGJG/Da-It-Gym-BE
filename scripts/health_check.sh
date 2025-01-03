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


# 새로 사용하려는 포트를 리스닝하는 컨테이너 id 찾기
echo "> Finding container listening on port $TARGET_PORT"
TARGET_CONTAINER_ID=$(docker ps --filter "publish=${TARGET_PORT}" --format "{{.ID}}")

if [ ! -z ${TARGET_CONTAINER_ID} ]; then
  echo "> Complete to find container, id is ${TARGET_CONTAINER_ID}."
else
  echo "> No container is currently listening on port ${TARGET_PORT}."
  exit 1
fi

# 헬스체크 대상 설정
echo "Checking health for container : $STANDBY_CONTAINER, id is $TARGET_CONTAINER_ID."
for i in {1..10}; do
  HEALTH_STATUS=$(docker inspect --format='{{json .State.Health.Status}}' "$TARGET_CONTAINER_ID")
  if [ "$HEALTH_STATUS" == "\"healthy\"" ]; then
    echo "Container $STANDBY_CONTAINER is healthy."
    exit 0
  fi
  echo "Health check attempt $i failed. Status=$HEALTH_STATUS Retrying..."
  sleep 10
done

echo "Health check failed for container $STANDBY_CONTAINER."
exit 1
