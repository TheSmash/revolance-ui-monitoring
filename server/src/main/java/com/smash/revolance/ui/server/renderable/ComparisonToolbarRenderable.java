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
import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;

import java.io.IOException;

import static org.rendersnake.HtmlAttributesFactory.class_;

/**
 * Created by ebour on 26/12/13.
 */
public class ComparisonToolbarRenderable implements Renderable
{
    private final PageComparison comparison;
    private final String newApp;
    private final String refApp;
    private final String reviewId;

    public ComparisonToolbarRenderable(String reviewId, PageComparison comparison, String refApp, String newApp)
    {
        this.comparison = comparison;
        this.refApp = refApp;
        this.newApp = newApp;
        this.reviewId = reviewId;
    }

    @Override
    public void renderOn(HtmlCanvas html) throws IOException
    {
        if(!reviewId.isEmpty())
        {
            html
                .div(class_("toolbar span2"))
                    .a( class_( "disabled" )
                            .id( "commit-button" )
                            .onClick( "$.applyChanges( this );" )
                            .data( "ref-app-id", refApp)
                            .data( "new-app-id", newApp)
                            .data( "review-id", reviewId)
                            .data( "ref-page-id", comparison.getReference().getId() )
                            .data( "new-page-id", comparison.getMatch().getId() ) ).div( class_( "commit" ) )._div()._a()
                    .a( class_( "disabled" )
                            .id( "revert-button" )
                            .onClick( "$.revertChanges( this );" )
                            .data( "ref-app-id", refApp)
                            .data("new-app-id", newApp)
                            .data("ref-page-id", comparison.getReference().getId())
                            .data("new-page-id", comparison.getMatch().getId()) ).div( class_( "revert" ) )._div()._a()
                .a( class_("disabled").id("previous-button").href( "#" ).onClick( "$.previous(this);" ) ).div( class_( "previous" ) )._div()._a()
                .a( class_("disabled").id("next-button").href( "#" ).onClick( "$.next(this);" ) ).div( class_( "next" ) )._div()._a()
                .a( class_("").id("confirm-all-button").href( "#" ).onClick( "$.confirmAllChanges(this);" ) ).div( class_( "confirm-all" ) )._div()._a()
                /*.a( class_( "disabled")
                        .id("next-button")
                        .onClick( "$.firstDifferency( this );" ) ).div( class_( "next" ) )._div()._a()*/
            ._div();
        }
    }
}
