package com.smash.revolance.ui.explorer.application;

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

import com.smash.revolance.ui.explorer.helper.JsonHelper;
import com.smash.revolance.ui.explorer.diff.DiffType;
import com.smash.revolance.ui.explorer.diff.PageDiffType;
import com.smash.revolance.ui.explorer.element.api.ElementBean;
import com.smash.revolance.ui.explorer.page.api.PageBean;
import com.smash.revolance.ui.explorer.sitemap.SiteMap;
import com.smash.revolance.ui.explorer.user.User;
import com.smash.revolance.ui.explorer.user.UserBean;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * User: wsmash
 * Date: 23/02/13
 * Time: 08:30
 */
@JsonAutoDetect(fieldVisibility= JsonAutoDetect.Visibility.ANY,
                getterVisibility= JsonAutoDetect.Visibility.NONE,
                isGetterVisibility= JsonAutoDetect.Visibility.NONE)
public class ApplicationDifferencies
{
    @JsonDeserialize(contentAs = PageDifferencies.class, as = ArrayList.class)
    private final List<PageDifferencies> differencies;

    private UserBean refUser;
    private UserBean user;

    public ApplicationDifferencies()
    {
        this.differencies = new ArrayList<PageDifferencies>();
    }

    public ApplicationDifferencies(SiteMap sitemap, SiteMap sitemapRef) throws Exception
    {
        this();
        this.refUser = sitemapRef.getUser();
        this.user = sitemap.getUser();
        computeDifferencies( sitemap, sitemapRef );
    }

    private void computeDifferencies(SiteMap sitemap, SiteMap sitemapRef) throws Exception
    {
        List<PageBean> toBeMatched = new ArrayList<PageBean>();
        toBeMatched.addAll( sitemapRef.getPages() );

        // TODO: detect removed pages
        for ( PageBean page : sitemap.getPages() )
        {
            PageBean refPage = findRefPageBean( sitemapRef, page );

            if ( refPage != null )
            {
                toBeMatched.remove( refPage );

                PageDifferencies pageDifferencies = new PageDifferencies( page, refPage );
                pageDifferencies.setDiffType( DiffType.BASE );
                pageDifferencies.setPageDiffTypes( getPageDiffTypes( page, refPage ) );
                pageDifferencies.setContent( aggregateContent( page, refPage ) );

                differencies.add( pageDifferencies );

                // Detect variant variations from the original content
                if(page.isOriginal())
                {
                    for(PageBean variant : page.getVariants())
                    {
                        PageBean refVariant = refPage.getInstance().getVariant( variant.getSource(), false );
                        pageDifferencies = new PageDifferencies( page, refPage );
                        pageDifferencies.setDiffType( DiffType.BASE );
                        pageDifferencies.setPageDiffTypes( getPageDiffTypes( page, refVariant ) );
                        pageDifferencies.setContent( aggregateContent( page, refVariant ) );

                        differencies.add( pageDifferencies );
                    }
                }
            }
            else
            {
                PageDifferencies pageDifferencies = new PageDifferencies( page, DiffType.ADDED );
                pageDifferencies.setContent( aggregateContent( page, null ) );
                differencies.add( pageDifferencies );
            }
        }

        for(PageBean page : toBeMatched)
        {
            PageDifferencies pageDifferencies = new PageDifferencies( page, DiffType.DELETED );
            differencies.add( pageDifferencies );
        }
    }

    private List<PageDiffType> getPageDiffTypes(PageBean page, PageBean refPage)
    {
        List<PageDiffType> pageDiffTypes = new ArrayList<PageDiffType>( );
        if(!page.equalsByLook( refPage ))
        {
            pageDiffTypes.add( PageDiffType.LOOK );
        }
        if(!page.equalsByLayout( refPage ))
        {
            pageDiffTypes.add( PageDiffType.LAYOUT );
        }
        if(!page.equalsByContent( refPage ))
        {
            pageDiffTypes.add( PageDiffType.CONTENT );
        }
        if(!page.equalsByUrl( refPage ))
        {
            pageDiffTypes.add( PageDiffType.URL );
        }

        return pageDiffTypes;
    }

    private PageBean findRefPageBean(SiteMap sitemapRef, PageBean page)
    {
        PageBean refPage = getRefPageByUrl( page, sitemapRef );
        if( refPage == null )
        {
            refPage = getRefPageByLayout( page, sitemapRef );
        }
        if( refPage == null )
        {
            refPage = getRefPageByContent(page, sitemapRef);
        }
        if( refPage == null )
        {
            refPage = getRefPageByLook(page, sitemapRef);
        }
        return refPage;
    }

    private PageBean getRefPageByLook(PageBean page, SiteMap sitemapRef)
    {
        for(PageBean refPage : sitemapRef.getPages())
        {
            if(refPage.equalsByLook( page ))
            {
                return refPage;
            }
        }
        return null;
    }

    private PageBean getRefPageByLayout(PageBean page, SiteMap sitemapRef)
    {
        for( PageBean refPage : sitemapRef.getPages())
        {
            if( refPage.equalsByLayout( page ) )
            {
                return refPage;
            }
        }
        return null;
    }

    private PageBean getRefPageByContent(PageBean page, SiteMap sitemapRef)
    {
        for( PageBean refPage : sitemapRef.getPages())
        {
            if( refPage.equalsByContent( page ) )
            {
                return refPage;
            }
        }
        return null;
    }

    private PageBean getRefPageByUrl(PageBean page, SiteMap sitemapRef)
    {
        for( PageBean refPage : sitemapRef.getPages())
        {
            if(refPage.getRelativeUrl().contentEquals( page.getRelativeUrl() ))
            {
                return refPage;
            }
        }
        return null;
    }

    private List<ElementBean> aggregateContent(PageBean page, PageBean refPage) throws Exception
    {
        List<ElementBean> elements = new ArrayList<ElementBean>();
        if( refPage == null && page != null )
        {
            elements.addAll( page.getContent() );
            PageBean.markElements( elements, DiffType.ADDED );
        }
        else if( page == null && refPage != null )
        {
            elements.addAll( page.getContent() );
            PageBean.markElements( elements, DiffType.DELETED );
        }
        else if( page != null && refPage != null )
        {
            Map<DiffType, List<ElementBean>> diff = page.getDiff( refPage );
            for(DiffType diffType : diff.keySet())
            {
                elements.addAll( diff.get( diffType ) );
            }

        }
        return elements;
    }

    public ApplicationDifferencies(User user, User userRef) throws Exception
    {
        this(user.getSiteMap(), userRef.getSiteMap());
    }

    private List<ElementBean> getComparedPage(PageBean compared, Map<String, List<ElementBean>> refContent)
    {
        if(!refContent.containsKey( compared.getId() ))
        {
            refContent.put( compared.getId(), new ArrayList<ElementBean>(  ) );
        }
        return refContent.get( compared.getId() );
    }

    private List<ElementBean> getDifferencies(DiffType diffType, Map<DiffType, List<ElementBean>> elements)
    {
        if(!elements.containsKey( diffType ))
        {
            elements.put( diffType, new ArrayList<ElementBean>(  ) );
        }
        return elements.get( diffType );
    }

    public String toJSon() throws Exception
    {
        return JsonHelper.getInstance().map( this );
    }

    public File doReport(File report) throws IOException
    {
        JsonHelper.getInstance().map( report, this );
        return report;
    }
}
