package com.smash.revolance.ui.explorer.helper;

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

import com.smash.revolance.ui.explorer.element.api.Button;
import com.smash.revolance.ui.explorer.element.api.Element;
import com.smash.revolance.ui.explorer.element.api.ElementBean;
import com.smash.revolance.ui.explorer.element.api.Link;
import com.smash.revolance.ui.explorer.page.api.PageBean;
import com.smash.revolance.ui.explorer.sitemap.SiteMap;

import java.util.ArrayList;
import java.util.List;

/**
 * User: wsmash
 * Date: 20/02/13
 * Time: 22:31
 */
public class PageHelper
{
    public static void filterElementsIncludedInEachOthers(List<Element> elements)
    {
        List<Element> clickables = new ArrayList<Element>(  );

        clickables.addAll( Link.filterLinks( elements ) );
        clickables.addAll( Button.filterButtons( elements ) );

        elements.removeAll( clickables );

        List<Element> toBeRemoved = new ArrayList<Element>(  );

        for(Element clickable : clickables)
        {
            for(Element element : elements)
            {
                if( clickable.isIncluded( element ) )
                {
                    toBeRemoved.add( element );
                }
            }
        }

        elements.removeAll( toBeRemoved );
        elements.addAll( clickables );
    }

    public static Element getBiggestElement(List<Element> elements)
    {
        if ( elements == null )
        {
            return null;
        }
        if ( elements.isEmpty() )
        {
            return null;
        }
        int maxArea = 0;
        Element pageElement = null;
        for ( Element element : elements )
        {
            if ( element.getArea() > maxArea )
            {
                pageElement = element;
                maxArea = element.getArea();
            }
        }
        return pageElement;
    }

    public static boolean contentHasBeenExplored(SiteMap sitemap, PageBean page) throws Exception
    {
        for(ElementBean element : page.getContent())
        {
            if( !element.isBroken()
                    && element.getInstance().isClickable()
                    && (!element.isDisabled() || !element.hasBeenClicked() || sitemap.hasBeenExplored( element.getHref() ) ) )
            {
                return false;
            }
        }
        return true;
    }
}
