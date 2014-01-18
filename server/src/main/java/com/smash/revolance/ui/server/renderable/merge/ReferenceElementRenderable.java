package com.smash.revolance.ui.server.renderable.merge;

import com.smash.revolance.ui.comparator.element.ElementComparison;
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
public class ReferenceElementRenderable implements Renderable
{
    private ElementComparison comparison;
    private ElementBean       element;

    public ReferenceElementRenderable(String pageId, ElementComparison elementComparison)
    {
        this.comparison = elementComparison;
        if ( elementComparison.getReference() != null )
        {
            if( elementComparison.getDiffType() == DiffType.DELETED )
            {
                this.element = elementComparison.getReference();
            }
            else if( elementComparison.getDiffType() == DiffType.CHANGED )
            {
                this.element = elementComparison.getMatch();
            }
            else if( elementComparison.getDiffType() == DiffType.BASE )
            {
                this.element = elementComparison.getMatch();
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
                              .data( "impl", element.getImpl() )
                              .data( "href", element.getHref() )
                              .data( "bg", element.getBg() )
                              .data( "caption", element.getCaption() )
                              .data( "diff-type", comparison.getDiffType().toString() )
                              .data( "match-element-id", comparison.getReference() != null ? comparison.getReference().getInternalId() : element.getInternalId() ) )
                ._div();
        }
    }
}
