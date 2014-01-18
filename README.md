[![Stories in Ready](https://badge.waffle.io/TheSmash/revolance-ui-monitoring.png?label=ready)](https://waffle.io/TheSmash/revolance-ui-monitoring)
[![Stories in Dev](https://badge.waffle.io/TheSmash/revolance-ui-monitoring.png?label=in%20dev)](https://waffle.io/TheSmash/revolance-ui-monitoring)
[![Stories in Test](https://badge.waffle.io/TheSmash/revolance-ui-monitoring.png?label=in%20test)](https://waffle.io/TheSmash/revolance-ui-monitoring)
[![Stories in Done](https://badge.waffle.io/TheSmash/revolance-ui-monitoring.png?label=done)](https://waffle.io/TheSmash/revolance-ui-monitoring)
Revolance UI  [![Build Status](https://travis-ci.org/TheSmash/revolance-ui-monitoring.png)](https://travis-ci.org/TheSmash/revolance-ui-monitoring)

============

Welcome to the RevoLance initiative!

-> [![RevoLance](http://thesmash.github.io/revolance-ui-monitoring/images/logo-cutted.png)](http://thesmash.github.io/revolance-ui-monitoring) <-

# Want to know more?

> http://thesmash.github.io/revolance-ui-monitoring/ui-monitoring.html


# User quick start

> ./start.sh and then connect to http://localhost:8080/ui-monitoring-server

#Developer quick start

## Requirements

  - Java 1.7
  - Maven at least 3.04
  - Firefox

## compilation 

`mvn clean install -DskipTests`

## unit testing

`mvn clean test`
`mvn clean test -Dmaven.surefire.debug`

## integration testing

`mvn clean install`
`mvnDebug clean install`
  
# From Libraries to the Distribution

  * base components : model + explorer + database
  * war  : base components + explorer + server
  * distrib : war + cmdline + distrib
  
# Testing vision

  * each modules has his unit tests
  * the integration tests are run on the war directly passing through the UI and with bdd scenarios
  * the smoke tests checks that the scripting and the necessary libraries are well packaged



Author
======

> smash william / bethesmash@gmail.com
   
   
