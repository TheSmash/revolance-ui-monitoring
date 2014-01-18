package com.smash.revolance.ui.server.renderable.merge;

import com.smash.revolance.ui.comparator.element.ElementComparison;
import com.smash.revolance.ui.comparator.element.ElementDifferency;
import com.smash.revolance.ui.model.element.api.ElementBean;
import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;

import java.io.IOException;

import static org.rendersnake.HtmlAttributesFactory.class_;

/**
 * User: wsmash
 * Date: 20/09/13
 * Time: 23:52
 */
public class MergeElementRenderable implements Renderable
{
    private final ElementComparison comparison;

    public MergeElementRenderable(ElementComparison comparison)
    {
        this.comparison = comparison;
    }

    @Override
    public void renderOn(HtmlCanvas html) throws IOException
    {
        if ( comparison.getReference() != null )
        {
            if ( !comparison.getElementDifferencies().isEmpty() ) // We only render elements is needed
            {
                ElementBean element = comparison.getMatch();
                html.div( class_( "page-element" )
                                  .data( "w", String.valueOf( element.getWidth() ) )
                                  .data( "h", String.valueOf( element.getHeight() ) )
                                  .data( "x", element.getX() )
                                  .data( "y", element.getY() )
                                  .data( "caption", element.getCaption() )
                                  .data( "diff-type", comparison.getDiffType().toString() )
                                  .data( "ref-element-id", comparison.getMatch().getId() )
                                  .data( "match-element-id", comparison.getReference().getId() ) )
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
}
