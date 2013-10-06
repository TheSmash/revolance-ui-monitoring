package com.smash.revolance.ui.cmdline;

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

import org.apache.commons.lang3.ArrayUtils;

/**
 * User: wsmash
 * Date: 28/04/13
 * Time: 22:03
 */
public class Main
{
    public static void main(String... args)
    {
        printHeader();
        if ( !isValidCmd( args ) )
        {
            printUsages();
        } else
        {
            String[] opts = getOpts( args );
            Commands cmd = getCmd( args[0], opts.length );

            exec( cmd, opts );
        }
    }

    private static Commands getCmd(String cmd, int optsCount)
    {
        return Commands.toCommand( cmd, optsCount );

    }

    private static void exec(Commands arg, String[] opts)
    {
        try
        {
            arg.exec( opts );
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static String[] getOpts(String[] args)
    {
        return ArrayUtils.subarray( args, 1, args.length );
    }

    private static void printUsages()
    {
        System.out.println( Commands.getUsages() );
    }

    private static boolean isValidCmd(String... args)
    {
        if ( args == null )
        {
            return false;
        } else
        {
            return args.length < 1 ? false : _isValidCmd( args[0], args.length - 1 );
        }
    }

    private static boolean _isValidCmd(String cmd, int optsCount)
    {
        return Commands.toCommand( cmd, optsCount ) != null;
    }

    private static void printHeader()
    {
        System.out.println( "*********************************************************" );
        System.out.println( "*" );
        System.out.println( "*  Revolance UI Explorer Command line v" + getVersion() );
        System.out.println( "*" );
        System.out.println( "*********************************************************" );
        System.out.println( "" );
    }

    public static String getVersion()
    {
        return "0.1.0-SNAPSHOT";
    }
}
