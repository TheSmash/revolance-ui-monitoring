package com.smash.revolance.ui.server.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by ebour on 11/01/14.
 */
public class Review
{
    @NotNull
    @NotEmpty
    private String applicationId;

    @NotNull
    @NotEmpty
    private String referenceApplicationId;

    private boolean firstVersion;

    @NotEmpty
    @NotNull
    private String reviewer;

    public void setReviewer(String reviewer)
    {
        this.reviewer = reviewer;
    }

    public String getReviewer()
    {
        return this.reviewer;
    }

    public void setFirstVersion(boolean b)
    {
        this.firstVersion = b;
    }

    public boolean isFirstVersion()
    {
        return this.firstVersion;
    }

    public void setApplicationId(String applicationId)
    {
        this.applicationId = applicationId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setReferenceApplicationId(String referenceApplicationId)
    {
        this.referenceApplicationId = referenceApplicationId;
    }

    public String getReferenceApplicationId()
    {
        return referenceApplicationId;
    }
}
