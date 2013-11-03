package com.smash.revolance.ui.model.application;

/*
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Revolance-UI-Model
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

import com.smash.revolance.ui.model.page.IPage;
import com.smash.revolance.ui.model.user.User;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;

/**
 * User: wsmash
 * Date: 26/01/13
 * Time: 17:25
 */
public class SecuredApplication extends Application
{
    public SecuredApplication() throws Exception
    {
        super();
    }

    @Override
    public boolean enterLogin(User user, IPage page) throws Exception
    {
        if ( page.getUrl().endsWith( "login.html" ) )
        {
            user.getBrowser().findElement( By.id( "username" ) ).sendKeys( user.getLogin() );
            user.getBrowser().findElement( By.id( "password" ) ).sendKeys( user.getPasswd() );
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public boolean isPageBroken(IPage page)
    {
        return page.getUrl().endsWith( "broken.html" );
    }

    @Override
    public boolean isAuthorized(IPage page)
    {
        return true;
    }

    @Override
    public void awaitPageLoaded(IPage page)
    {

    }

    @Override
    public boolean isSecured()
    {
        return true;
    }

    @Override
    public void handleAlert(Alert alert)
    {
        alert.accept();
    }

    @Override
    public boolean enterNewPassword(User user, IPage page) throws Exception
    {
        if ( page.getUrl().endsWith( "change-passwd.html" ) )
        {
            user.getBrowser().findElement( By.id( "newPasswd" ) ).sendKeys( user.getNewPasswd() );
            user.getBrowser().findElement( By.id( "confNewPasswd" ) ).sendKeys( user.getNewPasswd() );
            return true;
        } else
        {
            return false;
        }
    }

}
