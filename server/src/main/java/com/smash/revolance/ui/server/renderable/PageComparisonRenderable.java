package com.smash.revolance.ui.server.renderable;

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

import com.smash.revolance.ui.comparator.page.PageComparison;
import com.smash.revolance.ui.model.diff.PageDiffType;
import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;

import java.io.IOException;

import static org.rendersnake.HtmlAttributesFactory.class_;
import static org.rendersnake.HtmlAttributesFactory.href;

/**
 * User: wsmash
 * Date: 09/06/13
 * Time: 19:20
 */
public class PageComparisonRenderable implements Renderable
{
    private final PageComparison comparison;
    private final String reviewId;

    public PageComparisonRenderable(String reviewId, PageComparison comparison)
    {
        this.reviewId = reviewId;
        this.comparison = comparison;
    }

    @Override
    public void renderOn(HtmlCanvas html) throws IOException
    {
        html.div()
                .a( href( "#" ).onClick( "$.previousVariant( this );" ) ).i( class_( "icon-chevron-left" ) )._i()._a()
                .a( class_("pull-right").href( "#" ).onClick( "$.nextVariant( this );" ) ).i( class_( "icon-chevron-right" ) )._i()._a()
            ._div()
            .div( class_( "span4 ref-page" ) )
                .render( new PageRenderable(reviewId, comparison.getReference()) )
            ._div()
            .div( class_( "span4 new-page" ) )
                .render( new PageRenderable(reviewId, comparison.getMatch()) )
            ._div()
            .div( class_( "data-container span3" ) )
                .div( class_( "cloud-tag" ) )
                    .p( class_( "data-container-title" ) )
                        .i( class_( "icon-tags" ) )._i().write( "  Tags" )
                    ._p();
        if ( comparison.getPageDifferencies().isEmpty() )
        {
            html.span( class_( "label label-success no-changes" ) )
                    .write( "change: none" )
                    ._span();

        } else
        {
            if ( comparison.getPageDifferencies().contains( PageDiffType.CONTENT ) )
            {
                html.span( class_( "label label-important content" ) )
                        .write( "change: content" )
                        ._span();
            }
            if ( comparison.getPageDifferencies().contains( PageDiffType.LAYOUT ) )
            {
                html.span( class_( "label label-warning layout" ) )
                        .write( "change: layout" )
                        ._span();
            }
            if ( comparison.getPageDifferencies().contains( PageDiffType.LOOK ) )
            {
                html.span( class_( "label label-warning look" ) )
                        .write( "change: look" )
                        ._span();
            }
            if ( comparison.getPageDifferencies().contains( PageDiffType.TITLE ) )
            {
                html.span( class_( "label label-info title" ) )
                        .write( "change: title" )
                        ._span();
            }
            if ( comparison.getPageDifferencies().contains( PageDiffType.URL ) )
            {
                html.span( class_( "label label-info url" ) )
                        .write( "change: url" )
                        ._span();
            }
        }

        html.span( class_( "label label-info" ) ).write( "o:title: " )
                .em().write( comparison.getReference().getTitle() )._em()
            ._span()
            .span( class_( "label label-info" ) ).write( "n:title: " )
                .em().write( comparison.getMatch().getTitle() )._em()
            ._span()
            .span( class_( "label label-info" ) ).write( "o:url: " )
                .em().write( comparison.getReference().getUrl() )._em()
            ._span()
            .span( class_( "label label-info" ) ).write( "n:url: " )
                .em().write( comparison.getMatch().getUrl() )._em()
            ._span();

        html._div();

        html._div();

    }

}
