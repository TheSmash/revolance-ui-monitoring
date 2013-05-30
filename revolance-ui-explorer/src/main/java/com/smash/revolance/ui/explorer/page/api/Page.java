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

import com.smash.revolance.ui.explorer.element.IElement;
import com.smash.revolance.ui.explorer.element.api.*;
import com.smash.revolance.ui.explorer.helper.*;
import com.smash.revolance.ui.explorer.page.IPage;
import com.smash.revolance.ui.explorer.sitemap.SiteMap;
import com.smash.revolance.ui.explorer.user.User;
import com.smash.revolance.ui.explorer.bot.Bot;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.openqa.selenium.WebDriver;

import java.awt.image.BufferedImage;
import java.io.File;
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
    private boolean broken;

    @JsonIgnore
    private List<IElement> links;

    @JsonIgnore
    private List<IElement> buttons;
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
        setRightDomain( BotHelper.rightDomain( this ) );
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
    public IElement getSource()
    {
        if(this.bean.getSource() == null)
        {
            return null;
        }
        else
        {
            return this.bean.getSource().getInstance();
        }
    }

    @Override
    public String getUrl()
    {
        return getUser().getDomain() + bean.getUrl();
    }

    @Override
    public void setUrl(String url)
    {
        if( url.startsWith( getUser().getDomain() ) )
        {
            url = url.substring( getUser().getDomain().length() );
        }
        bean.setUrl( url );
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
            browse( this );
            _explore();

            setExplored( true );
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

    private void explore( List<IElement> clickables ) throws Exception
    {
        for ( IElement clickable : clickables )
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
                clickable.setClicked( towardCurrentUrl );
            }
        }
    }

    private void handleDynamicChange(IElement source) throws Exception
    {
        String id = UUID.randomUUID().toString();
        String caption = BotHelper.pageContentHasChanged(getBot(), bean.getCaption());
        if ( !caption.contentEquals( bean.getCaption() ) )
        {
            // Create new page and inject data to speed up the computations

            // Checking if there is already a variant with that checksum
            PageBean variant = findVariantByCaption( caption );

            // otherwise we've to create one from scratch
            if( variant == null )
            {
                variant = findVariantBySource( source.getBean() );
            }

            if( variant == null )
            {
                variant = _buildVariant( source, false ).getBean();
                variant.setId( id );

                variant.setCaption( caption );
                variant.getInstance().setImage( ImageHelper.decodeToImage( caption ) );

                addVariant( variant );

                // Parse the new content. But does not explore added clickable elements
                UserHelper.parseContent( variant.getInstance() );
                variant.removeBaseContent();
                variant.getInstance().setParsed( true );

                if(getUser().wantsToExploreVariants())
                {
                    variant.getInstance().explore( );
                }
            }

            source.setDisabled( false );
        }
    }

    private void handleUrlChange(IElement clickable, String currentUrl) throws Exception
    {
        clickable.setDisabled( false );
        if(UrlHelper.containsHash(currentUrl))
        {
            String hash = UrlHelper.getHash(currentUrl);
            IPage variant = getVariant(clickable, hash, true).getInstance();
            if(!variant.hasBeenExplored() && getUser().wantsToExploreVariants())
            {
                variant.explore();
            }
            else
            {
                browse( getOriginal() ); // return to the original page
            }
        }
        else // Real change of page
        {
            IPage page = getUserSitemap().getPage(currentUrl, true).getInstance();
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
                logIn();
                changePasswd();
            }
        }
    }

    private void changePasswd() throws Exception
    {
        getApplication().enterNewPassword( getUser(), this );
    }

    private void logIn() throws Exception
    {
        getApplication().enterLogin( getUser(), this );
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

    @Override
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
            for(IElement element : getContent())
            {
                element.delete();
                getContent().remove( element );
            }
        }

        bean.clearContent();
        bean.setInstance( null );
        bean = null;
    }

    private Page _buildVariant(IElement source, boolean takeScreenshot) throws Exception
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

    @Override
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
    public List<IElement> getLinks() throws Exception
    {
        if( links == null )
        {
            if(!getUser().isExplorationDone())
            {
                if(rightDomain() && !isBroken())
                {
                    System.out.print( "Retrieving links" );

                    links = Link.getLinks( this );
                    ArrayList<ElementBean> beanLinks = new ArrayList<ElementBean>(  );
                    for(IElement link : links)
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

    public boolean rightDomain()
    {
        return bean.isRightDomain();
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
    public List<IElement> getBrokenLinks() throws Exception
    {
        List<IElement> brokenLinks = new ArrayList<IElement>();

        for ( IElement link : getLinks() )
        {
            if ( ( (Link) link ).isBroken() )
            {
                brokenLinks.add( link );
            }
        }
        return brokenLinks;
    }

    @Override
    public boolean isHomePage()
    {
        return bean.isHome();
    }

    @Override
    public void setBroken(boolean b)
    {
        broken = b;
    }

    @Override
    public void setTitle(String title)
    {
        bean.setTitle( title );
    }

    @Override
    public void setCaption(String img) throws IOException
    {
        this.bean.setCaption( img );
    }

    @Override
    public void setBrowsed(boolean b)
    {
        bean.setBrowsed( b );
    }

    @Override
    public User getUser()
    {
        return bean.getUser().getInstance();
    }

    @Override
    public SiteMap getUserSitemap()
    {
        return getUser().getSiteMap();
    }

    public WebDriver getBrowser() throws Exception
    {
        return getUser().getBrowser();
    }

    private void takeScreenshots(List<IElement> content) throws Exception
    {
        if ( getUser().isPageScreenshotEnabled() && getUser().isPageElementScreenshotEnabled() )
        {
            int contentIdx = 0;
            for ( IElement pageElement : content )
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
    public List<IElement> getButtons() throws Exception
    {
        if( buttons == null )
        {
            if(!getUser().isExplorationDone())
            {
                if(rightDomain() && !isBroken() )
                {
                    System.out.print("Retrieving buttons");
                    buttons = Button.getButtons( this );
                    ArrayList<ElementBean> beanButtons = new ArrayList<ElementBean>(  );

                    for(IElement button : buttons)
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

    public List<PageBean> getVariants()
    {
        if ( isOriginal() )
        {
            return _getVariants();
        }
        else
        {
            return getOriginal().getVariants();
        }
    }

    private List<PageBean> _getVariants()
    {
        if ( bean.getVariants() == null )
        {
            bean.setVariants( new ArrayList<PageBean>() );
        }
        return bean.getVariants();
    }

    @Override
    public boolean isOriginal()
    {
        return bean.isOriginal();
    }

    public PageBean getVariant(IElement source, String hash, boolean generate) throws Exception
    {
        PageBean page = findVariantByHash(hash);
        if(page == null)
        {
            if(generate)
            {
                page = buildHashVariant(source, hash);
            }
        }
        return page;
    }

    public PageBean findVariantByHash(String hash)
    {
        if(!hash.isEmpty())
        {
            for(PageBean variant : getVariants())
            {
                if( UrlHelper.getHash(getUrl()).contentEquals(hash) )
                {
                    return variant;
                }
            }
        }
        return null;
    }

    public PageBean findVariantByCaption(String caption) throws Exception
    {
        for(PageBean variant : getVariants())
        {
            if( variant.getCaption().contentEquals(caption) )
            {
                return variant;
            }
        }
        return null;
    }

    private PageBean buildHashVariant(IElement source, String hash) throws Exception
    {
        PageBean variant = _buildVariant( source, getUser().isPageScreenshotEnabled() ).getBean();
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
    public PageBean getVariant(ElementBean source, boolean generate) throws Exception
    {
        PageBean page = findVariantBySource(source);
        if(page != null)
        {
            return page;
        }
        else
        {
            if(generate)
            {
                return _buildVariant( source.getInstance(), getUser().isPageScreenshotEnabled() ).getBean();
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
    public PageBean getVariant(ElementBean source) throws Exception
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
    public PageBean findVariantBySource(ElementBean source) throws Exception
    {
        if(source == null)
        {
            return null;
        }
        else
        {
            for(PageBean page : getVariants())
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
        return (Page) bean.getOriginal().getInstance();
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

    public void addVariant(PageBean page)
    {
        if(isOriginal())
        {
            _getVariants().add( page );
        }
        else
        {
            getOriginal().addVariant( page );
        }
    }

    private List<IElement> getClickableVariations() throws Exception
    {
        List<IElement> elements = new ArrayList<IElement>();

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
        IPage page = new Page(user, url);
        page.setTitle( title );
        user.getSiteMap().addPage( page );
        return page.getBean();
    }

    public static PageBean createPage(User user, String url, String title, String caption) throws Exception
    {
        IPage page = new Page(user, url);
        page.setTitle( title );
        page.setCaption( caption );
        user.getSiteMap().addPage(page);
        return page.getBean();
    }

    public void setSource(IElement source)
    {
        this.bean.setSource( source.getBean() );
    }

    @Override
    public PageBean getBean()
    {
        return bean;
    }

    public void setHome(boolean b)
    {
        this.bean.setHome( b );
    }

    public void setRightDomain(boolean rightDomain)
    {
        this.bean.setRightDomain( rightDomain );
    }

    public boolean isRightDomain()
    {
        return bean.isRightDomain();
    }

    public List<IElement> getOriginalContent() throws Exception
    {
        return getOriginal().getContent();
    }

    public List<IElement> getContent() throws Exception
    {
        System.out.println( "Parsing page content" );

        List<IElement> content = new ArrayList<IElement>();
        if ( isRightDomain() && !isBroken() )
        {
            content.addAll( getClickableContent() );

            Collections.sort( content );
        }

        System.out.println( "Parsing page content [Done]" );
        return content;
    }

    @Override
    public List<IElement> getClickableContent() throws Exception
    {
        List<IElement> clickableElements = new ArrayList<IElement>();

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
}
