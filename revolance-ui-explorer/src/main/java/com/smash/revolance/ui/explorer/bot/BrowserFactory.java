package com.smash.revolance.ui.explorer.bot;

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

import com.smash.revolance.ui.explorer.user.User;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.service.DriverService;

import java.io.File;
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
        instanceciateNavigator(user, BrowserType.fromString( browserType ) );
    }

    public static void instanceciateNavigator(User user, BrowserType browserType) throws InstanciationError
    {
        if ( !user.isExplorationDone() )
        {
            WebDriver browser = null;
            DriverService service = null;

            if( browserType == BrowserType.Firefox )
            {
                 browser = new FirefoxDriver();
            }
            else if ( browserType == BrowserType.Chrome )
            {
                File driver = new File( user.getDriverPath( ) );
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

            if( browser != null)
            {

                browser.manage().timeouts().implicitlyWait( 1, TimeUnit.MINUTES );

                System.out.println( "The user: " + user.getId() + " is starting the browser (" + user.getBrowserWidth() + ", " + user.getBrowserHeight() + ")" );
                browser.manage().window().setSize( new Dimension( user.getBrowserWidth(), user.getBrowserHeight() ) );
                user.setBrowser( browser );
                user.setBrowserActive( true );
            }

            if( service != null )
            {
                user.setDriverService( service );
            }
        }
    }

    public static class InstanciationError extends Throwable
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
        Chrome, Firefox, IE;

        public static BrowserType fromString(String browserType) throws InstanciationError
        {
            for(BrowserType browser : BrowserType.values())
            {
                if( String.valueOf( browser ).toLowerCase().contentEquals( browserType.trim().toLowerCase() ) )
                {
                    return browser;
                }
            }
            throw new InstanciationError("Undefined browser type: '" + browserType + "'.");
        }
    }
}
