package com.smash.revolance.ui.comparator.page;

import com.smash.revolance.ui.comparator.NoMatchFound;
import com.smash.revolance.ui.model.element.api.ElementBean;
import com.smash.revolance.ui.model.page.api.PageBean;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

/**
 * User: wsmash
 * Date: 09/06/13
 * Time: 11:07
 */
public class PageMatchMaker implements IPageMatchMaker
{
    public PageMatch getBestMatch(final Collection<PageMatch> matches) throws IllegalArgumentException, NoMatchFound
    {
        if ( matches == null )
        {
            throw new IllegalArgumentException( "Null collection of matches cannot be parsed." );
        }

        PageMatch bestMatch = new PageMatch();
        for ( PageMatch match : matches )
        {
            if ( match.getRate() > bestMatch.getRate() )
            {
                bestMatch = match;
            }
        }

        if ( bestMatch.getMatch() == null )
        {
            throw new NoMatchFound( "Unable to find a match." );
        } else
        {
            return bestMatch;
        }
    }

    @Override
    public Collection<PageMatch> findMatch(final Collection<PageBean> pages, final PageBean page, PageSearchMethod... methods) throws NoMatchFound, IllegalArgumentException
    {
        if ( pages == null )
        {
            throw new IllegalArgumentException( "Null collection of pages to be parsed" );
        }
        if ( page == null )
        {
            throw new IllegalArgumentException( "Null page cannot be matched." );
        }

        Collection<PageBean> localPages = new ArrayList<PageBean>();
        localPages.addAll( pages );
        if ( localPages.contains( page ) )
        {
            pages.remove( page );
        }

        List<PageMatch> matches = getPageMatches( page, localPages, methods );

        if ( matches.isEmpty() )
        {
            throw new NoMatchFound( "Unable to find a single match." );
        } else
        {
            return matches;
        }
    }

    private List<PageMatch> getPageMatches(PageBean page, Collection<PageBean> localPages, PageSearchMethod... methods)
    {
        List<PageMatch> results = new ArrayList<PageMatch>();
        PageBean match = null;

        if ( ArrayUtils.contains( methods, PageSearchMethod.URL ) )
        {
            if ( ( match = findPageByUrl( localPages, page.getUrl() ) ) != null )
            {
                PageMatch pageMatch = new PageMatch( page );
                pageMatch.setMatch( match, 1 );
                results.add( pageMatch );
            }
        }

        if ( ArrayUtils.contains( methods, PageSearchMethod.TITLE ) )
        {
            match = null;
            if ( ( match = findPageByTitle( localPages, page.getTitle() ) ) != null )
            {
                PageMatch pageMatch = new PageMatch( page );
                pageMatch.setMatch( match, 1 );
                results.add( pageMatch );
            }
        }

        if ( ArrayUtils.contains( methods, PageSearchMethod.CONTENT ) )
        {
            match = null;
            if ( ( match = findPageByContentValue( localPages, getContentValue( page.getContent() ) ) ) != null )
            {
                PageMatch pageMatch = new PageMatch( page );
                pageMatch.setMatch( match, 1 );
                results.add( pageMatch );
            }
        }

        if ( ArrayUtils.contains( methods, PageSearchMethod.LOOK ) )
        {
            match = null;
            if ( ( match = findPageByLook( localPages, page.getCaption() ) ) != null )
            {
                PageMatch pageMatch = new PageMatch( page );
                pageMatch.setMatch( match, 1 );
                results.add( pageMatch );
            }
        }

        return results;
    }

    public static List<String> getContentValue(Collection<ElementBean> content)
    {
        List<String> contentValues = new ArrayList<String>();

        for ( ElementBean element : content )
        {
            String contentValue = element.getContent();
            if ( contentValue != null && !contentValue.isEmpty() )
            {
                contentValues.add( contentValue );
            }
        }

        return contentValues;
    }

    public static PageBean findPageByLook(Collection<PageBean> pages, String caption)
    {
        for ( PageBean page : pages )
        {
            if ( page.getCaption().contentEquals( caption ) )
            {
                return page;
            }
        }
        return null;
    }

    public static PageBean findPageByTitle(Collection<PageBean> pages, String title)
    {
        for ( PageBean page : pages )
        {
            if ( page.getTitle().contentEquals( title ) )
            {
                return page;
            }
        }
        return null;
    }

    public static PageBean findPageByUrl(Collection<PageBean> pages, String url)
    {
        for ( PageBean page : pages )
        {
            if ( page.getUrl().contentEquals( url ) )
            {
                return page;
            }
        }
        return null;
    }

    public static PageBean findPageByContentValue(Collection<PageBean> pages, List<String> contentValues)
    {
        for ( PageBean page : pages )
        {
            List<String> pageContentValues = getContentValue( page.getContent() );
            for ( String value : contentValues )
            {
                if ( pageContentValues.contains( value ) )
                {
                    pageContentValues.remove( value );
                }

                if ( pageContentValues.isEmpty() )
                {
                    break;
                }
            }

            if ( pageContentValues.isEmpty() )
            {
                return page;
            }
        }
        return null;
    }
}
