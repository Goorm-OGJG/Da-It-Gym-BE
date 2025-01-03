# switch_container.sh

#!/bin/bash

# Crawl current connected port of container
CURRENT_PORT=$(cat /home/ec2-user/guen3/service_url.inc  | grep -Po '[0-9]+' | tail -1)
TARGET_PORT=0
PREVIOUS_PORT=0

echo "> Nginx currently proxies to ${CURRENT_PORT}."

# Toggle port number
if [ ${CURRENT_PORT} -eq 8081 ]; then
    TARGET_PORT=8082
    PREVIOUS_PORT=8081
elif [ ${CURRENT_PORT} -eq 8082 ]; then
    TARGET_PORT=8081
    PREVIOUS_PORT=8082
else
    echo "> No container is connected to nginx"
    exit 1
fi

# 프록시 포트 변경
echo "set \$service_url http://127.0.0.1:${TARGET_PORT};" | tee /home/ec2-user/guen3/service_url.inc
echo "> Now Nginx proxies to ${TARGET_PORT}."

# Reload nginx
sudo service nginx reload
echo "> Nginx reloaded."

# 이전 컨테이너 중지
# 새로 사용하려는 포트를 리스닝하는 컨테이너가 있는지 확인
echo "> Previous Port is ${PREVIOUS_PORT}."

PREVIOUS_CONTAINER_ID=$(docker ps --filter "publish=${PREVIOUS_PORT}" --format "{{.ID}}")

if [ ! -z ${PREVIOUS_CONTAINER_ID} ]; then
  echo "> Stopping and removing container listening on port ${PREVIOUS_PORT}."
  docker stop ${PREVIOUS_CONTAINER_ID}
  docker rm ${PREVIOUS_CONTAINER_ID}
else
  echo "> No container is currently listening on port ${PREVIOUS_PORT}."
fi