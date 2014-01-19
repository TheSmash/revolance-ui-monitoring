package com.smash.revolance.ui.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * User: wsmash
 * Date: 08/10/13
 * Time: 20:41
 */
@Controller
public class LifecycleController
{
    @ResponseBody
    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public String status()
    {
        return "STARTED";
    }

    @ResponseBody
    @RequestMapping(value = "/shutdown", method = RequestMethod.GET)
    public String shutdown()
    {
        System.exit( 0 );
        return "";
    }

}
