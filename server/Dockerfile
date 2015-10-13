FROM ubuntu
MAINTAINER Erwin van Eyk <erwinvaneyk@gmail.com>

# Install dependencies
RUN apt-get update && apt-get install openjdk-7-jre-headless -y

# Build application
ADD target/scala-2.11/productio-server.jar /app/

ENTRYPOINT ["java -jar /app/productio-server.jar"]