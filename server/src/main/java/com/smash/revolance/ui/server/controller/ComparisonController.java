package com.smash.revolance.ui.server.controller;

/*
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * ui-monitoring-server
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2012 - 2014 RevoLance
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

import com.smash.revolance.ui.comparator.application.ApplicationComparison;
import com.smash.revolance.ui.comparator.application.IApplicationComparator;
import com.smash.revolance.ui.comparator.element.ElementDifferency;
import com.smash.revolance.ui.comparator.element.IElementComparator;
import com.smash.revolance.ui.comparator.page.IPageComparator;
import com.smash.revolance.ui.comparator.page.PageComparison;
import com.smash.revolance.ui.database.IStorage;
import com.smash.revolance.ui.database.StorageException;
import com.smash.revolance.ui.model.element.ElementNotFound;
import com.smash.revolance.ui.model.element.api.ElementBean;
import com.smash.revolance.ui.model.page.api.PageBean;
import com.smash.revolance.ui.model.sitemap.SiteMap;
import com.smash.revolance.ui.server.renderable.ApplicationDetailsRenderable;
import com.smash.revolance.ui.server.renderable.DetailedPageComparisonRenderable;
import com.smash.revolance.ui.server.renderable.PageComparisonRenderable;
import org.rendersnake.HtmlCanvas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.rendersnake.HtmlAttributesFactory.*;


/**
 * Created by ebour on 12/01/14.
 */
@Controller
public class ComparisonController
{
    IStorage applications;

    @Autowired
    public void setApplications(IStorage applications)
    {
        this.applications = applications;
    }

    IApplicationComparator applicationComparator;

    @Autowired
    public void setApplicationComparator(IApplicationComparator applicationComparator)
    {
        this.applicationComparator = applicationComparator;
    }

    IPageComparator pageComparator;

    @Autowired
    public void setPageComparator(IPageComparator pageComparator)
    {
        this.pageComparator = pageComparator;
    }

    IElementComparator elementComparator;

    @Autowired
    public void setElementComparator(IElementComparator elementComparator)
    {
        this.elementComparator = elementComparator;
    }


    @RequestMapping(value = "/compare/{contentTagRef}/{contentTagNew}", method = RequestMethod.GET)
    public ModelAndView renderComparison(@PathVariable String contentTagRef, @PathVariable String contentTagNew, @RequestParam(value="reviewId", defaultValue="") String reviewId, @ModelAttribute("model") ModelMap model) throws StorageException, IOException
    {

        if(contentTagNew.contentEquals(contentTagRef))
        {
            List<PageBean> pages = new ArrayList<PageBean>();
            for ( PageBean page : SiteMap.fromJson( applications.retrieve( contentTagRef ) ).getPages() )
            {
                pages.add(page);
                if ( page.hasVariants() )
                {
                    pages.addAll(page.getVariants());
                }
            }

            model.addAttribute( "applicationId", contentTagRef );
            model.addAttribute("pagesCount", pages.size());

            HtmlCanvas canvas = new HtmlCanvas();
            new ApplicationDetailsRenderable(reviewId, pages).renderOn(canvas);
            model.addAttribute( "pages", canvas.toHtml() );

            return new ModelAndView( "ApplicationDetail", model );
        }
        else
        {
            model.addAttribute( "refAppTag", contentTagRef );
            model.addAttribute( "newAppTag", contentTagNew );

            SiteMap refSitemap = SiteMap.fromJson(applications.retrieve(contentTagRef));
            SiteMap newSitemap = SiteMap.fromJson( applications.retrieve( contentTagNew ) );

            ApplicationComparison comparison = applicationComparator.compare( refSitemap, newSitemap );

            model.addAttribute( "pageComparisons", renderComparisonsWithRenderables( reviewId, contentTagRef, contentTagNew, comparison.getPageComparisons() ) );
            model.addAttribute( "pageAdded", comparison.getAddedPages() );
            model.addAttribute( "pageRemoved", comparison.getRemovedPages() );

            return new ModelAndView( "ApplicationComparison", model );
        }
    }

