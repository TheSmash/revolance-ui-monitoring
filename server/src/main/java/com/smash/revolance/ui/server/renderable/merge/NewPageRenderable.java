package com.smash.revolance.ui.server.renderable.merge;

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
public class NewPageRenderable implements Renderable
{
    private final PageComparison comparison;
    private final PageBean       page;

    public NewPageRenderable(PageComparison comparison)
    {
        this.comparison = comparison;
        this.page = comparison.getMatch();
    }

    @Override
    public void renderOn(HtmlCanvas html) throws IOException
    {
        if ( page != null )
        {
            html.div( class_( "page-decorator new").style("float: left") )
                    .div( class_( "page" )
                                  .data( "w", page.getWidth() )
                                  .data( "h", page.getHeight() )
                                  .data( "id", page.getId())
                                  .data( "caption", page.getCaption() ) );
            for ( ElementComparison elementComparison : comparison.getElementComparisons() )
            {
                new NewElementRenderable( page.getId(), elementComparison ).renderOn( html );
            }
            html._div()._div();
        }
    }
}
