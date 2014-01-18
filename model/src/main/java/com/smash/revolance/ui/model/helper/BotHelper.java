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
import com.smash.revolance.ui.model.element.api.Button;
import com.smash.revolance.ui.model.element.api.Element;
import com.smash.revolance.ui.model.element.api.Link;
import com.smash.revolance.ui.model.page.api.Page;
import com.smash.revolance.ui.model.user.User;
import org.apache.log4j.Level;
import org.openqa.selenium.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User: wsmash
 * Date: 03/05/13
 * Time: 08:15
 */
public class BotHelper
{
    public static boolean rightDomain(User user, String url)
    {
        String domain = user.getDomain();
        return rightDomain( domain, url );
    }

    public static boolean rightDomain(String domain, String url)
    {
        if ( domain != null && !domain.isEmpty() && url != null && !url.isEmpty() )
        {
            return url.startsWith( domain );
        }
        else
        {
            return false;
        }
    }

    public static void sleep(long duration)
    {
        int count = 0;
        while ( count < duration )
        {
            try
            {
                Thread.sleep( 1000 );
                count++;
            }
            catch (InterruptedException e)
            {
                // Ignore gently !
            }
        }
    }

    public static String takeScreenshot(Bot bot) throws Exception
    {
        try
        {
            return ImageHelper.BASE64_IMAGE_PNG + ( (TakesScreenshot) bot.getBrowser() ).getScreenshotAs( OutputType.BASE64 );
        }
        catch (Exception e)
        {
            System.err.println(e);
            throw e;
        }
    }

    /**
     * Return the checksum of the new page if the page content has changed or the previous one
     *
     * @param bot
     * @param img
     * @return
     * @throws Exception
     */
    public static String pageContentHasChanged(Bot bot, String img) throws Exception
    {
        String newImg = BotHelper.takeScreenshot( bot );
        if ( newImg != img )
        {
            return newImg;
        }
        else
        {
            return img;
        }
    }

    public static List<WebElement> getRawElements(Bot bot, Page page) throws Exception
    {
        UserHelper.browseTo( page );
        WebDriver browser = bot.getBrowser();
        return browser.findElements( By.xpath( "//body//*" ) );
    }

    public static List<WebElement> getRawLinks(Bot bot, Page page) throws Exception
    {
        UserHelper.browseTo( page );
        WebDriver browser = bot.getBrowser();

        List<WebElement> elements = new ArrayList();

        By selector = By.xpath( "//body//a" );
        elements.addAll( browser.findElements( selector ) );

        return elements;
    }

    public static List<WebElement> getRawButtons(Bot bot, Page page) throws Exception
    {
        UserHelper.browseTo( page );
        WebDriver browser = bot.getBrowser();

        List<WebElement> elements = new ArrayList();

        By selector = By.xpath( "//body//input" );
        elements.addAll( browser.findElements( selector ) );

        selector = By.xpath( "//body//button" );
        elements.addAll( browser.findElements( selector ) );

        return elements;
    }

    public static WebElement findMatchingElement(Bot bot, Element element) throws Exception
    {
        WebElement webelement = findWebElement( bot, element );
        if ( webelement.isDisplayed() && webelement.isDisplayed() )
        {
            return webelement;
        } else
        {
            throw new ElementNotVisibleException( "Element: " + webelement + " is not visible." );
        }
    }

    public static WebElement findWebElement(Bot bot, Element element) throws Exception
    {
        WebDriver browser = bot.getBrowser();
        if ( !element.getId().trim().isEmpty() )
        {
            return browser.findElement( By.xpath( "//body//" + element.getTag() + "[@id='" + element.getId() + "']" ) );
        }
        else if ( element instanceof Link || element instanceof Button )
        {
            // Take the longest time (++)
            if ( element instanceof Link )
            {
                return Element.filterElementByLocation( getRawLinks( bot, element.getPage() ), element.getLocation() );
            } else if ( element instanceof Button )
            {
                return Element.filterElementByLocation( getRawButtons( bot, element.getPage() ), element.getLocation() );
            }
            return null;
        }
        else
        {
            bot.getUser().getLogger().log(Level.WARN, "There is no way to find back that element: " + element.toJson());
            return null;
        }
    }

    public static void handleApplicationAlertPopup(User user, Alert popup) throws Exception
    {
        if(!user.getBrowserType().contentEquals("PhantomJS"))
        {
            user.getApplication().handleAlert( popup );
        }
    }

    public static boolean isAlertPresent(User user) throws Exception
    {
        if(!user.getBrowserType().contentEquals("PhantomJS"))
        {
            try
            {
                String alertMsg = user.getBrowser().switchTo().alert().getText();
                user.getLogger().log(Level.INFO, "handling alert: " + alertMsg);
                return true;
            }
            catch (NoAlertPresentException e)
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    public static List<WebElement> getRawImages(Bot bot, Page page) throws Exception
    {
        UserHelper.browseTo( page );
        WebDriver browser = bot.getBrowser();

        List<WebElement> elements = new ArrayList();

        By selector = By.xpath( "//body//*" );
        elements.addAll( browser.findElements( selector ) );

        return elements;
    }
}
