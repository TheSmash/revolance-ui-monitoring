package com.smash.revolance.ui.server.renderable;

import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;

import java.io.IOException;

import static org.rendersnake.HtmlAttributesFactory.class_;
import static org.rendersnake.HtmlAttributesFactory.id;

/**
 * Created by ebour on 13/01/14.
 */
public class ApplicationDetailsToolbarRenderable implements Renderable
{
    private final String reviewId;

    public ApplicationDetailsToolbarRenderable(final String reviewId)
    {
        this.reviewId = reviewId;
    }

    @Override
    public void renderOn(HtmlCanvas html) throws IOException
    {
        if(!reviewId.isEmpty())
        {
            html
                .div(class_("row"))
                    .div(class_("page-toolbar pull-right"))
                        .div(id("commit-button").class_("commit disabled").onClick("$.applyChanges(this)").data("review-id", reviewId))._div()
                        .div(id("revert-button").class_("revert disabled"))._div()
                        .a(class_("disabled").id("previous-button").href("#").onClick("$.previous(this);")).div( class_( "previous" ) )._div()._a()
                        .a(class_("disabled").id("next-button").href("#").onClick("$.next(this);")).div( class_( "next" ) )._div()._a()
                        .div(id("confirm-all-button").class_("confirm-all").onClick("$.confirmAllChanges(this)"))._div()
                        .a(id("link").hidden("true").href("#"))._a()
                    ._div()
                ._div();
        }
    }
}
