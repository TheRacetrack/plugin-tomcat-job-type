FROM gradle:8.4.0-jdk21 as build
WORKDIR /src
COPY . .
RUN gradle build

#---------------------------
FROM {{ base_image }}

{% for env_key, env_value in env_vars.items() %}
ENV {{ env_key }} "{{ env_value }}"
{% endfor %}

{% if manifest.system_dependencies and manifest.system_dependencies|length > 0 %}
RUN apk add \
    {{ manifest.system_dependencies | join(' ') }}
{% endif %}


ENV JOB_NAME "{{ manifest.name }}"
ENV JOB_VERSION "{{ manifest.version }}"
ENV GIT_VERSION "{{ git_version }}"
ENV DEPLOYED_BY_RACETRACK_VERSION "{{ deployed_by_racetrack_version }}"
ENV JOB_TYPE_VERSION "{{ job_type_version }}"


WORKDIR /app
COPY --from=build /src/app/build/libs/app.jar ./

# Now there are two Tomcat embedded servers, one in app.jar, one in base.jar
# If they could be run together, that would be best.
# However I'm afraid that the base sources and user sources will need to be copied in single place,
# where they will form single codebase, and only then, after compiling with gradle

CMD ["java", "-cp", "app.jar", "Main"]