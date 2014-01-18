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
import com.smash.revolance.ui.model.element.api.Element;
import com.smash.revolance.ui.model.element.api.ElementBean;
import com.smash.revolance.ui.model.helper.*;
import com.smash.revolance.ui.model.page.api.Page;
import com.smash.revolance.ui.model.page.api.PageBean;
import com.smash.revolance.ui.model.sitemap.SiteMap;
import com.smash.revolance.ui.model.user.User;
import org.apache.log4j.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * User: wsmash
 * Date: 02/06/13
 * Time: 12:35
 */
public class UserExplorer extends Thread
{
    private Logger log = Logger.getLogger(UserExplorer.class);

    private int timeoutInSec;
    private User user;
    private File reportFile;

    public UserExplorer(User user, WriterAppender log, File reportFile, int timeoutInSec)
    {
        this.user = user;
        this.log.addAppender(log);
        this.user.setLogger(this.log);
        this.reportFile = reportFile;
        this.timeoutInSec = timeoutInSec;
    }

    public UserExplorer(User user, File reportFolder, int timeoutInSec) throws IOException
    {
        this(user, new FileAppender(new SimpleLayout(), File.createTempFile("exploration-" + UUID.randomUUID().toString(), ".log").getAbsolutePath()), reportFolder, timeoutInSec);
    }

    public File doContentReport()
    {
        try
        {
            JsonHelper.getInstance().map( getReportFile(), getSiteMap() );
            getLogger().log(Level.INFO, "Report has been generated with id: " + getReportFile().getName());
        }
        catch (IOException e)
        {
            e.printStackTrace();
            getLogger().log(Level.ERROR, "Unable to generate report!");
        }
        return getReportFile();
    }

    public File getReportFile()
    {
        return reportFile;
    }

    public Logger getLogger()
    {
        return log;
    }

    public void run()
    {
        try
        {
            this._explore();
        }
        catch (Exception e)
        {
            getLogger().log(Level.ERROR, "Exploration aborted. Reason: " + e.getMessage());
            getLogger().log(Level.ERROR, e );
        }
        finally
        {
            user.setExplorationDone(true);
            try
            {
                user.stopBot();
                doContentReport();
            }
            catch (Exception e)
            {
                getLogger().log(Level.ERROR, e);
            }
        }
    }

    public void explore()
    {
        start();

        if(timeoutInSec == 0)
        {
            return; // We don't want to monitor the duration of the exploration
        }
        else
        {
            long mark = System.currentTimeMillis();
            while( (System.currentTimeMillis()-mark) < timeoutInSec*1000 )
            {
                try
                {
                    sleep(10000); //10s of sleep
                }
                catch (InterruptedException e)
                {

                }
                if(user.isExplorationDone())
                {
                    return;
                }
            }

            user.setExplorationDone(true);
        }
    }

    private User _explore() throws Exception
    {
        long start = System.currentTimeMillis();
        Page home = new Page( user, user.getHome() );

        user.goTo( home ).awaitLoaded();
        new PageParser(home).parse(); // Just for the home page
        if ( !home.hasBeenExplored() )
        {
            if ( !home.isExternal() && !home.isBroken() )
            {
                // This call will recursively:
                // parse & handleUserAccout logic & explore
                // of all the pages accessible from home
                explore( home );
                home.setExplored( true );
            }
            else if ( home.isBroken() )
            {
                home.getSource().setBroken( true );
            }
        }

        user.setExplorationDone( true );
        user.stopBot();

        long duration = System.currentTimeMillis() - start;
        user.getLogger().log(Level.INFO, "Exploration [Done] [Duration " + duration / 60000 + "mn]");

        return user;
    }

    private void _explore(Page page) throws Exception
    {
        doRecursiveExploration(page);
        doContentReport();
    }

    private void doRecursiveExploration(Page page) throws Exception
    {
        String title = page.getTitle();
        user.getLogger().log(Level.INFO, "Exploring page: " + title);
        if ( page.isOriginal() )
        {
            exploreOriginal( page );
            user.getLogger().log(Level.INFO, "Exploring page: " + title + " [Done]");
        }
        else
        {
            exploreVariant( page );
            user.getLogger().log(Level.INFO, "Exploring variant [Done]");
        }
    }

    private void exploreVariant(Page page) throws Exception
    {
        if ( !page.getContent().isEmpty() )
        {
            user.getLogger().log(Level.INFO, "New content has been found in this variant. Exploration will begin...");
            _explore( page );
            user.getLogger().log(Level.INFO, "New content has been found in this variant. Exploration [Done]");
        }
        else
        {
            user.getLogger().log(Level.INFO, "No variations have been found. Deleting variant and associated content...");
            page.delete(); // Remove itself
            user.getLogger().log(Level.INFO, "No variations have been found. Deletion [Done]");
        }
    }

