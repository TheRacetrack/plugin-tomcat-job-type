from __future__ import annotations
from pathlib import Path


class Plugin:
    def job_types(self) -> dict[str, tuple[Path, Path]]:
        """
        Job types provided by this plugin
        :return dict of job type name (with version) -> (base image path, dockerfile template path)
        """
        return {
            f'tomcat10-jdk17:{self.plugin_manifest.version}': (
                self.plugin_dir / 'base.Dockerfile',
                self.plugin_dir / 'job-template.Dockerfile',
            ),
        }

