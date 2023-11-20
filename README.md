# plugin-tomcat-job-type
A plugin for Racetrack which extends it with Tomcat (Java) Job Type. Used stack:
- Tomcat 10.1.12
- gradle 8.4.0
- java version: 17

## Setup
1. Install `racetrack` client and generate ZIP plugin by running `make bundle`.

2. Activate the plugin in Racetrack Dashboard Admin page
  by uploading the zipped plugin file.

## Usage
You can deploy sample Tomcat job by running:
```bash
racetrack deploy sample/sample
```

You are expected to have a .java file with class that overrides few methods. For details, see `docs/job_tomcat.md`
