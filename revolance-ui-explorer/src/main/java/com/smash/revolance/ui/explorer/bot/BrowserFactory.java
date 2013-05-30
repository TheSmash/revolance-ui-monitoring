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

import com.smash.revolance.ui.explorer.application.ApplicationManager;
import com.smash.revolance.ui.explorer.helper.OSHelper;
import com.smash.revolance.ui.explorer.user.User;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
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
    public static void instanceciateNavigator(User user) throws InstanciationError
    {
        if ( !user.isExplorationDone() )
        {
            File driver = new File( getDriverPath( user.getApplication().getApplicationManager() ) );
            File binary = new File( user.getBrowserBinary() );

            // ImmutableMap<String, String> env = new ImmutableMap.Builder<String, String>().build();
            ChromeDriverService.Builder serviceBuilder = new ChromeDriverService.Builder()
                    .usingDriverExecutable( driver )
                    .usingAnyFreePort();

            // serviceBuilder.withEnvironment( env );
            DriverService service = serviceBuilder.build();
            DesiredCapabilities capabilities = DesiredCapabilities.chrome();
            capabilities.setCapability( "chrome.binary", binary.getAbsolutePath() );
            // capabilities.setCapability("chrome.switches", Arrays.asList("--start-maximized"));

            WebDriver browser = new ChromeDriver( (ChromeDriverService) service, capabilities );

            browser.manage().timeouts().implicitlyWait( 1, TimeUnit.MINUTES );

            System.out.println( "The user: " + user.getId() + " is starting the browser (" + user.getBrowserWidth() + ", " + user.getBrowserHeight() + ")" );
            browser.manage().window().setSize( new Dimension( user.getBrowserWidth(), user.getBrowserHeight() ) );

            user.setDriverService( service );
            user.setBrowser( browser );
            user.setBrowserActive( true );
        }
    }

    private static String getDriverPath( ApplicationManager manager )
    {
        return manager.getHome() + "/driver/" + ( OSHelper.isUnix()?"unix":"win") + "/chromedriver" + (!OSHelper.isUnix()?".exe":"");
    }

    public static class InstanciationError extends Throwable
    {
        public InstanciationError(String s, Throwable t)
        {
            super(s, t);
        }
    }
}
