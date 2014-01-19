package com.smash.revolance.ui.server.model;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

/**
 * Created by wsmash on 27/10/13.
 */
public class Exploration
{
    @NotEmpty
    private String tag;

    @NotEmpty
    private String page;

    @NotEmpty
    @URL
    private String domain;

    private String login = "";

    private String loginField = "";

    private String password = "";

    private String passwordField = "";

    private int timeout = 1800;

    private String resolution;

    private String browserType;

    private boolean followLinks = false;

    private boolean followButtons = false;

    private boolean secured = false;

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

    public String getBrowserType() {
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

    public String getPasswordField()
    {
        return passwordField;
    }

    public void setPasswordField(String passField)
    {
        this.passwordField = passField;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getLoginField()
    {
        return loginField;
    }

    public void setLoginField(String loginField)
    {
        this.loginField = loginField;
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

}
