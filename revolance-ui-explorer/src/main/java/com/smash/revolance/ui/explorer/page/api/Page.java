package com.smash.revolance.ui.explorer.page.api;

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

import com.smash.revolance.ui.explorer.element.api.*;
import com.smash.revolance.ui.explorer.helper.*;
import com.smash.revolance.ui.explorer.page.IPage;
import com.smash.revolance.ui.explorer.sitemap.SiteMap;
import com.smash.revolance.ui.explorer.user.User;
import com.smash.revolance.ui.explorer.bot.Bot;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.openqa.selenium.WebDriver;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

import static com.smash.revolance.ui.explorer.helper.UserHelper.*;

/**
 * User: wsmash
 * Date: 30/11/12
 * Time: 17:04
 */
public class Page implements IPage
{
    private PageBean bean = new PageBean( this );

    private boolean screenshotTaken;

    @JsonIgnore
    private List<Element> links;

    @JsonIgnore
    private List<Element> buttons;
    private BufferedImage  image;

    private boolean        explored;
    private boolean        parsed;

    private Page()
    {
        setOriginal( this );
    }

    public Page(User user, String url)
    {
        this();
        setId( UUID.randomUUID().toString() );
        setUser( user );
        setUrl( url );
        setHome( user.getHome() == null ? true : getUrl().contentEquals( user.getHome() ) );
        setExternal( !BotHelper.rightDomain( this ) );
    }

    public Page(PageBean bean)
    {
        this();
        this.bean = bean;
    }

    public void setId(String id)
    {
        this.bean.setId( id );
    }

    public void setOriginal(Page original)
    {
        this.bean.setOriginal( original.getBean() );
    }

    @Override
    public Element getSource()
    {
        return this.bean.getSource().getInstance();
    }

    @Override
    public String getUrl()
    {
        if( !isExternal() )
        {
            // The url is relative to the domain
            return getUser().getDomain() + bean.getUrl();
        }
        else
        {
            return bean.getUrl();
        }
    }

    public void setUrl(String url)
    {
        if( url.startsWith( getUser().getDomain() ) )
        {
            url = url.substring( getUser().getDomain().length() );
            bean.setUrl( url );
        }
        else
        {
            bean.setExternal( true );
            bean.setUrl( url );
        }
    }

    private void setUser(User user)
    {
        this.bean.setUserBean( user.getBean() );
    }

    @Override
    public void explore() throws Exception
    {
        if ( !hasBeenExplored() )
        {
            browseAndParseContent( this );
            if(!isExternal() && !isBroken())
            {
                _explore();
                setExplored( true );
            }
            else if(isBroken())
            {
                bean.getSource().setBroken( true );
            }
        }
    }

    private void _explore() throws Exception
    {
        handleUserAccountLogic();
        System.out.println("Exploring page: " + getTitle());
        if ( isOriginal() )
        {
            exploreOriginal();
            System.out.println("Exploring page: " + getTitle() + " [Done]");
        }
        else
        {
            exploreVariant();
            System.out.println("Exploring variant [Done]");
        }
    }

    private void exploreVariant() throws Exception
    {
        if( bean.hasVariations() )
        {
            System.out.println("New content has been found in this variant. Exploration will begin...");
            explore( getClickableVariations() );
            System.out.println("New content has been found in this variant. Exploration [Done]");
        }
        else
        {
            System.out.println("No variation has been found. Deleting variant and associated content...");
            delete(); // Remove itself
            System.out.println("No variation has been found. Deletion [Done]");
        }
    }

    private void explore( List<Element> clickables ) throws Exception
    {
        for ( Element clickable : clickables )
        {
            if( getSiteMap().hasBeenExplored( clickable.getHref() ) || clickable.hasBeenClicked() )
            {
                clickable.setClicked( true );
            }
            else
            {
                boolean towardCurrentUrl = UrlHelper.areEquivalent( clickable.getHref(), getUrl() );
                if( !towardCurrentUrl && clickable.isClickable() )
                {
                    clickable.click();
                    if(clickable.hasBeenClicked())
                    {
                        String currentUrl = getBrowser().getCurrentUrl();
                        if ( !UrlHelper.areEquivalent( currentUrl, getUrl() ) )
                        {
                            handleUrlChange(clickable, currentUrl);
                        }
                        else // url are equivalent (without hash) so we've to check if this is a dynamic change in the content
                        {
                            if(getUser().isPageScreenshotEnabled())
                            {
                                handleDynamicChange( clickable );
                            }
                        }
                    }
                }
                clickable.setClicked( !towardCurrentUrl );
            }
        }
    }

