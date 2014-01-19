
Welcome to RevoLance UI Monitoring!
============

[![Build Status](https://travis-ci.org/TheSmash/revolance-ui-monitoring.png)](https://travis-ci.org/TheSmash/revolance-ui-monitoring)


[![Stories in Ready](https://badge.waffle.io/TheSmash/revolance-ui-monitoring.png?label=ready)](https://waffle.io/TheSmash/revolance-ui-monitoring)
[![Stories in Dev](https://badge.waffle.io/TheSmash/revolance-ui-monitoring.png?label=in%20dev)](https://waffle.io/TheSmash/revolance-ui-monitoring)
[![Stories in Test](https://badge.waffle.io/TheSmash/revolance-ui-monitoring.png?label=in%20test)](https://waffle.io/TheSmash/revolance-ui-monitoring)
[![Stories in Done](https://badge.waffle.io/TheSmash/revolance-ui-monitoring.png?label=done)](https://waffle.io/TheSmash/revolance-ui-monitoring)



# Want to know more?

> http://thesmash.github.io/revolance-ui-monitoring/ui-monitoring.html


# Booting the distribution

> ./start.sh and then connect to http://localhost:8080/ui-monitoring-server

# Developer starting guide

## Requirements

  - Java 1.7
  - Maven at least 3.04
  - Firefox

## Compile

`mvn clean install -DskipTests`

## Test

* unit testing

`mvn clean test`
`mvn clean test -Dmaven.surefire.debug`

* integration testing

`mvn clean install`
`mvnDebug clean install`

# How to contribute

  * Send a mail at bethesmash [at] gmail [dot] com
  * Then have a look at the taskboard and choose something that is ready to be implemented: [![Stories in Ready](https://badge.waffle.io/TheSmash/revolance-ui-monitoring.png?label=ready)](https://waffle.io/TheSmash/revolance-ui-monitoring)
  
# Architecture

  * base components : model + comparator + explorer + database
  * war  : base components + explorer + server
  * distrib : war + cmdline
  * materials : test usage or commons library between test & production code
  
# Testing vision

  * each modules has his unit tests
  * the integration tests are run on the war directly passing through the UI and with bdd scenarios
  * the smoke tests checks that the scripting and the necessary libraries are well packaged (to be run locally and not in travis ci)



Author
======

> smash william / bethesmash [at] gmail [dot] com
   
   
