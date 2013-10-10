package com.smash.revolance.ui.cmdline;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ServerCommandsTests
{

    private final String port = "8080";

    @Test
    public void lifecycleTest() throws Exception
    {
        assertThat( Commands.STATUS       .exec( port ), is(-1) );
        assertThat( Commands.START_SERVER .exec( port ), is(0) );
        assertThat( Commands.STATUS       .exec( port ), is(0) );
        assertThat( Commands.STOP_SERVER  .exec( port ), is(0) );
        assertThat( Commands.STATUS       .exec( port ), is(0) );
    }

}