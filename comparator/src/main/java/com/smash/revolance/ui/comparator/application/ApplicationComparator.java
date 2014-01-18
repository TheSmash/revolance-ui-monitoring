package com.smash.revolance.ui.comparator.application;

import com.smash.revolance.ui.comparator.NoMatchFound;
import com.smash.revolance.ui.comparator.page.*;
import com.smash.revolance.ui.model.page.api.PageBean;
import com.smash.revolance.ui.model.sitemap.SiteMap;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: wsmash
 * Date: 09/06/13
 * Time: 12:08
 */
@Service
public class ApplicationComparator implements IApplicationComparator
{

    IPageMatchMaker pageMatchMaker = new PageMatchMaker();

    IPageComparator pageComparator = new PageComparator();

    @Override
    public ApplicationComparison compare(SiteMap reference, SiteMap sitemap) throws IllegalArgumentException
    {
        if ( reference == null )
        {
            throw new IllegalArgumentException( "Null sitemap reference passed in." );
        }

        if ( sitemap == null )
        {
            throw new IllegalArgumentException( "Null sitemap passed in." );
        }

        ApplicationComparison comparison = new ApplicationComparison();

        List<PageBean> refPages = new ArrayList<PageBean>();
        for ( PageBean refPage : reference.getPages() )
        {
            refPages.add( refPage );
        }

        List<PageBean> pages = new ArrayList<PageBean>();
        pages.addAll( sitemap.getPages() );

        while ( !refPages.isEmpty() )
        {
            PageBean page = refPages.get( 0 );
            if ( !page.isBroken() && !page.isExternal() )
            {
                try
                {
                    Collection<PageMatch> matches = pageMatchMaker.findMatch( pages, page, PageSearchMethod.URL, PageSearchMethod.TITLE, PageSearchMethod.CONTENT );
                    PageMatch bestMatch = pageMatchMaker.getBestMatch( matches );

                    if ( bestMatch.getReference() != null && bestMatch.getMatch() != null )
                    {
                        PageComparison pageComparison = pageComparator.compare( bestMatch.getMatch(), bestMatch.getReference() );
                        comparison.addPageComparison( pageComparison );

                        refPages.remove( bestMatch.getReference() );
                        pages.remove( bestMatch.getMatch() );
                    }
                }
                catch (NoMatchFound noMatchFound)
                {
                    // Then the page is added
                    comparison.addRemovedPage( page );
                    refPages.remove( page );
                }
            }
            else
            {
                refPages.remove( page );
            }
        }

        for ( PageBean page : pages )
        {
            comparison.addNewPage( page );
        }

        return comparison;
    }
}