    public void explore(Page page) throws Exception
    {
        new PageParser(page).parse();
        handleUserAccountLogic( page );

        if ( !page.hasBeenExplored() && !page.isBroken() && !page.isExternal() )
        {
            List<Element> clickables = page.getClickableContent();
            Collections.sort( clickables );
            for ( Element clickable : clickables )
            {
                if ( !clickable.hasBeenClicked()
                        && clickable.isClickable()
                        && !clickable.isDisabled()
                        && BotHelper.rightDomain(user, clickable.getHref()) )
                {
                    // Checking if some content is left to be explored on the target page (if already being explored)
                    if ( !getSiteMap().hasBeenExplored( UrlHelper.removeHash(clickable.getHref()) )
                            && !UrlHelper.areEquivalent( UrlHelper.removeHash( clickable.getHref() ), UrlHelper.removeHash( page.getUrl() ) )
                            && !getSourceContent( page ).contentEquals( clickable.getContent() ) )
                    {

                        String oldWindowHandle = getBrowser().getWindowHandle();
                        int windowCount = getBrowser().getWindowHandles().size();

                        if ( clickable.click() )
                        {
                            if ( getBrowser().getWindowHandles().size() > windowCount )
                            {
                                exploreNewWindowChanges( page, clickable, oldWindowHandle );
                            }
                            else
                            {
                                exploreChanges( page, clickable );
                            }
                        }
                    }
                    else
                    {
                        // Either the target page has already been explored or we're already exploring the page
                        clickable.setClicked( true );
                    }

                }
            }
        }
    }

    private String getSourceContent(Page page)
    {
        Element source = page.getSource();
        if ( source == null )
        {
            return "";
        }
        else
        {
            return source.getContent();
        }
    }

    private boolean isUnexploredContentLeadingToThisPage(Page page, Element newClickElement) throws Exception
    {
        ElementBean target = getSiteMap().getFirstUnexploredElement( newClickElement.getUrl() );
        if ( target == null )
        {
            return false;
        }
        else
        {
            String currentUrl = UrlHelper.removeHash( page.getUrl() );
            String targetUrl = UrlHelper.removeHash( target.getPage().getUrl() );
            return UrlHelper.areEquivalent( currentUrl, targetUrl );
        }
    }

    private void exploreNewWindowChanges(Page prevPage, Element clickable, String oldWindowHandle) throws Exception
    {
        WebDriver oldBot = swithToNewWindow( oldWindowHandle );

        exploreChanges( prevPage, clickable );

        // Closing the new window after exploration has completed
        WebDriver oldOldBot = user.setBrowser( oldBot );
        oldOldBot.close();

        getBrowser().switchTo().window( oldWindowHandle );
    }

    private WebDriver swithToNewWindow(String oldWindowHandle) throws Exception
    {
        WebDriver oldBot;
        Set<String> handles = user.getBrowser().getWindowHandles();
        String newHandle = "";
        for ( String handle : handles )
        {
            if ( !handle.contentEquals( oldWindowHandle ) )
            {
                newHandle = handle;
                break;
            }
        }

        oldBot = user.setBrowser( getBrowser().switchTo().window( newHandle ) );
        UserHelper.handleAlert( user );

        // Adapting the size of the new window
        Dimension size = new Dimension( user.getBrowserWidth(), user.getBrowserHeight() );
        getBrowser().manage().window().setSize( size );

        return oldBot;
    }

    private void exploreChanges(Page page, Element clickable) throws Exception
    {
        String currentUrl = getBrowser().getCurrentUrl();
        if ( !UrlHelper.areEquivalent( currentUrl, page.getUrl() ) )
        {
            handleUrlChange( page, clickable, currentUrl );
        }
        /*
        else // url are equivalent (without hash) so we've to check if this is a dynamic change in the content
        {
            if ( user.isPageScreenshotEnabled() )
            {
                handleDynamicChange( page, clickable );
            }
            else
            {
                clickable.setDisabled( true ); // nothing seems to have changed
            }
        }
        */
    }

