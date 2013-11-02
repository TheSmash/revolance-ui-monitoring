package com.smash.revolance.ui.server.renderable;

import com.smash.revolance.ui.comparator.page.PageComparison;
import com.smash.revolance.ui.model.diff.PageDiffType;
import org.rendersnake.HtmlCanvas;

import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.rendersnake.HtmlAttributesFactory.class_;
import static org.rendersnake.HtmlAttributesFactory.href;

/**
 * User: wsmash
 * Date: 09/06/13
 * Time: 19:20
 */
public class PageComparisonRenderable extends ContextualRenderable
{
    private final PageComparison comparison;

    public PageComparisonRenderable(PageComparison comparison)
    {
        this.comparison = comparison;
    }

    @Override
    public void renderWithContext(HtmlCanvas html, HttpSession context) throws IOException
    {
        html.div( class_( "span1" ) )
                .a( href( "#" ).onClick( "$.previousVariant( this );" ) ).i( class_( "icon-chevron-left" ) )._i()._a()
                ._div()
                .div( class_( "span4 ref-page" ) )
                .render( new PageRenderable( comparison.getReference() ).withContext( context ) )
                ._div()
                .div( class_( "span4 new-page" ) )
                .render( new PageRenderable( comparison.getMatch() ).withContext( context ) )
                ._div()
                .div( class_( "span1" ) )
                .a( href( "#" ).onClick( "$.nextVariant( this );" ) ).i( class_( "icon-chevron-right" ) )._i()._a()
                ._div()
                .div( class_( "data-container span10" ) )
                .p( class_( "data-container-title" ) )
                .i( class_( "icon-tags" ) )._i().write( "  Tags" )
                ._p()
                .div( class_( "span10" ) )
                .span( class_( "label label-info" ) ).write( "o:title: " )
                .em().write( comparison.getReference().getTitle() )._em()
                ._span()
                ._div()
                .div( class_( "span10" ) )
                .span( class_( "label label-info" ) ).write( "n:title: " )
                .em().write( comparison.getMatch().getTitle() )._em()
                ._span()
                ._div()
                .div( class_( "span10" ) )
                .span( class_( "label label-info" ) ).write( "o:url: " )
                .em().write( comparison.getReference().getUrl() )._em()
                ._span()
                ._div()
                .div( class_( "span10" ) )
                .span( class_( "label label-info" ) ).write( "n:url: " )
                .em().write( comparison.getMatch().getUrl() )._em()
                ._span()
                ._div()
                .div( class_( "cloud-tag span10" ) );
        if ( comparison.getPageDifferencies().isEmpty() )
        {
            html.span( class_( "label label-success no-changes" ) )
                    .write( "change: none" )
                    ._span();

        } else
        {
            if ( comparison.getPageDifferencies().contains( PageDiffType.CONTENT ) )
            {
                html.span( class_( "label label-important content" ) )
                        .write( "change: content" )
                        ._span();
            }
            if ( comparison.getPageDifferencies().contains( PageDiffType.LAYOUT ) )
            {
                html.span( class_( "label label-warning layout" ) )
                        .write( "change: layout" )
                        ._span();
            }
            if ( comparison.getPageDifferencies().contains( PageDiffType.LOOK ) )
            {
                html.span( class_( "label label-warning look" ) )
                        .write( "change: look" )
                        ._span();
            }
            if ( comparison.getPageDifferencies().contains( PageDiffType.TITLE ) )
            {
                html.span( class_( "label label-info title" ) )
                        .write( "change: title" )
                        ._span();
            }
            if ( comparison.getPageDifferencies().contains( PageDiffType.URL ) )
            {
                html.span( class_( "label label-info url" ) )
                        .write( "change: url" )
                        ._span();
            }
        }

        html._div();

        html._div();

    }

}
