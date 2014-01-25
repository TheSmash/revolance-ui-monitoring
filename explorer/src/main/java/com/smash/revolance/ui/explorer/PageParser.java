package com.smash.revolance.ui.explorer;

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

import com.smash.revolance.ui.model.bot.Bot;
import com.smash.revolance.ui.model.element.api.Button;
import com.smash.revolance.ui.model.element.api.Data;
import com.smash.revolance.ui.model.element.api.Element;
import com.smash.revolance.ui.model.element.api.Link;
import com.smash.revolance.ui.model.helper.BotHelper;
import com.smash.revolance.ui.model.helper.ImageHelper;
import com.smash.revolance.ui.model.page.api.Page;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: wsmash
 * Date: 02/06/13
 * Time: 12:35
 */
public class PageParser
{

    private Page page;

    public PageParser(Page page)
    {
        this.page = page;
    }

    private List<Element> parseContent() throws Exception
    {
        page.getUser().getLogger().log(Level.INFO, "Parsing elements" );
        long mark = System.currentTimeMillis();

        List<Element> content = _parseContent();

        logClickableContent( content );

        long duration = ( System.currentTimeMillis() - mark ) / 1000;
        page.getUser().getLogger().log(Level.INFO, "Parsing elements [Done] [Duration: " + duration + " sec]" );

        return content;
    }

    private void logClickableContent(List<Element> content)
    {
        // List<Element> clickableContent = Element.filterClickableElements( content );
        if ( !content.isEmpty() )
        {
            page.getUser().getLogger().log(Level.INFO, "Clickable content found: " );
            for ( Element element : content )
            {
                if(element instanceof Link || element instanceof Button)
                {
                    page.getUser().getLogger().log(Level.INFO, "--|  " + element.getContent() );
                }
            }
        }
        else
        {
            page.getUser().getLogger().log(Level.INFO, "No clickable content has been found." );
        }
    }

    private List<Element> _parseContent() throws Exception
    {
        List<Element> content = new ArrayList();
        if ( !page.isExternal() && !page.isBroken() )
        {
            // retrieve all the elements of the html body
            content = getElements();
            // filter the elements included in each other (optimization)
            _filterElementsIncludedInEachOther(content);
            // takes screenshot of all the content
            takeScreenshots( content );
            // only for convenience to be able to track the click sequence
            Collections.sort( content );
        }

        page.setContent( content );
        return content;
    }

    private void _filterElementsIncludedInEachOther(List<Element> content) throws Exception
    {
        long mark = System.currentTimeMillis();
        page.getUser().getLogger().log(Level.INFO, "Filtering page elements" );
        Element.filterElementsIncludedInEachOthers( content, page.getArea() * 0.95, 1 / 8 );

        long duration = ( System.currentTimeMillis() - mark ) / 1000;
        page.getUser().getLogger().log(Level.INFO, "Filtering page elements [Done] [Duration: " + duration + "sec]");
        page.getUser().getLogger().log(Level.INFO, "Found: " + content.size() + " pertinent elements" );
    }

    public String takeScreenShot() throws Exception
    {
        Bot bot = page.getUser().getBot();
        if ( page.getCaption().isEmpty() )
        {
//            if(getTitle().isEmpty())
//            {
//                setTitle( getBot().getCurrentTitle() );
//            }

            if ( page.getUser().isPageScreenshotEnabled() )
            {
                page.getUser().getLogger().log(Level.INFO, "Taking page snapshot: '" + page.getTitle() + "'" );
                long mark = System.currentTimeMillis();

                String img = BotHelper.takeScreenshot( bot );

                if ( img != null )
                {
                    // update the image and the caption
                    page.setImage( ImageHelper.decodeToImage( img ) );
                    page.setScreenshotTaken( true );
                }

                long duration = ( System.currentTimeMillis() - mark ) / 1000;
                page.getUser().getLogger().log(Level.INFO, "Taking page snapshot: '" + page.getTitle() + "' [Done] [Duration: " + duration + " sec]" );
            }

        }
        return page.getCaption();
    }

    private void takeScreenshots(List<Element> content) throws Exception
    {
        if ( page.getUser().isPageScreenshotEnabled()
                && page.getUser().isPageElementScreenshotEnabled() )
        {
            long mark = System.currentTimeMillis();

            int contentIdx = 0;
            for ( Element pageElement : content )
            {
                contentIdx++;
                page.getUser().getLogger().log(Level.INFO, String.format("Taking element screenshots ( %d / %d )", contentIdx, content.size() ));
                pageElement.takeScreenShot();
            }

            long duration = ( System.currentTimeMillis() - mark ) / 1000;
            page.getUser().getLogger().log(Level.INFO, "Taking elements screenshots [Done] [Duration: " + duration + "sec]");
        }
    }


