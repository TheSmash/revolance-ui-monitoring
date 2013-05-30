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

import com.smash.revolance.ui.explorer.element.IElement;
import com.smash.revolance.ui.explorer.helper.BotHelper;
import com.smash.revolance.ui.explorer.page.IPage;
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
    public Link(IPage source, WebElement element)
    {
        super( source, element );
        setHref( element.getAttribute( "href" ) );
    }

    public Link(ElementBean bean)
    {
        super(bean.getPage(), bean);
    }

    @Override
    public boolean equals(IElement element) throws IOException
    {
        if ( element == null )
        {
            return false;
        }
        return getBean().equals( element.getBean() );
    }

    public static boolean isLink(WebElement element)
    {
        String tag = element.getTagName();
        return Element.isVisible( element ) && tag.contentEquals( "a" );
    }

    public static boolean containsLink(List<IElement> elements, Link link)
    {
        for ( IElement element : filterLinks( elements ) )
        {
            if ( element.getText().contentEquals( link.getText() )
                    && element.getHref().contentEquals( link.getHref() )  )
            {
                return true;
            }
        }
        return false;
    }

    public static List<IElement> filterLinks(List<IElement> elements)
    {
        List<IElement> links = new ArrayList<IElement>(  );

        for( IElement element : elements )
        {
            if( element instanceof Link )
            {
                links.add( element );
            }
        }

        return links;
    }

    public static List<IElement> getLinks(IPage page) throws Exception
    {
        List<IElement> links = new ArrayList<IElement>();

        for(WebElement element : BotHelper.getRawLinks(page.getUser().getBot(), page))
        {
            try
            {
                if(element.isDisplayed())
                {
                    Link link = new Link( page, element );

                    if(link.getArea()>0 && !link.getHref().isEmpty())
                    {
                        if(!containsLink(links, link))
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
    public static boolean containsLink(List<IElement> elements, String link)
    {
        for ( IElement element : filterLinks( elements ) )
        {
            if ( element.getText().contentEquals( link ) )
            {
                return true;
            }
        }
        return false;
    }
}
