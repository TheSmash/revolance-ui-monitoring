/*
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Revolance-UI-Materials-Resources-Website-Ref
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2012 - 2013 RevoLance
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
$(document).ready(function() {
	var day=['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'],
	   month=['January','February','March','April','May','June','July','August','September','October','Novermber','December'];
   SetData();
   function SetData() {
	   var now = new Date();
	   $('.date').html(day[now.getDay()]+', ');
	   $('.date').append(' '+month[now.getMonth()]+' ');
	   $('.date').append(now.getDate()+', ');
	   $('.date').append(now.getFullYear()+' &nbsp; &nbsp; ');
	   hour=now.getHours();
	   minutes=now.getMinutes();
	   if (minutes<10) {minutes='0'+minutes};
	   $('.date').append(hour+':'+minutes);
	}
  	setInterval(SetData,60);

});