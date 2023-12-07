FROM gradle:8.4.0-jdk21 as build
WORKDIR /src
COPY . .
RUN gradle war

#---------------------------
FROM {{ base_image }}

{% for env_key, env_value in env_vars.items() %}
ENV {{ env_key }} "{{ env_value }}"
{% endfor %}

RUN apt update && apt install -y unzip

RUN sed -i 's/port="8080"/port="7000"/' ${CATALINA_HOME}/conf/server.xml


{% if manifest.system_dependencies and manifest.system_dependencies|length > 0 %}
RUN apk add \
    {{ manifest.system_dependencies | join(' ') }}
{% endif %}


COPY --from=build /src/app/build/libs/app.war /tmp/app.war
ENV JOB_FOLDER="${CATALINA_HOME}/webapps/pub#job#{{ manifest.name }}#{{ manifest.version }}"

# Take the user war file, put it at path which will specify the servlet path.
# Quoting Tomcat doc: "Contexts can be multiple levels deep, so if you deploy a WAR file called demo#v1#myfeature.war
# it will be made available under the demo/v1/myfeature context."
# This time we have to unzip it manually, so that the folder is ready for the addition of swagger files.
RUN unzip /tmp/app.war -d "$JOB_FOLDER"

# Swagger files are served from the same path.
RUN cp -rv ${CATALINA_HOME}/webapps/swagger-ui/* "$JOB_FOLDER"

# Put the root.war under public url so that /swagger endpoint of Swagger servlet can be accessed by swagger-ui, and point it.
RUN unzip -o "${CATALINA_HOME}/webapps/ROOT.war" -d "$JOB_FOLDER" && \
    sed -i 's/job_name/{{ manifest.name }}/' "$JOB_FOLDER/swagger-initializer.js" && \
    sed -i 's/job_version/{{ manifest.version }}/' "$JOB_FOLDER/swagger-initializer.js"

ENV JOB_NAME "{{ manifest.name }}"
ENV JOB_VERSION "{{ manifest.version }}"
ENV GIT_VERSION "{{ git_version }}"
ENV DEPLOYED_BY_RACETRACK_VERSION "{{ deployed_by_racetrack_version }}"
ENV JOB_TYPE_VERSION "{{ job_type_version }}"

# The CMD for this image is specified in base tomcat image. It is sth like CMD ["catalina.sh", "run"]
