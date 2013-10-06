package com.smash.revolance.ui.materials.mock.webdriver.browser;

import org.apache.commons.exec.OS;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.ErrorCodes;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: wsmash
 * Date: 29/09/13
 * Time: 13:47
 */
public class MockedBrowserController implements JSonWireListener
{
    private MockedBrowser browser;

    @Override
    public synchronized void notify(JSonWireEvent event)
    {
        String name = (String) event.getProp( "command.name" );
        String path = (String) event.getProp( "command.path" );
        String elementId = (String) event.getProp( "command.elementId" );
        String htmlAttribute = (String) event.getProp( "command.elementHtmlAttribute" );
        String cssAttribute = (String) event.getProp( "command.elementCssAttribute" );

        System.out.println( "Receiving command: " + name + " with path: " + path );

        Map<String, Object> commandPayload = (Map<String, Object>) event.getProp( "command.payload" );

        // Just the value part in the payload
        Map<String, Object> resultPayload = new HashMap<String, Object>();

        if ( isCommand( event, DriverCommand.NEW_SESSION ) )
        {
            browser = new MockedBrowser();

            resultPayload.put( "browserName", this.getClass().getSimpleName() );
            resultPayload.put( "browserPlatform", OS.isFamilyUnix() ? "LINUX" : "WINDOWS" );
            resultPayload.put( "browserVersion", "0.0.1-SNAPSHOT" );
        }
        if ( isCommand( event, DriverCommand.SET_WINDOW_SIZE ) )
        {
            browser.setSize( (Integer) commandPayload.get( "width" ), (Integer) commandPayload.get( "height" ) );
        }
        if ( isCommand( event, DriverCommand.GET_WINDOW_SIZE ) )
        {
            Dimension dim = browser.getDimension();
            resultPayload.put( "width", dim.getWidth() );
            resultPayload.put( "height", dim.getHeight() );
        }
        if ( isCommand( event, DriverCommand.SET_WINDOW_POSITION ) )
        {
            browser.setLocation( (Integer) commandPayload.get( "x" ), (Integer) commandPayload.get( "y" ) );
        }
        if ( isCommand( event, DriverCommand.GET_WINDOW_POSITION ) )
        {
            Point location = browser.getLocation();
            resultPayload.put( "x", location.getX() );
            resultPayload.put( "y", location.getY() );
        }
        if ( isCommand( event, DriverCommand.GET_CURRENT_URL ) )
        {
            resultPayload.put( "url", browser.getUrl() );
        }
        if ( isCommand( event, DriverCommand.GET ) )
        {
            try
            {
                browser.goToUrl( (String) commandPayload.get( "url" ) );
            }
            catch (Exception e)
            {
                System.err.println( e );
            }
        }
        if ( isCommand( event, DriverCommand.EXECUTE_SCRIPT ) )
        {
            resultPayload.put( "value",
                               browser.executeJavaScript( (String) commandPayload.get( "script" ), (Object[]) commandPayload.get( "args" ) ) );
        }
        if ( isCommand( event, DriverCommand.GET_ALERT_TEXT ) )
        {
            resultPayload.put( "alertStatusCode", ErrorCodes.NO_ALERT_PRESENT );
            resultPayload.put( "alertMessage", "No alert is present" );
        }
        if ( isCommand( event, DriverCommand.GET_TITLE ) )
        {
            resultPayload.put( "title", browser.getTitle() );
        }
        if ( isCommand( event, DriverCommand.SCREENSHOT ) )
        {
            resultPayload.put( "screenshot", browser.takeScreenshot() );
        }
        if ( isCommand( event, DriverCommand.FIND_ELEMENTS ) )
        {
            List<String> elements = browser.findElements( (String) commandPayload.get( "value" ) );
            resultPayload.put( "elementList", buildElementList( elements ) );
        }
        if ( isCommand( event, DriverCommand.GET_ELEMENT_LOCATION ) )
        {
            Point location = browser.getElementLocation( elementId );

            resultPayload.put( "elementX", String.valueOf( location.getX() ) );
            resultPayload.put( "elementY", String.valueOf( location.getY() ) );
        }
        if ( isCommand( event, DriverCommand.GET_ELEMENT_SIZE ) )
        {
            Dimension dim = browser.getElementSize( elementId );

            resultPayload.put( "elementW", String.valueOf( dim.getWidth() ) );
            resultPayload.put( "elementH", String.valueOf( dim.getHeight() ) );
        }
        if ( isCommand( event, DriverCommand.GET_ELEMENT_TAG_NAME ) )
        {
            resultPayload.put( "elementTagName", browser.getElementTag( elementId ) );
        }
        if ( isCommand( event, DriverCommand.GET_ELEMENT_TEXT ) )
        {
            resultPayload.put( "elementText", browser.getElementText( elementId ) );
        }
        if ( isCommand( event, DriverCommand.GET_ELEMENT_ATTRIBUTE ) )
        {
            resultPayload.put( "elementHtmlAttributeValue", browser.getElementHtmlAttribute( elementId, htmlAttribute ) );
        }
        if ( isCommand( event, DriverCommand.IS_ELEMENT_DISPLAYED ) )
        {
            resultPayload.put( "isElementDisplayed", browser.isElementDisplayed( elementId ) );
        }
        if ( isCommand( event, DriverCommand.GET_ELEMENT_VALUE_OF_CSS_PROPERTY ) )
        {
            resultPayload.put( "elementCssAttributeValue", browser.getElementCssAttribute( elementId, cssAttribute ) );
        }
        event.setProp( "command.value", resultPayload );
        notifyAll();
    }

    private boolean isCommand(JSonWireEvent event, String cmd)
    {
        return ( (String) event.getProp( "command.name" ) ).contentEquals( cmd );
    }


    private String buildElementList(java.util.List<String> elementIds)
    {
        StringBuilder str = new StringBuilder();

        str.append( "[" );
        for ( String elementId : elementIds )
        {
            str.append( "{" );
            str.append( "\"ELEMENT\":\"{" ).append( elementId ).append( "}\"" );
            str.append( "}" );
            if ( isLastElement( elementId, elementIds ) )
            {
                str.append( "," );
            }
        }
        str.append( "]" );

        return str.toString();
    }

    private boolean isLastElement(String elementId, List<String> elementIds)
    {
        return !elementIds.get( elementIds.size() - 1 ).contentEquals( elementId );
    }


}
