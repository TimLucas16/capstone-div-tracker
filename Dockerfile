FROM openjdk:17

ENV ENVIRONMENT=prod

LABEL maintainer="super_grobi@hotmail.com"

ADD backend/target/app.jar app.jar

CMD [ "sh", "-c", "java -Dserver.port=$PORT -jar /app.jar" ]