#!/bin/bash

CURRENT_PORT=$(cat /home/ubuntu/service_url.inc | grep -Po '[0-9]+' | tail -1)
TARGET_PORT=0

echo "> Current port of running WAS is ${CURRENT_PORT}."

if [ ${CURRENT_PORT} -eq 8081 ]; then
  TARGET_PORT=8081
elif [ ${CURRENT_PORT} -eq 8082 ]; then
  TARGET_PORT=8082
else
  echo "> No WAS is connected to nginx"
fi

TARGET_PID=$(lsof -Fp -i TCP:${TARGET_PORT} | grep -Po 'p[0-9]+' | grep -Po '[0-9]+')

if [ ! -z ${TARGET_PID} ]; then
  echo "> Kill WAS running at ${TARGET_PORT}."
  # shellcheck disable=SC2046
  # shellcheck disable=SC2006
  kill -9 `ps -ef|grep redis|awk '{print $2}'`
  sudo kill -9 ${TARGET_PID}
fi

REPOSITORY=/home/ubuntu/rhythme
# shellcheck disable=SC2164
cd $REPOSITORY

# shellcheck disable=SC2010
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep 'app.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

nohup java -jar -Dserver.port=${TARGET_PORT} ${JAR_PATH} >/home/ubuntu/rhythme/nohup.out 2>&1 &
echo "> Now new WAS runs at ${TARGET_PORT}."
exit 0