package com.smash.revolance.ui.parser;

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

import com.smash.revolance.ui.materials.TestConstants;
import com.smash.revolance.ui.model.application.Application;
import com.smash.revolance.ui.model.application.ApplicationManager;
import com.smash.revolance.ui.model.bot.Bot;
import com.smash.revolance.ui.model.sitemap.SiteMap;
import com.smash.revolance.ui.model.user.User;
import org.apache.commons.exec.OS;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;

import java.io.File;

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
    static User               user;
    static WebDriver          browser;
    static SiteMap            sitemap;

    public static void setupBrowserForFirefox(User user)
    {
        user.setBrowserType( "Firefox" );
    }

    public static void setupBrowserForChrome(User user)
    {
        user.setBrowserType( "Chrome" );
        user.setDriverPath( getChromeDriverPath() );
        user.setBrowserBinary( "/usr/bin/google-chrome" );
    }

    public static void setupBrowserForTest(User user)
    {
        user.setBrowserType( "MockedWebDriver" );
    }

    public static String getChromeDriverPath()
    {
        return new File( new File( "" ).getAbsoluteFile(), "src/test/driver/" + ( OS.isFamilyUnix() ? "unix" : "win" ) + "/chromedriver" + ( OS.isFamilyUnix() ? "" : ".exe" ) ).getAbsolutePath();
    }

    @BeforeClass
    public static void setUp() throws Exception
    {
        manager = new ApplicationManager( new File( APP_CFG ) );
        app = manager.getApplication( "website" );

        user = app.getUser( "user_A" );

        //setupBrowserForFirefox( user );
        setupBrowserForTest( user );

        user.enablePageScreenshot( true );

        user.enablePageElementScreenshot( true );

        user.setFollowButtons( false );

        user.setFollowLinks( false );

        user.setExploreVariantsEnabled( false );
    }

}

