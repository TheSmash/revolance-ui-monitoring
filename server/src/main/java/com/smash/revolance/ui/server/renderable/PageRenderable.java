package com.smash.revolance.ui.server.renderable;

import com.smash.revolance.ui.model.element.api.ElementBean;
import com.smash.revolance.ui.model.page.api.PageBean;
import com.smash.revolance.ui.server.model.Settings;
import com.smash.revolance.ui.server.renderable.ContextualRenderable;
import org.rendersnake.HtmlCanvas;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.rendersnake.HtmlAttributesFactory.class_;

/**
 * User: wsmash
 * Date: 09/06/13
 * Time: 19:05
 */
public class PageRenderable extends ContextualRenderable
{

    private PageBean page;
    private List<String>      blackList = new ArrayList<String>();
    private List<ElementBean> content   = new ArrayList<ElementBean>();

    public PageRenderable(PageBean page)
    {
        this.page = page;
        this.content = page.getContent();
    }

    public PageRenderable withoutContent(List<String> content)
    {
        this.blackList = content;
        return this;
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
                              .data( "w", page.getWidth() )
                              .data( "h", page.getHeight() )
                              .data( "caption", page.getCaption() ) );

        for ( ElementBean element : content )
        {

            if ( !blackList.contains( element.getInternalId() ) )
            {
                html.render( new ElementRenderable( element ) );
            }
        }
        html._div()._div();
    }


}
