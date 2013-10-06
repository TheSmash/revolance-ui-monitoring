package com.smash.revolance.ui.materials.mock.webdriver.command;

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
