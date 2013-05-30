package com.smash.revolance.ui.explorer.bot;

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

import com.smash.revolance.ui.explorer.element.IElement;
import com.smash.revolance.ui.explorer.helper.BotHelper;
import com.smash.revolance.ui.explorer.helper.ImageHelper;
import com.smash.revolance.ui.explorer.helper.UrlHelper;
import com.smash.revolance.ui.explorer.helper.UserHelper;
import com.smash.revolance.ui.explorer.page.IPage;
import com.smash.revolance.ui.explorer.page.api.PageBean;
import com.smash.revolance.ui.explorer.user.User;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.UUID;

import static com.smash.revolance.ui.explorer.helper.UserHelper.browse;

/**
 * User: wsmash
 * Date: 30/11/12
 * Time: 16:58
 */
public class Bot
{
    private User   user;

    private double xScale = 1;
    private double yScale = 1;

    public Bot(User user) throws BrowserFactory.InstanciationError
    {
        BrowserFactory.instanceciateNavigator( user );
        setUser( user );
    }

    public WebDriver getBrowser() throws Exception
    {
        return getUser().getBrowser();
    }

    public Bot setUser(User user)
    {
        this.user = user;
        return this;
    }

    public User getUser()
    {
        return user;
    }

    public IPage explore(String url) throws Exception
    {
        PageBean page = getUser().getSiteMap().getPage(url, true);
        if(!page.hasBeenBrowsed() && !getUser().isExplorationDone())
        {
            page.getInstance().explore( );
            if(getUser().isExplorationDone())
            {
                getUser().getBrowser().quit();
                getUser().setBrowserActive(false);
            }
            getUser().setExplorationDone(true);
        }
        return page.getInstance();
    }

    public String getCurrentUrl() throws Exception
    {
        UserHelper.handleAlert(getUser());
        return getBrowser().getCurrentUrl();
    }

    public String getCurrentTitle() throws Exception
    {
        UserHelper.handleAlert(getUser());
        return getBrowser().getTitle();
    }

    public double getYScale()
    {
        return yScale;
    }

    public double getXScale()
    {
        return xScale;
    }

}
