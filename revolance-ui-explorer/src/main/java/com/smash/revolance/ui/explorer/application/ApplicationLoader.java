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

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: wsmash
 * Date: 16/05/13
 * Time: 19:53
 */
public class ApplicationLoader
{
    private final  File           userCfg;
    private JarClassLoader jarLoader;
    private boolean        loaded;

    public ApplicationLoader(File userCfg) throws MalformedURLException
    {
        this.userCfg = userCfg;
    }

    private URL[] toURL(Collection<File> files)
    {
        List<URL> urls = new ArrayList<URL>();

        for ( File file : files )
        {
            try
            {
                urls.add( file.toURI().toURL() );
            }
            catch (MalformedURLException e)
            {
                // Ignore gently
            }
        }

        return urls.toArray( new URL[urls.size()] );
    }

    private Class loadClass(File appDir, String className) throws ClassNotFoundException
    {
        loadAllApplications( appDir );
        return jarLoader.loadClass( className );
    }

    public Application loadApplication(ApplicationManager manager, String applicationImpl) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException
    {
        Class applicationClass = loadClass( manager.getApplicationsDir(), applicationImpl );
        Application application = (Application) applicationClass.getDeclaredConstructor(ApplicationManager.class, File.class).newInstance(manager, userCfg);
        return application;
    }

    public void loadAllApplications(File appDir)
    {
        if(!loaded)
        {
            Collection<File> files = FileUtils.listFiles(appDir , new String[]{"jar"}, false );
            jarLoader = new JarClassLoader( toURL( files ) );
            loaded = true;
        }
    }



}
