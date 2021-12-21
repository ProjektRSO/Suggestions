FROM adoptopenjdk:15-jre-hotspot

RUN mkdir /app

WORKDIR /app

ADD  ./api/target/suggestions-api-1.0-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java", "-jar", "suggestions-api-1.0-SNAPSHOT.jar"]
#ENTRYPOINT ["java", "-jar", "suggestions-api-1.0-SNAPSHOT.jar"]
#CMD java -jar suggestions-api-1.0-SNAPSHOT.jar
