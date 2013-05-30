package com.smash.revolance.ui.explorer.cmdline;

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

import com.smash.revolance.ui.explorer.application.ApplicationDifferencies;
import com.smash.revolance.ui.explorer.application.ApplicationManager;
import com.smash.revolance.ui.explorer.sitemap.SiteMap;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * User: wsmash
 * Date: 28/04/13
 * Time: 22:10
 */
public enum Commands
{
    EXPLORE("explore", "appCfg.xml");

    private final String[] opts;
    private final String   cmd;

    private Commands(String cmd, String... opts)
    {
        this.cmd = cmd;
        this.opts = opts;
    }

    public String getCmd()
    {
        return cmd;
    }

    public int getOptsCount()
    {
        return opts == null? 0 : opts.length;
    }

    private String getOpts()
    {
        String optsString = "";
        for(int optIdx = 0; optIdx < getOptsCount(); optIdx++)
        {
            optsString += opts[optIdx];
        }
        return optsString.trim();
    }

    public String getUsage()
    {
        return String.format( "%s %s", getCmd(), getOpts() );
    }

    public static String getUsages()
    {
        String usage = "";
        for(Commands cmd : Commands.values())
        {
            usage += String.format( "Usage: %s\n", cmd.getUsage() );
        }
        return usage;
    }

    public static Commands toCommand(String cmd, int optsCount)
    {
        for(Commands command : Commands.values())
        {
            if( command.getOptsCount() == optsCount && command.getCmd().contentEquals( cmd ) )
            {
                return command;
            }
        }
        return null;
    }

    public void exec(String... opts) throws Exception
    {
        if(getOptsCount() == opts.length)
        {
            File workingDir = new File(".").getAbsoluteFile();
            switch ( this )
            {
                case EXPLORE:

                    execExporeCmd(workingDir, opts);
                    break;
            }
        }
    }

    private void execCompareCmd(File workingDir, String[] opts) throws Exception
    {
        File sitemapFile = new File( workingDir, opts[0] );
        if(!sitemapFile.exists())
        {
            throw new FileNotFoundException("Missing sitemap file: " + sitemapFile);
        }

        File sitemapRefFile = new File( workingDir, opts[1] );
        if(!sitemapRefFile.exists())
        {
            throw new FileNotFoundException("Missing sitemapRef file: " + sitemapRefFile);
        }

        SiteMap sitemap = SiteMap.fromJson( sitemapFile );
        SiteMap sitemapRef = SiteMap.fromJson( sitemapRefFile );
        ApplicationDifferencies diffMaker = new ApplicationDifferencies( sitemap, sitemapRef );

        File regression = new File(opts[2]);
        diffMaker.doReport( regression );
    }

    private void execExporeCmd(File workingDir, String[] opts) throws Exception
    {
        File appCfg = new File(workingDir, opts[0]);
        if(!appCfg.exists())
        {
            throw new FileNotFoundException("Missing appCfg file: " + appCfg);
        }

        ApplicationManager manager = new ApplicationManager(appCfg);
        manager.explore();
    }
}
