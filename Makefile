
build:
	cd tomcat-job-type &&\
	DOCKER_BUILDKIT=1 docker build \
		-t ghcr.io/theracetrack/racetrack/job-base/tomcat:latest \
		-f base.Dockerfile .

bundle:
	cd tomcat-job-type && \
	racetrack plugin bundle --out=..

install:
	racetrack plugin install *.zip

deploy-sample:
	racetrack deploy sample/adder --force

test-sample:
	curl -X POST -H "X-Racetrack-Auth: $(shell racetrack get auth-token)" \
		-H "Content-Type: application/json" \
		-d '{"numbers": [40, 2]}' \
		$(shell racetrack get pub -q)/job/tomcat-adder/latest/api/v1/perform
