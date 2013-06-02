package com.smash.revolance.ui.explorer.element.api;

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

import com.smash.revolance.ui.explorer.helper.BotHelper;
import com.smash.revolance.ui.explorer.page.IPage;
import com.smash.revolance.ui.explorer.page.api.Page;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: wsmash
 * Date: 29/01/13
 * Time: 19:11
 */
public class Link extends Element
{
    public Link(Page source, WebElement element)
    {
        super( source, element );
        setImplementation( "Link" );
        setHref( element.getAttribute( "href" ) );
        setTarget( element.getAttribute( "target" ) );
    }

    public Link(ElementBean bean)
    {
        super(bean.getPage(), bean);
    }

    @Override
    public boolean equals(Element element) throws IOException
    {
        if ( element == null )
        {
            return false;
        }
        return getBean().equals( element.getBean() );
    }

    public static boolean containsLink(List<Element> elements, Link link)
    {
        for ( Element element : filterLinks( elements ) )
        {
            if ( element.getContent().contentEquals( link.getContent() )
                    && element.getHref().contentEquals( link.getHref() )  )
            {
                return true;
            }
        }
        return false;
    }

    public static List<Element> filterLinks(List<Element> elements)
    {
        List<Element> links = new ArrayList<Element>(  );

        if(elements != null)
        {
            for( Element element : elements )
            {
                if( element instanceof Link )
                {
                    links.add( element );
                }
            }
        }

        return links;
    }

    public static List<Element> getLinks(Page page) throws Exception
    {
        List<Element> links = new ArrayList<Element>();

        for(WebElement element : BotHelper.getRawLinks(page.getUser().getBot(), page))
        {
            try
            {
                if(element.isDisplayed()
                    && Element.getImplementation( element ).getClass().getName().contentEquals( Link.class.getName() ))
                {
                    Link link = new Link( page, element );
                    if( link.getArea()>0 )
                    {
                        if(!containsLink(links, link.getContent()))
                        {
                            links.add( link );
                        }
                    }
                }
            }
            catch (StaleElementReferenceException e)
            {
                System.err.println(e);
            }
        }

        return links;
    }

    /**
     * For testing convenience
     *
     * @param elements
     * @param link
     * @return
     */
    public static boolean containsLink(List<Element> elements, String link)
    {
        for ( Element element : filterLinks( elements ) )
        {
            if ( element.getContent().contentEquals( link ) )
            {
                return true;
            }
        }
        return false;
    }
}
