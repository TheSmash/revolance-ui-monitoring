#!/bin/sh

java -jar bin/${project.parent.artifactId}-library-${project.version}.jar explore config/cfg-app.xml
