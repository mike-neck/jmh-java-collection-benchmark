FROM adoptopenjdk/openjdk12:jdk-12.0.1_12-slim

WORKDIR /work

RUN apt-get -y update && \
    apt-get -y install python3 python3-pip unzip && \
    pip3 install awscli && \
    mkdir -p /work

ADD app.sh /work/app.sh
