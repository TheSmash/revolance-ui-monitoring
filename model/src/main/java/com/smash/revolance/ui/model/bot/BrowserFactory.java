package com.smash.revolance.ui.model.bot;

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

import com.smash.revolance.ui.materials.mock.webdriver.driver.MockedWebDriver;
import com.smash.revolance.ui.model.user.User;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.service.DriverService;

import java.io.File;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

/**
 * User: wsmash
 * Date: 23/01/13
 * Time: 11:09
 */
public class BrowserFactory
{
    public static void instanceciateNavigator(User user, String browserType) throws InstanciationError
    {
        instanceciateNavigator( user, BrowserType.fromString( browserType ) );
    }

    public static void instanceciateNavigator(User user, BrowserType browserType) throws InstanciationError
    {
        final Logger logger = user.getLogger();

        if ( !user.isExplorationDone() )
        {
            WebDriver browser = null;
            DriverService service = null;

            if ( browserType == BrowserType.Firefox )
            {
                browser = new FirefoxDriver();
            }
            else if ( browserType == BrowserType.Chrome )
            {
                File driver = new File( user.getDriverPath() );
                File binary = new File( user.getBrowserBinary() );


                // ImmutableMap<String, String> env = new ImmutableMap.Builder<String, String>().build();
                ChromeDriverService.Builder serviceBuilder = new ChromeDriverService.Builder()
                        .usingDriverExecutable( driver )
                        .usingAnyFreePort();

                // serviceBuilder.withEnvironment( env );
                service = serviceBuilder.build();
                DesiredCapabilities capabilities = DesiredCapabilities.chrome();
                capabilities.setCapability( "chrome.binary", binary.getAbsolutePath() );
                // capabilities.setCapability("chrome.switches", Arrays.asList("--start-maximized"));

                browser = new ChromeDriver( (ChromeDriverService) service, capabilities );
            }
            else if ( browserType == BrowserType.MockedWebDriver )
            {
                try
                {
                    System.setProperty( "webdriver.remote.server", String.valueOf( 9090 ) );
                    browser = new MockedWebDriver( 9090 );
                }
                catch (MalformedURLException e)
                {
                    throw new InstanciationError( "Unable to start the mocked web driver!", e );
                }
            }

            if ( browser != null )
            {
                browser.manage().timeouts().implicitlyWait( 1, TimeUnit.MINUTES );
                logger.log(Level.INFO, "Launching a " + browser + " browser");

                browser.manage().window().setSize( new Dimension( user.getBrowserWidth(), user.getBrowserHeight() ) );
                logger.log(Level.INFO, "Setting up resolution to: " + user.getBrowserWidth() + "x" + user.getBrowserHeight());

                user.setBrowser( browser );
                user.setBrowserActive( true );
            }
            else
            {
                logger.log(Level.ERROR, "Unable to start the browser: " + browser);
            }

            if ( service != null )
            {
                user.setDriverService( service );
            }
        }
    }

    public static class InstanciationError extends Exception
    {
        public InstanciationError(String s, Throwable t)
        {
            this( s );
            super.initCause( t.getCause() );
        }

        public InstanciationError(String s)
        {
            super( s );
        }
    }

    private static enum BrowserType
    {
        Chrome, Firefox, IE, HtmlUnit, MockedWebDriver;

        public static BrowserType fromString(String browserType) throws InstanciationError
        {
            for ( BrowserType browser : BrowserType.values() )
            {
                if ( String.valueOf( browser ).toLowerCase().contentEquals( browserType.trim().toLowerCase() ) )
                {
                    return browser;
                }
            }
            throw new InstanciationError( "Undefined browser type: '" + browserType + "'." );
        }
    }
}
