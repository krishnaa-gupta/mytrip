FROM tomcat:9.0.52-jre11-openjdk-slim

RUN apt-get update && \
    apt-get install -y openjdk-17-jdk && \
    apt-get clean

RUN update-alternatives --set java /usr/lib/jvm/java-17-openjdk-amd64/bin/java && \
    update-alternatives --set javac /usr/lib/jvm/java-17-openjdk-amd64/bin/javac

COPY ./target/mytrip-0.0.1-SNAPSHOT.jar /usr/local/tomcat/webapps/

EXPOSE 8080

USER root

WORKDIR /usr/local/tomcat/webapps