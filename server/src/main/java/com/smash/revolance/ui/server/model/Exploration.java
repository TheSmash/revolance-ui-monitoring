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

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by wsmash on 27/10/13.
 */
public class Exploration
{
    @NotEmpty
    private String tag;

    @NotEmpty
    @URL
    private String domain;

    private String page = "";

    private String login = "";

    private String password = "";

    private MultipartFile applicationModel;

    private int timeout = 1800;

    private String resolution;

    private String browserType;

    private boolean followLinks = false;

    private boolean followButtons = false;

    private boolean secured = false;

    private String applicationClassName;

    public Exploration()
    {

    }

    public String getTag()
    {
        return tag;
    }

    public void setTag(String tag)
    {
        this.tag = tag;
    }

    public boolean isFollowLinks()
    {
        return followLinks;
    }

    public void setFollowLinks(boolean followLinks)
    {
        this.followLinks = followLinks;
    }

    public String getUrl()
    {
        return domain + (!domain.endsWith("/") ? "/" : "") + this.page;
    }

    public void setPage(String page)
    {
        this.page = page;
    }

    public String getPage()
    {
        return this.page;
    }

    public int getTimeout()
    {
        return timeout;
    }

    public void setTimeout(int timeout)
    {
        this.timeout = timeout;
    }

    public String getDomain()
    {
        return domain;
    }

    public void setDomain(String domain)
    {
        this.domain = domain;
    }

    public String getResolution()
    {
        return resolution;
    }

    public void setResolution(String resolution)
    {
        this.resolution = resolution;
    }

    public String getBrowserType()
    {
        return browserType;
    }

    public void setBrowserType(String browserType)
    {
        this.browserType = browserType;
    }

    public int getHeight()
    {
        return Integer.parseInt(getResolution().split("x")[1]);
    }

    public int getWidth()
    {
        return Integer.parseInt(getResolution().split("x")[0]);
    }

    public boolean isFollowButtons()
    {
        return followButtons;
    }

    public void setFollowButtons(boolean followButtons)
    {
        this.followButtons = followButtons;
    }

    public MultipartFile getApplicationModel()
    {
        return applicationModel;
    }

    public void setApplicationModel(MultipartFile file)
    {
        this.applicationModel = file;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getLogin()
    {
        return login;
    }

    public void setLogin(String login)
    {
        this.login = login;
    }

    public boolean isSecured()
    {
        return secured;
    }

    public void setSecured(boolean secured)
    {
        this.secured = secured;
    }

    public String getApplicationClassName()
    {
        return applicationClassName;
    }

    public void setApplicationClassName(String applicationClassName)
    {
        this.applicationClassName = applicationClassName;
    }
}
