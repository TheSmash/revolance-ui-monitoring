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

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static java.io.File.createTempFile;

/**
 * User: wsmash
 * Date: 10/10/13
 * Time: 22:14
 */
public class CmdlineHelper extends TestConstants
{
    private final String id;
    private boolean sync = false;
    private File     out;
    private String[] cmd;
    private File     dir;
    private Process  process;
    private File     in;
    private File     err;


    public CmdlineHelper() throws IOException
    {
        id = UUID.randomUUID().toString();

        err = createTempFile ( "err-" + id, ".txt" );
        out = createTempFile( "out-" + id, ".txt" );
        in = createTempFile ( "in-"  + id, ".txt" );
    }

    public CmdlineHelper cmd(String... cmd)
    {
        this.cmd = cmd;
        return this;
    }

    public CmdlineHelper dir(File dir)
    {
        this.dir = dir;
        return this;
    }

    public CmdlineHelper dir(String target)
    {
        dir( new File( target ) );
        return this;
    }

    public CmdlineHelper sync()
    {
        sync = true;
        return this;
    }

    public CmdlineHelper exec() throws InterruptedException, IOException
    {
        ProcessBuilder pb = new ProcessBuilder(  );

        if(dir != null)
        {
            pb.directory( dir );
        }

        pb.command( cmd );

        pb.redirectError( ProcessBuilder.Redirect.to( err ) );
        pb.redirectOutput( ProcessBuilder.Redirect.to( out ) );
        pb.redirectInput( ProcessBuilder.Redirect.from( in ) );

        System.out.println( "Executing cmd: " + cmd[0] + " from dir: " + dir  );
        System.out.println( "Redirecting out to: " + out.getAbsolutePath() );
        System.out.println( "Redirecting err to: " + err.getAbsolutePath() );

        Process process = pb.start();
        if(sync)
        {
            process.waitFor();
        }

        this.process = process;

        return this;
    }

    public int exitValue()
    {
        return process.exitValue();
    }

    public String id()
    {
        return id;
    }

    public void write(String in) throws IOException
    {
        FileUtils.writeStringToFile( this.in, in );
    }

    public void waitInSec(int seconds)
    {
        long mark = System.currentTimeMillis();
        while( (System.currentTimeMillis() - mark) < seconds*1000)
        {
            sleep( 1 );
        }
    }

    private void sleep(int seconds)
    {
        try
        {
            Thread.sleep( seconds );
        }
        catch (InterruptedException e)
        {
            // Ignore gently
        }
    }

    public CmdlineHelper awaitOut(String line, int timeout) throws Exception
    {
        long mark = System.currentTimeMillis();
        timeout = timeout*1000;

        do
        {
            sleep( 1000 );
            if( outContains( line ) )
            {
                return this;
            }
        }
        while( (System.currentTimeMillis()-mark) <timeout );

        if( !outContains( line ) )
        {
            throw new Exception( "Unable to find: '" + line + "' in out" );
        }
        return this;
    }

    public boolean hasExited()
    {
        try
        {
            process.exitValue();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public CmdlineHelper kill()
    {
        process.destroy();
        return this;
    }

    public File getOut()
    {
        return out;
    }

    public File getErr()
    {
        return err;
    }

    public File getIn()
    {
        return in;
    }

    public String out() throws IOException
    {
        return FileUtils.readFileToString( getOut() );
    }

    public String err() throws IOException
    {
        return FileUtils.readFileToString( getErr() );
    }

    public CmdlineHelper withoutErrors() throws Exception
    {
        if(!err().isEmpty())
        {
            throw new Exception( "Process execution generated errors in file: " + err );
        }
        return this;
    }

    public CmdlineHelper awaitErr(String line, int timeout) throws Exception
    {
        long mark = System.currentTimeMillis();
        timeout = timeout*1000;

        do
        {
            if( errContains( line ) )
            {
                return this;
            }
            else
            {
                sleep( 1000 );
            }
        }
        while( (System.currentTimeMillis()-mark) <timeout );

        if( !errContains( line ) )
        {
            throw new Exception( "Unable to find: '" + line + "' in err" );
        }
        return this;
    }

    public boolean errContains(String line) throws IOException
    {
        return err().contains( line );
    }

    public boolean outContains(String line) throws IOException
    {
        return out().contains( line );
    }

}
