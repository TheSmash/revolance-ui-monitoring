package com.smash.revolance.ui.parser;

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

import com.smash.revolance.ui.explorer.UserExplorer;
import com.smash.revolance.ui.model.application.Application;
import com.smash.revolance.ui.model.application.ApplicationManager;
import com.smash.revolance.ui.model.bot.Bot;
import com.smash.revolance.ui.model.element.api.Button;
import com.smash.revolance.ui.model.element.api.Element;
import com.smash.revolance.ui.model.element.api.Link;
import com.smash.revolance.ui.model.page.IPage;
import com.smash.revolance.ui.model.page.api.PageBean;
import com.smash.revolance.ui.model.sitemap.SiteMap;
import com.smash.revolance.ui.model.user.User;
import org.apache.commons.exec.OS;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * User: wsmash
 * Date: 30/11/12
 * Time: 20:08
 */
@Ignore
public class BotTest
{
    private static final File userCfg;
    private static final File appCfg;

    private static final String website;
    private static final String index;
    private static final String pageA;
    private static final String pageB;

    private static final String help;

    private static final String BROKEN = "broken.html";

    static
    {
        appCfg = new File( new File( "" ).getAbsoluteFile(), "src/test/config/cfg-app.xml" );
        userCfg = new File( new File( "" ).getAbsoluteFile(), "src/test/config/cfg-users.xml" );

        website = "file://" + new File( new File( "" ).getAbsoluteFile(), "src/test/resources/website" ).getAbsolutePath();

        index = website + "/index.html";
        pageA = website + "/page_A.html";
        pageB = website + "/page_B.html";

        help = website + "/help.html";
    }

    private static User               user;
    private static SiteMap            sitemap;
    private static WebDriver          browser;
    private static Bot                bot;
    private static ApplicationManager manager;
    private static Application        app;

    @BeforeClass
    public static void setUp() throws Exception
    {
        manager = new ApplicationManager( appCfg );
        app = manager.getApplication( "website" );

        assertThat( app.getUserCount(), is( 3 ) );

        user = app.getUser( "super user" );

        setupBrowserForTest( user );

        user.enablePageScreenshot( false );
        user.enablePageElementScreenshot( false );

        user.setFollowButtons( true );
        user.setFollowLinks( true );
        user.setExploreVariantsEnabled( true );

        user.setDomain( website );
        user.setHome( index );

        new UserExplorer( user ).explore();

        bot = user.getBot();
        browser = bot.getBrowser();
        sitemap = user.getSiteMap();
    }

    private static void setupBrowserForFirefox(User user)
    {
        user.setBrowserType( "Firefox" );
    }

    public static void setupBrowserForTest(User user)
    {
        user.setBrowserType( "MockedWebDriver" );
    }

    private static void setupBrowserForChrome(User user)
    {
        user.setBrowserType( "Chrome" );
        user.setDriverPath( getChromeDriverPath() );
        user.setBrowserBinary( "/usr/bin/google-chrome" );
    }

    public static String getChromeDriverPath()
    {
        return new File( new File( "" ).getAbsoluteFile(), "src/test/driver/" + ( OS.isFamilyUnix() ? "unix" : "win" ) + "/chromedriver" + ( OS.isFamilyUnix() ? "" : ".exe" ) ).getAbsolutePath();
    }


    @After
    public void tearDown()
    {
        browser.quit();
    }

    @Test
    public void botShouldExploreAllPages() throws Exception
    {
        Collection<PageBean> pages = sitemap.getPages();
        assertThat( pages.size(), is( 6 ) );
    }

    @Test
    public void botShouldExtractButtonsFromPageContent() throws Exception
    {
        IPage page_B = sitemap.findPageByUrl( pageB ).getInstance();
        List<Element> buttons = page_B.getButtons();

        assertThat( buttons.size(), is( 2 ) );
        assertThat( Button.containsButton( buttons, "button" ), is( true ) );
        assertThat( Button.containsButton( buttons, "index" ), is( true ) );
    }

    @Test
    public void botShouldExtractLinksFromPageContent() throws Exception
    {
        // page_B page check
        IPage page_B = sitemap.findPageByUrl( pageB ).getInstance();
        List<Element> links = page_B.getLinks();
        assertThat( links.size(), is( 3 ) );
        assertThat( Link.containsLink( links, "index" ), is( true ) );
        assertThat( Link.containsLink( links, "page_A" ), is( true ) );
        assertThat( Link.containsLink( links, "help" ), is( true ) );
    }

    @Test
    public void botShouldDetectBrokenLinks() throws Exception
    {
        assertThat( sitemap.getBrokenPages().size(), is( 1 ) );
        assertThat( sitemap.getBrokenLinks().contains( "Broken" ), is( true ) );
        assertThat( sitemap.getBrokenPages().get( 0 ).getUrl(), endsWith( BROKEN ) );
        assertThat( sitemap.getBrokenPages().get( 0 ).getSource().isBroken(), is( true ) );
        ;
    }

    @Test
    public void botShouldNotExploreAnyPageOutOfTheUserDomain() throws Exception
    {
        IPage google = sitemap.findPageByUrl( "www.google.fr" ).getInstance();
        assertThat( google.isExternal(), is( true ) );
        assertThat( google.hasBeenExplored(), is( false ) );
        assertThat( google.getSource().getContent(), is( "Google" ) );
    }


}
