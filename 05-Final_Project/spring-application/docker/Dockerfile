FROM openjdk:8u151-jdk-alpine3.7
LABEL maintainer="andrii_zbarazhskyi"

EXPOSE 80

ENV APP_HOME /usr/src/app

COPY target/shopping-cart-0.0.1-SNAPSHOT.jar $APP_HOME/app.jar

WORKDIR $APP_HOME

ENTRYPOINT ["/bin/sh", "-c", "java -jar app.jar"]
# ENTRYPOINT exec java -jar app.jar