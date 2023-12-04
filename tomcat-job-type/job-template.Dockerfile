FROM gradle:8.4.0-jdk21 as build
WORKDIR /src
COPY . .
RUN gradle war

#---------------------------
FROM {{ base_image }}

{% for env_key, env_value in env_vars.items() %}
ENV {{ env_key }} "{{ env_value }}"
{% endfor %}

RUN sed -i 's/port="8080"/port="7000"/' ${CATALINA_HOME}/conf/server.xml


{% if manifest.system_dependencies and manifest.system_dependencies|length > 0 %}
RUN apk add \
    {{ manifest.system_dependencies | join(' ') }}
{% endif %}


# Add the user war file, put it at path which will specify the servlet path.
# Quoting Tomcat doc: "Contexts can be multiple levels deep, so if you deploy a WAR file called demo#v1#myfeature.war
# it will be made available under the demo/v1/myfeature context."
COPY --from=build /src/app/build/libs/app.war "${CATALINA_HOME}/webapps/pub#job#{{ manifest.name }}#{{ manifest.version }}.war"

RUN cp -r "${CATALINA_HOME}/webapps/swagger-ui/" "${CATALINA_HOME}/webapps/pub#job#{{ manifest.name }}#{{ manifest.version }}#swagger-ui/"

ENV JOB_NAME "{{ manifest.name }}"
ENV JOB_VERSION "{{ manifest.version }}"
ENV GIT_VERSION "{{ git_version }}"
ENV DEPLOYED_BY_RACETRACK_VERSION "{{ deployed_by_racetrack_version }}"
ENV JOB_TYPE_VERSION "{{ job_type_version }}"

# The CMD for this image is specified in base tomcat image. It is sth like CMD ["catalina.sh", "run"]
