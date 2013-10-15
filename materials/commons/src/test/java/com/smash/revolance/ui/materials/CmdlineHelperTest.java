package com.smash.revolance.ui.materials;

import org.junit.Test;


/**
 * User: wsmash
 * Date: 15/10/13
 * Time: 19:55
 */
public class CmdlineHelperTest extends TestConstants
{
    @Test
    public void cmdlineHelperShouldExecCmdAndRedirectStdOut() throws Exception
    {
        CmdlineHelper pwd = new CmdlineHelper().dir( TARGET ).cmd("pwd");
        pwd.exec().awaitOut( TARGET, 1 );
        pwd.exec().withoutErrors();
    }
}
