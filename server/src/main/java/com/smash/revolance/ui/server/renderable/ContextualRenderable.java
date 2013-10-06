package com.smash.revolance.ui.server.renderable;

/*
        This file is part of Revolance UI Suite.

        Revolance UI Suite is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        Revolance UI Suite is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with Revolance UI Suite.  If not, see <http://www.gnu.org/licenses/>.
*/

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
