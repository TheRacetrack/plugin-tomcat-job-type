FROM gradle:8.4.0-jdk21 as build-base
WORKDIR /src
COPY --from=jobtype ./app ./app
COPY --from=jobtype ./prometheus ./prometheus
COPY --from=jobtype settings.gradle.kts ./
RUN gradle war

#---------------------------
FROM gradle:8.4.0-jdk21 as build-job
WORKDIR /src
COPY . .
RUN gradle war

#---------------------------
FROM tomcat:10.1.15-jre21-temurin

COPY --from=jobtype server.xml "${CATALINA_HOME}/conf/server.xml"

# This war contains Health component, which will serve /health, /live and /ready, on root endpoint.
COPY --from=build-base /src/app/build/libs/app.war "${CATALINA_HOME}/webapps/ROOT.war"

COPY --from=build-base /src/prometheus/build/libs/prometheus.war "${CATALINA_HOME}/webapps/prometheus.war"

# It won't run from this folder, but we need it in image, so that job-template can put it
# in appropriate place, once it knows the manifest.name
COPY --from=jobtype ./swagger-ui/ "${CATALINA_HOME}/webapps/swagger-ui/"

LABEL racetrack-component="job"

{% for env_key, env_value in env_vars.items() %}
ENV {{ env_key }} "{{ env_value }}"
{% endfor %}

RUN apt update && apt install -y unzip

{% if manifest.system_dependencies and manifest.system_dependencies|length > 0 %}
RUN apk add \
    {{ manifest.system_dependencies | join(' ') }}
{% endif %}

COPY --from=build-job /src/app/build/libs/app.war /tmp/app.war
ENV JOB_FOLDER="${CATALINA_HOME}/webapps/pub#job#{{ manifest.name }}#{{ manifest.version }}"
ENV ROOT_FOLDER="${CATALINA_HOME}/webapps/ROOT"

# Take the user war file, put it at path which will specify the servlet path.
# Quoting Tomcat doc: "Contexts can be multiple levels deep, so if you deploy a WAR file called demo#v1#myfeature.war
# it will be made available under the demo/v1/myfeature context."
# This time we have to unzip it manually, so that the folder is ready for the addition of swagger files.
# Swagger and prometheus metrics are also served from the same path.
RUN unzip /tmp/app.war -d "$JOB_FOLDER" && \
    unzip -o "${CATALINA_HOME}/webapps/ROOT.war" -d "$JOB_FOLDER" && \
    unzip -o "${CATALINA_HOME}/webapps/prometheus.war" -d "$JOB_FOLDER" && \
    cp -rv ${CATALINA_HOME}/webapps/swagger-ui/* "$JOB_FOLDER" && \
    sed -i 's/job_name/{{ manifest.name }}/' "$JOB_FOLDER/swagger-initializer.js" && \
    sed -i 's/job_version/{{ manifest.version }}/' "$JOB_FOLDER/swagger-initializer.js"

# These servlets also have to be served at root endpoint.
RUN unzip -n "${CATALINA_HOME}/webapps/ROOT.war" -d "$ROOT_FOLDER" && \
    unzip -n "${CATALINA_HOME}/webapps/prometheus.war" -d "$ROOT_FOLDER" && \
    unzip -n /tmp/app.war -d "$ROOT_FOLDER"

ENV JOB_NAME "{{ manifest.name }}"
ENV JOB_VERSION "{{ manifest.version }}"
ENV GIT_VERSION "{{ git_version }}"
ENV DEPLOYED_BY_RACETRACK_VERSION "{{ deployed_by_racetrack_version }}"
ENV JOB_TYPE_VERSION "{{ job_type_version }}"

# The CMD for this image is specified in base tomcat image. It is sth like CMD ["catalina.sh", "run"]
RUN mkdir -p /usr/local/tomcat/conf/Catalina/localhost && chmod 777 /usr/local/tomcat /usr/local/tomcat/webapps && useradd --create-home --uid 100000 job
USER 100000
