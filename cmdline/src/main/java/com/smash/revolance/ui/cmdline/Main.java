package com.smash.revolance.ui.cmdline;

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
