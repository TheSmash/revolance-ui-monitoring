package com.smash.revolance.ui.explorer.helper;

/*
        This file is part of Revolance UI Suite.

        Revolance UI Suite is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        Revolance UI Suite is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with Revolance UI Suite.  If not, see <http://www.gnu.org/licenses/>.
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
        url2 = removeTrailingSlash( url2 );
        return url1.contentEquals( url2 );
    }

    private static String removeTrailingSlash(String url)
    {
        if(url.endsWith("/"))
        {
            url = url.substring(0, url.length()-1);
        }
        return url;
    }

    public static String getHash(String url)
    {
        if(containsHash(url))
        {
            return url.split( "#" )[1];
        }
        else
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
        if(containsHash(url))
        {
            return url.split( "#" )[0];
        }
        else
        {
            return url;
        }
    }

    public static String removeHash(String url)
    {
        return containsHash(url)?_removeHash(url):url;
    }
}
