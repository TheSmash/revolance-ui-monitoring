package com.smash.revolance.ui.materials.mock.webdriver.browser;

/*
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Revolance-UI-Materials-Mock-Webdriver-Service
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

import com.smash.revolance.ui.materials.JsonHelper;
import com.smash.revolance.ui.materials.TestConstants;
import com.smash.revolance.ui.materials.mock.webdriver.MockedWebPage;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * User: wsmash
 * Date: 29/09/13
 * Time: 23:49
 */
public class MockedBrowser extends TestConstants
{
    private MockedWebPage page = new MockedWebPage();

    // Browser's list of pages that have been visited.
    // private ArrayList<MockedWebPage> history = new ArrayList<MockedWebPage>();

    private Point     location;
    private Dimension dimension;

    public MockedBrowser()
    {
        setSize( 800, 600 );
        setLocation( 0, 0 );
    }

    public void setLocation(int x, int y)
    {
        location = new Point( x, y );
    }

    public void setSize(int w, int h)
    {
        dimension = new Dimension( w, h );
    }

    /*
    public void goBack()
    {
        int pageIndex = history.indexOf( page );
        if ( pageIndex != 0 )
        {
            page = history.get( pageIndex - 1 );
        }
    }
    */

    /*
    public void refresh()
    {

    }
    */

    /*
    public void goForward()
    {
        int pageIndex = history.indexOf( page.getUrl() );
        if ( pageIndex != history.size() - 1 )
        {
            page = history.get( pageIndex + 1 );
        }
    }
    */

    public String getUrl()
    {
        return page.getUrl();
    }

    public void goToUrl(String url) throws Exception
    {
        page = (MockedWebPage) JsonHelper.getInstance().map( MockedWebPage.class, getMockedPage( url ) );
        page.setUrl( url );
        // history.add( page );
    }

    public String getTitle()
    {
        return page.getTitle();
    }

    public String takeScreenshot()
    {
        return page.getCaption();
    }

    public Object executeJavaScript(String script, Object[] args)
    {
        if ( script.contains( "clientHeight" ) )
        {
            return getPageHeight();
        }
        if ( script.contains( "clientWidth" ) )
        {
            return getPageWidth();
        }
        return "";
    }

    public Point getLocation()
    {
        return location;
    }

    public Dimension getDimension()
    {
        return dimension;
    }

    public List<String> findElements(String xpath)
    {
        return page.findElements( xpath );
    }


    public Point getElementLocation(String elementId)
    {
        return page.getElementLocation( elementId );
    }

    public Dimension getElementSize(String elementId)
    {
        return page.getElementSize( elementId );
    }

    public String getElementTag(String elementId)
    {
        return page.getElementTag( elementId );
    }

    public String getElementText(String elementId)
    {
        return page.getElementText( elementId );
    }

    public String getElementHtmlAttribute(String elementId, String attr)
    {
        return page.getElementAttribute( elementId, attr );
    }

    public String getPageHeight()
    {
        return String.valueOf( page.getH() );
    }

    public String getPageWidth()
    {
        return String.valueOf( page.getW() );
    }

    public String isElementDisplayed(String elementId)
    {
        return String.valueOf( page.isElementDisplayed( elementId ) );
    }

    public String getElementCssAttribute(String elementId, String cssAttribute)
    {
        return page.getCssAttribute( elementId, cssAttribute );
    }
}
