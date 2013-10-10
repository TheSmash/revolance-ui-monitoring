package com.smash.revolance.ui.server.renderable.merge;

import com.smash.revolance.ui.comparator.element.ElementComparison;
import com.smash.revolance.ui.comparator.element.ElementDifferency;
import com.smash.revolance.ui.model.element.api.ElementBean;
import com.smash.revolance.ui.server.renderable.ContextualRenderable;
import org.rendersnake.HtmlCanvas;

import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.rendersnake.HtmlAttributesFactory.class_;

/**
 * User: wsmash
 * Date: 20/09/13
 * Time: 23:51
 */
public class ReferenceElementRenderable extends ContextualRenderable
{
    private ElementComparison comparison;
    private ElementBean       element;

    public ReferenceElementRenderable(String pageId, ElementComparison elementComparison)
    {
        this.comparison = elementComparison;
        if ( elementComparison.getReference() != null && elementComparison.getReference().getPage().getId().contentEquals( pageId ) )
        {
            this.element = elementComparison.getReference();
        }
    }

    @Override
    public void renderWithContext(HtmlCanvas html, HttpSession context) throws IOException
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
                              .data( "diff-type", comparison.getDiffType().toString() )
                              .data( "match-element-id", comparison.getReference() != null ? comparison.getReference().getInternalId() : element.getInternalId() ) )
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
                        if ( element.getImpl().contentEquals( "Button" ) || element.getImpl().contentEquals( "Link" ) && comparison.getElementDifferencies().contains( ElementDifferency.TARGET ) )
                        {
                            html.div( class_( "target-comparison" ).data( "changed", comparison.getElementDifferencies().contains( ElementDifferency.TARGET ) ? "true" : "false" ) )._div();
                        }
                    html._div();
            html._div();
        }
    }
}
