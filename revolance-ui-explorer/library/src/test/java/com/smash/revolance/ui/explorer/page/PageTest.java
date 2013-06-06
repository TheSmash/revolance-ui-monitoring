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
import com.smash.revolance.ui.explorer.element.api.Data;
import com.smash.revolance.ui.explorer.element.api.Element;
import com.smash.revolance.ui.explorer.element.api.Link;
import com.smash.revolance.ui.explorer.page.api.Page;
import com.smash.revolance.ui.explorer.user.User;
import com.smash.revolance.ui.explorer.UserExplorer;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
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
        Application app = manager.getApplication( "website" );

        user = app.getUser( "super user" );

        user.setDomain( index );

        user.enablePageScreenshot( false );
        user.enablePageElementScreenshot( false );
        user.setFollowButtons( false );
        user.setFollowLinks( false );
        user.setBrowserType( "Firefox" );
    }

    @After
    public void tearDown() throws Exception
    {
        user.getBrowser().quit();
    }

    @Test
    public void botShouldGrabAllPageContent() throws Exception
    {
        Page page = new Page( user, index );
        new UserExplorer( user ).explore( page );

        List<Element> content = page.getContent();

        assertThat( content.size(), is( 14 ) );

        assertThat( page.getLink( "link_1" ), notNullValue() );
        assertThat( page.getLink( "link_2" ), notNullValue() );

        assertThat( page.getButton( "button_1" ), notNullValue() );
        assertThat( page.getButton( "button_2" ), notNullValue() );

        assertThat( page.getData( "h1 content" ), notNullValue() );
        assertThat( page.getData( "h2 content" ), notNullValue() );
        assertThat( page.getData( "h3 content" ), notNullValue() );
        assertThat( page.getData( "p content" ), notNullValue() );

        assertThat( page.getFields().size(), is( 2 ) );

        assertThat( page.getImages().size(), is( 1 ) );
    }




}
