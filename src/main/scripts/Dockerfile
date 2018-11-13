FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY wait-for.sh wait-for.sh
COPY ${JAR_FILE} app.jar
CMD ["-c", "./wait-for.sh && java -jar /app.jar"]
ENTRYPOINT ["/bin/sh"]