    private void handleDynamicChange(Element source) throws Exception
    {
        String id = UUID.randomUUID().toString();
        String caption = BotHelper.pageContentHasChanged(getBot(), bean.getCaption());
        if ( !caption.contentEquals( bean.getCaption() ) )
        {
            // Create new page and inject data to speed up the computations

            // Checking if there is already a variant with that checksum
            Page variant = findVariantByCaption( caption );

            // otherwise we've to create one from scratch
            if( variant == null )
            {
                variant = findVariantBySource( source.getBean() );
            }

            if( variant == null )
            {
                variant = _buildVariant( source, false );
                variant.setId( id );

                variant.setCaption( caption );
                variant.setImage( ImageHelper.decodeToImage( caption ) );

                addVariant( variant );

                // Parse the new content. But does not explore added clickable elements
                UserHelper.parseContent( variant );
                variant.removeBaseContent();
                variant.setParsed( true );

                if(getUser().wantsToExploreVariants())
                {
                    variant.explore();
                }
            }

            source.setDisabled( false );
        }
    }

    public void removeBaseContent() throws Exception
    {
        List<Element> content = getContent();
        List<Element> contentToRemove = new ArrayList<Element>(  );

        for(Element element : content)
        {
            if( getOriginal().getBean().contains( element.getBean() ) )
            {
                contentToRemove.add( element );
            }
        }

        getContent().removeAll( contentToRemove );
    }

    private void handleUrlChange(Element clickable, String currentUrl) throws Exception
    {
        clickable.setDisabled( false );
        if(UrlHelper.containsHash(currentUrl))
        {
            String hash = UrlHelper.getHash(currentUrl);
            Page variant = getVariant( clickable, hash, true );
            if(!variant.hasBeenExplored() && getUser().wantsToExploreVariants())
            {
                variant.explore();
            }
            else
            {
                browseAndParseContent( getOriginal() ); // return to the original page
            }
        }
        else // Real change of page
        {
            Page page = getUserSitemap().getPage(currentUrl, true).getInstance();
            if( !page.hasBeenExplored() )
            {
                page.setSource(clickable);
                page.explore();
            }
        }
    }



    public void exploreOriginal() throws Exception
    {
        if(!isBroken())
        {
            System.out.println("Exploring the original version of the page: " + getTitle());

            explore( getClickableContent() );
        }
    }

    private void handleUserAccountLogic() throws Exception
    {
        if(getApplication() != null)
        {
            if(getApplication().isSecured())
            {
                if( logIn() )
                {
                    return;
                }
                else if( changePasswd() )
                {
                    return;
                }
            }
        }
    }

    private boolean changePasswd() throws Exception
    {
        return getApplication().enterNewPassword( getUser(), this );
    }

    private boolean logIn() throws Exception
    {
        return getApplication().enterLogin( getUser(), this );
    }

    @Override
    public boolean hasBeenExplored()
    {
        return explored;
    }

    @Override
    public boolean hasBeenParsed()
    {
        return parsed;
    }

    public void setParsed(boolean b)
    {
        parsed = true;
    }

    public void delete() throws Exception
    {
        if(isOriginal())
        {
            getSiteMap().delPage(this);
        }
        else
        {
            getOriginal().getVariants().remove(this);
        }

        if( getUser().isPageElementScreenshotEnabled() )
        {
            for(Element element : getContent())
            {
                element.delete();
                getContent().remove( element );
            }
        }

        bean.clearContent();
        bean.setInstance( null );
        bean = null;
    }

    private Page _buildVariant(Element source, boolean takeScreenshot) throws Exception
    {
        Page variant = new Page( getUser(), getUrl() );
        variant.setOriginal( getOriginal() );
        variant.setSource( source );
        if(takeScreenshot)
        {
            variant.takeScreenShot();
        }
        return variant;
    }

    public boolean hasBeenBrowsed()
    {
        return bean.hasBeenBrowsed();
    }

    public String takeScreenShot() throws Exception
    {
        Bot bot = getUser().getBot();
        if(bean.getCaption().isEmpty())
        {
            if(getTitle().isEmpty())
            {
                setTitle( getBot().getCurrentTitle() );
            }
            if(!screenshotTaken && getUser().isPageScreenshotEnabled())
            {
                System.out.print("\rTaking page snapshot: " + getTitle());
                String img = BotHelper.takeScreenshot( bot );

                if(img != null)
                {
                    setImage( ImageHelper.decodeToImage( img ) );
                    setCaption( img );
                    screenshotTaken = true;
                }
                System.out.println("\rTaking page snapshot: " + getTitle() + " [Done]");
            }

        }
        return getCaption();
    }


