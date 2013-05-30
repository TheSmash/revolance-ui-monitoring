package com.smash.revolance.ui.explorer.application;

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
public class ApplicationSetup
{
    private boolean followLinks;
    private boolean followButtons;
    private boolean takePageScreenshot;
    private boolean takePageElementScreenshot;

    private boolean buildSitemap;
    private String regressionReportFolder = new File( "." ).getAbsolutePath();
    private String reportFolder           = new File( "." ).getAbsolutePath();

    private ArrayList<String> excludedLinks = new ArrayList<String>(  );
    private ArrayList<String> excludedButtons = new ArrayList<String>(  );

    private String url = "";
    private String id = "";
    private String domain = "";
    private String applicationInstanceImpl = "";
    private File appCfgFile;
    private File userCfgFile;
    private int browserHeight;
    private int browserWidth;
    private String browserBinary;
    private String applicationDir;

    public ApplicationSetup()
    {

    }

    public ApplicationSetup(ApplicationSetup setup)
    {
        this.domain = setup.domain;
        this.appCfgFile = new File(setup.appCfgFile.getAbsolutePath());
        this.userCfgFile = new File(setup.userCfgFile.getAbsolutePath());
        this.id = setup.id;
        this.url = setup.url;

        this.excludedButtons = new ArrayList<String>( setup.excludedButtons );
        this.excludedLinks = new ArrayList<String>( setup.excludedLinks );
        this.reportFolder = setup.reportFolder;
        this.regressionReportFolder = setup.regressionReportFolder;

        this.followButtons = setup.followButtons;
        this.followLinks = setup.followLinks;
        this.takePageScreenshot = setup.takePageScreenshot;
        this.takePageElementScreenshot = setup.takePageElementScreenshot;


        this.browserHeight = setup.browserHeight;
        this.browserWidth = setup.browserWidth;
        this.browserBinary = setup.browserBinary;
        this.applicationDir = setup.applicationDir;
    }

    public void setFollowLinks(boolean followLinks)
    {
        this.followLinks = followLinks;
    }

    public boolean isFollowLinksEnabled()
    {
        return followLinks;
    }

    public void setBuildSiteMap(boolean buildSitemap)
    {
        this.buildSitemap = buildSitemap;
    }

    public boolean isBuildSitemapEnabled()
    {
        return buildSitemap;
    }

    public void setTakePageScreenshot(boolean b)
    {
        takePageScreenshot = b;
    }

    public boolean isTakePageScreenshotEnabled()
    {
        return takePageScreenshot;
    }

    public void setReportFolder(String reportFolder)
    {
        if(reportFolder !=null)
            this.reportFolder = reportFolder;
    }

    public String getReportFolder()
    {
        return reportFolder;
    }

    public void setRegressionReportFolder(String folder)
    {
        if(folder != null)
            this.regressionReportFolder = folder;
    }

    public String getRegressionReportFolder()
    {
        return regressionReportFolder;
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

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
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

    public String getApplicationImpl()
    {
        return applicationInstanceImpl;
    }

    public void setApplicationImpl(String applicationInstanceImpl)
    {
        this.applicationInstanceImpl = applicationInstanceImpl;
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
        this.userCfgFile = new File(getAppCfgFile().getParentFile(), userCfgFile).getAbsoluteFile();
    }

    public void setAppCfgFile(File appCfgFile)
    {
        this.appCfgFile = appCfgFile;
    }

    public void setTakePageElementScreenshot(boolean takePageElementScreenshot)
    {
        this.takePageElementScreenshot = takePageElementScreenshot;
    }

    public boolean isTakePageElementScreenshotEnabled()
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
        this.applicationDir = applicationDir;
    }

    public String getApplicationDir()
    {
        return applicationDir;
    }
}
