TAG ?= 0.0.1

# runs the tomcat directly on host
run:
	cd tomcat-job-type/tomcat_wrapper &&\
	JOB_NAME=tomcat-function JOB_VERSION=${TAG} java run . # todo

perform:
	curl -X POST \
		"http://localhost:7000/pub/job/tomcat-adder/latest/api/v1/perform" \
		-H "Content-Type: application/json" \
		-d '{"numbers": [40, 2]}'

build:
	cd tomcat-job-type &&\
	DOCKER_BUILDKIT=1 docker build \
		-t ghcr.io/theracetrack/racetrack/job-base/tomcat:latest \
		-f base.Dockerfile .

bundle:
	cd tomcat-job-type &&\
	racetrack plugin bundle --plugin-version=${TAG} --out=..

deploy-sample:
	cd sample/adder && make build && racetrack deploy .

install:
	racetrack plugin install *.zip
