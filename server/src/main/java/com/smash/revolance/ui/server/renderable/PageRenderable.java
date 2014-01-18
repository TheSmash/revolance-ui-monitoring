package com.smash.revolance.ui.server.renderable;

import com.smash.revolance.ui.model.page.api.PageBean;
import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;

import java.io.IOException;

import static org.rendersnake.HtmlAttributesFactory.class_;

/**
 * User: wsmash
 * Date: 09/06/13
 * Time: 19:05
 */
public class PageRenderable implements Renderable
{

    private boolean reviewed;
    private final PageBean page;
    private final String reviewId;

    public PageRenderable(String reviewId, PageBean page)
    {
        this.reviewId = reviewId;
        this.page = page;
    }

    public PageRenderable(String reviewId, boolean reviewed, PageBean page)
    {
        this(reviewId, page);
        this.reviewed = reviewed;
    }

    @Override
    public void renderOn(HtmlCanvas html) throws IOException
    {
        if(!reviewId.isEmpty())
        {
            html.div( class_( "page-decorator" )
                .data("page-reviewed", String.valueOf(reviewed)) );
        }
        else
        {
            html.div( class_( "page-decorator" ) );
        }

        html.div(class_("page")
                    .data("w", page.getWidth())
                    .data("h", page.getHeight())
                    .data("id", page.getId())
                    .data("caption", page.getCaption()));

        /*
            for ( ElementBean element : content )
            {
                html.render( new ElementRenderable( element ) );
            }
        */

        html._div()._div();
    }


}
