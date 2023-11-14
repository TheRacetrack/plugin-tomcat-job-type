# plugin-tomcat-job-type
A plugin for Racetrack which extends it with Tomcat (Java) Job Type. Currently it runs Tomcat 10.1.12

## Setup
1. Install `racetrack` client and generate ZIP plugin by running `make bundle`.

2. Activate the plugin in Racetrack Dashboard Admin page
  by uploading the zipped plugin file.

## Usage
You can deploy sample Tomcat job by running:
```bash
racetrack deploy sample-tomcat-function
```

You are expected to have a .java file with class that overrides few methods. 