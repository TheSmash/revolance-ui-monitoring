package com.smash.revolance.ui.explorer.helper;

/*
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
        along with Revolance UI Suite.  If not, see <http://www.gnu.org/licenses/>.
*/

import com.smash.revolance.ui.explorer.page.IPage;
import com.smash.revolance.ui.explorer.user.User;
import com.smash.revolance.ui.explorer.bot.Bot;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;

/**
 * User: wsmash
 * Date: 23/01/13
 * Time: 12:06
 */
public class UserHelper
{
    public static void browseTo(IPage page)
    {
        browseTo( page, false );
    }

    public static void browseTo(IPage page, boolean force)
    {
        if ( page != null )
        {
            final String url = page.getUrl();
            try
            {
                final User user = page.getUser();
                //TODO: mark the time so that we know how long it took to load the page

                if( browseTo( user, url, force ) )
                {
                    Bot bot = page.getUser().getBot();

                    // Handle URL rewritting
                    page.setUrl( bot.getCurrentUrl() );
                    page.setTitle( bot.getCurrentTitle() );

                    System.out.println( user.getId() + " is browsing page: " + page.getTitle() );
                    user.getSiteMap().addPage(page);

                    awaitPageLoaded( page );
                }
            }
            catch (Exception e)
            {
                System.err.println(e);
                System.err.println("Page: " + url + " is broken.");
            }
        }
    }

    public static void  awaitPageLoaded(IPage page) throws Exception
    {
        page.getApplication().awaitPageLoaded( page );
    }

    public static void browse(IPage page) throws Exception
    {
        browseTo(page);

        if (!page.hasBeenParsed())
        {
            parseContent(page);
            page.setParsed( true );
        }
    }

    public static void parseContent(IPage page) throws Exception
    {
        if( ! page.hasBeenParsed() )
        {
            User user = page.getUser();

            if ( user.getApplication().isPageBroken( page ) )
            {
                page.setBroken( true );
            }
            else if ( user.getApplication().isUnauthorized( page ) )
            {
                page.setAuthorized( false );
            }
            else
            {
                if ( user.isPageScreenshotEnabled() && page.getCaption().isEmpty() )
                {
                    page.takeScreenShot();
                }

                page.getContent();

                if ( !BotHelper.rightDomain( page ) )
                {
                    System.out.println( "Url: " + page.getUrl() + " is out of the domain: " + page.getSiteMap().getDomain() );
                }
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
        if ( !UrlHelper.areEquivalent( oldUrl, url ) || force)
        {
            handleAlert( user );
            doBrowse( user, url );
            return true;
        }
        else
        {
            return false;
        }
    }

    private static void doBrowse(User user, String url) throws Exception
    {
        // Acting on the alert window could move to another url
        if( !user.getBot().getCurrentUrl().contentEquals( url ) )
        {
            try
            {
                user.getBrowser().navigate().to( url );
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

    public static boolean hasBeenExplored(User user, String url) throws Exception
    {
        return user.getSiteMap().hasBeenExplored( url );
    }

    public static void handleAlert(User user) throws Exception
    {
        if(BotHelper.isAlertPresent( user ))
        {
            BotHelper.handleApplicationAlertPopup( user, user.getBrowser().switchTo().alert() );
        }
    }
}
