package com.smash.revolance.ui.materials.mock.webdriver.handler;

import com.smash.revolance.ui.materials.mock.webdriver.browser.MockedBrowserController;
import com.smash.revolance.ui.materials.mock.webdriver.command.*;
import com.sun.jersey.server.impl.application.WebApplicationContext;
import org.openqa.selenium.remote.DriverCommand;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.UUID;


/**
 * User: wsmash
 * Date: 28/09/13
 * Time: 14:59
 */
@Path("/hub")
public class JSonWireController
{
    static
    {
        CommandExecutor.registerListener( new MockedBrowserController() );
    }

    @Context
    Request request;

    @Context
    UriInfo uriInfo;

    /*
        public void setElementClicked(boolean b)
        {
            context.put( "elementClicked", "ok" );
        }

        public void setElementId(String id)
        {
            context.put( "elementIdAttribute", id );
        }

        public void setElementDisabled(boolean disabled)
        {
            context.put( "elementDisabledAttribute", String.valueOf( disabled ) );
        }

        public void setElementClass(String clazz)
        {
            context.put( "elementClassAttribute", clazz );
        }

        public void setElementHref(String href)
        {
            context.put( "elementHrefAttribute", href );
        }

        public void setElementType(String type)
        {
            context.put( "elementTypeAttribute", type );
        }

        public void setElementLocation(int x, int y)
        {
            context.put( "elementX", String.valueOf( x ) );
            context.put( "elementY", String.valueOf( y ) );
        }

        public void setElementSize(int w, int h)
        {
            context.put( "elementWidth", String.valueOf( w ) );
            context.put( "elementHeight", String.valueOf( h ) );
        }

        public void setElementText(String text)
        {
            context.put( "elementText", text );
        }

        public void setElementTagName(String tagName)
        {
            context.put( "elementTagName", tagName );
        }

        private void setSessionId(String id)
        {
            context.put( "sessionId", id );
        }

        public void setUrl(String url)
        {
            context.put( "currentUrl", url );
        }

        public void setAlertMessage(String msg)
        {
            context.put("alertMessage", msg);
        }

        public void setAlertStatusCode(WireProtocolResponseCode code)
        {
            context.put( "alertStatusCode", String.valueOf( code.getCode() ) );
        }

        private ResponseDefinitionBuilder buildJsonResponse(RequestMethod method, String url)
        {
            String body = buildJsonResponse(context, String.valueOf( method ), url);

            ResponseDefinitionBuilder builder = new ResponseDefinitionBuilder();
            builder.withHeader( "content-type", "application/json; charset=UTF-8" ).withBody( body );

            return builder;
        }

        private String buildJsonResponse(Map<String, Object> context, String method, String templatePath)
        {
            try
            {
                templatePath = templatePath+"/"+String.valueOf( method ).toLowerCase();
                return TemplateHelper.processTemplate(templatePath+"/body.json", context);
            }
            catch (IOException e)
            {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            catch (TemplateException e)
            {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            return "";
        }

    */
    @POST
    @Path("/session")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSession()
    {
        Command cmd = new Command( UUID.randomUUID().toString(), DriverCommand.NEW_SESSION, "POST" );
        CommandResult cmdResult = new CommandExecutor( cmd, getPath() ).execute();
        return new CommandResponse( cmdResult ).buildResponse();
    }

    @POST
    @Path("/session/{sessionId}/timeouts")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setTimeouts(@PathParam("sessionId") String sessionId)
    {
        Command cmd = new Command( sessionId, DriverCommand.SET_TIMEOUT, "POST" );
        CommandResult cmdResult = new CommandExecutor( cmd, getPath() ).execute();
        return new CommandResponse( cmdResult ).buildResponse();
    }

