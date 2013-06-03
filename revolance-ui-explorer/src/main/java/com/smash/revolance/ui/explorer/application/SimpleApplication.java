package com.smash.revolance.ui.explorer.application;

import com.smash.revolance.ui.explorer.page.IPage;
import com.smash.revolance.ui.explorer.user.User;
import org.openqa.selenium.Alert;

/**
 * User: wsmash
 * Date: 01/06/13
 * Time: 10:31
 */
public class SimpleApplication extends Application
{

    @Override
    public void handleAlert(Alert alert)
    {

    }

    @Override
    public boolean enterNewPassword(User user, IPage page) throws Exception
    {
        return false;
    }

    @Override
    public boolean enterLogin(User user, IPage page) throws Exception
    {
        return false;
    }

    @Override
    public boolean isPageBroken(IPage page)
    {
        return false;
    }

    @Override
    public boolean isAuthorized(IPage page)
    {
        return true;
    }

    @Override
    public boolean isSecured()
    {
        return false;
    }

    @Override
    public void awaitPageLoaded(IPage page) throws Exception
    {

    }
}
