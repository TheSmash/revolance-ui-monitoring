package com.smash.revolance.ui.materials.mock.webdriver.handler;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * User: wsmash
 * Date: 28/09/13
 * Time: 15:52
 */
@Path("/")
public class WebDriverController
{

    @GET
    @Path("/status")
    public Response getStatus()
    {
        return Response.ok().build();
    }

    @GET
    @Path("/shutdown")
    public Response shutdown()
    {
        return Response.ok().build();
    }

}
