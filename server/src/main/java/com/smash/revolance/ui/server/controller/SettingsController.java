package com.smash.revolance.ui.server.controller;

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
