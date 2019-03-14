# spring-redis-websocket-gis

## Pre-requisite:
Install and run Redis locally:
```
brew install redis

redis-server /usr/local/etc/redis.conf 
redis-cli
>config set requirepass root
```

## Set-up
To set up the application, just go to the root folder and execute below commands:
```
mvn clean package
mvn spring-boot:run
```
device.html is the page to mimic the mobile devices to send back the geolocation information.
dashboard.html is the page to mimic the monitoring system.

## Concurrency test
To test the concurrency, you can use Apache Benchmarking:
```
ab -p post_gis.txt -T application/json -c 1000 -n 10000  http://127.0.0.1:8080/message
```
