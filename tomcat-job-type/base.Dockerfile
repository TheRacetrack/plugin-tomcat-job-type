FROM tomcat:10.1.15-jre21-temurin

# This war contains Health component, which will serve /health, /live and /ready, on root endpoint.
ADD "app/build/libs/app.war" "${CATALINA_HOME}/webapps/ROOT.war"

LABEL racetrack-component="job"
