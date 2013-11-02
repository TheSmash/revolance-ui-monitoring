package com.smash.revolance.ui.explorer;

/*
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Revolance-UI-Explorer
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2012 - 2013 RevoLance
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

import java.io.File;
import java.util.ArrayList;

/**
 * User: wsmash
 * Date: 26/01/13
 * Time: 16:03
 */
public class ExplorationConfiguration
{
    private boolean followLinks;
    private boolean followButtons;
    private boolean takePageScreenshot;
    private boolean takePageElementScreenshot;

    private ArrayList<String> excludedLinks   = new ArrayList<String>();
    private ArrayList<String> excludedButtons = new ArrayList<String>();

    private String id     = "";
    private String domain = "";
    private String url = "";
    private String browserType = "";

    private int     browserWidth;
    private int     browserHeight;
    private boolean exploreVariants;
    private int timeout = 0;

    private File reportFile;
    private File logFile;

    public ExplorationConfiguration()
    {

    }

    public void setFollowLinks(boolean b)
    {
        this.followLinks = b;
    }

    public boolean isFollowLinksEnabled()
    {
        return followLinks;
    }

    public void setPageScreenshotEnabled(boolean b)
    {
        takePageScreenshot = b;
    }

    public boolean isPageScreenshotEnabled()
    {
        return takePageScreenshot;
    }

    public void setExcludedLinks(ArrayList<String> excludedLinks)
    {
        this.excludedLinks = excludedLinks;
    }

    public ArrayList<String> getExcludedLinks()
    {
        return excludedLinks;
    }

    public void setExcludedButtons(ArrayList<String> excludedButtons)
    {
        this.excludedButtons = excludedButtons;
    }

    public ArrayList<String> getExcludedButtons()
    {
        return excludedButtons;
    }

    public void setFollowButtons(boolean followButtons)
    {
        this.followButtons = followButtons;
    }

    public boolean isFollowButtonsEnabled()
    {
        return followButtons;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setDomain(String domain)
    {
        this.domain = domain;
    }

    public String getDomain()
    {
        return this.domain;
    }

    public void setPageElementScreenshotEnabled(boolean b)
    {
        this.takePageElementScreenshot = b;
    }

    public boolean isPageElementScreenshotEnabled()
    {
        return takePageElementScreenshot;
    }

    public void setBrowserHeight(int browserHeight)
    {
        this.browserHeight = browserHeight;
    }

    public int getBrowserHeight()
    {
        return browserHeight;
    }

    public void setBrowserWidth(int browserWidth)
    {
        this.browserWidth = browserWidth;
    }

    public int getBrowserWidth()
    {
        return browserWidth;
    }

    public String getBrowserType()
    {
        return browserType;
    }

    public void setBrowserType(String browserType)
    {
        if ( browserType != null )
            this.browserType = browserType;
    }

    public boolean isExploreVariantsEnabled()
    {
        return exploreVariants;
    }

    public void setExploreVariantsEnabled(boolean b)
    {
        exploreVariants = b;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        if(url != null)
            this.url = url;
    }

    public void setTimeout(int timeout)
    {
        this.timeout = timeout;
    }

    public int getTimeout()
    {
        return timeout;
    }

    public void setLogFile(File logFile)
    {
        this.logFile = logFile;
    }

    public File getLogFile()
    {
        return logFile;
    }

    public void setReportFile(File reportFile)
    {
        this.reportFile = reportFile;
    }

    public File getReportFile()
    {
        return reportFile;
    }
}
