package com.smash.revolance.ui.comparator;

import com.smash.revolance.ui.materials.TestConstants;
import com.smash.revolance.ui.model.application.Application;
import com.smash.revolance.ui.model.application.ApplicationManager;
import com.smash.revolance.ui.model.bot.Bot;
import com.smash.revolance.ui.model.sitemap.SiteMap;
import com.smash.revolance.ui.model.user.User;
import org.openqa.selenium.WebDriver;

/**
 * User: wsmash
 * Date: 17/09/13
 * Time: 20:03
 */
public class BaseTests extends TestConstants
{
    static Application        app;
    static ApplicationManager manager;
    static Bot                bot;
    static WebDriver          browser;
    static SiteMap            sitemap;

    public static void setupBrowserForFirefox(User user)
    {
        user.setBrowserType( "Firefox" );
    }

    public static void setupBrowserForChrome(User user)
    {
        user.setBrowserType( "Chrome" );
        user.setDriverPath( CHROME_DRIVER );
        user.setBrowserBinary( "/usr/bin/google-chrome" );
    }

    public static void setupBrowserForTest(User user)
    {
        user.setBrowserType( "MockedWebDriver" );
    }


}
