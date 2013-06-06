package com.smash.revolance.ui.explorer.application;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * User: wsmash
 * Date: 01/06/13
 * Time: 00:44
 */
public class ApplicationFactory
{
    private Map<String, Application>    applicationInstances = new HashMap<String, Application>();
    private Map<String, ClassLoader> applicationLoaders   = new HashMap<String, ClassLoader>();

    public Application buildApplication(ApplicationConfiguration setup) throws NoSuchMethodException, IllegalAccessException, InstantiationException, IOException, InvocationTargetException, ClassNotFoundException
    {
        Application app = getApplication( setup.getId(), setup.getApplicationDir(), setup.getApplicationImplementation(), setup.getApplicationVersion() );
        app.setup( setup );
        return app;
    }

    private Class loadClass(String appDir, String appId, String impl, String version) throws ClassNotFoundException, IOException
    {
        ClassLoader loader = getLoader( appDir, appId, impl, version );
        if ( loader == null )
        {
            loader = Thread.currentThread().getContextClassLoader();
        }
        return loader.loadClass( impl );
    }

    private Application getApplication(String appId, String appDir, String impl, String version) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException
    {
        if(!applicationInstances.containsKey( getKey( appId, impl, version ) ))
        {
            Class applicationClass = loadClass( appDir, appId, impl, version );
            Application application = (Application) applicationClass.getDeclaredConstructor().newInstance();
            applicationInstances.put( getKey( appId, impl, version ), application );
        }
        return applicationInstances.get( getKey( appId, impl, version ) );
    }

    private ClassLoader getLoader(String appDir, String appId, String impl, String version) throws IOException
    {
        if ( !applicationLoaders.containsKey( getKey( appId, impl, version ) ) )
        {
            loadApplication( appDir, appId, impl, version );
        }
        return applicationLoaders.get( getKey( appId, impl, version ) );
    }

    private String getKey(String appId, String impl, String version)
    {
        return appId+"["+impl+(version==null?"":"#"+version);
    }

    private void loadApplication(String appDir, String appId, String impl, String version) throws IOException
    {
        if(!appDir.isEmpty() && new File(appDir).isDirectory())
        {
            Collection<File> files = FileUtils.listFiles( new File( appDir ), new String[]{"jar"}, false );

            for(File file : files)
            {
                JarFile jar = new JarFile( file );
                Manifest manifest = jar.getManifest();
                Attributes attributes = manifest.getMainAttributes();

                String implAttr = attributes.getValue( "revolance-ui-explorer-applicationImpl" );
                String versionAttr =  attributes.getValue( "revolance-ui-explorer-applicationVersion" );

                if( implAttr.contentEquals( impl ) && (versionAttr.contentEquals( version ) || version == null) )
                {
                    applicationLoaders.put( getKey( appId, impl, version ), new JarClassLoader( file.toURI().toURL() ) );
                }
            }

        }
    }
}
