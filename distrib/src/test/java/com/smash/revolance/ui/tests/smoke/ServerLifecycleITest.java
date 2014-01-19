package com.smash.revolance.ui.tests.smoke;

/*
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * ui-monitoring-distrib
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2012 - 2014 RevoLance
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

import com.smash.revolance.ui.materials.CmdlineHelper;
import com.smash.revolance.ui.materials.TestConstants;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.text.StringEndsWith.endsWith;
import static org.junit.Assert.assertThat;
import static org.junit.internal.matchers.IsCollectionContaining.hasItems;
import static org.junit.internal.matchers.StringContains.containsString;

public class ServerLifecycleITest extends TestConstants
{
    private static CmdlineHelper statusAfterStop;
    private static CmdlineHelper start;
    private static CmdlineHelper stop;
    private static CmdlineHelper statusAfterStart;

    @BeforeClass
    public static void setupTests() throws Exception
    {
        start = new CmdlineHelper().dir( DISTRIB_DIR ).cmd( START_SCRIPT );
        statusAfterStart = new CmdlineHelper().dir( DISTRIB_DIR ).cmd( STATUS_SCRIPT );
        stop = new CmdlineHelper().dir( DISTRIB_DIR ).cmd( STOP_SCRIPT );
        statusAfterStop = new CmdlineHelper().dir( DISTRIB_DIR ).cmd( STATUS_SCRIPT );

        // startServerAndCheckStatus();
    }

    @AfterClass
    public static void teardownTests() throws Exception
    {
        // stopServerAndCheckStatus();
    }

    @Test
    public void checkDistribContent()
    {
        assertThat( listDistribFiles(), hasItems(   endsWith("/start.sh"),
                                                    endsWith("/stop.sh"),
                                                    endsWith("/status.sh"),
                                                    endsWith("/config/browsers.xml"),
                                                    containsString("/bin/ui-monitoring-cmdline"),
                                                    containsString("/web-apps/ui-monitoring-server"),
                                                    containsString("/samples/")));
    }

    public static List<String> listDistribFiles()
    {
        List<String> files = new ArrayList<>();
        List<File> distribFiles = (List<File>) FileUtils.listFiles(DISTRIB_DIR, null, true);
        for(File file : distribFiles)
        {
            files.add(file.toString());
        }
        return files;
    }

    public static void stopServerAndCheckStatus() throws Exception
    {
        stop.sync().exec().outContains( "Stopping server [Done]" );
        assertThat( stop.exitValue(), is(0) );

        statusAfterStop.sync().exec().awaitOut( "server is stopped", 10 );
        assertThat( statusAfterStop.exitValue(), is(0) );
    }

    public static void startServerAndCheckStatus() throws Exception
    {
        start.exec().awaitOut("Starting server [Done]", 10);
        assertThat(start.hasExited(), is(false));

        statusAfterStart.sync().exec().awaitOut( "server is started", 10 );
        assertThat( statusAfterStart.exitValue(), is(0) );
    }

}