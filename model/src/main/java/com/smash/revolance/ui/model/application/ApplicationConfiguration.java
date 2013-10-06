package com.smash.revolance.ui.model.application;

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

import java.io.File;
import java.util.ArrayList;

/**
 * User: wsmash
 * Date: 26/01/13
 * Time: 16:03
 */
public class ApplicationConfiguration
{
    private boolean followLinks;
    private boolean followButtons;
    private boolean takePageScreenshot;
    private boolean takePageElementScreenshot;

    private ArrayList<String> excludedLinks   = new ArrayList<String>();
    private ArrayList<String> excludedButtons = new ArrayList<String>();

    private String id                        = "";
    private String domain                    = "";
    private String reportFolder              = ".";
    private String browserBinary             = "";
    private String applicationDir            = ".";
    private String applicationVersion        = "";
    private String applicationImplementation = "";

    private File    appCfgFile;
    private File    userCfgFile;
    private int     browserHeight;
    private int     browserWidth;
    private String  driverPath;
    private String  browserType;
    private boolean exploreVariants;


    public ApplicationConfiguration()
    {

    }

    public ApplicationConfiguration(ApplicationConfiguration setup)
    {
        this.domain = setup.domain;
        this.appCfgFile = new File( setup.appCfgFile.getAbsolutePath() );
        this.userCfgFile = new File( setup.userCfgFile.getAbsolutePath() );
        this.id = setup.id;

        this.excludedButtons = new ArrayList<String>( setup.excludedButtons );
        this.excludedLinks = new ArrayList<String>( setup.excludedLinks );
        this.reportFolder = setup.reportFolder;

        this.followButtons = setup.followButtons;
        this.followLinks = setup.followLinks;
        this.takePageScreenshot = setup.takePageScreenshot;
        this.takePageElementScreenshot = setup.takePageElementScreenshot;

        this.browserHeight = setup.browserHeight;
        this.browserWidth = setup.browserWidth;
        this.browserBinary = setup.browserBinary;
        this.applicationDir = setup.applicationDir;
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

    public void setReportFolder(String reportFolder)
    {
        if ( reportFolder != null )
            this.reportFolder = reportFolder;
    }

    public String getReportFolder()
    {
        return reportFolder;
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

    public String getApplicationImplementation()
    {
        return applicationImplementation;
    }

    public void setApplicationImpl(String applicationImplementation)
    {
        if ( applicationImplementation != null )
            this.applicationImplementation = applicationImplementation;
    }

    public File getAppCfgFile()
    {
        return appCfgFile;
    }

    public File getUsersCfgFile()
    {
        return userCfgFile;
    }

    public void setUsersCfgFile(String userCfgFile)
    {
        this.userCfgFile = new File( getAppCfgFile().getParentFile(), userCfgFile ).getAbsoluteFile();
    }

    public void setAppCfgFile(File appCfgFile)
    {
        this.appCfgFile = appCfgFile;
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

    public void setBrowserBinary(String browserBinary)
    {
        this.browserBinary = browserBinary;
    }

    public String getBrowserBinary()
    {
        return browserBinary;
    }

    public void setApplicationDir(String applicationDir)
    {
        if ( applicationDir != null )
            this.applicationDir = applicationDir;
    }

    public String getApplicationDir()
    {
        return new File( appCfgFile.getParentFile().getParentFile(), applicationDir ).getAbsolutePath();
    }

    public String getApplicationVersion()
    {
        return applicationVersion;
    }

    public void setApplicationVersion(String applicationVersion)
    {
        this.applicationVersion = applicationVersion;
    }

    public String getDriverPath()
    {
        return driverPath;
    }

    public void setDriverPath(String driverPath)
    {
        if ( driverPath != null )
            this.driverPath = driverPath;
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
}
