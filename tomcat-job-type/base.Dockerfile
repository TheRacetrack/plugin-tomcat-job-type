FROM gradle:8.4.0-jdk21 as build
WORKDIR /src
COPY ./app ./app
COPY ./prometheus ./prometheus
COPY settings.gradle.kts ./
RUN gradle war


#---------------------------
FROM tomcat:10.1.15-jre21-temurin

# This war contains Health component, which will serve /health, /live and /ready, on root endpoint.
COPY --from=build /src/app/build/libs/app.war "${CATALINA_HOME}/webapps/ROOT.war"

# It won't run from this folder, but we need it in image, so that job-template can put it
# in appropriate place, once it knows the manifest.name
COPY ./swagger-ui/ "${CATALINA_HOME}/webapps/swagger-ui/"

# todo put it into ROOT so that /metrics is served as Prometheus expects.
COPY --from=build /src/prometheus/build/libs/prometheus.war "${CATALINA_HOME}/webapps/prometheus.war"


LABEL racetrack-component="job"
