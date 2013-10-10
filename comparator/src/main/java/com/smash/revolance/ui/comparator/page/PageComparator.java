package com.smash.revolance.ui.comparator.page;

import com.smash.revolance.ui.comparator.element.ElementComparison;
import com.smash.revolance.ui.comparator.page.content.ContentComparator;
import com.smash.revolance.ui.comparator.page.content.IContentComparator;
import com.smash.revolance.ui.model.diff.DiffType;
import com.smash.revolance.ui.model.diff.PageDiffType;
import com.smash.revolance.ui.model.element.api.ElementBean;
import com.smash.revolance.ui.model.page.api.PageBean;

import java.awt.*;
import java.security.InvalidParameterException;
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
 * Time: 10:53
 */
public class PageComparator implements IPageComparator
{
    IContentComparator contentComparator = new ContentComparator();

    @Override
    public PageComparison compare(PageBean page, PageBean reference)
    {
        if ( reference == null )
        {
            throw new InvalidParameterException( "Null page reference passed in for comparison." );
        }

        if ( page == null )
        {
            throw new InvalidParameterException( "Null page passed in for comparison." );
        }

        PageComparison comparison = new PageComparison( reference );
        comparison.setMatch( page );

        if ( page == null )
        {
            comparison.setDiffType( DiffType.DELETED );
        }
        else if ( reference == null )
        {
            comparison.setDiffType( DiffType.ADDED );
        }
        else
        {
            comparison.setDiffType( DiffType.BASE );

            if ( !urlEquals( reference, page ) )
            {
                comparison.addPageDifferency( PageDiffType.URL );
            }

            if ( !layoutEquals( reference, page ) )
            {
                comparison.addPageDifferency( PageDiffType.LAYOUT );
            }

            if ( !contentEquals( reference, page ) )
            {
                comparison.addPageDifferency( PageDiffType.CONTENT );
            }

            if ( !lookEquals( reference, page ) )
            {
                comparison.addPageDifferency( PageDiffType.LOOK );
            }

            if ( !titleEquals( reference, page ) )
            {
                comparison.addPageDifferency( PageDiffType.TITLE );
            }

            Collection<ElementComparison> comparisons = contentComparator.compare( reference.getContent(), page.getContent() );
            for ( ElementComparison elementComparison : comparisons )
            {
                comparison.addElementComparison( elementComparison );
            }

        }

        return comparison;
    }

    private boolean titleEquals(PageBean reference, PageBean page)
    {
        return reference.getTitle().contentEquals( page.getTitle() );
    }

    private boolean lookEquals(PageBean reference, PageBean page)
    {
        return reference.getCaption().contentEquals( page.getCaption() );
    }

    private boolean contentEquals(PageBean reference, PageBean page)
    {
        List<String> refElementValue = PageMatchMaker.getContentValue( reference.getContent() );
        for ( String contentValues : PageMatchMaker.getContentValue( page.getContent() ) )
        {
            if ( refElementValue.contains( contentValues ) )
            {
                refElementValue.remove( contentValues );
            }
        }

        return refElementValue.isEmpty();
    }

    private boolean layoutEquals(PageBean reference, PageBean page)
    {
        List<Rectangle> layout = getLayout( page );
        for ( Rectangle refElementRect : getLayout( reference ) )
        {
            int matchCount = 0;
            for ( Rectangle pageElementRect : layout )
            {
                if ( refElementRect.getX() == pageElementRect.getX()
                        && refElementRect.getY() == pageElementRect.getY()
                        && refElementRect.getWidth() == pageElementRect.getWidth()
                        && refElementRect.getHeight() == pageElementRect.getHeight() )
                {
                    matchCount++;
                }
            }
            if ( layout.size() == matchCount )
            {
                return true;
            }
        }
        return false;
    }

    private List<Rectangle> getLayout(PageBean page)
    {
        List<Rectangle> layout = new ArrayList<Rectangle>();
        for ( ElementBean element : page.getContent() )
        {
            layout.add( element.getLocation() );

        }

        return layout;

    }

    private boolean urlEquals(PageBean reference, PageBean page)
    {
        return reference.getUrl().contentEquals( page.getUrl() );
    }
}
