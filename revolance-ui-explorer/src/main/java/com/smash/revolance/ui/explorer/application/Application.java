package com.smash.revolance.ui.explorer.application;

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

import com.smash.revolance.ui.explorer.page.IPage;
import com.smash.revolance.ui.explorer.user.User;
import org.openqa.selenium.Alert;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: wsmash
 * Date: 26/01/13
 * Time: 15:21
 */
public abstract class Application
{
    private File userCfg;

    private List<User> users = new ArrayList<User>();

    private User             refUser;
    private ApplicationSetup setup;

    private String id;
    private boolean ref = false;
    private String url;

    private File               regressionReport;
    private ApplicationManager manager;
    private String             domain;
    private String             impl;
    private boolean            exploreVariants;

    protected Application(ApplicationManager manager)
    {
        users = new ArrayList<User>();
        this.manager = manager;
    }

    public Application(ApplicationManager manager, File userCfg) throws Exception
    {
        this( manager );
        this.userCfg = userCfg;
    }

    public Application(ApplicationManager manager, String userCfg) throws Exception
    {
        this( manager, new File( userCfg ) );
    }

    public Application(Application application, String version)
    {
        this.setup = application.setup;
        this.manager = application.manager;
        this.regressionReport = new File( application.regressionReport.getAbsolutePath() );
        this.url = application.url;
        this.ref = application.ref;
        this.refUser = application.refUser;
        this.users = new ArrayList<User>( application.users );
        this.userCfg = new File(application.userCfg.getAbsolutePath());
        this.domain = application.domain;
        this.id = version;
    }

    public String getId()
    {
        return id;
    }

    public void setup(ApplicationSetup cfg)
    {
        this.setup = new ApplicationSetup(cfg);
    }

    public List<User> getUsers()
    {
        return users;
    }

    public void addUser(User user) throws Exception
    {
        user.setApplication( this );

        user.setDomain( getDomain() );
        user.setHome( getHomeUrl() );

        user.enableFollowLinks(isFollowLinksEnabled());
        if (isFollowLinksEnabled())
        {
            user.setExcludedLinks(getExcludedLinks());
        }

        user.enableFollowButtons(isFollowButtonsEnabled());
        if (isFollowButtonsEnabled())
        {
            user.setExcludedButtons(getExcludedButtons());
        }

        user.enablePageScreenshot(isTakePageScreenshotEnabled());
        user.enablePageElementScreenshot(isTakePageElementScreenshotEnabled());

        user.setBaseReportFolder(new File(getReportFolder()));

        user.setBrowserHeight(getBrowserHeight());
        user.setBrowserWidth( getBrowserWidth() );
        user.setBrowserBinary(getBrowserBinary());

        user.setExploreVariants( getExploreVariants() );

        users.add(user);
    }

    public User getRefUser()
    {
        return refUser;
    }

    public void setRefUser(User refUser)
    {
        this.refUser = refUser;
    }

    /*
    {

        //TODO: Check if the user has already logged in or not and raise error accordingly

        user.getBrowser().findElement(By.id("username")).sendKeys(user.getLogin());
        user.getBrowser().findElement(By.id("password")).sendKeys(user.getPasswd());
        user.getBrowser().findElement(By.id("login-form")).submit();
        user.setLoggedIn(true);


    }
    */

    /*
{
    //TODO: Check if the user has already change the pwd or not and raise error accordingly

    //TODO: add new password field in the user object

    user.getBrowser().findElement(By.id("passwordNew")).sendKeys(user.getPasswd());
    user.getBrowser().findElement(By.id("passwordConfirm")).sendKeys(user.getPasswd());
    user.getBrowser().findElement(By.id("change-password-form")).submit();

}
*/

    public int getUserCount() throws Exception
    {
        return getUsers().size();
    }

    public String getReportFolder()
    {
        return setup.getReportFolder();
    }

    public User getUser(String id) throws Exception
    {
        for(User user : getUsers())
        {
            if(user.getId().contentEquals(id))
            {
                return user;
            }
        }
        throw new Exception("User with id: " + id + " cannot be found in the application.");
    }

    public boolean isTakePageScreenshotEnabled()
    {
        return setup.isTakePageScreenshotEnabled();
    }

    public boolean isTakePageElementScreenshotEnabled()
    {
        return setup.isTakePageElementScreenshotEnabled();
    }

    public boolean isFollowLinksEnabled()
    {
        return setup.isFollowLinksEnabled();
    }

    public List<String> getExcludedLinks()
    {
        return setup.getExcludedLinks();
    }

    public List<String> getExcludedButtons()
    {
        return setup.getExcludedButtons();
    }


    public boolean isFollowButtonsEnabled()
    {
        return setup.isFollowButtonsEnabled();
    }

    public boolean isRef()
    {
        return ref;
    }

    public void setRef(boolean ref)
    {
        this.ref = ref;
    }

    public void setHomeUrl(String url) throws Exception
    {
        this.url = url;
        for(User user : getUsers())
        {
            user.setHome( url );
        }
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getHomeUrl()
    {
        return url;
    }

    public void setReportFolder(String reportFolder)
    {
        setup.setReportFolder(reportFolder + "/" + id);
    }

    public void addUsers(List<User> users) throws Exception
    {
        for(User user : users)
        {
            if (user.isRef())
            {
                setRefUser(user);
            }
            addUser(user);
        }
    }

    public void setDomain(String domain)
    {
        this.domain = domain;
        for(User user : getUsers())
        {
            user.setDomain(domain);
        }
    }

    public String getDomain()
    {
        return domain;
    }

    public void explore() throws Exception
    {
        for(User user : getUsers())
        {
            Tester tester = new Tester( user );
            new Thread( tester ).start();
        }

        awaitExplorationComplete(getUsers());
    }

    private void awaitExplorationComplete(List<User> users)
    {
        List<User> remainingUsers = new ArrayList<User>(  );
        remainingUsers.addAll( users );

        while ( !remainingUsers.isEmpty() )
        {
            await( 90 );
            List<User> usersToRemove = new ArrayList<User>(  );
            for(User user : users)
            {
                if( user.isExplorationDone() )
                {
                    usersToRemove.add( user );
                }
            }

            remainingUsers.removeAll( usersToRemove );
        }
    }

    private void await(int duration)
    {
        int i = 0;
        while(i<duration)
        {
            try
            {
                Thread.sleep( 1000 );
                i++;

            }
            catch (InterruptedException e)
            {
                // Ignore gently
            }
        }
    }


    public ApplicationManager getApplicationManager()
    {
        return manager;
    }

    public int getBrowserWidth()
    {
        return setup.getBrowserWidth();
    }

    public int getBrowserHeight()
    {
        return setup.getBrowserHeight();
    }

    public abstract void enterNewPassword(User user, IPage page) throws Exception;
    public abstract void enterLogin(User user, IPage page) throws Exception;

    public abstract boolean isPageBroken(IPage page);
    public abstract boolean isUnauthorized(IPage page);

    public abstract boolean awaitPageLoaded(IPage page) throws Exception;

    public abstract boolean isSecured();


    public void doSitemapReport() throws Exception
    {
        for(User user : getUsers())
        {
            user.doSitemapReport();
        }
    }

    public String getBrowserBinary()
    {
        return setup.getBrowserBinary();
    }

    public void setImpl(String impl)
    {
        this.impl = impl;
    }

    public String getImpl()
    {
        return impl;
    }

    public abstract void handleAlert(Alert alert);

    public boolean getExploreVariants()
    {
        return exploreVariants;
    }

    public void setExploreVariants(boolean b)
    {
        this.exploreVariants = b;
    }
}
