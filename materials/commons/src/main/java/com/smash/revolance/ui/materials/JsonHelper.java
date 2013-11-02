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

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import java.io.File;
import java.io.IOException;

/**
 * User: wsmash
 * Date: 21/04/13
 * Time: 18:47
 */
public class JsonHelper
{
    private        ObjectMapper mapper   = null;
    private static JsonHelper   instance = null;

    private JsonHelper()
    {
        mapper = new ObjectMapper();
        mapper.enable( SerializationConfig.Feature.INDENT_OUTPUT );
        mapper.enable( SerializationConfig.Feature.USE_ANNOTATIONS );
        mapper.enable( DeserializationConfig.Feature.AUTO_DETECT_FIELDS );
        mapper.enable( DeserializationConfig.Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT );
        mapper.enable( DeserializationConfig.Feature.READ_ENUMS_USING_TO_STRING );
        mapper.enable( DeserializationConfig.Feature.USE_JAVA_ARRAY_FOR_JSON_ARRAY );
        mapper.disable( SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS );

        mapper.getJsonFactory().enable( JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER );
        //mapper.getJsonFactory().enable( JsonParser.Feature.ALLOW_COMMENTS );
        mapper.getJsonFactory().enable( JsonParser.Feature.ALLOW_SINGLE_QUOTES );

    }

    public static JsonHelper getInstance()
    {
        if ( instance == null )
        {
            instance = new JsonHelper();
        }
        return instance;
    }

    public ObjectMapper getMapper()
    {
        return mapper;
    }

    public String map(Object object) throws IOException
    {
        return getMapper().writeValueAsString( object );
    }

    public void map(File file, Object object) throws IOException
    {
        getMapper().writeValue( file, object );
    }

    public Object map(Class clazz, File json) throws IOException
    {
        return getMapper().readValue( json, clazz );
    }

    public Object map(Class clazz, String json) throws IOException
    {
        return getMapper().readValue( json, clazz );
    }
}
