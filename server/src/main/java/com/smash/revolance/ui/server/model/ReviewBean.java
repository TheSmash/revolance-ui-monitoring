package com.smash.revolance.ui.server.model;

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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by ebour on 12/01/14.
 */
public class ReviewBean
{
    private String id = "";

    private String application = "";

    private String referenceApplication = "";
    List<String> reviewedPages = new ArrayList<>();

    private String status = "created";
    private int applicationPagesCount = 0;

    private int progress = 0;

    private String reviewer = "";

    public ReviewBean()
    {
        this.id = UUID.randomUUID().toString();
    }

    public void setApplication(String application)
    {
        this.application = application;
    }

    public void setReferenceApplication(String referenceApplication)
    {
        this.referenceApplication = referenceApplication;
    }

    public String getId()
    {
        return this.id;
    }

    public String getReferenceApplication()
    {
        return this.referenceApplication;
    }

    public String getApplication()
    {
        return this.application;
    }

    public boolean hasBeenReviewed(String page)
    {
        return this.reviewedPages.contains(page);
    }

    public void addReviewedPage(String page)
    {
        if(!this.hasBeenReviewed(page))
        {
            this.reviewedPages.add(page);
        }
    }

    public String getStatus()
    {
        return this.status;
    }

    public void setApplicationPagesCount(int applicationPagesCount)
    {
        this.applicationPagesCount = applicationPagesCount;
    }

    public int getApplicationPagesCount()
    {
        return applicationPagesCount;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public void setReviewer(String reviewer)
    {
        this.reviewer = reviewer;
    }

    public String getReviewer()
    {
        return reviewer;
    }

    public void clearReviewedPagesList()
    {
        this.reviewedPages.clear();
    }

    public List<String> getReviewedPages()
    {
        return this.reviewedPages;
    }

    public int getProgress()
    {
        return progress;
    }

    public void setProgress(int progress)
    {
        this.progress = progress;
    }
}
