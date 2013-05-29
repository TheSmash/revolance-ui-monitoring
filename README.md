Revolance UI
============

Copyleft

This file is part of Revolance UI Suite.

    Revolance UI Suite is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Revolance UI Suite is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Foobar.  If not, see <http://www.gnu.org/licenses/>.

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
This project is trying to mix the two approch. Thus validating an element of the content should not be too strongly 
linked to his appearance neither to his position. But it should be at first about his textual value and his href.
Functionally the most important thing is that the link is there in the content and properly working. 
Your user is expecting to get redirected toward the appropriate href. And secondly it's also about
having a nice page where the layout is ok. We do want our users to have a good UX through our ui, right?
Well I think this project can help us to reach it!

That's it for the speech guys and now let's have some more technical insight.


Components:
===========


A bot (revolance-ui-explorer) explores all the pages of a web app and then produces a JSon report.
  - At the moment the generated report only contains page, link and buttons parsed.
  - The retrieved data are a collection of pages.
  
A plugin that aggregate a user defined application and produces a ready to use distribution.
  - Usefull for an execution of the bot through the command line interface
      
A gui (revolance-ui-merger) allow the user to load two reports to compare them. 
  - Useful to compare regression or content enforcement given some user rights.

Data Model:
===========

A Page has:
  - an internal id
  - a url
  - a title
  - a width 
  - a height
  - a screenshot (base64 encoded)
  - some content (for now the links & buttons)
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
  - an impl (Link or Button)
      
