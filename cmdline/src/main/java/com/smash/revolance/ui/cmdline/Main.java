package com.smash.revolance.ui.cmdline;

/*
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Revolance-UI-Cmdline
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
        }
        else
        {
            exec( getCmd( args[0] ) );
        }
    }

    private static Commands getCmd(String cmd)
    {
        return Commands.toCommand( cmd );

    }

    private static void exec(Commands command)
    {
        try
        {
            command.exec();
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
        System.out.println( "" );
        System.out.println( Commands.getUsages() );
    }

    private static boolean isValidCmd(String... args)
    {
        if ( args == null )
        {
            return false;
        }
        else
        {
            return _isValidCmd( args[0], args.length - 1 );
        }
    }

    private static boolean _isValidCmd(String cmd, int optsCount)
    {
        Commands command = Commands.toCommand( cmd );
        if(command != null )
        {
            return Commands.allParametersDefined(command);
        }
        else
        {
            System.out.println( "Unknown command: '" + cmd + "'" );
            return false;
        }
    }

    private static void printHeader()
    {
        System.out.println( "*********************************************************" );
        System.out.println( "*" );
        System.out.println( "*  Revolance UI Command line" );
        System.out.println( "*" );
        System.out.println( "*********************************************************" );
        System.out.println( "" );
    }

}
