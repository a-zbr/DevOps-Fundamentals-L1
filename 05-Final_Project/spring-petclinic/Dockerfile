FROM openjdk:11-slim
LABEL maintainer="andrii_zbarazhskyi"
EXPOSE 80
COPY ./target/*.jar /home/app.jar
CMD ["java","-jar","/home/app.jar"]