    @Override
    public List<Element> getLinks() throws Exception
    {
        if( links == null )
        {
            links = new ArrayList<Element>(  );

            if(!getUser().isExplorationDone())
            {
                if(!isExternal() && !isBroken())
                {
                    System.out.print( "Retrieving links" );

                    links.addAll( Link.getLinks( this ) );
                    ArrayList<ElementBean> beanLinks = new ArrayList<ElementBean>(  );
                    for(Element link : links)
                    {
                        beanLinks.add( link.getBean() );
                    }
                    bean.setLinks( beanLinks );

                    System.out.println( "\r" + bean.getLinks().size() + " links found" );
                    takeScreenshots( links );
                }
            }
        }
        return links;
    }

    @Override
    public String getTitle()
    {
        if(!isOriginal())
        {
            setTitle( getOriginal().getTitle() + "_VARIANT_" + getId() );
        }
        return bean.getTitle();
    }

    @Override
    public boolean isBroken()
    {
        return bean.isBroken();
    }

    @Override
    public boolean hasBrokenLinks() throws Exception
    {
        return !getBrokenLinks().isEmpty();
    }

    @Override
    public List<Element> getBrokenLinks() throws Exception
    {
        List<Element> brokenLinks = new ArrayList<Element>();

        for ( Element link : getLinks() )
        {
            if ( link.isBroken() )
            {
                brokenLinks.add( link );
            }
        }
        return brokenLinks;
    }

    public boolean isHomePage()
    {
        return bean.isHome();
    }

    public void setBroken(boolean b)
    {
        bean.setBroken( b );
    }

    public void setTitle(String title)
    {
        bean.setTitle( title );
    }

    public void setCaption(String img) throws IOException
    {
        this.bean.setCaption( img );
    }

    public void setBrowsed(boolean b)
    {
        bean.setBrowsed( b );
    }

    public User getUser()
    {
        return bean.getUser().getInstance();
    }

    public SiteMap getUserSitemap()
    {
        return getUser().getSiteMap();
    }

    public WebDriver getBrowser() throws Exception
    {
        return getUser().getBrowser();
    }

    private void takeScreenshots(List<Element> content) throws Exception
    {
        if ( getUser().isPageScreenshotEnabled() && getUser().isPageElementScreenshotEnabled() )
        {
            int contentIdx = 0;
            for ( Element pageElement : content )
            {
                contentIdx ++;
                System.out.print(String.format("\rTaking element screenshot ( %d / %d )", contentIdx, content.size()));
                pageElement.takeScreenShot();
            }
            System.out.println(String.format("\rTaking elements screenshot ( %d ) [Done]", contentIdx));
        }
    }

    @Override
    public Application getApplication() throws Exception
    {
        return getUser().getApplication();
    }

    @Override
    public BufferedImage getImage() throws Exception
    {
        if( image == null )
        {
            takeScreenShot();
        }
        return image;
    }

    @Override
    public List<Element> getButtons() throws Exception
    {
        if( buttons == null )
        {
            buttons = new ArrayList<Element>(  );

            if(!getUser().isExplorationDone())
            {
                if(!isExternal() && !isBroken() )
                {
                    System.out.print("Retrieving buttons");
                    buttons.addAll( Button.getButtons( this ) );
                    ArrayList<ElementBean> beanButtons = new ArrayList<ElementBean>(  );

                    for(Element button : buttons)
                    {
                        beanButtons.add( button.getBean() );
                    }

                    bean.setButtons( beanButtons );
                    System.out.println("\r" + bean.getButtons().size() + " buttons found");

                    takeScreenshots( buttons );
                }
            }
        }
        return buttons;
    }

    public List<Page> getVariants()
    {
        if ( isOriginal() )
        {
            List<Page> variants = new ArrayList<Page>(  );
            for(PageBean page : _getVariants())
            {
                variants.add( page.getInstance() );
            }
            return variants;
        }
        else
        {
            return getOriginal().getVariants();
        }
    }

    private List<PageBean> _getVariants()
    {
        return bean.getVariants();
    }

    @Override
    public boolean isOriginal()
    {
        return bean.isOriginal();
    }

    public Page getVariant(Element source, String hash, boolean generate) throws Exception
    {
        Page page = findVariantByHash(hash);
        if(page == null)
        {
            if(generate)
            {
                page = buildHashVariant(source, hash);
            }
        }
        return page;
    }

    public Page findVariantByHash(String hash)
    {
        if(!hash.isEmpty())
        {
            for(Page variant : getVariants())
            {
                if( UrlHelper.getHash(getUrl()).contentEquals(hash) )
                {
                    return variant;
                }
            }
        }
        return null;
    }

    public Page findVariantByCaption(String caption) throws Exception
    {
        for(Page variant : getVariants())
        {
            if( variant.getCaption().contentEquals(caption) )
            {
                return variant;
            }
        }
        return null;
    }

    private Page buildHashVariant(Element source, String hash) throws Exception
    {
        Page variant = _buildVariant( source, getUser().isPageScreenshotEnabled() );
        variant.setUrl(variant.getUrl() + "#" + hash);
        return variant;
    }

