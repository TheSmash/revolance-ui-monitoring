package com.smash.revolance.ui.materials;

/*
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Revolance-UI-Materials-Commons
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

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * User: wsmash
 * Date: 28/09/13
 * Time: 12:32
 */
public class TemplateHelper
{
    public static String processTemplate(String templateName, Map<String, Object> variables) throws TemplateException, IOException
    {
        // load template
        Template template = null;
        InputStreamReader stream = null;
        try
        {
            InputStream content = TemplateHelper.class.getClassLoader().getResourceAsStream( templateName );
            if ( content == null )
            {
                System.err.println( "Unable to find template: " + templateName );
                return "Unable to find template: " + templateName;
            } else
            {
                stream = new InputStreamReader( content );
                template = new Template( "template", stream, null );
            }
        }
        catch (IOException e)
        {
            System.err.println( "Unable to find template: " + templateName );
            return "Unable to find template: " + templateName;
        }
        finally
        {
            IOUtils.closeQuietly( stream );
        }
        // process template
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if ( template != null )
        {
            // Fill the template with parameter values
            Writer out = new OutputStreamWriter( baos );
            template.process( variables, out );
            out.flush();
        }
        return baos.toString();
    }

    public String buildElementList(List<String> elementIds)
    {
        StringBuilder str = new StringBuilder();

        str.append( "[" );
        for ( String elementId : elementIds )
        {
            str.append( "{" );
            str.append( "\"ELEMENT\":\"{" ).append( elementId ).append( "}\"" );
            str.append( "}" );
            if ( !elementIds.get( elementIds.size() - 1 ).contentEquals( elementId ) )
            {
                str.append( "," );
            }
        }
        str.append( "]" );

        return str.toString();
    }

}
