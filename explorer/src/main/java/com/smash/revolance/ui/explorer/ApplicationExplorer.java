package com.smash.revolance.ui.explorer;

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

import com.smash.revolance.ui.model.application.Application;
import com.smash.revolance.ui.model.user.User;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: wsmash
 * Date: 02/06/13
 * Time: 12:35
 */
public class ApplicationExplorer
{
    private final Application application;

    public ApplicationExplorer(Application application)
    {
        this.application = application;
    }

    public void explore(long timeout) throws Exception
    {
        for ( User user : application.getUsers() )
        {
            new UserExplorer( user ).start();
        }

        awaitExplorationComplete( application.getUsers(), timeout * 60000 );

        for ( User user : application.getUsers() )
        {
            user.doContentReport( new File( user.getReportFolder(), "sitemap.json" ) );
        }
    }

    private void awaitExplorationComplete(List<User> users, long timeout)
    {
        List<User> remainingUsers = new ArrayList<User>();
        remainingUsers.addAll( users );
        long duration = 0;
        while ( !remainingUsers.isEmpty() && duration < timeout )
        {
            duration += await( 15 );

            List<User> usersToRemove = new ArrayList<User>();
            for ( User user : users )
            {
                if ( user.isExplorationDone() )
                {
                    usersToRemove.add( user );
                }
            }

            remainingUsers.removeAll( usersToRemove );
        }
    }

    private int await(int duration)
    {
        int i = 0;
        while ( i < duration )
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
        return i;
    }


}
