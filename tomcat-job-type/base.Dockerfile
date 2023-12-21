FROM gradle:8.4.0-jdk21 as build
WORKDIR /src
COPY ./app ./app
COPY ./prometheus ./prometheus
COPY settings.gradle.kts ./
RUN gradle build


#---------------------------
FROM openjdk:11-ea-17-jre-slim

WORKDIR /src

# This war contains Health component, which will serve /health, /live and /ready, on root endpoint.
COPY --from=build /src/app/build/libs/app.jar ./base.jar

LABEL racetrack-component="job"
