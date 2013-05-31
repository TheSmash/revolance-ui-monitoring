package com.smash.revolance.ui.explorer.application.secured;

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

import com.smash.revolance.ui.explorer.application.Application;
import com.smash.revolance.ui.explorer.application.ApplicationManager;
import com.smash.revolance.ui.explorer.page.api.ElementNotFound;
import com.smash.revolance.ui.explorer.page.api.Page;
import com.smash.revolance.ui.explorer.user.User;
import com.smash.revolance.ui.explorer.bot.BrowserFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * User: wsmash
 * Date: 26/01/13
 * Time: 17:50
 */
public class SecuredApplicationTest
{
    private static String      target;
    private static Application app;

    private static File appCfg;

    private static final String             domain;
    private static       ApplicationManager manager;

    private static final String securedWebsite;

    private static final String HOME;

    private static final String login;

    private static final String changePasswd;

    static
    {
        securedWebsite = new File( new File( "" ).getAbsoluteFile(), "src/test/resources/secured-website" ).getAbsolutePath();

        target = new File( new File( "" ).getAbsoluteFile(), "target" ).getAbsolutePath();

        appCfg = new File( new File( "" ).getAbsoluteFile(), "src/test/config/cfg-app.xml" );

        domain = "file://" + securedWebsite;
        HOME = domain + "/home.html";
        login = domain + "/login.html";
        changePasswd = domain + "/change-passwd.html";

    }

    private static Application app2;
    private static User        administrator;
    private static User        user_A;
    private static User        user_B;

    @BeforeClass
    public static void setUp() throws Exception
    {
        manager = new ApplicationManager( appCfg );
        app = manager.getApplication( "website" );

        app.setReportFolder( target );
        app.setUsersHome( login );
        app.setDomain( domain );

        app.setExcludedLink( "link-1" );
        app.setExcludedLink( "link-2" );

        app.setExcludedButton( "button-1" );
        app.setExcludedButton( "button-2" );

        assertThat( app.getId(), is( "website" ) );
        assertThat( app.getReportFolder(), is( target + "/" + app.getId() ) );
        assertThat( app.getUserCount(), is( 3 ) );

        administrator = app.getUser( "super user" );
        setupBrowserForFirefox( administrator );
        checkUser( administrator );

        user_A = app.getUser( "user_A" );
        setupBrowserForFirefox( user_A );
        checkUser( user_A );

        user_B = app.getUser( "user_B" );
        setupBrowserForFirefox( user_B );
        checkUser( user_B );
    }

    private static void setupBrowserForFirefox(User user)
    {
        user.setBrowserType( "Firefox" );
    }

    private static void checkUser(User user)
    {
        assertNotNull( user );
        assertThat( user.getBrowserHeight(), is( 600 ) );
        assertThat( user.getBrowserWidth(), is( 800 ) );
        assertThat( user.isPageScreenshotEnabled(), is( true ) );
        assertThat( user.isPageElementScreenshotEnabled(), is( false ) );
        assertThat( user.wantsToFollowLinks(), is( true ) );
        assertThat( user.wantsToFollowButtons(), is( true ) );
        assertThat( user.wantsToExploreVariants(), is( false ) );
    }

    @AfterClass
    public static void tearDown() throws Exception
    {
        for ( User user : app.getUsers() )
        {
            try
            {
                if ( user.isBrowserActive() )
                {
                    user.getBrowser().quit();
                }
            }
            catch (Exception e)
            {
                // Ignore gently
            }
        }
    }

    @Test
    public void testerShouldDetectEnforcementOnASecuredWebsite() throws Exception, BrowserFactory.InstanciationError, ElementNotFound
    {
        app.explore();

        Page adminPage = administrator.getSiteMap().findPageByUrl( HOME ).getInstance();

        assertThat( "Wrong number of buttons for administrator", adminPage.getButtons().size(), is( 2 ) );
        assertThat( "Wrong number of links for administrator",   adminPage.getLinks().size(), is( 2 ) );
        assertThat( "Missing link 'link-1' for administrator",   adminPage.getLink( "link-1" ), notNullValue() );
        assertThat( "Missing link 'link-2' for administrator",   adminPage.getLink( "link-2" ), notNullValue() );
        assertThat( "Missing link 'button-1' for administrator", adminPage.getButton( "button-1" ), notNullValue() );
        assertThat( "Missing link 'button-2' for administrator", adminPage.getButton( "button-2" ), notNullValue() );

        Page userAPage = user_A.getSiteMap().findPageByUrl( HOME ).getInstance();

        assertThat( "Wrong number of links for user_A",   userAPage.getLinks().size(), is( 2 ) );
        assertThat( "Wrong number of buttons for user_A", userAPage.getButtons().size(), is( 0 ) );
        assertThat( "Missing link 'link-1' for user_A",   userAPage.getLink( "link-1" ), notNullValue() );
        assertThat( "Missing link 'link-2' for user_A",   userAPage.getLink( "link-2" ), notNullValue() );

        Page userBPage = user_B.getSiteMap().findPageByUrl( HOME ).getInstance();

        assertThat( "Wrong number of links for user_B",   userBPage.getLinks().size(), is( 0 ) );
        assertThat( "Wrong number of buttons for user_B", userBPage.getButtons().size(), is( 2 ) );
        assertThat( "Missing link 'button-1' for user_A", userBPage.getButton( "button-1" ), notNullValue() );
        assertThat( "Missing link 'button-2' for user_A", userBPage.getButton( "button-2" ), notNullValue() );

    }

}
