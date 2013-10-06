package com.smash.revolance.ui.comparator;

import com.smash.revolance.ui.explorer.UserExplorer;
import com.smash.revolance.ui.model.application.ApplicationManager;
import com.smash.revolance.ui.model.user.User;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

/**
 * User: wsmash
 * Date: 27/09/13
 * Time: 19:58
 */
@Ignore
public class TestRunner extends BaseTests
{
    User user;

    @Before
    public void setUp() throws Exception
    {
        manager = new ApplicationManager( new File( APP_CFG ) );
        app = manager.getApplication( "website" );

        user = app.getUser( "userA" );

        setupBrowserForFirefox( user );

        user.enablePageScreenshot( true );
        user.enablePageElementScreenshot( true );
        user.setFollowButtons( false );
        user.setFollowLinks( false );
        user.setExploreVariantsEnabled( false );
    }

    @Test
    public void run() throws Exception
    {
        user.setDomain( REF_PAGE );
        user.setHome( REF_PAGE_HOME );

        new UserExplorer( user ).explore();

        bot = user.getBot();
        browser = bot.getBrowser();
        sitemap = user.getSiteMap();

        user.doContentReport(new File( "target/sitemap.json" ));

        browser.quit();
    }

}
