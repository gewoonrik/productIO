Homeserver
=========

Building
---------
`docker build --tag=productio/homeserver:latest . `

Running
--------
```
docker run \
-e USER=<user> \
-e SERVER=<server> \
-e SCAN_INTERVAL=<interval-in-seconds> \
--name=homeserver-instance \
-d \
productio/homeserver:latest
```
