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

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;
import java.io.IOException;

/**
 * User: wsmash
 * Date: 28/04/13
 * Time: 22:10
 */
public enum Commands
{
    STOP( "stop", "port" ),
    STATUS( "status", "port" ),
    START( "start", "port", "war" );

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
            optsString += "-D"+opts[optIdx]+" ";
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

    public static Commands toCommand(String cmd)
    {
        for ( Commands command : Commands.values() )
        {
            if ( command.getCmd().contentEquals( cmd ) )
            {
                return command;
            }
        }
        return null;
    }

    public static boolean allParametersDefined(Commands cmd)
    {
        for(String parameter : cmd.opts)
        {
            if(System.getProperty(parameter)==null)
            {
                System.out.println( "Undefined parameter: '" + parameter + "' for command: '" + cmd.getCmd() + "" );
                return false;
            }
        }
        return true;
    }

    public int exec() throws Exception
    {
        int execStatus = -1;
        File workingDir = new File( "." ).getAbsoluteFile();
        switch ( this )
        {

            case START:

                execStatus = execStartCmd( workingDir, opts );
                break;

            case STOP:

                execStatus = execStopCmd(workingDir, opts);
                break;

            case STATUS:

                execStatus = execStatusCmd( workingDir, opts );
                break;
        }

        return execStatus;
    }

    private int execStatusCmd(File workingDir, String[] opts)
    {
        try
        {
            System.out.println( "Retrieving server status" );
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://localhost:"+Integer.getInteger("port").intValue()+"/ui-monitoring-server/status");
            client.execute(request); // throw an IOException when
            System.out.println("server is started");
            return 0;
        }
        catch (HttpHostConnectException e)
        {
            System.out.println( "server is stopped" );
            return 1;
        }
        catch (Exception e)
        {
            return 2;
        }
    }

    private int execStartCmd(final File workingDir, final String[] opts)
    {
        new Thread(new Runnable() {

            @Override
            public void run()
            {
                Server server = new Server();

                Connector connector = new SelectChannelConnector();
                connector.setPort(Integer.getInteger("port").intValue());
                server.setConnectors(new Connector[] { connector });

                String war = workingDir+"/"+System.getProperty("war");

                WebAppContext webapp = new WebAppContext(war, "/ui-monitoring-server");
                server.setHandler(webapp);

                try
                {
                    System.out.println( "Starting server" );
                    server.start();
                    System.out.println( "Starting server [Done]" );
                    server.join();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).run();

        return 0;
    }

    private int execStopCmd(File workingDir, String[] opts)
    {
        try
        {
            System.out.println( "Stopping server" );
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://localhost:"+Integer.getInteger("port").intValue()+"/ui-monitoring-server/shutdown");
            HttpResponse response = client.execute(request);
            if(response.getStatusLine().getStatusCode() != 200)
            {
                System.out.println("Stopping server [Done]");
                return 0;
            }
            else
            {
                System.err.println( "Stopping server [Failed]" );
                return 1;
            }
        }
        catch (IOException e)
        {
            return 2;
        }
    }


}
