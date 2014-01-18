package com.smash.revolance.ui.server.controller;

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
        return new ModelAndView( "UserGuide" );
    }
}
