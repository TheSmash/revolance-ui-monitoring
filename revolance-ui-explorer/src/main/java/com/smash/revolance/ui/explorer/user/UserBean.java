package com.smash.revolance.ui.explorer.user;

/*
        This file is part of Revolance.

        Revolance is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        Revolance is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with Revolance UI Suite.  If not, see <http://www.gnu.org/licenses/>.
*/

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * User: wsmash
 * Date: 21/04/13
 * Time: 11:50
 */
@JsonAutoDetect(fieldVisibility= JsonAutoDetect.Visibility.ANY,
                getterVisibility= JsonAutoDetect.Visibility.NONE,
                isGetterVisibility= JsonAutoDetect.Visibility.NONE)
public class UserBean
{
    @JsonIgnore
    private User    instance;

    private String  login;
    private String  id;
    private String  reportFolder;
    private String  captionFolder;
    private boolean ref;
    private String  newPasswd;
    private String passwd;
    private boolean followButtonsEnabled;
    private boolean followLinksEnabled;
    private boolean pageScreenshotEnabled;
    private boolean pageElementScreenShotEnabled;
    private String domain;
    private String sitemapFolder;
    private String home;

    public UserBean(User instance)
    {
        this.instance = instance;
    }

    public UserBean()
    {

    }

    public User getInstance()
    {
        return instance;
    }

    public String getLogin()
    {
        return login;
    }

    public void setLogin(String login)
    {
        this.login = login;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }

    public String getReportFolder()
    {
        return reportFolder;
    }

    public void setReportFolder(String reportFolder)
    {
        this.reportFolder = reportFolder;
    }

    public String getCaptionFolder()
    {
        return captionFolder;
    }

    public void setCaptionFolder(String captionFolder)
    {
        this.captionFolder = captionFolder;
    }

    public void setRef(boolean ref)
    {
        this.ref = ref;
    }

    public boolean isRef()
    {
        return ref;
    }

    public void setNewPasswd(String newPasswd)
    {
        this.newPasswd = newPasswd;
    }

    public String getNewPasswd()
    {
        return newPasswd;
    }

    public void setPasswd(String passwd)
    {
        this.passwd = passwd;
    }

    public String getPasswd()
    {
        return passwd;
    }

    public boolean isFollowButtonsEnabled()
    {
        return followButtonsEnabled;
    }

    public void setFollowButtonsEnabled(boolean b)
    {
        this.followButtonsEnabled = b;
    }

    public boolean isFollowLinksEnabled()
    {
        return followLinksEnabled;
    }

    public void setFollowLinksEnabled(boolean b)
    {
        this.followLinksEnabled = b;
    }

    public boolean isPageScreenshotEnabled()
    {
        return pageScreenshotEnabled;
    }

    public void setPageScreenshotEnabled(boolean b)
    {
        this.pageScreenshotEnabled = b;
    }

    public void setPageElementScreenshotEnabled(boolean b)
    {
        this.pageElementScreenShotEnabled = b;
    }

    public boolean isPageElementScreenshotEnabled()
    {
        return pageElementScreenShotEnabled;
    }

    public void setInstance(User instance)
    {
        this.instance = instance;
    }

    public String getDomain()
    {
        return domain;
    }

    public void setDomain(String domain)
    {
        this.domain = domain;
    }

    public String getSitemapFolder()
    {
        return sitemapFolder;
    }

    public void setSitemapFolder(String sitemapFolder)
    {
        this.sitemapFolder = sitemapFolder;
    }

    public String getHome()
    {
        return home;
    }

    public void setHome(String home)
    {
        this.home = home;
    }
}