    @RequestMapping(value = "/compare/{contentTagRef}/{contentTagNew}/{pageRefId}/{pageNewId}", method = RequestMethod.GET)
    public ModelAndView renderMergeView(@PathVariable String contentTagRef, @PathVariable String contentTagNew, @PathVariable String pageRefId, @PathVariable String pageNewId, @RequestParam(value="reviewId", defaultValue="") final String reviewId, @ModelAttribute("model") ModelMap model) throws StorageException, IOException
    {
        PageBean refPage = SiteMap.fromJson( applications.retrieve( contentTagRef ) ).findPageByInternalId(pageRefId);
        PageBean newPage = SiteMap.fromJson( applications.retrieve( contentTagNew ) ).findPageByInternalId(pageNewId);

        HtmlCanvas canvas = new HtmlCanvas();
        if ( refPage != null && newPage != null )
        {
            PageComparison comparison = pageComparator.compare( newPage, refPage );
            DetailedPageComparisonRenderable merge = new DetailedPageComparisonRenderable( reviewId, comparison, contentTagRef, contentTagNew );
            merge.renderOn(canvas);
        }
        else
        {
            canvas.h1().content("Oups! An Internal error occurred. Unable to do this comparison.");
        }

        model.addAttribute( "content", canvas.toHtml() );
        model.addAttribute( "refAppTag", contentTagRef );
        model.addAttribute( "newAppTag", contentTagNew );

        return new ModelAndView( "DetailedApplicationComparison", model );
    }

    @ResponseBody
    @RequestMapping(value = "/compare/{contentTagRef}/{contentTagNew}/{pageRefId}/{pageNewId}/{refElementId}/{elementId}", method = RequestMethod.GET, headers="Accept=application/json")
    public Collection<ElementDifferency> renderMergeView(@PathVariable String contentTagRef, @PathVariable String contentTagNew, @PathVariable String pageRefId, @PathVariable String pageNewId, @PathVariable String refElementId, @PathVariable String elementId) throws StorageException, IOException, ElementNotFound
    {
        ElementBean refElement = retrievePageElement(contentTagRef, pageRefId, refElementId);
        ElementBean element = retrievePageElement(contentTagNew, pageNewId, elementId);

        return elementComparator.compare(refElement, element);
    }

    private ElementBean retrievePageElement(String applicationId, String pageId, String elementId) throws StorageException, IOException, ElementNotFound
    {
        SiteMap sitemap = SiteMap.fromJson( applications.retrieve( applicationId ) );
        PageBean page = sitemap.findPageByInternalId(pageId);

        for(ElementBean e : page.getContent())
        {
            if(e.getInternalId().contentEquals(elementId))
            {
                return e;
            }
        }
        throw new ElementNotFound("Element with id: '"+elementId+"' could not be found in page: '"+pageId+"' of application: '"+applicationId+"'.");
    }

    private List<String> renderComparisonsWithRenderables(String reviewId, String contentTagRef, String contentTagNew, List<PageComparison> pageComparisons) throws IOException
    {
        List<String> values = new ArrayList<String>();

        int idx = 0;
        for ( PageComparison comparison : pageComparisons )
        {
            StringWriter html = new StringWriter();
            HtmlCanvas canvas = new HtmlCanvas( html );

            canvas.tr( data( "id", String.valueOf( idx ) ).data( "variant-idx", "0" ) )
                    .td( colspan( "2" ) );
            new PageComparisonRenderable(reviewId, comparison).renderOn(canvas);
            if ( !comparison.getPageDifferencies().isEmpty() )
            {
                canvas.div( class_( "span2" ) )
                        .a( type( "button" )
                                .class_( "btn btn-info btn-small" )
                                .data( "row-id", String.valueOf( idx ) )
                                .data( "new-page-id", comparison.getMatch().getId() )
                                .data( "ref-page-id", comparison.getReference().getId() )
                                .href( "/ui-monitoring-server/compare/" + contentTagRef + "/" + contentTagNew + "/" + comparison.getReference().getId() + "/" + comparison.getMatch().getId() + "?reviewId="+reviewId ) )
                        .content( "Inspect" )
                        ._div();
            }

            canvas._td()._tr();
            idx++;

            values.add( canvas.toHtml() );
        }

        return values;
    }

}
