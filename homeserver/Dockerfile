FROM ubuntu
MAINTAINER Erwin van Eyk <erwinvaneyk@gmail.com>

# Install dependencies
RUN apt-get update && apt-get install golang git -y

# Build application
ENV GOPATH /go/
ADD . $GOPATH/src/homeserver/
WORKDIR $GOPATH/src/homeserver/
RUN go get
RUN go build -o /opt/homeserver

# Program default variables
# 900 seconds = 15 min
ENV SCAN_INTERVAL 900
ENV USER Rik
ENV SERVER http://erwinvaneyk.nl

ENTRYPOINT ["/opt/homeserver"]