    private void handleDynamicChange(Page page, Element source) throws Exception
    {
        String id = UUID.randomUUID().toString();
        String caption = BotHelper.pageContentHasChanged( user.getBot(), page.getCaption() );
        if ( !caption.contentEquals( page.getCaption() ) )
        {
            // Create new page and inject data to speed up the computations

            // Checking if there is already a variant with that checksum
            Page variant = page.findVariantByCaption( caption );

            // otherwise we've to create one from scratch
            if ( variant == null )
            {
                variant = page.findVariantBySource( source.getBean() );
            }

            if ( variant == null )
            {
                variant = _buildVariant( page, source, false );
                variant.setId( id );


                variant.setImage( ImageHelper.decodeToImage( caption ) );

                page.addVariant( variant );

                // Parse the new content.
                new PageParser( variant ).parse();
                removeBaseContent( variant );

                if ( user.wantsToExploreVariants() )
                {
                    explore( variant );
                }
            }

            source.setDisabled( false );
        }
    }

    private void removeBaseContent(Page page) throws Exception
    {
        List<Element> content = page.getContent();
        List<Element> contentToRemove = new ArrayList<Element>();

        for ( Element element : content )
        {
            if ( page.getOriginal().getBean().contains( element.getBean() ) )
            {
                contentToRemove.add( element );
            }
        }

        content.removeAll( contentToRemove );
        page.setContent( content );
    }

    private void handleUrlChange(Page prevPage, Element clickable, String currentUrl) throws Exception
    {
        if ( UrlHelper.containsHash( currentUrl ) )
        {
            String hash = UrlHelper.getHash( currentUrl );
            Page variant = getVariant( prevPage, clickable, hash, true ).getInstance();

            variant.setScrollX( String.valueOf( (Long) user.getBot().runJS( "return window.scrollX" ) ) );
            variant.setScrollY( String.valueOf( (Long) user.getBot().runJS( "return window.scrollY" ) ) );

            prevPage.addVariant( variant );
        }
        else if(BotHelper.rightDomain( prevPage.getUser(), currentUrl))// Real change of page
        {
            Page page = getSiteMap().getPage( currentUrl, true ).getInstance();
            if ( !page.hasBeenExplored() && !page.isExternal())
            {
                page.setSource( clickable );
                explore( page );
            }
        }
    }

    private void exploreOriginal(Page page) throws Exception
    {
        if ( !page.isBroken() )
        {
            user.getLogger().log(Level.TRACE, "Exploring the original version of the page: " + page.getTitle() );

            explore( page );
        }
    }

    private void handleUserAccountLogic(Page page) throws Exception
    {
        if ( getApplication() != null )
        {
            logIn( page );
        }
    }

    /**
     * Retrieve a variant given the source element of the page. Do not generate it if not found.
     *
     * @param source
     * @return
     * @throws Exception
     */
    public Page getVariant(Page prevPage, ElementBean source) throws Exception
    {
        return getVariant( prevPage, source, false );
    }

    /**
     * Retrieve a variant given the source element of the page
     * Build the variant if not existing.
     *
     * @param source
     * @return
     * @throws Exception
     */
    public Page getVariant(Page prevPage, ElementBean source, boolean generate) throws Exception
    {
        Page page = prevPage.findVariantBySource( source );
        if ( page != null )
        {
            return page;
        } else
        {
            if ( generate )
            {
                return _buildVariant( prevPage, source.getInstance(), user.isPageScreenshotEnabled() );
            } else
            {
                return null;
            }
        }
    }

    public PageBean getVariant(Page prevPage, Element source, String hash, boolean generate) throws Exception
    {
        PageBean page = prevPage.findVariantByHash( hash );
        if ( page == null )
        {
            if ( generate )
            {
                page = buildHashVariant( prevPage, source, hash );
            }
        }
        return page;
    }

    private Page _buildVariant(Page page, Element source, boolean takeScreenshot) throws Exception
    {
        Page variant = new Page( user, page.getUrl() );
        variant.setOriginal( page.getOriginal() );
        variant.setSource( source );
        if ( takeScreenshot )
        {
            new PageParser( variant ).takeScreenShot();
        }
        return variant;
    }

    private PageBean buildHashVariant(Page prevPage, Element source, String hash) throws Exception
    {
        Page variant = _buildVariant( prevPage, source, user.isPageScreenshotEnabled() );
        variant.setUrl( variant.getUrl() + "#" + hash );

        return variant.getBean();
    }

    private boolean logIn(Page page) throws Exception
    {
        return getApplication().login(user, page);
    }

    private SiteMap getSiteMap()
    {
        return user.getSiteMap();
    }

    private Application getApplication()
    {
        return user.getApplication();
    }

    private WebDriver getBrowser() throws Exception
    {
        return user.getBrowser();
    }

}
