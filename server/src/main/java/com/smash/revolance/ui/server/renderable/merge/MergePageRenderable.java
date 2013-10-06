package com.smash.revolance.ui.server.renderable.merge;

import com.smash.revolance.ui.comparator.element.ElementComparison;
import com.smash.revolance.ui.comparator.page.PageComparison;
import com.smash.revolance.ui.model.diff.DiffType;
import com.smash.revolance.ui.model.page.api.PageBean;
import com.smash.revolance.ui.server.model.Settings;
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
public class MergePageRenderable extends ContextualRenderable
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
    public void renderWithContext(HtmlCanvas html, HttpSession context) throws IOException
    {
        Settings settings = null;
        if ( context != null )
        {
            settings = (Settings) context.getAttribute( "settings" );
        }
        if ( settings == null )
        {
            settings = new Settings();
        }
        int w = settings.getDecoratorWidth();
        int h = settings.getDecoratorHeight();
        html.div( class_( "page-decorator" )
                          .style( "width: " + w + "px; height: " + h + "px" ) )
                .div( class_( "page" )
                              .data( "w", reference.getWidth() )
                              .data( "h", reference.getHeight() ) );
        //.data( "caption", page.getCaption() ) );
        for ( ElementComparison elementComparison : comparison.getElementComparisons() )
        {
            if ( elementComparison.getDiffType() == DiffType.BASE
                    && elementComparison.getElementDifferencies().isEmpty() )
            {
                html.render( new MergeElementRenderable( elementComparison ) );
            }
        }
        html._div()._div();
    }
}
