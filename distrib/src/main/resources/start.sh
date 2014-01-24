#!/bin/sh

PWD=`pwd`
java -Dport=8080 -Dstorage.location="${PWD}/database" -Dsettings=conf/settings.json -Dwar=web-apps/ui-monitoring-server-${project.version}.war -jar bin/ui-monitoring-cmdline-${project.version}.jar start > logs/server.log 2>&1
