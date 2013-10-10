package com.smash.revolance.ui.server;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * User: wsmash
 * Date: 08/10/13
 * Time: 20:41
 */
public class AdminController
{
    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public void ping()
    {

    }

    @RequestMapping(value = "/shutdown", method = RequestMethod.GET)
    public void shutdown()
    {
        System.out.println("Stopping server");
        System.exit( 0 );
    }

}
