package com.smash.revolance.ui.server.renderable.merge;

import com.smash.revolance.ui.comparator.element.ElementComparison;
import com.smash.revolance.ui.comparator.page.PageComparison;
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
 * Time: 23:49
 */
public class NewPageRenderable extends ContextualRenderable
{
    private final PageComparison comparison;
    private final PageBean       page;

    public NewPageRenderable(PageComparison comparison)
    {
        this.comparison = comparison;
        this.page = comparison.getMatch();
    }

    @Override
    public void renderWithContext(HtmlCanvas html, HttpSession context) throws IOException
    {
        if ( page != null )
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
                                  .data( "w", page.getWidth() )
                                  .data( "h", page.getHeight() )
                                  .data( "caption", page.getCaption() ) );
            for ( ElementComparison elementComparison : comparison.getElementComparisons() )
            {
                new NewElementRenderable( page.getId(), elementComparison ).renderOn( html );
            }
            html._div()._div();
        }
    }
}
