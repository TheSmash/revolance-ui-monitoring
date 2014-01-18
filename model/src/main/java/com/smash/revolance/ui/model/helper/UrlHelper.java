package com.smash.revolance.ui.model.helper;

/*
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Revolance-UI-Model
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

/**
 * User: wsmash
 * Date: 24/02/13
 * Time: 18:48
 */
public class UrlHelper
{
    public static boolean areEquivalent(String url1, String url2)
    {
        url1 = removeTrailingSlash( url1 );
        url1 = removeStartingProtocol( url1 );

        url2 = removeTrailingSlash( url2 );
        url2 = removeStartingProtocol( url2 );

        return url1.contentEquals( url2 );
    }

    public static String removeStartingProtocol(String url)
    {
        if ( url.startsWith( "file://" ) )
        {
            return url.split( "file://" )[1];
        } else if ( url.startsWith( "http://" ) )
        {
            return url.split( "http://" )[1];
        } else if ( url.startsWith( "https://" ) )
        {
            return url.split( "https://" )[1];
        } else
        {
            return url;
        }
    }

    private static String removeTrailingSlash(String url)
    {
        if ( url.endsWith( "/" ) )
        {
            url = url.substring( 0, url.length() - 1 );
        }
        return url;
    }

    public static String getHash(String url)
    {
        if ( containsHash( url ) )
        {
            return url.split( "#" )[1];
        } else
        {
            return url;
        }
    }

    public static boolean containsHash(String url)
    {
        return url.contains( "#" );
    }

    private static String _removeHash(String url)
    {
        if ( containsHash( url ) )
        {
            return url.substring(0, url.indexOf("#" ));
        }
        else
        {
            return url;
        }
    }

    public static String removeHash(String url)
    {
        return containsHash( url ) ? _removeHash( url ) : url;
    }
}
