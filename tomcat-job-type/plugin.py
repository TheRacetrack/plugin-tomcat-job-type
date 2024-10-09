class Plugin:
    def job_types(self) -> dict[str, dict]:
        plugin_version: str = getattr(self, 'plugin_manifest').version
        return {
            f'tomcat10-jdk17:{plugin_version}': {
                'images': [
                    {
                        'source': 'jobtype',
                        'dockerfile_path': 'job-template.Dockerfile',
                        'template': True,
                    },
                ],
            },
        }
