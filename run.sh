#!/bin/bash

# Prevent SIGTERM from ending process.
trap "echo Service attempted a SIGTERM." SIGTERM

for BITS in 0 2 4 6 8 10; do
    # Node
    java -Xms10g -jar target/green-loader.jar load-configs/node.json $BITS
    java -Xms10g -jar target/green-loader.jar load-configs/node-logging.json $BITS
    
    # Jetty
    java -Xms10g -jar target/green-loader.jar load-configs/jetty.json $BITS
    java -Xms10g -jar target/green-loader.jar load-configs/jetty-logging.json $BITS
    
    # Tomcat
    java -Xms10g -jar target/green-loader.jar load-configs/tomcat.json $BITS
    java -Xms10g -jar target/green-loader.jar load-configs/tomcat-logging.json $BITS

    # Netty
    java -Xms10g -jar target/green-loader.jar load-configs/netty.json $BITS
    
    # Green Lightning
    java -Xms10g -jar target/green-loader.jar load-configs/gl.json $BITS
    java -Xms10g -jar target/green-loader.jar load-configs/gll.json $BITS
    java -Xms10g -jar target/green-loader.jar load-configs/glt.json $BITS
    java -Xms10g -jar target/green-loader.jar load-configs/gltl.json $BITS

    # PHP
    java -Xms10g -jar target/green-loader.jar load-configs/php.json $BITS
    # Note: For PHP logging, we MUST delete its logs between runs; it just creates too much data.
    rm -rf php/logs/error_log
    java -Xms10g -jar target/green-loader.jar load-configs/php-logging.json $BITS
    
    # Micronaut
    java -Xms10g -jar target/green-loader.jar load-configs/micronaut.json $BITS

    # Play!
    java -Xms10g -jar target/green-loader.jar load-configs/play.json $BITS
    
    # Squall
    # java -Xms6g -jar target/green-loader.jar squall.json
done