#!/bin/sh

java -Dport=8080 -jar bin/ui-monitoring-cmdline-${project.version}.jar status > /dev/null 2>&1
