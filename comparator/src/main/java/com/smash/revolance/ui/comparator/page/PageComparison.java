package com.smash.revolance.ui.comparator.page;

import com.smash.revolance.ui.comparator.element.ElementComparison;
import com.smash.revolance.ui.comparator.element.ElementDifferency;
import com.smash.revolance.ui.model.diff.DiffType;
import com.smash.revolance.ui.model.diff.PageDiffType;
import com.smash.revolance.ui.model.element.api.ElementBean;
import com.smash.revolance.ui.model.page.api.PageBean;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.util.ArrayList;
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
 * Time: 10:54
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
                getterVisibility = JsonAutoDetect.Visibility.NONE,
                isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class PageComparison
{
    private PageMatch match;

    private DiffType diffType;

    @JsonDeserialize(contentAs = PageDiffType.class, as = ArrayList.class)
    private List<PageDiffType> pageDifferencies = new ArrayList<PageDiffType>();

    @JsonDeserialize(contentAs = PageComparison.class, as = ArrayList.class)
    private List<PageComparison> variantComparisons = new ArrayList<PageComparison>();

    @JsonDeserialize(contentAs = ElementComparison.class, as = ArrayList.class)
    private List<ElementComparison> elementComparisons = new ArrayList<ElementComparison>();

    public void setDiffType(DiffType diffType)
    {
        this.diffType = diffType;
    }

    public DiffType getPageDiffType()
    {
        return diffType;
    }

    public PageComparison(PageBean reference)
    {
        match = new PageMatch( reference );
    }

    public void setMatch(PageBean match)
    {
        this.match.setMatch( match );
    }

    public PageBean getReference()
    {
        return match.getReference();
    }

    public PageBean getMatch()
    {
        return match.getMatch();
    }

    public void addPageDifferency(PageDiffType differency)
    {
        pageDifferencies.add( differency );
    }

    public void addElementComparison(ElementComparison comparison)
    {
        this.elementComparisons.add( comparison );
    }

    public List<ElementComparison> getElementComparisons()
    {
        return elementComparisons;
    }

    public List<PageDiffType> getPageDifferencies()
    {
        return pageDifferencies;
    }

    public List<String> getReferenceCommonContent()
    {
        List<String> content = new ArrayList<String>();

        if ( diffType == DiffType.BASE )
        {
            for ( ElementComparison comparison : elementComparisons )
            {
                if ( comparison.getDiffType() == DiffType.BASE && comparison.getElementDifferencies().isEmpty() )
                {
                    content.add( comparison.getReference().getInternalId() );
                }
            }
        }

        return content;
    }

    public List<String> getMatchCommonContent()
    {
        List<String> content = new ArrayList<String>();

        if ( diffType == DiffType.BASE )
        {
            for ( ElementComparison comparison : elementComparisons )
            {
                if ( comparison.getDiffType() == DiffType.BASE && comparison.getElementDifferencies().isEmpty() )
                {
                    content.add( comparison.getMatch().getInternalId() );
                }
            }
        }

        return content;
    }

    public List<ElementBean> getBaseContent()
    {
        List<ElementBean> content = new ArrayList<ElementBean>();

        if ( diffType == DiffType.BASE )
        {
            for ( ElementComparison comparison : elementComparisons )
            {
                if ( comparison.getDiffType() == DiffType.BASE )
                {
                    if ( comparison.getReference() != null )
                    {
                        content.add( comparison.getReference() );
                    }
                }
            }
        }

        return content;
    }

    public List<ElementBean> getAddedContent()
    {
        List<ElementBean> content = new ArrayList<ElementBean>();

        if ( diffType == DiffType.BASE )
        {
            for ( ElementComparison comparison : elementComparisons )
            {
                if ( comparison.getDiffType() == DiffType.ADDED )
                {
                    if ( comparison.getMatch() != null )
                    {
                        content.add( comparison.getMatch() );
                    }
                }
            }
        }

        return content;
    }

    public List<ElementBean> getDeletedContent()
    {
        List<ElementBean> content = new ArrayList<ElementBean>();

        if ( diffType == DiffType.BASE )
        {
            for ( ElementComparison comparison : elementComparisons )
            {
                if ( comparison.getDiffType() == DiffType.DELETED )
                {
                    if ( comparison.getReference() != null )
                    {
                        content.add( comparison.getReference() );
                    }
                }
            }
        }

        return content;
    }


    public List<ElementComparison> getPageElementComparisons()
    {
        return elementComparisons;
    }

    public List<ElementBean> getMovedContent()
    {
        List<ElementBean> content = new ArrayList<ElementBean>();

        if ( diffType == DiffType.BASE )
        {
            for ( ElementComparison comparison : elementComparisons )
            {
                if ( comparison.getDiffType() == DiffType.BASE
                        && comparison.getElementDifferencies().contains( ElementDifferency.POS ) )
                {
                    if ( comparison.getReference() != null && match.getMatch() != null )
                    {
                        content.add( comparison.getReference() );
                    }
                }
            }
        }

        return content;
    }

    public List<ElementBean> getLookChanges()
    {
        List<ElementBean> content = new ArrayList<ElementBean>();

        if ( diffType == DiffType.BASE )
        {
            for ( ElementComparison comparison : elementComparisons )
            {
                if ( comparison.getDiffType() == DiffType.BASE
                        && comparison.getElementDifferencies().contains( ElementDifferency.LOOK ) )
                {
                    if ( comparison.getReference() != null && match.getMatch() != null )
                    {
                        content.add( comparison.getReference() );
                    }
                }
            }
        }

        return content;
    }
}
