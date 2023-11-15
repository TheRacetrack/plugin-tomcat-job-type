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
	cd sample/adder && make build && racetrack deploy .

test-sample:
	curl -X POST -H "X-Racetrack-Auth: $(shell racetrack get auth-token)" \
		http://127.0.0.1:7105/pub/job/tomcat-adder/0.0.3/api/v1/perform

# todo add numbers above
perform:
	curl -X POST \
		"http://localhost:7000/pub/job/tomcat-adder/latest/api/v1/perform" \
		-H "Content-Type: application/json" \
		-d '{"numbers": [40, 2]}'

