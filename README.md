[![Stories in Ready](https://badge.waffle.io/TheSmash/revolance-ui-monitoring.png?label=ready)](https://waffle.io/TheSmash/revolance-ui-monitoring)
[![Stories in Ready](https://badge.waffle.io/TheSmash/revolance-ui-monitoring.png?label=In%20dev)](https://waffle.io/TheSmash/revolance-ui-monitoring)
Revolance UI  [![Build Status](https://travis-ci.org/TheSmash/revolance-ui-monitoring.png)](https://travis-ci.org/TheSmash/revolance-ui-monitoring)
============


Testers prove their value on automation, explorating testing and not on repeting
clicks on buttons to look for broken links.

The process is to validate once and for all pages of the app:
  - the content
  - the layout
  - the title
  - the look
  - the url
  
What is a change in the content?
Well there are a lot of UI automation tools out there. Some rely on the UI while some others don't.

This project is trying to mix the two approches. Thus validating an element of the content should not be too strongly 
linked to his appearance neither to his position. But it should be at first about his textual value and his href.
Functionally the most important thing is that the link is there in the content and properly working. 
Your user is expecting to get redirected toward the appropriate href. And secondly it's also about
having a nice page where the layout is ok. We do want our users to have a good UX through our ui, right?
Well I think this project can help us to fill that purpose!

That's it for the speech guys and now let's have some more technical insight.



# User quick start

  - start => ./start.sh
  - stop => ./stop.sh
  - status => ./status.sh
  
  connect to http://localhost/ui-monitoring-server

#Developer quick start

## Requirements

  - Java 1.7
  - Maven at least 3.04
  - Firefox

## compilation 

  > mvn clean install -DskipTests

## unit testing

  > mvn clean test
  > mvn clean test -Dmaven.surefire.debug

## integration testing

  > mvn clean install
  > mvnDebug clean install
  
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

    smash william / bethesmash@gmail.com
   
   
