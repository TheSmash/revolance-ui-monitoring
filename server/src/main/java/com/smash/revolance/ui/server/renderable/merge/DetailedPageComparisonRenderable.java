package com.smash.revolance.ui.server.renderable.merge;

import com.smash.revolance.ui.comparator.page.PageComparison;
import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;

import java.io.IOException;

import static org.rendersnake.HtmlAttributesFactory.style;

/**
 * User: wsmash
 * Date: 20/09/13
 * Time: 23:44
 */
public class DetailedPageComparisonRenderable implements Renderable
{
    private PageComparison comparison;

    public DetailedPageComparisonRenderable(PageComparison comparison)
    {
        this.comparison = comparison;
    }

    @Override
    public void renderOn(HtmlCanvas html) throws IOException
    {
        html.div( style( "float: left;" ).class_( "ref" ) ).render( new ReferencePageRenderable( comparison ) )._div()
                .div( style( "float: left;" ).class_( "merge" ).onDrop( "$.handleDrop( this );" ).dropzone( "move string:text/plain" ) ).render( new MergePageRenderable( comparison ) )._div()
                .div( style( "float: left;" ).class_( "new" ) ).render( new NewPageRenderable( comparison ) )._div();
    }
}
