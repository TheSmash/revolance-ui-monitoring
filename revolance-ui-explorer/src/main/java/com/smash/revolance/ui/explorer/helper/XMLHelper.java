package com.smash.revolance.ui.explorer.helper;

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
import com.smash.revolance.ui.explorer.application.ApplicationConfiguration;
import com.smash.revolance.ui.explorer.application.ApplicationFactory;
import com.smash.revolance.ui.explorer.application.ApplicationManager;
import com.smash.revolance.ui.explorer.user.User;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: wsmash
 * Date: 23/01/13
 * Time: 12:06
 */
public class XMLHelper
{

    public static List<User> loadUsersFromConfigFile(Application application) throws ParserConfigurationException, SAXException, IOException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        List<User> users = new ArrayList<User>(  );

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(application.getUsersCfgFile());

        NodeList elements = doc.getDocumentElement().getElementsByTagName("user");
        for(int elementIdx = 0 ; elementIdx < elements.getLength(); elementIdx ++)
        {
            Element element = (Element) elements.item( elementIdx );
            NodeList elementChilds = element.getChildNodes();

            String id = null;
            String appId = null;
            String login = null;
            String home = null;
            String password = null;
            String newPassword = null;

            String browserType = null;
            String driverPath = null;
            String browserBinary = null;

            for(int elementChildIdx = 0; elementChildIdx < elementChilds.getLength() ; elementChildIdx++)
            {
                Node elementChild = elementChilds.item( elementChildIdx );
                String elementChildName = elementChild.getNodeName();
                Node elementChildData = elementChild.getFirstChild();
                if( elementChildData != null )
                {
                    if( elementChildName.contentEquals("id") )
                    {
                        id = elementChildData.getNodeValue();
                    }
                    if(elementChildName.contentEquals( "appId" ))
                    {
                        appId = elementChildData.getNodeValue();
                    }
                    if( elementChildName.contentEquals("login") )
                    {
                        login = elementChildData.getNodeValue();
                    }
                    if( elementChildName.contentEquals("password") )
                    {
                        password = elementChildData.getNodeValue();
                    }
                    if( elementChildName.contentEquals("newPassword") )
                    {
                        newPassword = elementChildData.getNodeValue();
                    }
                    if( elementChildName.contentEquals("home") )
                    {
                        home = elementChildData.getNodeValue();
                    }
                    if(elementChildName.contentEquals( "browserType" ))
                    {
                        browserType = elementChildData.getNodeValue();
                    }
                    if(elementChildName.contentEquals( "browserBinary" ))
                    {
                        browserBinary = elementChildData.getNodeValue();
                    }
                    if(elementChildName.contentEquals( "driverPath" ))
                    {
                        driverPath = elementChildData.getNodeValue();
                    }
                }
            }

            if( application.getId().contentEquals( appId ) )
            {
                try
                {
                    User user = new User();
                    user.setId( id );
                    user.setLogin( login );
                    user.setPasswd( password );
                    user.setNewPasswd( newPassword );
                    user.setHome( home );

                    application.addUser( user );

                    user.setBrowserType( browserType );
                    user.setBrowserBinary( browserBinary );
                    user.setDriverPath( driverPath );

                    user.setFollowButtons( application.isFollowButtonsEnabled() );
                    user.setFollowLinks( application.isFollowLinksEnabled() );
                    user.setExploreVariantsEnabled( application.isExploreVariantsEnabled() );

                    user.setBaseReportFolder( application.getReportFolder() );
                    user.setPageScreenshotEnabled( application.isPageScreenshotEnabled() );
                    user.setPageElementScreenshotEnabled( application.isPageElementScreenshotEnabled() );

                    user.setBrowserWidth  ( application.getBrowserWidth()  );
                    user.setBrowserHeight ( application.getBrowserHeight() );

                    user.setExcludedButtons ( application.getExcludedButtons() );
                    user.setExcludedLinks   ( application.getExcludedLinks()   );

                    users.add( user );

                }
                catch (Exception e)
                {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }

        return users;
    }

    public static ApplicationConfiguration getApplicationSetup(File appCfg) throws Exception
    {
        if (!appCfg.exists())
        {
            throw new FileNotFoundException("Unable to find file: " + appCfg);
        }

        ApplicationConfiguration setup = new ApplicationConfiguration();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(appCfg);

        NodeList elements = doc.getDocumentElement().getElementsByTagName("configuration");
        Element element = (Element) elements.item( 0 );
        NodeList elementChilds = element.getChildNodes();


        // Purely application parameters
        String applicationDir = null;
        String applicationImpl = null;
        String applicationVersion = null;
        String usersCfgFile = null;

        // user parameters that are centralized
        String driverPath = null;
        String reportFolder = null;

        boolean followLinks = false;
        boolean followButtons = false;

        boolean takePageScreenshot = false;
        boolean takePageElementScreenshot = false;

        int browserHeight = 0;
        int browserWidth = 0;

        String browserBinary = null;
        String browserType = null;

        for(int elementChildIdx = 0; elementChildIdx < elementChilds.getLength() ; elementChildIdx++)
        {
            Node elementChild = elementChilds.item( elementChildIdx );
            String elementChildName = elementChild.getNodeName();
            Node elementChildData = elementChild.getFirstChild();
            if( elementChildName == null )
            {
                continue;
            }
            else
            {
                if(elementChildData != null)
                {
                    if( elementChildName.contentEquals("applicationImpl") )
                    {
                        applicationImpl = elementChildData.getNodeValue();
                    }
                    else if( elementChildName.contentEquals("applicationVersion") )
                    {
                        applicationVersion = elementChildData.getNodeValue();
                    }
                    else if( elementChildName.contentEquals("applicationDir") )
                    {
                        applicationDir = elementChildData.getNodeValue();
                    }
                    else if( elementChildName.contentEquals("usersCfgFile") )
                    {
                        usersCfgFile = elementChildData.getNodeValue();
                    }
                    else if( elementChildName.contentEquals("reportFolder") )
                    {
                        reportFolder = elementChildData.getNodeValue();
                    }
                    else if( elementChildName.contentEquals("followLinks") )
                    {
                        followLinks = Boolean.valueOf(elementChildData.getNodeValue());
                    }
                    else if( elementChildName.contentEquals("followButtons") )
                    {
                        followButtons = Boolean.valueOf(elementChildData.getNodeValue());
                    }
                    else if( elementChildName.contentEquals("takePageScreenshot") )
                    {
                        takePageScreenshot = Boolean.valueOf( elementChildData.getNodeValue() );
                    }
                    else if( elementChildName.contentEquals("takePageElementScreenshot") )
                    {
                        takePageElementScreenshot = Boolean.valueOf( elementChildData.getNodeValue() );
                    }
                    else if( elementChildName.contentEquals("driverPath") )
                    {
                        driverPath = elementChildData.getNodeValue();
                    }
                    else if( elementChildName.contentEquals("browserType") )
                    {
                        browserType = elementChildData.getNodeValue();
                    }
                    else if( elementChildName.contentEquals("browserWidth") )
                    {
                        browserWidth = Integer.parseInt( elementChildData.getNodeValue() );
                    }
                    else if( elementChildName.contentEquals("browserHeight") )
                    {
                        browserHeight = Integer.parseInt( elementChildData.getNodeValue() );
                    }
                    else if( elementChildName.contentEquals( "browserBinary" ))
                    {
                        browserBinary = elementChildData.getNodeValue();
                    }

                    else
                    {
                        System.err.println("The tag: " + elementChildName + " will not be proceed.");
                    }

                }
            }
        }

        // Application parameters
        setup.setAppCfgFile(appCfg);
        setup.setUsersCfgFile(usersCfgFile);
        setup.setApplicationDir( applicationDir );
        setup.setApplicationImpl(applicationImpl);
        setup.setApplicationVersion( applicationVersion );

        // User configuration parameters
        setup.setFollowLinks(followLinks);
        setup.setExcludedLinks( getExcludedLinks( appCfg ) );

        setup.setFollowButtons( followButtons );
        setup.setExcludedButtons( getExcludedButtons( appCfg ) );

        setup.setPageScreenshotEnabled( takePageScreenshot );
        setup.setPageElementScreenshotEnabled( takePageElementScreenshot );
        setup.setReportFolder( reportFolder );

        setup.setBrowserHeight(browserHeight);
        setup.setBrowserWidth( browserWidth );
        setup.setBrowserBinary( browserBinary );
        setup.setDriverPath( driverPath );
        setup.setBrowserType( browserType );

        setup.setExcludedButtons( getExcludedButtons( appCfg ) );
        setup.setExcludedLinks( getExcludedLinks( appCfg ) );

        return setup;
    }

    private static ArrayList<String> getExcludedLinks(File file) throws IOException, ParserConfigurationException, SAXException
    {
        if (!file.exists())
        {
            throw new FileNotFoundException("Unable to find file: " + file);
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);

        NodeList elements = doc.getDocumentElement().getElementsByTagName("excludeLinks");
        Element element = (Element) elements.item( 0 );
        NodeList elementChilds = element.getChildNodes();

        ArrayList<String> excludedLinks = new ArrayList<String>();
        for(int elementChildIdx = 0; elementChildIdx < elementChilds.getLength() ; elementChildIdx++)
        {
            Node elementChild = elementChilds.item( elementChildIdx );
            String elementChildName = elementChild.getNodeName();
            Node elementChildData = elementChild.getFirstChild();
            if( elementChildName != null )
            {
                if(elementChildData != null)
                {
                    if( elementChildName.contentEquals("excludeLinkText") )
                    {
                        String link = elementChildData.getNodeValue();
                        if(!link.trim().isEmpty())
                        {
                            excludedLinks.add(link);
                        }
                    }
                }
            }
        }
        return excludedLinks;
    }

    public static List<Application> getApplications(ApplicationFactory appFactory, File appCfg) throws Exception
    {
        if (!appCfg.exists())
        {
            throw new FileNotFoundException("Unable to find file: " + appCfg);
        }

        List<Application> applications = new ArrayList<Application>(  );

        ApplicationConfiguration setup = getApplicationSetup(appCfg);

        String applicationImpl = null;
        if(setup.getApplicationImplementation() != null)
        {
            applicationImpl = setup.getApplicationImplementation();
        }
        String applicationVersion = null;
        if(setup.getApplicationVersion() != null)
        {
            applicationVersion = setup.getApplicationVersion();
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(appCfg);

        NodeList elements = doc.getDocumentElement().getElementsByTagName("instance");
        for(int instanceIdx = 0; instanceIdx < elements.getLength(); instanceIdx++)
        {
            Element element = (Element) elements.item( instanceIdx );
            NodeList elementChilds = element.getChildNodes();

            String id = null;
            String domain = "";

            for(int elementChildIdx = 0; elementChildIdx < elementChilds.getLength() ; elementChildIdx++)
            {
                Node elementChild = elementChilds.item( elementChildIdx );
                String elementChildName = elementChild.getNodeName();
                Node elementChildData = elementChild.getFirstChild();
                if( elementChildName != null )
                {
                    if(elementChildData != null)
                    {
                        if( elementChildName.contentEquals("id") )
                        {
                            id = elementChildData.getNodeValue();
                        }
                        else if(elementChildName.contentEquals("domain"))
                        {
                            domain = elementChildData.getNodeValue();
                        }
                        else if(elementChildName.contentEquals( "applicationImpl" ))
                        {
                            applicationImpl = elementChildData.getNodeValue();
                        }
                        else if(elementChildName.contentEquals( "applicationVersion" ))
                        {
                            applicationVersion = elementChildData.getNodeValue();
                        }
                    }
                }
            }
            if( id != null && applicationImpl != null )
            {
                ApplicationConfiguration newSetup = new ApplicationConfiguration( setup );

                newSetup.setId( id );
                newSetup.setDomain( domain );
                newSetup.setApplicationImpl( applicationImpl );
                newSetup.setApplicationVersion( applicationVersion );

                Application app = appFactory.buildApplication( newSetup );

                XMLHelper.loadUsersFromConfigFile( app );

                applications.add( app );
            }
        }
        return applications;
    }

    private static ArrayList<String> getExcludedButtons(File file) throws IOException, ParserConfigurationException, SAXException
    {
        if (!file.exists())
        {
            throw new FileNotFoundException("Unable to find file: " + file);
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);

        NodeList elements = doc.getDocumentElement().getElementsByTagName("excludeButtons");
        Element element = (Element) elements.item( 0 );
        NodeList elementChilds = element.getChildNodes();

        ArrayList<String> excludedButtons = new ArrayList<String>();
        for(int elementChildIdx = 0; elementChildIdx < elementChilds.getLength() ; elementChildIdx++)
        {
            Node elementChild = elementChilds.item( elementChildIdx );
            String elementChildName = elementChild.getNodeName();
            Node elementChildData = elementChild.getFirstChild();
            if( elementChildName != null )
            {
                if(elementChildData != null)
                {
                    if( elementChildName.contentEquals("excludeButtonText") )
                    {
                        String button = elementChildData.getNodeValue();
                        if(!button.trim().isEmpty())
                        {
                            excludedButtons.add( button );
                        }
                    }
                }
            }
        }
        return excludedButtons;
    }

}

