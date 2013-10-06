package com.smash.revolance.ui.model.application;

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

import com.smash.revolance.ui.model.helper.XMLHelper;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: wsmash
 * Date: 16/02/13
 * Time: 19:01
 */
public class ApplicationManager
{
    private Map<String, Application> apps = new HashMap<String, Application>();

    public ApplicationManager(List<Application> applications)
    {
        for ( Application application : applications )
        {
            add( application );
        }
    }

    public ApplicationManager(File appCfg) throws Exception
    {
        this( XMLHelper.getApplications( new ApplicationFactory(), appCfg ) );
    }

    public Application getApplication(String appId)
    {
        return apps.get( appId );
    }

    public void add(Application application)
    {
        apps.put( application.getId(), application );
    }

    public List<Application> getApplications()
    {
        return Arrays.asList( apps.values().toArray( new Application[apps.size()] ) );
    }

}
