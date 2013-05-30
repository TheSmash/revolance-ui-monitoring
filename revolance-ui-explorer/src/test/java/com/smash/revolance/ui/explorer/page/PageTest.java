package com.smash.revolance.ui.explorer.page;

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

import com.smash.revolance.ui.explorer.application.Application;
import com.smash.revolance.ui.explorer.application.ApplicationManager;
import com.smash.revolance.ui.explorer.element.IElement;
import com.smash.revolance.ui.explorer.element.api.Button;
import com.smash.revolance.ui.explorer.element.api.Link;
import com.smash.revolance.ui.explorer.page.api.Page;
import com.smash.revolance.ui.explorer.user.User;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * User: wsmash
 * Date: 20/02/13
 * Time: 22:37
 */
public class PageTest
{
    String index  = "file://" + new File( new File( "" ).getAbsoluteFile(), "src/test/resources/pages/page.html" ).getAbsolutePath();
    File   appCfg = new File( new File( "" ).getAbsoluteFile(), "src/test/config/cfg-app.xml" );
    private User user;

    @Before
    public void setupTest() throws Exception
    {
        ApplicationManager manager = new ApplicationManager( appCfg );
        Application app = manager.get( "website" );

        user = app.getUser( "super user" );

        user.setDomain( "file://" );
        user.enablePageScreenshot( false );
        user.enablePageElementScreenshot( false );
        user.enableFollowButtons( false );
        user.enableFollowLinks( false );
    }

    @Test
    public void filteringElementIncludedInEachOthersShouldConserveClickableElementsInsteadOfData() throws Exception
    {
        Page page = new Page( user, index );
        page.explore();

        List<IElement> content = page.getContent();

        assertThat( content.size(), is( 3 ) );
        assertThat( Link.containsLink( content, "link_1" ), is( true ) );
        assertThat( Button.containsButton( content, "button_1" ), is( true ) );
        assertThat( Button.containsButton( content, "button_2" ), is( true ) );

    }
}
