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

/**
 * User: wsmash
 * Date: 26/01/13
 * Time: 15:21
 */
public abstract class Application
{

    public abstract void handleAlert(Alert alert) throws Exception;

    public abstract void login(User user, IPage page) throws Exception;

    public abstract boolean isLoginPage(IPage page) throws Exception;

    public abstract boolean isPageBroken(IPage page) throws Exception;

    public abstract boolean isAuthorized(IPage page) throws Exception;

    public abstract void awaitLoaded(IPage page) throws Exception;

}
