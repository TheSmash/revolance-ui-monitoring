package com.smash.revolance.ui.server.services.controller;

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

import com.smash.revolance.ui.server.model.Settings;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

/**
 * User: wsmash
 * Date: 08/06/13
 * Time: 18:26
 */
@Controller
public class SettingsController
{

    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public ModelAndView displaySettings(HttpServletRequest request) throws IOException
    {
        Settings settings = (Settings) request.getSession().getAttribute( "settings" );
        if ( settings == null )
        {
            settings = new Settings();
            request.getSession().setAttribute( "settings", settings );
        }
        return new ModelAndView( "SettingsForm", "settings", settings );
    }

    @RequestMapping(value = "/settings", method = RequestMethod.POST)
    public ModelAndView updateSettings(@Valid Settings settings, BindingResult result, HttpServletRequest request)
    {
        if ( result.hasErrors() )
        {
            return new ModelAndView( "SettingsForm" );
        } else
        {
            request.getSession().setAttribute( "settings", settings );
            return new ModelAndView( "ApplicationList" );
        }
    }
}