    @POST
    @Path("/session/{sessionId}/timeouts/implicit_wait")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setTimeoutsImplicitWait(@PathParam("sessionId") String sessionId)
    {
        Command cmd = new Command( sessionId, DriverCommand.IMPLICITLY_WAIT, "POST" );
        CommandResult cmdResult = new CommandExecutor( cmd, getPath() ).execute();
        return new CommandResponse( cmdResult ).buildResponse();
    }

    @GET
    @Path("/session/{sessionId}/screenshot")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response takeScreenshot(@PathParam("sessionId") String sessionId)
    {
        Command cmd = new Command( sessionId, DriverCommand.SCREENSHOT, "GET" );
        CommandResult cmdResult = new CommandExecutor( cmd, getPath() ).execute();
        return new CommandResponse( cmdResult ).buildResponse();
    }

    @GET
    @Path("/session/{sessionId}/title")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTitle(@PathParam("sessionId") String sessionId)
    {
        Command cmd = new Command( sessionId, DriverCommand.GET_TITLE, "GET" );
        CommandResult cmdResult = new CommandExecutor( cmd, getPath() ).execute();
        return new CommandResponse( cmdResult ).buildResponse();
    }

    @DELETE
    @Path("/session/{sessionId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response closeSession(@PathParam("sessionId") String sessionId)
    {
        return Response.ok().build();
    }

    @POST
    @Path("/session/{sessionId}/window/current/size")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setWindowSize(@PathParam("sessionId") String sessionId, String payload)
    {
        Command cmd = new Command( sessionId, DriverCommand.SET_WINDOW_SIZE, "POST", payload );
        CommandResult cmdResult = new CommandExecutor( cmd, getPath() ).execute();
        return new CommandResponse( cmdResult ).buildResponse();
    }

    @GET
    @Path("/session/{sessionId}/alert_text")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAlertText(@PathParam("sessionId") String sessionId)
    {
        Command cmd = new Command( sessionId, DriverCommand.GET_ALERT_TEXT, "GET" );
        CommandResult cmdResult = new CommandExecutor( cmd, getPath() ).execute();
        return new CommandResponse( cmdResult ).buildResponse();
    }

    @GET
    @Path("/session/{sessionId}/url")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getUrl(@PathParam("sessionId") String sessionId)
    {
        Command cmd = new Command( sessionId, DriverCommand.GET_CURRENT_URL, "GET" );
        CommandResult cmdResult = new CommandExecutor( cmd, getPath() ).execute();
        return new CommandResponse( cmdResult ).buildResponse();
    }

    @POST
    @Path("/session/{sessionId}/url")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setCurrentUrl(@PathParam("sessionId") String sessionId, String payload)
    {
        Command cmd = new Command( sessionId, DriverCommand.GET, "POST", payload );
        CommandResult cmdResult = new CommandExecutor( cmd, getPath() ).execute();
        return new CommandResponse( cmdResult ).buildResponse();
    }

    @GET
    @Path("/session/{sessionId}/element/{elementId}/displayed")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response isElementDisplayed(@PathParam("sessionId") String sessionId, @PathParam("elementId") String elementId)
    {
        ElementCommand cmd = new ElementCommand( sessionId, DriverCommand.IS_ELEMENT_DISPLAYED, "GET", "", elementId );
        CommandResult cmdResult = new CommandExecutor( cmd, getPath() ).execute();
        return new CommandResponse( cmdResult ).buildResponse();
    }

    @GET
    @Path("/session/{sessionId}/element/{elementId}/size")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getElementSize(@PathParam("sessionId") String sessionId, @PathParam("elementId") String elementId)
    {
        ElementCommand cmd = new ElementCommand( sessionId, DriverCommand.GET_ELEMENT_SIZE, "GET", "", elementId );
        CommandResult cmdResult = new CommandExecutor( cmd, getPath() ).execute();
        return new CommandResponse( cmdResult ).buildResponse();
    }

    @GET
    @Path("/session/{sessionId}/element/{elementId}/css/{property}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getCssPropertyValue(@PathParam("sessionId") String sessionId, @PathParam("elementId") String elementId, @PathParam("property") String property)
    {
        ElementCSSCommand cmd = new ElementCSSCommand( sessionId, DriverCommand.GET_ELEMENT_VALUE_OF_CSS_PROPERTY, "GET", "", elementId, property );
        CommandResult cmdResult = new CommandExecutor( cmd, getPath() ).execute();
        return new CommandResponse( cmdResult ).buildResponse();
    }

    @GET
    @Path("/session/{sessionId}/element/{elementId}/location")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getElementLocation(@PathParam("sessionId") String sessionId, @PathParam("elementId") String elementId)
    {
        ElementCommand cmd = new ElementCommand( sessionId, DriverCommand.GET_ELEMENT_LOCATION, "GET", "", elementId );
        CommandResult cmdResult = new CommandExecutor( cmd, getPath() ).execute();
        return new CommandResponse( cmdResult ).buildResponse();
    }

    @GET
    @Path("/session/{sessionId}/element/{elementId}/name")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getElementTagName(@PathParam("sessionId") String sessionId, @PathParam("elementId") String elementId)
    {
        ElementCommand cmd = new ElementCommand( sessionId, DriverCommand.GET_ELEMENT_TAG_NAME, "GET", "", elementId );
        CommandResult cmdResult = new CommandExecutor( cmd, getPath() ).execute();
        return new CommandResponse( cmdResult ).buildResponse();
    }

    @GET
    @Path("/session/{sessionId}/element/{elementId}/text")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getElementText(@PathParam("sessionId") String sessionId, @PathParam("elementId") String elementId)
    {
        ElementCommand cmd = new ElementCommand( sessionId, DriverCommand.GET_ELEMENT_TEXT, "GET", "", elementId );
        CommandResult cmdResult = new CommandExecutor( cmd, getPath() ).execute();
        return new CommandResponse( cmdResult ).buildResponse();
    }

    @POST
    @Path("/session/{sessionId}/element/{elementId}/click")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response clickOnElement(@PathParam("sessionId") String sessionId, @PathParam("elementId") String elementId, String payload)
    {
        ElementCommand cmd = new ElementCommand( sessionId, DriverCommand.CLICK_ELEMENT, "POST", payload, elementId );
        CommandResult cmdResult = new CommandExecutor( cmd, getPath() ).execute();
        return new CommandResponse( cmdResult ).buildResponse();
    }

    @GET
    @Path("/session/{sessionId}/element/{elementId}/attribute/{attribute}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getElementClassAttribute(@PathParam("sessionId") String sessionId, @PathParam("elementId") String elementId, @PathParam("attribute") String attribute)
    {
        ElementAttributeCommand cmd = new ElementAttributeCommand( sessionId, DriverCommand.GET_ELEMENT_ATTRIBUTE, "GET", "", elementId, attribute );
        CommandResult cmdResult = new CommandExecutor( cmd, getPath() ).execute();
        return new CommandResponse( cmdResult ).buildResponse();
    }

    @POST
    @Path("/session/{sessionId}/elements")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getElements(@PathParam("sessionId") String sessionId, String payload)
    {
        Command cmd = new Command( sessionId, DriverCommand.FIND_ELEMENTS, "POST", payload );
        CommandResult cmdResult = new CommandExecutor( cmd, getPath() ).execute();
        return new CommandResponse( cmdResult ).buildResponse();
    }

    @POST
    @Path("/session/{sessionId}/execute")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response executeJS(@PathParam("sessionId") String sessionId, String payload)
    {
        Command cmd = new Command( sessionId, DriverCommand.EXECUTE_SCRIPT, "POST", payload );
        CommandResult cmdResult = new CommandExecutor( cmd, getPath() ).execute();
        return new CommandResponse( cmdResult ).buildResponse();
    }


    public String getPath()
    {
        return ( (WebApplicationContext) uriInfo ).getMatchedMethod().getAnnotation( Path.class ).value();
    }

}
