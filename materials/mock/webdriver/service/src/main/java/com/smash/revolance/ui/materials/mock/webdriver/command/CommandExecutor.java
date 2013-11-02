package com.smash.revolance.ui.materials.mock.webdriver.command;

/*
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Revolance-UI-Materials-Mock-Webdriver-Service
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2012 - 2013 RevoLance
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

import com.smash.revolance.ui.materials.JsonHelper;
import com.smash.revolance.ui.materials.mock.webdriver.browser.JSonWireEvent;
import com.smash.revolance.ui.materials.mock.webdriver.browser.JSonWireListener;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.openqa.selenium.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: wsmash
 * Date: 29/09/13
 * Time: 13:30
 */
public class CommandExecutor
{
    private static List<JSonWireListener> listners = new ArrayList<JSonWireListener>();

    public static void registerListener(JSonWireListener listener)
    {
        listners.add( listener );
    }

    public static void unregisterListener(JSonWireListener listener)
    {
        listners.remove( listener );
    }

    public synchronized void notify(JSonWireEvent event)
    {
        for ( JSonWireListener listner : listners )
        {
            listner.notify( event );
        }
    }

    private final Command       command;
    private final JSonWireEvent event;

    public CommandExecutor(Command cmd, String path)
    {
        this.command = cmd;

        event = new JSonWireEvent();
        event.setProp( "command.executor", this );
        event.setProp( "command.name", this.command.getName() );
        event.setProp( "command.payload", this.command.getPayload() );
        event.setProp( "command.method", this.command.getMethod() );
        event.setProp( "command.path", path );
        if ( cmd instanceof ElementCommand )
        {
            event.setProp( "command.elementId", ( (ElementCommand) cmd ).getElementId() );
        }
        if ( cmd instanceof ElementAttributeCommand )
        {
            event.setProp( "command.elementHtmlAttribute", ( (ElementAttributeCommand) cmd ).getAttribute() );
        }
        if ( cmd instanceof ElementCSSCommand )
        {
            event.setProp( "command.elementCssAttribute", ( (ElementCSSCommand) cmd ).getCssProperty() );
        }
    }

    public CommandResult execute()
    {
        Map<String, Object> payload = new HashMap<String, Object>();
        payload.put( "sessionId", command.getSessionId() );

        notify( event );

        InputStreamReader isr = null;
        InputStream is = null;
        try
        {
            is = this.getClass().getResourceAsStream( getBodyPayloadPath() );
            if ( is != null )
            {
                payload.putAll( (HashMap<String, Object>) event.getProp( "command.value" ) );

                isr = new InputStreamReader( is );
                Template template = new Template( "template", isr, null );
                StringWriter writer = new StringWriter();
                template.process( payload, writer );

                payload = (HashMap<String, Object>) JsonHelper.getInstance().map( HashMap.class, writer.toString() );
            } else
            {
                payload = new HashMap<String, Object>();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        catch (TemplateException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally
        {
            IOUtils.closeQuietly( isr );
        }

        return new CommandResult( payload, mapCommandResultToHttpCode( payload ) );
    }

    private String getBodyPayloadPath()
    {
        String templatePath = "/response_bodies" + event.getProp( "command.path" ) + "/" + String.valueOf( command.getMethod() ) + "/body.json";
        templatePath = templatePath.replaceAll( "\\{[^/]+?}", "any" );
        return templatePath;
    }

    public static int mapCommandResultToHttpCode(Map<String, Object> cmdResults)
    {
        return 200;
    }


}
