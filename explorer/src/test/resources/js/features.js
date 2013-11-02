/*
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Revolance-UI-Explorer
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

function createCookie(c_name)
{
	var today = new Date(), expires = new Date();
        expires.setTime(today.getTime() + (24*3600)); // One day expiry
//	alert("Creating cookie");
	document.cookie = c_name+"=; expires=" + escape(expires);
}

function loggoff()
{
	document.cookie = "";
}

function setValue(name, value)
{
	setCookieValue("bot-test", name, value);
}

function buildCookieData(name, value)
{
	if(name=="")
	{
		return "";	
	}
	else
	{
		return name+"="+escape(value);
	}
}

function hasData(c_name)
{
	var pCOOKIES = document.cookie.split(';');
	for(bb = 0; bb < pCOOKIES.length; bb++)
	{
		var NmeVal  = pCOOKIES[bb].split('=');
		if(NmeVal[0] == c_name)
		{
			var values = NmeVal[1].split('|');
			return values.length>0;
		}
	}
	return null;
}

function setCookieValue(c_name, name, value)
{
	var found = false;	
	var cookie = "";
	var pCOOKIES = document.cookie.split(';');
	for(bb = 0; bb < pCOOKIES.length; bb++)
	{
		var subCookie = "";
		var NmeVal  = pCOOKIES[bb].split('=');
		if(NmeVal[0] == c_name)
		{
			if(subCookie != "")
			{
				subCookie += ";";			
			}
			var subSubCookie = "";
			var data = pCOOKIES[bb].substring(c_name.length+1, pCOOKIES[bb].length);
			if(data.length > 0)
			{			
				var values = data.split('|');		
				for(vv = 0; vv < values.length; vv++)
				{
					if(subSubCookie != "")
					{
						subSubCookie += "|";			
					}
		
					var val  = values[vv].split("=");
					if( val.length == 2 )
					{
						if(val[0] == name)
						{
							found = true;
							val[1] = escape(value);
						}
						subSubCookie += buildCookieData(val[0], val[1]);
					}
				}
			}
			if(!found)
			{
				if(subSubCookie != "")
				{
					subSubCookie += "|";			
				}
				subSubCookie += buildCookieData(name, value);		
			}
			subCookie += subSubCookie;
		}
		cookie += NmeVal[0]+"="+subCookie + ";";
	}
	document.cookie = cookie;
}

function getValue(name)
{
	return getCookieValue("bot-test", name);
}

function getCookieValue(c_name, name)
{
	var pCOOKIES = document.cookie.split(';');
	for(bb = 0; bb < pCOOKIES.length; bb++)
	{
		var NmeVal  = pCOOKIES[bb].split('=');
		if(NmeVal[0] == c_name)
		{
			var data = pCOOKIES[bb].substring(c_name.length+1, pCOOKIES[bb].length);
			if(data.length > 0)
			{	
				var values = data.split('|');
				for(vv = 0; vv < values.length; vv++)
				{
					var val  = values[vv].split("=");
					if(val.length == 2)
					{
						if(val[0] == name)
						{
							return val[1];
						}
					}
				}
			}
		}
	}
//	alert("getCookie(" + name + ") = null");
        return null;
}

function cookieExists(c_name)
{
	var pCOOKIES = document.cookie.split(';');
	for(bb = 0; bb < pCOOKIES.length; bb++)
	{
		var NmeVal  = pCOOKIES[bb].split('=');
		if(NmeVal[0] == c_name)
		{
			return true;
		}
	}
        return false;
}

function setUsername(username)
{
	setValue("username", username);	
}

function getUsername()
{
	return getValue("username");
}

function setLoggedIn()
{
	setValue("logged", "true");
}

function setLoggedOff()
{
	setValue("logged", "false");
}

function getUsernameFromForm()
{
    return document.forms["login-form"]["username"].value;
}

function getPasswordFromForm()
{
    return document.forms["change-passwd-form"]["newPasswd"].value;
}

function hasUsernameFromForm()
{
    username = getUsernameFromForm();
    return username !== "" && username !== undefined;
}

function hasPasswordFromForm()
{
    passwd = getPasswordFromForm();
    return passwd != "" && passwd != undefined;
}

function hasPrivillege()
{
	return getValue("enforcement") != null;
}

function redirectTo( rel_page )
{	
	try
	{
		var path = document.URL.substring(0, document.URL.lastIndexOf('/')) + '/' + rel_page;
//		alert("page=" + path);
	 	window.location.href = path;
	}
	catch(e)
	{
	 	var pathArray = window.location.href.split( '/' );
		var path = "";
		for ( i = 0; i < pathArray.length-1; i++ ) 
		{
			  path += pathArray[i];
			  path += "/";
		}
		path += rel_page;
//		alert("page=" + path);
		window.location.href = path;
	}
}

function redirectToHome()
{
	var home = getHomePage();
//	alert("redirection to home page: " + home);	
    	redirectTo( home );
}

function redirectToLogin()
{
//	alert("redirection to login page");
	redirectTo( "login.html" );
}

function redirectToChangePasswd()
{
//	alert("redirection to change password page");
	redirectTo( "change-passwd.html" );
}

function setHasChangedPasswd()
{	
	setValue("passwdChanged", "true");
}

function hasChangedPasswd()
{
	return getValue("passwdChanged") === "true";
}

function hasLoggedIn()
{
	return getValue("logged") === "true";
}

function setPrivillege(privillege)
{
	setValue("enforcement", privillege);
}

function getPrivillege()
{
	if( hasLoggedIn( ) )
	{
		return getValue("enforcement");
	}
	return "zero";
}

function createPrivillege(username)
{
	if( username == "administrator" )
	{
		setPrivillege("all");
	}
	else if( username === "user_A" )
	{
		setPrivillege("navigation");
	}
	else if( username === "user_B" )
	{
		setPrivillege("action");	
	}
}

function pageIs(page_url)
{
	return window.location.href.indexOf(page_url) >= 0;		
}

function getHomePage()
{
	return getValue("home");
}

function setHomePage( )
{
	setValue("home", "home.html");
}

function showNavigation()
{
    var menu = '<ul><li><a href="#">link-1</a></li><li><a href="#">link-2</a></li></ul>';
	var div = document.createElement("div");
	div.innerHTML = menu;
	getBody().appendChild(div);
}

function showActions()
{
	var actions = '<input type="button" value="button-1" /><button type="button" >button-2</button>';
	var div = document.createElement("div");
	div.innerHTML = actions;
	getBody().appendChild(div);
}

function showGreedings()
{
	var greedings = "Greedings dear: " + getUsername();
	var div = document.createElement("div");
	div.innerHTML = greedings;
	getBody().appendChild(div);
}

function getBody()
{
	return document.body;
}

function enforceContent(privilege)
{
//	alert("enforcing page content for privilege " + privilege);

	showGreedings();

	if(privilege === "all")
	{
		showNavigation();
		showActions();
	}
	else if(privilege === "navigation")
	{
		showNavigation();
	}
	else if(privilege === "action")
	{
		showActions();
	}
}

