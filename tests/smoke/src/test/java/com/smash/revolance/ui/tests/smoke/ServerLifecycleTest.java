package com.smash.revolance.ui.tests.smoke;

import com.smash.revolance.ui.materials.CmdlineHelper;
import com.smash.revolance.ui.materials.TestConstants;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ServerLifecycleTest extends TestConstants
{
    @Test
    public void checkDistribContent()
    {
        Collection scripts = FileUtils.listFiles( DISTRIB_DIR, new String[]{"sh"}, false );
        assertThat( scripts.size(), is( 4 ) );
    }

    @Test
    public void serverLifecyle() throws Exception
    {
        CmdlineHelper start = new CmdlineHelper().dir( DISTRIB_DIR );
        CmdlineHelper statusAfterStart = new CmdlineHelper().dir( DISTRIB_DIR ).cmd( STATUS_SCRIPT );
        CmdlineHelper stop = new CmdlineHelper().dir( DISTRIB_DIR ).cmd( STOP_SCRIPT );
        CmdlineHelper statusAfterStop = new CmdlineHelper().dir( DISTRIB_DIR ).cmd( STATUS_SCRIPT );

        start.cmd( START_SCRIPT ).exec().awaitErr( "initialization completed", 10 );
        assertThat( start.hasExited(), is( false ) );

        statusAfterStart.sync().exec().awaitOut( "server is started", 10 );
        assertThat( statusAfterStart.exitValue(), is(0) );

        stop.sync().exec().outContains( "Stopping server" );
        assertThat( stop.exitValue(), is(0) );

        statusAfterStop.sync().exec().awaitOut( "server is stopped", 10 );
        assertThat( statusAfterStop.exitValue(), is(0) );
    }

}