    private int getHeight(Bot bot) throws Exception
    {
        //Object o = bot.runJS( "return Math.max(document.body.clientHeight, window.innerHeight)" );
        Object o = bot.runJS( "var D = document; return Math.max(D.body.scrollHeight, D.documentElement.scrollHeight,D.body.offsetHeight, D.documentElement.offsetHeight,D.body.clientHeight, D.documentElement.clientHeight);" );
        if ( o == null )
        {
            return page.getUser().getBrowserHeight();
        }
        return Integer.parseInt( String.valueOf( (Long) o ) );
    }

    private int getWidth(Bot bot) throws Exception
    {
        //Object o = bot.runJS( "return Math.max(document.body.clientWidth, window.innerWidth)" );
        Object o = bot.runJS( "var D = document; return Math.max(D.body.scrollWidth, D.documentElement.scrollWidth,D.body.offsetWidth, D.documentElement.offsetWidth,D.body.clientWidth, D.documentElement.clientWidth);" );
        if ( o == null )
        {
            return page.getUser().getBrowserWidth();
        }
        return Integer.parseInt( String.valueOf( (Long) o ) );
    }


    public void parse() throws Exception
    {
        Logger logger = page.getUser().getLogger();

        if ( !page.hasBeenParsed() && !page.isExternal() )
        {

            if ( page.getUser().getCurrentPage() != page )
            {
                page.getUser().goTo( page ).awaitLoaded();
            }

            page.setWidth( getWidth( page.getUser().getBot() ) );
            page.setHeight( getHeight( page.getUser().getBot() ) );

            if ( page.getApplication().isPageBroken( page ) )
            {
                page.setBroken( true );
            }

            if ( !page.getApplication().isAuthorized( page ) )
            {
                page.setAuthorized( false );
            }

            if ( page.getUser().isPageScreenshotEnabled()
                    && page.getCaption().isEmpty() )
            {
                takeScreenShot();
            }

            if ( !page.isBroken() && page.isEmpty() )
            {
                parseContent();
            }
            else
            {
                logger.log(Level.WARN, "Page with url: '" + page.getUrl() + "' is broken.");
            }

            page.setParsed( true );
        }
        else if ( page.isExternal() )
        {
            logger.log(Level.WARN, "Page with url: '" + page.getUrl() + "' is out of the domain: '" + page.getUser().getDomain() + "'.");
        }
    }

    public List<Element> getElements() throws Exception
    {
        final Logger logger = page.getUser().getLogger();

        long mark = System.currentTimeMillis();

        List<Element> elements = new ArrayList<Element>();
        List<WebElement> webElements = BotHelper.getRawElements( page.getUser().getBot(), page );
        int idx = 0;
        int elementCount = webElements.size();
        for ( WebElement element : webElements )
        {
            idx++;
            try
            {
                if ( element.isDisplayed() )
                {
                    Class<? extends Element> elemImpl = Element.getImplementation( element );
                    if ( elemImpl != null )
                    {
                        Element elem = elemImpl.getConstructor( Page.class, WebElement.class ).newInstance( page, element );

                        if ( elem.getArea() > 0 )
                        {
                            // handleAddition( elements, elem );
                            elements.add( elem );
                        }


                    }
                }
            }
            catch (StaleElementReferenceException e)
            {
                logger.log(Level.ERROR, e);
            }
            finally
            {
                logger.log(Level.INFO, "Retrieving page element ( " + idx + "/" + elementCount + " )" );
            }
        }

        long duration = ( System.currentTimeMillis() - mark ) / 1000;
        logger.log(Level.INFO, "Retrieving page elements [Done] [Duration: " + duration + "sec]" );
        return elements;
    }

    public static void handleAddition(List<Element> elements, Element elem)
    {
        boolean isToAdd = true;
        List<Element> toBeRemoved = new ArrayList<Element>();

        if ( elem instanceof Data
                && elem.getContent().isEmpty() )
        {
            return;
        } else if ( elem instanceof Button
                || elem instanceof Link
                || elem instanceof Data )
        {

            for ( Element element : elements )
            {

                if ( element.isIncluded( elem ) || elem.isIncluded( element ) )
                {
                    if ( elem instanceof Data && element instanceof Data )
                    {
                        if ( elem.getArea() < element.getArea() )
                        {
                            toBeRemoved.add( element );
                        } else
                        {
                            isToAdd = false;
                        }
                    } else if ( elem instanceof Data
                            && ( element instanceof Link || element instanceof Button ) )
                    {
                        isToAdd = false;
                    } else if ( elem instanceof Button || elem instanceof Link )
                    {
                        toBeRemoved.add( element );
                    }
                    // Optimisation since we're calling this at each addition & we add one element at a time
                    // there is no need to run the complete loop
                    break;
                }

            }
        }

        elements.removeAll( toBeRemoved );
        if ( isToAdd )
        {
            elements.add( elem );
        }
    }

}
