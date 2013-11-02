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
        page.getUser().log( "Parsing elements" );
        long mark = System.currentTimeMillis();

        List<Element> content = _parseContent();

        logClickableContent( content );

        long duration = ( System.currentTimeMillis() - mark ) / 1000;
        page.getUser().log( "Parsing elements [Done] [Duration: " + duration + " sec]" );

        return content;
    }

    private void logClickableContent(List<Element> content)
    {
        List<Element> clickableContent = Element.filterClickableElements( content );
        if ( !clickableContent.isEmpty() )
        {
            page.getUser().log( "Clickable content found: " );
        }
        else
        {
            page.getUser().log( "No clickable content has been found." );
        }

        for ( Element element : clickableContent )
        {
            page.getUser().log( "--|  " + element.getContent() );
        }
    }

    private List<Element> _parseContent() throws Exception
    {
        List<Element> content = new ArrayList<Element>();

        if ( !page.isExternal() && !page.isBroken() )
        {
            content = getElements();

            Element.filterElementsIncludedInEachOthers( content, page.getArea() * 0.95, 1 / 8 );

            page.setContent( content );

            page.getUser().log( page.getContent().size() + " pertinent elements found" );

            takeScreenshots( content );

            Collections.sort( content );
        }

        return content;
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
                page.getUser().log( "Taking page snapshot: '" + page.getTitle() + "'" );
                long mark = System.currentTimeMillis();

                String img = BotHelper.takeScreenshot( bot );

                if ( img != null )
                {
                    // update the image and the caption
                    page.setImage( ImageHelper.decodeToImage( img ) );
                    page.setScreenshotTaken( true );
                }

                long duration = ( System.currentTimeMillis() - mark ) / 1000;
                page.getUser().log( "Taking page snapshot: '" + page.getTitle() + "' [Done] [Duration: " + duration + " sec]" );
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
                page.getUser().log( String.format("Taking element screenshots ( %d / %d )", contentIdx, content.size() ) );
                pageElement.takeScreenShot();
            }

            long duration = ( System.currentTimeMillis() - mark ) / 1000;
            page.getUser().log( "Taking elements screenshots [Done] [Duration: \"" + duration + "\" sec]" );
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
        if ( !page.hasBeenParsed() )
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

            if ( !page.isBroken() && !page.isExternal() && page.isEmpty() )
            {
                parseContent();
            }
            else if ( page.isExternal() )
            {
                page.getUser().log( "Page with url: '" + page.getUrl() + "' is out of the domain: '" + page.getApplication().getDomain() + "'." );
            }
            else
            {
                page.getUser().log( "Page with url: '" + page.getUrl() + "' is broken." );
            }

            page.setParsed( true );
        }
    }

    public List<Element> getElements() throws Exception
    {
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
                System.err.println( e );
            }
            page.getUser().log( "Retrieving page element ( " + idx + "/" + elementCount + " )" );
        }

        long duration = ( System.currentTimeMillis() - mark ) / 1000;
        page.getUser().log( "Retrieving page elements [Done] [Duration: \"" + duration + "\" sec]" );
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
