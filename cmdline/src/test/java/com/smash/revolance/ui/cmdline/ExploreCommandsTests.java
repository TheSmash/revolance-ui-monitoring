package com.smash.revolance.ui.cmdline;

import org.junit.Test;

import java.util.UUID;

/**
 * User: wsmash
 * Date: 08/10/13
 * Time: 21:15
 */
public class ExploreCommandsTests
{
    @Test
    public void exploreCmdShoudHandleExploration() throws Exception
    {
        Commands command = Commands.EXPLORE;
        command.exec( "src/test/resources/cfg-app.xml", UUID.randomUUID().toString() );
    }

}
