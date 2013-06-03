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

import com.smash.revolance.ui.explorer.application.Application;
import com.smash.revolance.ui.explorer.element.api.Element;
import com.smash.revolance.ui.explorer.element.api.ElementBean;
import com.smash.revolance.ui.explorer.helper.BotHelper;
import com.smash.revolance.ui.explorer.helper.ImageHelper;
import com.smash.revolance.ui.explorer.helper.UrlHelper;
import com.smash.revolance.ui.explorer.page.api.Page;
import com.smash.revolance.ui.explorer.page.api.PageBean;
import com.smash.revolance.ui.explorer.sitemap.SiteMap;
import com.smash.revolance.ui.explorer.user.User;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

import java.util.*;

import static com.smash.revolance.ui.explorer.helper.UrlHelper.removeHash;
import static com.smash.revolance.ui.explorer.helper.UserHelper.handleAlert;

/**
 * User: wsmash
 * Date: 02/06/13
 * Time: 12:35
 */
public class UserExplorer extends Thread
{
    private User    user;

    public UserExplorer(User user)
    {
        this.user = user;
    }

    public void run()
    {
        try
        {
            this.explore();
        }
        catch (Exception e)
        {
            // Ignore
        }
    }

    public User explore() throws Exception
    {
        FileUtils.cleanDirectory( user.getReportFolder() );
        long start = System.currentTimeMillis();

        Page home = new Page( user, user.getHome() );
        if ( !home.hasBeenExplored() )
        {
            home.parse();

            if ( !home.isExternal() && !home.isBroken() )
            {
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
        user.doContentReport();

        System.out.println("\nDiconnecting user: " + user.getId());
        long duration = System.currentTimeMillis()-start;
        System.out.println( "Exploration done in " + duration / 60000 + "mn" );

        return user;
    }

    private void _explore( Page page ) throws Exception
    {
        handleUserAccountLogic( page );
        String title = page.getTitle();
        System.out.println( "Exploring page: " + title );
        if ( page.isOriginal() )
        {
            exploreOriginal( page );
            System.out.println( "Exploring page: " + title + " [Done]" );
        } else
        {
            exploreVariant( page );
            System.out.println( "Exploring variant [Done]" );
        }
    }

    private void exploreVariant( Page page ) throws Exception
    {
        if( !page.getContent().isEmpty() )
        {
            System.out.println("New content has been found in this variant. Exploration will begin...");
            _explore( page );
            System.out.println("New content has been found in this variant. Exploration [Done]");
        }
        else
        {
            System.out.println("No variations have been found. Deleting variant and associated content...");
            page.delete(); // Remove itself
            System.out.println("No variations have been found. Deletion [Done]");
        }
    }

    public void explore( Page page ) throws Exception
    {
        user.goTo( page ).awaitLoaded( ).parse( );
        handleUserAccountLogic( page );

        if(!page.hasBeenExplored() && !page.isBroken() && !page.isExternal())
        {
            List<Element> clickables = page.getClickableContent();
            Collections.sort( clickables );
            for ( Element clickable : clickables )
            {
                if ( !clickable.hasBeenClicked() && clickable.isClickable() && !clickable.isDisabled() )
                {
                    // Checking if some content is left to be explored on the target page (if already being explored)
                    if ( !getSiteMap().hasBeenExplored( clickable.getHref() )
                            && !UrlHelper.areEquivalent( removeHash( clickable.getHref() ), removeHash( page.getUrl() ) ) )
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

                    } else
                    {
                        // Either the target page has already been explored or we're already exploring the page
                        clickable.setClicked( true );
                    }

                }
            }
        }
    }

    private boolean isUnexploredContentLeadingToThisPage(Page page, Element newClickElement) throws Exception
    {
        ElementBean target = getSiteMap().getFirstUnexploredElement( newClickElement.getUrl() );
        if( target == null )
        {
            return false;
        }
        else
        {
            String currentUrl = removeHash( page.getUrl() );
            String targetUrl = removeHash( target.getPage().getUrl() );
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
        for(String handle : handles)
        {
            if(!handle.contentEquals( oldWindowHandle ))
            {
                newHandle = handle;
                break;
            }
        }

        oldBot = user.setBrowser( getBrowser().switchTo().window( newHandle ) );
        handleAlert( user );

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
            handleUrlChange(page, clickable, currentUrl);
        }
        else // url are equivalent (without hash) so we've to check if this is a dynamic change in the content
        {
            if(user.isPageScreenshotEnabled())
            {
                handleDynamicChange( page, clickable );
            }
            else
            {
                clickable.setDisabled( true ); // nothing seems to have changed
            }
        }
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
            if( variant == null )
            {
                variant = page.findVariantBySource( source.getBean() );
            }

            if( variant == null )
            {
                variant = _buildVariant( page, source, false );
                variant.setId( id );


                variant.setImage( ImageHelper.decodeToImage( caption ) );

                page.addVariant( variant );

                // Parse the new content.
                variant.parse( );
                removeBaseContent( variant );

                if(user.wantsToExploreVariants())
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
        List<Element> contentToRemove = new ArrayList<Element>(  );

        for(Element element : content)
        {
            if( page.getOriginal().getBean().contains( element.getBean() ) )
            {
                contentToRemove.add( element );
            }
        }

        content.removeAll( contentToRemove );
        page.setContent( content );
    }

    private void handleUrlChange(Page prevPage, Element clickable, String currentUrl) throws Exception
    {
        if(UrlHelper.containsHash(currentUrl))
        {
            String hash = UrlHelper.getHash(currentUrl);
            Page variant = getVariant( prevPage, clickable, hash, true ).getInstance();

            variant.setScrollX( String.valueOf( (Long) user.getBot().runJS( "return window.scrollX" ) ) );
            variant.setScrollY( String.valueOf( (Long) user.getBot().runJS( "return window.scrollY" ) ) );

            prevPage.addVariant( variant );
        }
        else // Real change of page
        {
            Page page = getSiteMap().getPage(currentUrl, true).getInstance();
            if( !page.hasBeenExplored() )
            {
                page.setSource(clickable);
                explore( page );
            }
        }
    }

    private void exploreOriginal(Page page) throws Exception
    {
        if(!page.isBroken())
        {
            System.out.println("Exploring the original version of the page: " + page.getTitle());

            explore( page );
        }
    }

    private void handleUserAccountLogic( Page page ) throws Exception
    {
        if(getApplication() != null)
        {
            if(getApplication().isSecured())
            {
                if( logIn( page ) )
                {
                    return;
                }
                else if( changePasswd( page ) )
                {
                    return;
                }
            }
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
        if(page != null)
        {
            return page;
        }
        else
        {
            if(generate)
            {
                return _buildVariant( prevPage, source.getInstance(), user.isPageScreenshotEnabled() );
            }
            else
            {
                return null;
            }
        }
    }

    public PageBean getVariant(Page prevPage, Element source, String hash, boolean generate) throws Exception
    {
        PageBean page = prevPage.findVariantByHash( hash );
        if(page == null)
        {
            if(generate)
            {
                page = buildHashVariant(prevPage, source, hash);
            }
        }
        return page;
    }

    private Page _buildVariant(Page page, Element source, boolean takeScreenshot) throws Exception
    {
        Page variant = new Page( user, page.getUrl() );
        variant.setOriginal( page.getOriginal() );
        variant.setSource( source );
        if(takeScreenshot)
        {
            variant.takeScreenShot();
        }
        return variant;
    }

    private PageBean buildHashVariant(Page prevPage, Element source, String hash) throws Exception
    {
        Page variant = _buildVariant( prevPage, source, user.isPageScreenshotEnabled() );
        variant.setUrl(variant.getUrl() + "#" + hash);

        return variant.getBean();
    }

    private boolean changePasswd(Page page) throws Exception
    {
        return getApplication().enterNewPassword( user, page );
    }

    private boolean logIn(Page page) throws Exception
    {
        return getApplication().enterLogin( user, page );
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
