TAG ?= 0.0.2

build:
	cd tomcat-job-type &&\
	DOCKER_BUILDKIT=1 docker build \
		-t ghcr.io/theracetrack/racetrack/job-base/tomcat:latest \
		-f base.Dockerfile .

bundle:
	cd tomcat-job-type && \
	make build && \
	racetrack plugin bundle --plugin-version=${TAG} --out=..

install:
	racetrack plugin install *.zip

deploy-sample:
	racetrack delete tomcat-adder --version 0.0.3
	cd sample/adder && make build && racetrack deploy .

test-sample:
	curl -X POST -H "X-Racetrack-Auth: $(shell racetrack get auth-token)" \
		-H "Content-Type: application/json" \
		-d '{"numbers": [40, 2]}' \
		http://127.0.0.1:7105/pub/job/tomcat-adder/0.0.3/api/v1/perform
