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
import com.smash.revolance.ui.explorer.application.ApplicationManager;
import com.smash.revolance.ui.explorer.application.ApplicationSetup;
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
import java.util.ArrayList;
import java.util.List;

/**
 * User: wsmash
 * Date: 23/01/13
 * Time: 12:06
 */
public class XMLHelper
{

    public static List<User> getUsersFromConfigFile(File file) throws ParserConfigurationException, SAXException, IOException
    {
        List<User> users = new ArrayList<User>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);

        NodeList elements = doc.getDocumentElement().getElementsByTagName("user");
        for(int elementIdx = 0 ; elementIdx < elements.getLength(); elementIdx ++)
        {
            Element element = (Element) elements.item( elementIdx );
            NodeList elementChilds = element.getChildNodes();

            String id = null;
            String login = null;
            String password = null;
            String newPassword = null;

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
                }
            }

            if( id != null && newPassword != null && password != null && login != null )
            {
                User user = new User(id, login, password, newPassword);
                users.add( user );
            }

        }

        return users;
    }

    public static ApplicationSetup getApplicationSetup(File appCfg) throws Exception
    {
        if (!appCfg.exists())
        {
            throw new FileNotFoundException("Unable to find file: " + appCfg);
        }
        ApplicationSetup setup = new ApplicationSetup();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(appCfg);

        NodeList elements = doc.getDocumentElement().getElementsByTagName("configuration");
        Element element = (Element) elements.item( 0 );
        NodeList elementChilds = element.getChildNodes();


        String applicationImpl = null;
        String applicationDir = null;

        String reportFolder = null;
        String usersCfgFile = null;

        boolean followLinks = false;
        boolean followButtons = false;

        boolean takePageScreenshot = false;
        boolean takePageElementScreenshot = false;

        int browserHeight = 0;
        int browserWidth = 0;
        String browserBinary = "";

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
                    if( elementChildName.contentEquals("reportFolder") )
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
                    else if( elementChildName.contentEquals("applicationImpl") )
                    {
                        applicationImpl = elementChildData.getNodeValue();
                    }
                    else if( elementChildName.contentEquals("applicationDir") )
                    {
                        applicationDir = elementChildData.getNodeValue();
                    }
                    else if( elementChildName.contentEquals("usersCfgFile") )
                    {
                        usersCfgFile = elementChildData.getNodeValue();
                    }
                    else if( elementChildName.contentEquals("browserWidth") )
                    {
                        browserWidth = Integer.parseInt(elementChildData.getNodeValue());
                    }
                    else if( elementChildName.contentEquals("browserHeight") )
                    {
                        browserHeight = Integer.parseInt(elementChildData.getNodeValue());
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

        setup.setFollowLinks(followLinks);
        setup.setFollowButtons(followButtons);

        setup.setTakePageScreenshot( takePageScreenshot );
        setup.setTakePageElementScreenshot( takePageElementScreenshot );

        setup.setReportFolder(reportFolder);

        if(applicationImpl != null)
        {
            setup.setApplicationImpl(applicationImpl);
        }

        setup.setApplicationDir( applicationDir );

        setup.setExcludedLinks(getExcludedLinks(appCfg));
        setup.setExcludedButtons(getExcludedButtons(appCfg));

        setup.setAppCfgFile(appCfg);
        setup.setUsersCfgFile(usersCfgFile);

        setup.setBrowserHeight(browserHeight);
        setup.setBrowserWidth(browserWidth);
        setup.setBrowserBinary(browserBinary);

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

    public static ApplicationManager getApplicationManager(File file) throws Exception
    {
        if (!file.exists())
        {
            throw new FileNotFoundException("Unable to find file: " + file);
        }

        ApplicationSetup setup = getApplicationSetup(file);

        ApplicationManager manager = new ApplicationManager( setup.getAppCfgFile(), setup.getUsersCfgFile() );
        manager.setApplicationsDir( setup.getApplicationDir() );

        String applicationImpl = null;
        if(setup.getApplicationImpl() != null)
        {
            applicationImpl = setup.getApplicationImpl();
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);

        NodeList elements = doc.getDocumentElement().getElementsByTagName("instance");
        for(int instanceIdx = 0; instanceIdx < elements.getLength(); instanceIdx++)
        {
            Element element = (Element) elements.item( instanceIdx );
            NodeList elementChilds = element.getChildNodes();

            String id = null;
            String homeUrl = "";
            String domain = "";

            boolean isRef = false;
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
                        else if(elementChildName.contentEquals("homeUrl"))
                        {
                            homeUrl = elementChildData.getNodeValue();
                        }
                        else if(elementChildName.contentEquals("domain"))
                        {
                            domain = elementChildData.getNodeValue();
                        }
                        else if(elementChildName.contentEquals("isRef"))
                        {
                            isRef = Boolean.valueOf(elementChildData.getNodeValue());
                        }
                        else if(elementChildName.contentEquals( "applicationImpl" ))
                        {
                            applicationImpl = elementChildData.getNodeValue();
                        }
                    }
                }
            }
            if( id != null && applicationImpl != null )
            {
                Application application = manager.buildApplication( applicationImpl );

                application.setId(id);
                application.setRef( isRef );
                application.setImpl( applicationImpl );
                application.setHomeUrl( homeUrl );
                application.setup(setup);
                application.setDomain( domain );
                // application.setReportFolder(setup.getReportFolder());

                application.addUsers( XMLHelper.getUsersFromConfigFile( setup.getUsersCfgFile() ) );

                manager.add( application );
            }
        }
        return manager;
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

