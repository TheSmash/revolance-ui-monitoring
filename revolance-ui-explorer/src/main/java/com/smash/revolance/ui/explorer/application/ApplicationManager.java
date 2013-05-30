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

import com.smash.revolance.ui.explorer.helper.XMLHelper;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.*;

/**
 * User: wsmash
 * Date: 16/02/13
 * Time: 19:01
 */
public class ApplicationManager
{
    private Application appRef;
    private Map<String, Application> apps = new HashMap<String, Application>();

    private File appCfg;
    private File userCfg;
    private File applicationsDir;

    public ApplicationManager(ApplicationManager manager)
    {
        this.appRef = manager.appRef;
        this.apps = new HashMap<String, Application>( manager.apps );
        this.appCfg = new File(manager.appCfg.getAbsolutePath());
        this.userCfg = new File(manager.userCfg.getAbsolutePath());
        this.applicationsDir = new File(manager.applicationsDir.getAbsolutePath());
    }

    public ApplicationManager(File appCfg) throws Exception
    {
        this( XMLHelper.getApplicationManager( appCfg ) );
    }

    public ApplicationManager(File appCfg, File userCfg)
    {
        this.appCfg = appCfg;
        this.userCfg = userCfg;
    }

    public Application getRefApp()
    {
        return appRef;
    }

    public Application get(String instanceId)
    {
        return apps.get(instanceId);
    }

    public Application buildApplication(String applicationImpl) throws ClassNotFoundException, IllegalAccessException, InstantiationException, MalformedURLException, NoSuchMethodException, InvocationTargetException
    {
        ApplicationLoader appLoader = new ApplicationLoader( userCfg );
        Application app = appLoader.loadApplication( this, applicationImpl );
        return app;
    }

    public void add(Application application)
    {
        apps.put( application.getId(), application );
        if(application.isRef())
        {
            appRef = application;
        }
    }

    public List<Application> getApplications()
    {
        return Arrays.asList( apps.values().toArray( new Application[apps.size()] ) );
    }

    public void explore() throws Exception
    {
        for(Application application : getApplications())
        {
            application.explore();
        }
    }

    public File getApplicationsDir()
    {
        return applicationsDir;
    }

    public void setApplicationsDir(String applicationDir)
    {
        this.applicationsDir = new File(getHome(), applicationDir);
    }

    public String getHome()
    {
        return appCfg.getParentFile().getParentFile().getAbsolutePath();
    }
}
