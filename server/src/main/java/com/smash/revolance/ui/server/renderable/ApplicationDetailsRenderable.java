package com.smash.revolance.ui.server.renderable;

import com.smash.revolance.ui.database.FileSystemStorage;
import com.smash.revolance.ui.database.IStorage;
import com.smash.revolance.ui.database.StorageException;
import com.smash.revolance.ui.materials.JsonHelper;
import com.smash.revolance.ui.model.page.api.PageBean;
import com.smash.revolance.ui.server.model.ReviewBean;
import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;

import java.io.IOException;
import java.util.List;

import static org.rendersnake.HtmlAttributesFactory.class_;

/**
 * Created by ebour on 13/01/14.
 */
public class ApplicationDetailsRenderable implements Renderable
{
    IStorage reviews = new FileSystemStorage("reviews");

    private final List<PageBean> pages;
    private final String reviewId;

    public ApplicationDetailsRenderable(final String reviewId, final List<PageBean> pages)
    {
        this.pages = pages;
        this.reviewId = reviewId;
    }

    @Override
    public void renderOn(HtmlCanvas html) throws IOException
    {
        html.render(new ApplicationDetailsToolbarRenderable(reviewId))
            .div(class_("row"))
                .div(class_("span12"));
                    for(PageBean page : pages)
                    {
                        try
                        {
                            if(!reviewId.isEmpty())
                            {
                                boolean reviewed = getReview(reviewId).hasBeenReviewed(page.getId());
                                html.div(class_("span6 centered"));
                                    html.render(new PageRenderable(reviewId, reviewed, page));
                                html._div();
                                // new PageInformationRenderable( pageBean ).renderOn(canvas );
                            }
                            else
                            {
                                html.div(class_("span6 centered"));
                                    html.render(new PageRenderable(reviewId, page));
                                html._div();
                            }
                        }
                        catch (Exception e)
                        {
                            // TODO log this
                        }
                    }
        html
                ._div()
           ._div();
    }

    private ReviewBean getReview(String reviewId) throws StorageException, IOException
    {
        return (ReviewBean) JsonHelper.getInstance().map(ReviewBean.class, reviews.retrieve(reviewId));
    }

}
