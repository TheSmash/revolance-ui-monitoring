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
import com.smash.revolance.ui.server.renderable.merge.NewPageRenderable;
import com.smash.revolance.ui.server.renderable.merge.ReferencePageRenderable;
import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;

import java.io.IOException;

import static org.rendersnake.HtmlAttributesFactory.class_;

/**
 * User: wsmash
 * Date: 10/06/13
 * Time: 19:01
 */
public class DetailedPageComparisonRenderable implements Renderable
{
    private final PageComparison comparison;
    private final String newApp;
    private final String refApp;
    private final String reviewId;

    public DetailedPageComparisonRenderable(String reviewId, PageComparison comparison, String contentTagRef, String contentTagNew)
    {
        this.comparison = comparison;
        this.refApp = contentTagRef;
        this.newApp = contentTagNew;
        this.reviewId = reviewId;
    }

    @Override
    public void renderOn(HtmlCanvas html) throws IOException
    {
        html.div(class_("row"))
                .div(class_("offset1 span6"))
                    .div(class_("legend"))
                    .div()
                        .div(class_("icon").data("diff-type", "DELETED"))._div()
                        .content("deleted")
                    ._div()
                    .div(class_("legend"))
                        .div()
                        .div(class_("icon").data("diff-type", "CHANGED"))._div()
                        .content("changed")
                    ._div()
                    .div(class_("legend"))
                        .div()
                        .div(class_("icon").data("diff-type", "ADDED"))._div()
                        .content("added")
                    ._div()
                ._div()
                .h5(class_("counter span3").id("differency-count"))._h5()
                .render(new ComparisonToolbarRenderable(reviewId, comparison, refApp, newApp))
            ._div()
            .div(class_("row"))
                .div(class_("span6").style("text-align:center;"))
                    .content("previous revision")
                .div(class_("span6").style("text-align:center;"))
                    .content("current")
            ._div()
            .div(class_("row"))
                .render(new ReferencePageRenderable(comparison))
                .render(new NewPageRenderable(comparison))
            ._div();
    }

}
