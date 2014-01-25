package com.smash.revolance.ui.explorer;

/*
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Revolance-UI-Explorer
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2012 - 2013 RevoLance
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

import com.smash.revolance.ui.model.application.Application;
import com.smash.revolance.ui.model.helper.JarClassLoader;
import com.smash.revolance.ui.model.user.User;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.UUID;

/**
 * User: wsmash
 * Date: 19/10/13
 * Time: 22:29
 */
@Service
public class Explorer implements IExplorer
{
    private static Logger LOG = Logger.getLogger(Explorer.class);


    @Override
    public void explore(ExplorationConfiguration configuration) throws Exception
    {
        // LOG.addAppender(new FileAppender(new SimpleLayout(), internalConfiguration.getLogFile().getAbsolutePath()));

        User user = new User(configuration.getId(), configuration.getUrl());
        user.setDomain(configuration.getDomain());

        user.setFollowLinks(configuration.isFollowLinksEnabled());
        user.setFollowButtons(configuration.isFollowButtonsEnabled());

        if(configuration.isApplicationSecured())
        {
            File applicationJar = storeApplication(configuration.getApplication());
            Application application = instanciateApplication(applicationJar, configuration.getApplicationClassName());
            user.setApplication(application);
        }

        user.enablePageScreenshot(true);
        user.enablePageElementScreenshot(true);

        user.setBrowserType(configuration.getBrowserType());
        user.setBrowserWidth(configuration.getBrowserWidth());
        user.setBrowserHeight(configuration.getBrowserHeight());

        user.setExcludedLinks(configuration.getExcludedLinks());
        user.setExcludedButtons(configuration.getExcludedButtons());

        user.setLogin(configuration.getLogin());
        user.setPasswd(configuration.getPassword());

        user.setDriverPath(configuration.getDriverPath());
        user.setBrowserPath(configuration.getBrowserPath());

        LOG.info("Launching exploration id: " + user.getId());
        LOG.info("Log file: " + configuration.getLogFile());

        FileAppender logger = new FileAppender(new SimpleLayout(), configuration.getLogFile().getAbsolutePath());

        UserExplorer explorer = new UserExplorer( user, logger, configuration.getReportFile(), configuration.getTimeout() );
        explorer.explore();
    }

    private Application instanciateApplication(File applicationJar, String fullClassName) throws ClassNotFoundException, MalformedURLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException
    {
        ClassLoader loader = new JarClassLoader( applicationJar.toURI().toURL() );
        if ( loader == null )
        {
            loader = Thread.currentThread().getContextClassLoader();
        }
        Class<?> applicationClass = loader.loadClass(fullClassName);
        return (Application) applicationClass.getDeclaredConstructor().newInstance();
    }

    private File storeApplication(byte[] application) throws IOException
    {
        File file = File.createTempFile(UUID.randomUUID().toString(), "jar");
        FileUtils.writeByteArrayToFile(file, application);
        return file;
    }

}
