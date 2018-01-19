#!/usr/bin/env bash

clear
./gradlew clean 
./gradlew test 
[ -f build/libs/VagrantCloud-1.0-SNAPSHOT.jar ] && rm build/libs/VagrantCloud-1.0-SNAPSHOT.jar
./gradlew build 
java -jar build/libs/VagrantCloud-1.0-SNAPSHOT.jar 
ls -alh build/libs/VagrantCloud-1.0-SNAPSHOT.jar 
