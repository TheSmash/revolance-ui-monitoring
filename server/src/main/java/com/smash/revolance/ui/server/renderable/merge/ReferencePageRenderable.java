package com.smash.revolance.ui.server.renderable.merge;

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

import com.smash.revolance.ui.comparator.element.ElementComparison;
import com.smash.revolance.ui.comparator.page.PageComparison;
import com.smash.revolance.ui.model.page.api.PageBean;
import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;

import java.io.IOException;

import static org.rendersnake.HtmlAttributesFactory.class_;

/**
 * User: wsmash
 * Date: 20/09/13
 * Time: 23:49
 */
public class ReferencePageRenderable implements Renderable
{
    private final PageComparison comparison;
    private final PageBean       page;

    public ReferencePageRenderable(PageComparison comparison)
    {
        this.comparison = comparison;
        this.page = comparison.getReference();
    }

    @Override
    public void renderOn(HtmlCanvas html) throws IOException
    {
        if ( page != null )
        {
            html.div( class_( "page-decorator ref").style("float: left") )
                    .div( class_( "page" )
                                  .data( "w", page.getWidth() )
                                  .data( "h", page.getHeight() )
                                  .data( "id", page.getId())
                                  .data( "caption", page.getCaption() ) );
            for ( ElementComparison elementComparison : comparison.getElementComparisons() )
            {
                new ReferenceElementRenderable( page.getId(), elementComparison ).renderOn( html );
            }
            html._div()._div();
        }
    }
}
