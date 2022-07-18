FROM openjdk:11

ENV SBT_VERSION 1.5.8
RUN curl -L -o sbt-$SBT_VERSION.zip https://github.com/sbt/sbt/releases/download/v$SBT_VERSION/sbt-$SBT_VERSION.zip
RUN unzip sbt-$SBT_VERSION.zip -d ops
WORKDIR producer

COPY . .

RUN /ops/sbt/bin/sbt --version
RUN /ops/sbt/bin/sbt assembly

ENTRYPOINT ["java", "-jar", "target/scala-2.12/producer.jar"]