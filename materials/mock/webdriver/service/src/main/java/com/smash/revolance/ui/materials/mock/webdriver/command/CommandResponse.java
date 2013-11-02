package com.smash.revolance.ui.materials.mock.webdriver.command;

/*
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Revolance-UI-Materials-Mock-Webdriver-Service
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

import com.smash.revolance.ui.materials.JsonHelper;

import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * User: wsmash
 * Date: 29/09/13
 * Time: 12:18
 */
public class CommandResponse
{
    private final CommandResult cmdResult;

    public CommandResponse(CommandResult cmdResult)
    {
        this.cmdResult = cmdResult;
    }

    public Response buildResponse()
    {
        try
        {
            String payload = JsonHelper.getInstance().map( cmdResult.getPayload() );
            return Response.ok().entity( payload ).build();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

}
