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
    private List<Element> content;

    private BufferedImage image;

    private boolean            explored;
    private boolean            parsed;

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
        if ( !isExternal() )
        {
            // The url is relative to the domain
            return getUser().getDomain() + bean.getUrl();
        } else
        {
            return bean.getUrl();
        }
    }

    public void setUrl(String url)
    {
        if ( BotHelper.rightDomain( getUser(), url ) )
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

    public void awaitLoaded() throws Exception
    {
        System.out.println( "Awaiting page: '" + getTitle() + "' to be loaded" );
        long mark = System.currentTimeMillis();

        getApplication().awaitPageLoaded( this );

        long duration = (System.currentTimeMillis() - mark)/1000;
        System.out.println( "Awaiting page: '" + getTitle() + "' to be loaded [Done] [Duration: " + duration + " sec]" );
    }

    public void parse( ) throws Exception
    {
        if( !hasBeenParsed() )
        {
            if(getUser().getCurrentPage() != this)
            {
                getUser().goTo( this ).awaitLoaded();
            }

            setWidth(  Integer.parseInt( String.valueOf( (Long) getUser().getBot().runJS( "return document.body.clientWidth"  ) ) ) );
            setHeight( Integer.parseInt( String.valueOf( (Long) getUser().getBot().runJS( "return document.body.clientHeight" ) ) ) );

            if ( getApplication().isPageBroken( this ) )
            {
                setBroken( true );
            }
            if ( !getApplication().isAuthorized( this ) )
            {
                setAuthorized( false );
            }

            if ( getUser().isPageScreenshotEnabled()
                    && getCaption().isEmpty() )
            {
                takeScreenShot();
            }

            if(!isBroken() && !isExternal())
            {
                getContent();
            }
            else if ( isExternal() )
            {
                System.out.println( "Page with url: '" + getUrl() + "' is out of the domain: '" + getApplication().getDomain() + "'." );
            }
            else
            {
                System.out.println( "Page with url: '" + getUrl() + "' is broken." );
            }

            setParsed( true );
        }
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



    public boolean hasBeenBrowsed()
    {
        return bean.hasBeenBrowsed();
    }

    public String takeScreenShot() throws Exception
    {
        Bot bot = getUser().getBot();
        if(bean.getCaption().isEmpty())
        {
//            if(getTitle().isEmpty())
//            {
//                setTitle( getBot().getCurrentTitle() );
//            }

            if(!screenshotTaken
                    && getUser().isPageScreenshotEnabled())
            {
                System.out.print("\rTaking page snapshot: '" + getTitle() + "'");
                long mark = System.currentTimeMillis();

                String img = BotHelper.takeScreenshot( bot );

                if(img != null)
                {
                    // update the image and the caption
                    setImage( ImageHelper.decodeToImage( img ) );
                    screenshotTaken = true;
                }

                long duration = (System.currentTimeMillis()-mark)/1000;
                System.out.println("\rTaking page snapshot: '" + getTitle() + "' [Done] [Duration: " + duration + " sec]");
            }

        }
        return getCaption();
    }


    @Override
    public List<Element> getLinks() throws Exception
    {
        /*
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
        */
        return Link.filterLinks( content );
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
        if( getSource() != null )
        {
            getSource().setBroken( b );
        }
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
                System.out.print(String.format("\rTaking element screenshots ( %d / %d )", contentIdx, content.size()));
                pageElement.takeScreenShot();
            }
            System.out.println(String.format("\rTaking elements screenshots ( %d ) [Done]", contentIdx));
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
        /*
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
        */
        return Button.filterButtons( content );
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

    public PageBean findVariantByHash(String hash)
    {
        if(!hash.isEmpty())
        {
            for(Page variant : getVariants())
            {
                if( UrlHelper.getHash(getUrl()).contentEquals(hash) )
                {
                    return variant.getBean();
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
        if ( content == null )
        {
            System.out.println( "Parsing elements" );
            long mark = System.currentTimeMillis();

            content = _getContent();

            System.out.println("Clickable content found: ");
            for( Element element : Element.filterClickableElements( bean.getClickableContent() ) )
            {
                System.out.println( "--|  " + element.getContent() );
            }

            long duration = (System.currentTimeMillis()-mark)/1000;
            System.out.println( "Parsing elements [Done] [Duration: " + duration + " sec]" );
        }

        return content;
    }

    private List<Element> _getContent() throws Exception
    {
        List<Element> content = new ArrayList<Element>();

        if( !isExternal() && !isBroken() )
        {
            content = Element.getElements( this );
            PageHelper.filterElementsIncludedInEachOthers( content );

            setContent( content );

            System.out.println( bean.getContent().size() + " elements found" );

            takeScreenshots( content );

            Collections.sort( content );
        }

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

    public List<Element> getImages() throws Exception
    {
        /*
        if( images == null )
        {
            images = new ArrayList<Element>(  );

            if(!getUser().isExplorationDone())
            {
                if(!isExternal() && !isBroken() )
                {
                    System.out.print("Retrieving images");
                    images.addAll( Image.getImages( this ) );
                    ArrayList<ElementBean> beanImages = new ArrayList<ElementBean>(  );

                    for(Element image : images)
                    {
                        beanImages.add( image.getBean() );
                    }

                    bean.setImages( beanImages );
                    System.out.println("\r" + bean.getImages().size() + " images found");

                    takeScreenshots( images );
                }
            }
        }
        return images;
        */
        return Image.filterImages( content );
    }

    public Element getData(String data)
    {
        for(Element elem : Data.filterData( content ))
        {
            if( elem.getContent().contentEquals( data ) )
            {
                return elem;
            }
        }
        return null;
    }

    public int getArea()
    {
        return getHeight() * getWidth();
    }

    public int getWidth()
    {
        return bean.getWidth();
    }

    public int getHeight()
    {
        return bean.getWidth();
    }

    public void setScrollY(String scrollY)
    {
        this.bean.setScrollY( scrollY );
    }

    public void setScrollX(String scrollX)
    {
        this.bean.setScrollX( scrollX );
    }

    public void setContent(List<Element> content)
    {
        ArrayList<ElementBean> beanElements = new ArrayList<ElementBean>(  );
        for(Element element : content)
        {
            beanElements.add( element.getBean() );
        }
        bean.setContent( beanElements );
        this.content = content;
    }

    public List<Element> getFields()
    {
        return Input.filterFields( content );
    }
}
