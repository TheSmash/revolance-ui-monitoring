package com.smash.revolance.ui.server.renderable;

import com.smash.revolance.ui.model.element.api.ElementBean;
import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;

import java.io.IOException;

import static org.rendersnake.HtmlAttributesFactory.class_;

/**
 * User: wsmash
 * Date: 09/06/13
 * Time: 19:13
 */
public class ElementRenderable implements Renderable
{
    private final ElementBean element;

    public ElementRenderable(ElementBean element)
    {
        this.element = element;
    }

    @Override
    public void renderOn(HtmlCanvas html) throws IOException
    {
        html.div( class_( "page-element" )
                          .data( "id", element.getInternalId() )
                          .data( "w", String.valueOf( element.getWidth() ) )
                          .data( "h", String.valueOf( element.getHeight() ) )
                          .data( "x", element.getX() )
                          .data( "y", element.getY() )
                          .data( "caption", element.getCaption() ) );
        html._div();
    }
}
