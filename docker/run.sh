#!/usr/bin/env bash

if [ ${1} ] && [ -f ${1} ]; then
  java -server -Xms4g -Xmx4g -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:+UseStringDeduplication -jar ${1}
else
  ls *.jar
fi
