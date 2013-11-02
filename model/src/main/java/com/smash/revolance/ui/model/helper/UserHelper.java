package com.smash.revolance.ui.model.helper;

/*
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Revolance-UI-Model
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

import com.smash.revolance.ui.model.bot.Bot;
import com.smash.revolance.ui.model.page.api.Page;
import com.smash.revolance.ui.model.user.User;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;

/**
 * User: wsmash
 * Date: 23/01/13
 * Time: 12:06
 */
public class UserHelper
{
    public static void browseTo(Page page)
    {
        browseTo( page, false );
    }

    public static void browseTo(Page page, boolean force)
    {
        if ( page != null )
        {
            String url = page.getUrl();
            try
            {
                final User user = page.getUser();

                if ( browseTo( user, url, force ) )
                {
                    Bot bot = page.getUser().getBot();

                    // Handle URL rewritting
                    page.setUrl( bot.getCurrentUrl() );
                    page.setTitle( bot.getCurrentTitle() );

                    user.log( user.getId() + " is browsing page: " + page.getTitle() );
                    user.getSiteMap().addPage( page );

                }
            }
            catch (Exception e)
            {
                page.getUser().log( e.getMessage() );
                page.getUser().log( "Page: " + url + " is broken." );
            }
        }
    }

    public static boolean browseTo(User user, String url) throws Exception
    {
        return browseTo( user, url, false );
    }

    public static boolean browseTo(User user, String url, boolean force) throws Exception
    {
        String oldUrl = getCurrentUrl( user );
        if ( !UrlHelper.areEquivalent( oldUrl, url ) || force )
        {
            handleAlert( user );
            doBrowse( user, url );
            return true;
        } else
        {
            return false;
        }
    }

    private static void doBrowse(User user, String url) throws Exception
    {
        WebDriver browser = user.getBrowser(); // instanciate the navigator is needed
        // Acting on the alert window could move to another url
        if ( !user.getBot().getCurrentUrl().contentEquals( url ) )
        {
            try
            {
                browser.navigate().to( url );
                handleAlert( user );
            }
            catch (UnhandledAlertException e)
            {
                handleAlert( user );
                doBrowse( user, url );
            }
        }
    }

    private static String getCurrentUrl(User user) throws Exception
    {
        WebDriver browser = user.getBrowser();
        try
        {
            return browser.getCurrentUrl();
        }
        catch (UnhandledAlertException e)
        {
            UserHelper.handleAlert( user );
            return browser.getCurrentUrl();
        }
    }

    public static void handleAlert(User user) throws Exception
    {
        if ( BotHelper.isAlertPresent( user ) )
        {
            BotHelper.handleApplicationAlertPopup( user, user.getBrowser().switchTo().alert() );
        }
    }
}
