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
import com.smash.revolance.ui.explorer.user.Tester;
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
    private List<User> users = new ArrayList<User>();

    private ApplicationConfiguration bean = new ApplicationConfiguration();

    public Application()
    {

    }

    public Application(Application application)
    {
        this.bean = application.bean;
        this.users = new ArrayList<User>( application.users );
    }

    public String getId()
    {
        return bean.getId();
    }

    public void setup(ApplicationConfiguration cfg)
    {
        this.bean = new ApplicationConfiguration( cfg );
    }

    public List<User> getUsers()
    {
        return users;
    }

    public void addUser(User user) throws Exception
    {
        if ( !users.contains( user ) )
        {
            user.setApplication( this );
            users.add( user );
        }
    }

    public void reloadUsers() throws Exception
    {
        for ( User user : getUsers() )
        {
            addUser( user );
        }
    }

    public int getUserCount() throws Exception
    {
        return getUsers().size();
    }

    public String getReportFolder()
    {
        return bean.getReportFolder();
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

    public void setId(String id)
    {
        this.bean.setId( id );
    }

    public void setReportFolder(String reportFolder) throws Exception
    {
        bean.setReportFolder( reportFolder + "/" + getId() );
        for(User user : getUsers())
        {
            user.setBaseReportFolder( bean.getReportFolder() );
        }
    }

    public void setDomain(String domain) throws Exception
    {
        bean.setDomain( domain );
        for(User user : getUsers())
        {
            user.setDomain( domain );
        }
    }

    public void setUsersHome(String home)
    {
        for(User user : getUsers())
        {
            user.setHome( home );
        }
    }

    public void setExcludedLink(String link)
    {
        for(User user : getUsers())
        {
            user.addExcludedLink( link );
        }
    }

    public void setExcludedButton(String button)
    {
        for(User user : getUsers())
        {
            user.addExcludedButtons( button );
        }
    }

    public String getDomain()
    {
        return bean.getDomain();
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
            await( 15 );
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

    public abstract void handleAlert(Alert alert);

    public abstract boolean enterNewPassword(User user, IPage page) throws Exception;
    public abstract boolean enterLogin(User user, IPage page) throws Exception;

    public abstract boolean isPageBroken(IPage page);
    public abstract boolean isAuthorized(IPage page);
    public abstract boolean isSecured();

    public abstract void awaitPageLoaded(IPage page) throws Exception;

    public File getUsersCfgFile()
    {
        return bean.getUsersCfgFile();
    }

    public int getBrowserHeight()
    {
        return bean.getBrowserHeight();
    }

    public int getBrowserWidth()
    {
        return bean.getBrowserWidth();
    }

    public boolean isExploreVariantsEnabled()
    {
        return bean.isExploreVariantsEnabled();
    }

    public boolean isPageElementScreenshotEnabled()
    {
        return bean.isPageElementScreenshotEnabled();
    }

    public boolean isPageScreenshotEnabled()
    {
        return bean.isPageScreenshotEnabled();
    }

    public boolean isFollowLinksEnabled()
    {
        return bean.isFollowLinksEnabled();
    }

    public boolean isFollowButtonsEnabled()
    {
        return bean.isFollowButtonsEnabled();
    }
}
