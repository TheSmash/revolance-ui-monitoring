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
import com.smash.revolance.ui.comparator.element.ElementDifferency;
import com.smash.revolance.ui.model.diff.DiffType;
import com.smash.revolance.ui.model.element.api.ElementBean;
import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;

import java.io.IOException;

import static org.rendersnake.HtmlAttributesFactory.class_;

/**
 * User: wsmash
 * Date: 20/09/13
 * Time: 23:51
 */
public class NewElementRenderable implements Renderable
{
    private ElementComparison comparison;
    private ElementBean element = null;

    public NewElementRenderable(String pageId, ElementComparison elementComparison)
    {
        this.comparison = elementComparison;
        if ( elementComparison.getMatch() != null )
                // && elementComparison.getMatch().getPage().getId().contentEquals( pageId ) )
        {
            if( elementComparison.getDiffType() == DiffType.ADDED )
            {
                this.element = elementComparison.getMatch();
            }
            else if( elementComparison.getDiffType() == DiffType.CHANGED )
            {
                this.element = elementComparison.getReference();
            }
            else if( elementComparison.getDiffType() == DiffType.BASE )
            {
                this.element = elementComparison.getReference();
            }
        }
    }

    @Override
    public void renderOn(HtmlCanvas html) throws IOException
    {
        if ( element != null )
        {
            html.div( class_( "page-element" )
                              .data( "id", element.getInternalId() )
                              .data( "w", String.valueOf( element.getWidth() ) )
                              .data( "h", String.valueOf( element.getHeight() ) )
                              .data( "x", element.getX() )
                              .data( "y", element.getY() )
                              .data( "caption", element.getCaption() )
                              .data( "impl", element.getImpl() )
                              .data( "href", element.getHref() )
                              .data( "bg", element.getBg() )
                              .data( "diff-type", comparison.getDiffType().toString() )
                              .data( "ref-element-id", comparison.getReference() != null ? comparison.getReference().getInternalId() : "" )
                              .data( "match-element-id", comparison.getMatch() != null ? comparison.getMatch().getInternalId() : "" ) )
                    .div( class_( "comparisons" ) );

            if ( comparison.getElementDifferencies().contains( ElementDifferency.POS ) )
            {
                html.div( class_( "pos-comparison" ) )._div();
            }
            if ( comparison.getElementDifferencies().contains( ElementDifferency.LOOK ) )
            {
                html.div( class_( "look-comparison" ) )._div();
            }
            if ( comparison.getElementDifferencies().contains( ElementDifferency.IMPL ) )
            {
                html.div( class_( "look-comparison" ) )._div();
            }
            if ( comparison.getElementDifferencies().contains( ElementDifferency.VALUE ) )
            {
                html.div( class_( "value-comparison" ) )._div();
            }
            if ( element.getImpl().contentEquals( "Button" ) || element.getImpl().contentEquals( "Link" )
                    && comparison.getElementDifferencies().contains( ElementDifferency.TARGET ) )
            {
                html.div( class_( "target-comparison" ).data( "changed", comparison.getElementDifferencies().contains( ElementDifferency.TARGET ) ? "true" : "false" ) )._div();
            }

            html._div();
            html._div();
        }
    }
}
