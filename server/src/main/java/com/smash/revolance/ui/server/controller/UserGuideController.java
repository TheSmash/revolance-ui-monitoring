package com.smash.revolance.ui.server.controller;

import com.smash.revolance.ui.server.model.Settings;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * User: wsmash
 * Date: 08/06/13
 * Time: 18:44
 */
@Controller
public class UserGuideController
{
    @RequestMapping(value = "/user-guide", method = RequestMethod.GET)
    public ModelAndView displayHelp(HttpServletRequest request) throws IOException
    {
        Settings settings = (Settings) request.getSession().getAttribute( "settings" );
        if ( settings == null )
        {
            settings = new Settings();
            request.getSession().setAttribute( "settings", settings );
        }
        return new ModelAndView( "UserGuide", "settings", settings );
    }
}
