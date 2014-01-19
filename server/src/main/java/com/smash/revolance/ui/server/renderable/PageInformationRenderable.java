package com.smash.revolance.ui.server.renderable;

import com.smash.revolance.ui.model.element.api.ElementBean;
import com.smash.revolance.ui.model.page.api.PageBean;
import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;

import java.io.IOException;

import static org.rendersnake.HtmlAttributesFactory.class_;

/**
 * Created by ebour on 11/01/14.
 */
public class PageInformationRenderable implements Renderable
{
    private final PageBean page;

    public PageInformationRenderable(PageBean page)
    {
        this.page = page;
    }

    @Override
    public void renderOn(HtmlCanvas html) throws IOException
    {
        html.div(class_("page-details well"))
                .div(class_("page-buttons"))
                    .div(class_("count")).content(page.getButtons().size()+" buttons");
                    for(ElementBean button : page.getButtons())
                        html.div(class_("target")).content( button.getContent() + "->" + button.getHref() );
                html._div()
                .div(class_("page-links"))
                    .div(class_("count")).content(page.getLinks().size()+" links");
                    for(ElementBean link : page.getLinks())
                        html.div(class_("target")).content( link.getContent() + "->" + link.getHref() );
                html._div()
            ._div();
    }
}
