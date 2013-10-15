package com.smash.revolance.ui.materials;

/**
 * User: wsmash
 * Date: 14/10/13
 * Time: 23:42
 */
public class OSHelper
{
    public static String getScriptExtension()
    {
        return getScriptExtension( getOsName() );
    }

    public static String getScriptExtension(String osName)
    {
        return isUnix(osName)?".sh":".bat";
    }

    public static boolean isUnix()
    {
        return isUnix( getOsName() );
    }

    public static boolean isUnix(String osName)
    {
        return !osName.contains( "win" );
    }

    public static String getOsName()
    {
        return System.getProperty( "os.name" ).toLowerCase();
    }

}
