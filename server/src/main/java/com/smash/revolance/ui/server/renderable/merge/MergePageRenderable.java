package com.smash.revolance.ui.server.renderable.merge;

import com.smash.revolance.ui.comparator.element.ElementComparison;
import com.smash.revolance.ui.comparator.page.PageComparison;
import com.smash.revolance.ui.model.diff.DiffType;
import com.smash.revolance.ui.model.page.api.PageBean;
import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;

import java.io.IOException;

import static org.rendersnake.HtmlAttributesFactory.class_;

/**
 * User: wsmash
 * Date: 20/09/13
 * Time: 23:51
 */
public class MergePageRenderable implements Renderable
{
    private final PageBean       match;
    private final PageBean       reference;
    private final PageComparison comparison;

    public MergePageRenderable(PageComparison comparison)
    {
        this.comparison = comparison;
        this.reference = comparison.getReference();
        this.match = comparison.getMatch();
    }

    @Override
    public void renderOn(HtmlCanvas html) throws IOException
    {
        html.div( class_( "page-decorator" ) )
                .div( class_( "page" )
                              .data( "w", reference.getWidth() )
                              .data( "h", reference.getHeight() ) );
        //.data( "caption", page.getCaption() ) );
        for ( ElementComparison elementComparison : comparison.getElementComparisons() )
        {
            if ( elementComparison.getDiffType() == DiffType.CHANGED )
            {
                html.render( new MergeElementRenderable( elementComparison ) );
            }
        }
        html._div()._div();
    }
}
