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

import com.smash.revolance.ui.model.user.User;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * User: wsmash
 * Date: 19/10/13
 * Time: 22:29
 */
public class ExplorationService implements IExplorer
{
    @Override
    public void explore(ExplorationConfiguration configuration) throws IOException
    {
        User user = new User( configuration.getId(), configuration.getUrl() );
        user.setDomain( configuration.getDomain() );

        user.setFollowLinks( configuration.isFollowLinksEnabled() );
        user.setFollowButtons( configuration.isFollowButtonsEnabled() );

        user.enablePageScreenshot( true );
        user.enablePageElementScreenshot( true );

        user.setBrowserType( configuration.getBrowserType() );
        user.setBrowserWidth( configuration.getBrowserWidth() );
        user.setBrowserHeight( configuration.getBrowserHeight() );

        user.setExcludedLinks( configuration.getExcludedLinks() );
        user.setExcludedButtons(configuration.getExcludedButtons());

        user.setBrowserBinary("");
        user.setDriverPath("");

        File log = configuration.getLogFile();
        System.out.println("Launching exploration id: " + user.getId() + ". Log file: " + log);

        UserExplorer explorer = new UserExplorer( user, log, configuration.getReportFile(), configuration.getTimeout() );
        explorer.explore();
    }

}
