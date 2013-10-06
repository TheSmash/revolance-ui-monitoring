Revolance UI http://travis-ci.org/TheSmash/revolance-ui.png
============


This is the repository where the reign of manual QA shorten on the UI testing part.
QA have their value on automation, explorating testing of assembled components and 
certainly not on repeting manual UI regression tests as if they were monkeys.

The process is to validate once and for all pages of the app:
  - the content
  - the layout
  - the title
  - the look
  - the url
  
What is a change in the content?
Well there are a lot of UI automation tool out there. Some rely on the UI while some others don't.
This project is trying to mix the two approches. Thus validating an element of the content should not be too strongly 
linked to his appearance neither to his position. But it should be at first about his textual value and his href.
Functionally the most important thing is that the link is there in the content and properly working. 
Your user is expecting to get redirected toward the appropriate href. And secondly it's also about
having a nice page where the layout is ok. We do want our users to have a good UX through our ui, right?
Well I think this project can help us to fill that purpose!

That's it for the speech guys and now let's have some more technical insight.


Components:
===========


an Explorer (ui-explorer):
  - Parse and explore all the pages of a web app and then produces a JSon report.
  
a Comparator (ui-comparator): 
  - Compare two exploration report files. The output of this comparison says what are the differencies.
  
a Database (ui-database):
  - Store and retrieve the reports.
      
a Server (ui-server) provide a set of Rest APIs to:
  - store and retrieve reports
  - compare pages
  - accept (or not) a change on a page content
  - start / cancel an exploration

The server also provide an HTML5/CSS3 UI over the Rest APIs 

Data Model:
===========

A Page has:
  - an internal id
  - a url
  - a title
  - a width 
  - a height
  - a screenshot (base64 encoded)
  - some content (links / buttons / input fields / texts / images)
  - some variants

The page content is:
  - a list of elements
      
A Page variant is:
  - a url+hash page of an original url page
  - a popup menu dynamically drawn when clicking on a link or button of the original page.
  
The variant content is linked to another page (called the original). Therefore the variant content is only
the added content by comparison with the original page.
      
      
An element is:
  - a button
  - a link
  - some text
  - a field
  - an image
      
An element has:
  - an internal id
  - an x coordinate
  - an y coordinate
  - a width
  - a height
  - a screenshot (base64 encoded)
  - a textual value
  - a state (broken / not broken)
  - an href
  - an impl (Link / Button / Input / Data / Image)
      
Licence & Author
================

    Copyright [2012] [smash william / bethesmash@gmail.com]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
   
   
