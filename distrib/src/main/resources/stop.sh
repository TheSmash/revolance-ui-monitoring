#!/bin/sh

java -Dport=8080 -jar bin/ui-monitoring-cmdline-${project.version}.jar stop > logs/server.log 2>&1
