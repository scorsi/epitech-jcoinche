#!/usr/bin/env bash

echo 'Build Client';
cd ./client/ && ./gradlew install && cd ../ && mkdir -p out/ && cp -R ./client/build/install/client/* out/

echo 'Build Server';
cd ./server/ && ./gradlew install && cd ../ && mkdir -p out/ && cp -R ./server/build/install/server/* out/
