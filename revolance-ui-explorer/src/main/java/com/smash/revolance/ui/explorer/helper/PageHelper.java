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

import com.smash.revolance.ui.explorer.element.api.*;
import com.smash.revolance.ui.explorer.page.api.PageBean;
import com.smash.revolance.ui.explorer.sitemap.SiteMap;
import org.openqa.selenium.Point;

import java.util.*;

/**
 * User: wsmash
 * Date: 20/02/13
 * Time: 22:31
 */
public class PageHelper
{
    public static void filterElementsIncludedInEachOthers(List<Element> content)
    {
        Collections.sort( content, new Comparator<Element>()
        {
            @Override
            public int compare(Element o1, Element o2)
            {
                if( o1.getArea() > o2.getArea() )
                {
                    return -1;
                }
                else
                {
                    return 1;
                }
            }
        } );

        List<Element> hudgeBlocs = new ArrayList<Element>(  );

        for(Element bloc : Data.filterData( content ) )
        {
            double area = (double) bloc.getPage().getArea();
            if( bloc.getArea() >= 0.98*area )
            {
                content.remove( bloc );
            }
            else if( bloc.getArea() > area/8 )
            {
                hudgeBlocs.add( bloc );
            }
        }

        List<Element> contentToFilter = new ArrayList<Element>(  );
        contentToFilter.addAll( content );

        for(Element hudgeBloc : hudgeBlocs)
        {
            List<Element> matchingElements = new ArrayList<Element>(  );

            // What are the elements included in the bloc?
            for(Element element : contentToFilter)
            {
                if(element != hudgeBloc
                        && element.getArea()<(hudgeBloc.getArea()*2/3))
                {
                    if( (hudgeBloc.isIncluded( element )
                            && hudgeBloc.getContent().contains( element.getContent() ) ) )
                    {
                        matchingElements.add( element );
                    }
                }
            }

            // Does all those elements constitute the content of the bloc?
            List<String> blocContent = buildListFromArray( hudgeBloc.getContent().split( "\\n" ) );
            for(Element element : matchingElements)
            {
                if(!blocContent.isEmpty())
                {
                    List<String> matchingElementsContent = buildListFromArray( element.getContent().split( "\\n" ) );
                    for( String matchingElementContent : matchingElementsContent )
                    {
                        if( blocContent.contains( matchingElementContent ) )
                        {
                            blocContent.remove( element.getContent() );
                        }
                    }
                }
            }

            // When we can replace the bloc content by the smaller data content
            if( blocContent.isEmpty() )
            {
                // Nothing to be done on the data. Hudgebloc has already been removed.
            }
            else
            {
                // We keep the biggest data bloc, but we remove the smalled ones
                content.removeAll( Data.filterData( matchingElements ) );
                content.add( hudgeBloc );
            }

        }

    }

    private static <T> List<T> buildListFromArray(T[] array)
    {
        List<T> list = new ArrayList<T>( );

        for(T t : array)
        {
            list.add( t );
        }

        return list;
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

}
