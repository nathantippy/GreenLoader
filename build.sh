#!/bin/bash

# The load tester.
mvn clean package

# Node.
npm install body-parser
npm install express
npm install morgan

# Tomcat and Jetty
cd spring-boot
mvn clean
mvn package -P tomcat
mvn package -P jetty
cd ..

# Netty.
cd spring-webflux
mvn clean package
cd ..

# Pronghorn
cd pronghorn-app
mvn clean package
cd ..

# Green Lightning
cd green-lightning
mvn clean package
cd ..

# Micronaut
# Note: Disabled for now.
# cd micronaut-app
# ./gradlew clean shadowJar
# cd ..

# Play!
cd play
mvn clean package play2:dist-exploded
cd ..

# Squall
# cd squall
# mvn clean package
# cd ..