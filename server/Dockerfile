FROM ubuntu
MAINTAINER Erwin van Eyk <erwinvaneyk@gmail.com>

# Install dependencies
RUN apt-get update && apt-get install openjdk-7-jre-headless -y

# Build application

RUN mkdir -p /opt/productio-server/

ADD target/productio-server.jar /opt/productio-server/

ENTRYPOINT ["java", "-jar", "/opt/productio-server/productio-server.jar"]
