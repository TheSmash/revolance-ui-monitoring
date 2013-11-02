package com.smash.revolance.ui.server.renderable;

import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;

import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * User: wsmash
 * Date: 09/06/13
 * Time: 19:23
 */
public abstract class ContextualRenderable implements Renderable
{
    private HttpSession context;

    public Renderable withContext(HttpSession context)
    {
        this.context = context;
        return this;
    }

    public void renderOn(HtmlCanvas canvas) throws IOException
    {
        renderWithContext( canvas, context );
    }

    public abstract void renderWithContext(HtmlCanvas html, HttpSession context) throws IOException;

}
