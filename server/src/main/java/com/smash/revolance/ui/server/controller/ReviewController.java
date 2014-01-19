package com.smash.revolance.ui.server.controller;

/*
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * ui-monitoring-server
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2012 - 2014 RevoLance
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

import com.smash.revolance.ui.database.IStorage;
import com.smash.revolance.ui.database.StorageException;
import com.smash.revolance.ui.materials.JsonHelper;
import com.smash.revolance.ui.model.element.ElementNotFound;
import com.smash.revolance.ui.model.sitemap.SiteMap;
import com.smash.revolance.ui.server.model.Review;
import com.smash.revolance.ui.server.model.ReviewBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by ebour on 12/01/14.
 */
@Controller
public class ReviewController
{
    IStorage applications;

    @Autowired
    public void setApplication(IStorage applications)
    {
        this.applications = applications;
    }

    IStorage reviews;

    @Autowired
    public void setReviews(IStorage reviews)
    {
        this.reviews = reviews;
    }


    @RequestMapping(value = "/reviews", method = RequestMethod.POST)
    public ModelAndView createReview(@Valid Review reviewForm, BindingResult result, @ModelAttribute("model") ModelMap model) throws StorageException, IOException, ElementNotFound
    {
        if (result.hasErrors())
        {
            return declareReview();
        }
        else
        {
            ReviewBean review = new ReviewBean();
            review.setApplication(reviewForm.getApplicationId());

            String previousApplication = (reviewForm.isFirstVersion() ? reviewForm.getApplicationId() : reviewForm.getReferenceApplicationId());
            review.setReferenceApplication(previousApplication);

            SiteMap sitemap = SiteMap.fromJson( applications.retrieve( review.getReferenceApplication() ) );
            review.setApplicationPagesCount(sitemap.getPagesCount());

            review.setReviewer(reviewForm.getReviewer());

            reviews.store(review.getId(), JsonHelper.getInstance().map(review));

            return listReviews(model);
        }
    }

    @RequestMapping(value = "/reviews", method = RequestMethod.GET)
    public ModelAndView listReviews(@ModelAttribute("model") ModelMap model) throws StorageException, IOException, ElementNotFound
    {
        model.addAttribute("reviews", getReviews());
        return new ModelAndView("ReviewList", model);
    }

    @RequestMapping(value = "/reviews/declare", method = RequestMethod.GET)
    public ModelAndView declareReview() throws StorageException, IOException, ElementNotFound
    {
        return new ModelAndView("ReviewForm", "applications", applications.getKeys());
    }

    @ResponseBody
    @RequestMapping(value = "/reviews/{reviewId}", method = RequestMethod.DELETE)
    public String deleteReview(@PathVariable String reviewId) throws StorageException
    {
        reviews.delete(reviewId);

        return "";
    }

    @ResponseBody
    @RequestMapping(value = "/reviews/{reviewId}/pages/{pageId}", method = RequestMethod.POST)
    public String commitPage(@PathVariable String reviewId, @PathVariable String pageId) throws StorageException, IOException, ElementNotFound
    {
        ReviewBean review = getReview(reviewId);
        review.setStatus("started");

        review.addReviewedPage(pageId);
        review.setProgress(computeProgress(review));

        if(review.getProgress()==100)
        {
            review.setStatus("closed");
        }

        reviews.store(review.getId(), JsonHelper.getInstance().map(review), true);

        return "";
    }

    public int computeProgress(ReviewBean reviewBean)
    {
        if(reviewBean.getApplicationPagesCount()==0)
        {
            return 100;
        }
        else
        {
            return 100*(reviewBean.getReviewedPages().size()/reviewBean.getApplicationPagesCount());
        }
    }

    public List<ReviewBean> getReviews()
    {
        List<ReviewBean> reviewList = new ArrayList<>();

        for(String o : reviews.retrieveAll().values())
        {
            try
            {
                ReviewBean review = (ReviewBean) JsonHelper.getInstance().map(ReviewBean.class, o);
                reviewList.add(review);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        return reviewList;
    }

    private ReviewBean getReview(String reviewId) throws StorageException, IOException
    {
        return (ReviewBean) JsonHelper.getInstance().map(ReviewBean.class, reviews.retrieve(reviewId));
    }
}