    /**
     * Retrieve a variant given the source element of the page
     * Build the variant if not existing.
     *
     * @param source
     * @return
     * @throws Exception
     */
    public Page getVariant(ElementBean source, boolean generate) throws Exception
    {
        Page page = findVariantBySource(source);
        if(page != null)
        {
            return page;
        }
        else
        {
            if(generate)
            {
                return _buildVariant( source.getInstance(), getUser().isPageScreenshotEnabled() );
            }
            else
            {
                return null;
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
    public Page getVariant(ElementBean source) throws Exception
    {
        return getVariant( source, false );
    }

    /**
     * Retrieve a variant given the source element (button / link) that lead to it.
     * When the source is null then the null page is returned
     *
     *
     * @param source
     * @return
     * @throws Exception
     */
    public Page findVariantBySource(ElementBean source) throws Exception
    {
        if(source == null)
        {
            return null;
        }
        else
        {
            for(Page page : getVariants())
            {
                if(source.equals(page.getSource()))
                {
                    return page;
                }
            }
            return null;
        }
    }

    @Override
    public Page getOriginal()
    {
        return bean.getOriginal().getInstance();
    }

    @Override
    public SiteMap getSiteMap()
    {
        return getUser().getSiteMap();
    }

    public Bot getBot() throws Exception
    {
        return getUser().getBot();
    }

    public void setAuthorized(boolean authorized)
    {
        this.bean.setAuthorized( authorized );
    }

    public boolean isAuthorized()
    {
        return bean.isAuthorized();
    }

    public void addVariant(Page page)
    {
        if(isOriginal())
        {
            _getVariants().add( page.getBean() );
        }
        else
        {
            getOriginal().addVariant( page );
        }
    }

    @Override
    public boolean isExternal()
    {
        return bean.isExternal();
    }

    public void setExternal(boolean b)
    {
        bean.setExternal( b );
    }

    private List<Element> getClickableVariations() throws Exception
    {
        List<Element> elements = new ArrayList<Element>();

        elements.addAll( Element.filterClickableElements( bean.getAddedVariations() ) );

        return elements;
    }

    public String toString()
    {
        return String.format( "%s [%s]%n", getTitle(), getUrl() );
    }

    public String getId()
    {
        return bean.getId();
    }

    public static PageBean createPage(User user, String url, String title) throws Exception
    {
        Page page = new Page(user, url);
        page.setTitle( title );
        user.getSiteMap().addPage( page );
        return page.getBean();
    }

    public static PageBean createPage(User user, String url, String title, String caption) throws Exception
    {
        Page page = new Page(user, url);
        page.setTitle( title );
        page.setCaption( caption );
        user.getSiteMap().addPage(page);
        return page.getBean();
    }

    public void setSource(Element source)
    {
        this.bean.setSource( source.getBean() );
    }

    public PageBean getBean()
    {
        return bean;
    }

    public void setHome(boolean b)
    {
        this.bean.setHome( b );
    }

    public List<Element> getOriginalContent() throws Exception
    {
        return getOriginal().getContent();
    }

    @Override
    public List<Element> getContent() throws Exception
    {
        System.out.println( "Parsing page content" );

        List<Element> content = new ArrayList<Element>();
        if ( !isExternal() && !isBroken() )
        {
            content.addAll( getClickableContent() );

            Collections.sort( content );
        }

        System.out.println( "Parsing page content [Done]" );
        return content;
    }

    @Override
    public List<Element> getClickableContent() throws Exception
    {
        List<Element> clickableElements = new ArrayList<Element>();

        clickableElements.addAll( getLinks() );
        clickableElements.addAll( getButtons() );

        Collections.sort(clickableElements);
        return clickableElements;
    }


    public void setHeight(int height)
    {
        this.bean.setHeight( height );
    }

    public void setWidth(int width)
    {
        this.bean.setWidth( width );
    }

    public String getCaption()
    {
        return bean.getCaption();
    }

    public void setImage(BufferedImage img) throws Exception
    {
        setWidth( img.getWidth()  );
        setHeight( img.getHeight() );

        setCaption( ImageHelper.encodeToString( img ) );
        image = img;
    }

    public void setExplored(boolean explored)
    {
        this.explored = explored;
    }

    public Element getButton(String button) throws Exception
    {
        for( Element aButton : getButtons() )
        {
            if( aButton.getContent().contentEquals( button ) )
            {
                return aButton;
            }
        }
        return null;
    }

    public Element getLink(String link) throws Exception
    {
        for( Element aLink : getLinks() )
        {
            if( aLink.getContent().contentEquals( link ) )
            {
                return aLink;
            }
        }
        return null;
    }
}
