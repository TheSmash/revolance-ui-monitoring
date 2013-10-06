package com.smash.revolance.ui.server.renderable;

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

import com.smash.revolance.ui.comparator.page.PageComparison;
import com.smash.revolance.ui.server.renderable.merge.DetailedPageComparisonRenderable;
import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;

import java.io.IOException;

import static org.rendersnake.HtmlAttributesFactory.*;

/**
 * User: wsmash
 * Date: 10/06/13
 * Time: 19:01
 */
public class MergerRenderable implements Renderable
{
    private final PageComparison comparison;

    public MergerRenderable(PageComparison comparison)
    {
        this.comparison = comparison;
    }

    @Override
    public void renderOn(HtmlCanvas html) throws IOException
    {
        html.table( class_( "table" ).id( "merger" ) )
                .tbody()
                .tr()
                .td( class_( "span1" ) )
                ._td()
                .td()
                .div( class_( "counter" ).id( "ref-element-left-count" ) )._div()
                ._td()
                .td()
                .div( class_( "toolbar" ) )
                .button( class_( "btn btn-success disabled" )
                                 .id( "commit-button" )
                                 .onClick( "$.applyChanges( this );" )
                                 .data( "ref-page-id", comparison.getReference().getId() )
                                 .data( "new-page-id", comparison.getMatch().getId() ) ).i( class_( "icon-share" ) )._i()._button()
                .button( class_( "btn btn-warning disabled" )
                                 .id( "revert-button" )
                                 .onClick( "$.revertChanges( this );" )
                                 .data( "ref-page-id", comparison.getReference().getId() )
                                 .data( "new-page-id", comparison.getMatch().getId() ) ).div( class_( "revert" ) )._div()._button()
                ._div()
                ._td()
                .td()
                .div( class_( "counter" ).id( "element-right-count" ) )._div()
                ._td()
                .td( class_( "span1" ) )
                ._td()
                ._tr()
                .tr()
                .td( style( "vertical-align: middle" ) )
                .a( href( "#" ).onClick( "$.previous(this);" ) ).i( class_( "icon-chevron-left" ) )._i()._a()
                ._td()
                .td( colspan( "3" ).id( "dialog" ) )
                .render( new DetailedPageComparisonRenderable( comparison ) )
                ._td()
                .td( style( "vertical-align: middle" ) )
                .a( href( "#" ).onClick( "$.next(this);" ) ).i( class_( "icon-chevron-right" ) )._i()._a()
                ._td()
                ._tr()
                .tr()
                .td( colspan( "5" ) )
                .div( class_( "row trash" ).onDrop( "$.handleTrashDrop(this);" ).dropzone( "move string:text/plain" ) )
                .i( class_( "icon-trash" ) )._i()
                ._div()
                ._td()
                ._tr()
                ._tbody();
    }

}
