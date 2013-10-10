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

import com.smash.revolance.ui.comparator.application.ApplicationComparator;
import com.smash.revolance.ui.comparator.application.ApplicationComparison;
import com.smash.revolance.ui.explorer.ApplicationExplorer;
import com.smash.revolance.ui.materials.JsonHelper;
import com.smash.revolance.ui.model.application.Application;
import com.smash.revolance.ui.model.application.ApplicationManager;
import com.smash.revolance.ui.model.sitemap.SiteMap;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import sun.net.www.http.HttpClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

/**
 * User: wsmash
 * Date: 28/04/13
 * Time: 22:10
 */
public enum Commands
{
    EXPLORE( "explore", "appCfg.xml", "out.txt" ),
    START_SERVER( "startServer", "httpPort" ),
    STOP_SERVER( "stopServer", "httpPort" ),
    STATUS( "status", "httpPort" );

    private String[] opts = new String[]{};
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
        return this.opts == null ? 0 : this.opts.length;
    }

    private String getOpts()
    {
        String optsString = "";
        for ( int optIdx = 0; optIdx < getOptsCount(); optIdx++ )
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
        for ( Commands cmd : Commands.values() )
        {
            usage += String.format( "Usage: %s\n", cmd.getUsage() );
        }
        return usage;
    }

    public static Commands toCommand(String cmd, int optsCount)
    {
        for ( Commands command : Commands.values() )
        {
            if ( command.getOptsCount() == optsCount && command.getCmd().contentEquals( cmd ) )
            {
                return command;
            }
        }
        return null;
    }

    public int exec(String... opts) throws Exception
    {
        int execStatus = -1;

        if ( getOptsCount() == opts.length )
        {
            File workingDir = new File( "." ).getAbsoluteFile();
            switch ( this )
            {
                case EXPLORE:

                    execStatus = execExploreCmd( workingDir, opts );
                    break;

                case START_SERVER:

                    execStatus = execStartServerCmd( workingDir, opts );
                    break;

                case STOP_SERVER:

                    execStatus = execStopServerCmd( workingDir, opts );
                    break;

                case STATUS:

                    execStatus = execStatusCmd( workingDir, opts );
                    break;
            }
        }

        return execStatus;
    }

    private int execStatusCmd(File workingDir, String[] opts)
    {
        try
        {
            System.out.println( "Retrieving server status" );
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://localhost:"+opts[0]+"/ping");
            HttpResponse response = client.execute(request);
            if(response.getStatusLine().getStatusCode() == 200)
            {
                System.out.println("server is running");
                return 0;
            }
            else
            {
                System.err.println( "server is stopped" );
                return -1;
            }
        }
        catch (IOException e)
        {
            System.err.println( "server is stopped" );
            return -1;
        }
    }

    private int execStopServerCmd(File workingDir, String[] opts)
    {
        try
        {
            System.out.println( "Stopping server" );
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://localhost:"+opts[0]+"/shutdown");
            HttpResponse response = client.execute(request);
            if(response.getStatusLine().getStatusCode() == 200)
            {
                System.out.println("Stopping server [Done]");
                return 0;
            }
            else
            {
                System.err.println( "Stopping server [Failed]" );
                return -1;
            }
        }
        catch (IOException e)
        {
            return -1;
        }
    }

    private int execStartServerCmd(File workingDir, String[] opts)
    {
        ServletContainer container = new ServletContainer();
        ServletHolder h = new ServletHolder( container );

        h.setInitParameter( "com.sun.jersey.config.property.packages", "com.smash.revolance.ui.server" );
        h.setInitParameter( "com.sun.jersey.api.json.POJOMappingFeature", "com.sun.jersey.api.json.POJOMappingFeature" );

        Server server = new Server( Integer.parseInt( opts[0] ) );
        ServletContextHandler context = new ServletContextHandler( ServletContextHandler.SESSIONS );
        context.setContextPath( "/" );
        server.setHandler( context );

        context.addServlet( h, "/*" );
        try
        {
            server.start();
            return 0;
        }
        catch (Exception e)
        {
            return -1;
        }

    }

    private void execCompareCmd(File workingDir, String[] opts) throws Exception
    {
        File sitemapFile = new File( workingDir, opts[0] );
        if ( !sitemapFile.exists() )
        {
            throw new FileNotFoundException( "Missing sitemap file: " + sitemapFile );
        }

        File sitemapRefFile = new File( workingDir, opts[0] );
        if ( !sitemapRefFile.exists() )
        {
            throw new FileNotFoundException( "Missing sitemapRef file: " + sitemapRefFile );
        }

        SiteMap sitemap = SiteMap.fromJson( sitemapFile );
        SiteMap sitemapRef = SiteMap.fromJson( sitemapRefFile );
        ApplicationComparator diffMaker = new ApplicationComparator();

        ApplicationComparison comparison = diffMaker.compare( sitemap, sitemapRef );

        File regression = new File( opts[1] );
        JsonHelper.getInstance().map( regression, comparison );
    }

    private int execExploreCmd(File workingDir, String[] opts) throws Exception
    {
        File appCfg = new File( workingDir, opts[0] );
        if ( !appCfg.exists() )
        {
            throw new FileNotFoundException( "Missing appCfg file: " + appCfg );
        }

        ApplicationManager manager = new ApplicationManager( appCfg );
        for ( Application app : manager.getApplications() )
        {
            new ApplicationExplorer( app ).explore( 60 );
        }

        return 0;
    }

}
