package com.smash.revolance.ui.model.page.api;

/*
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Revolance-UI-Model
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
import com.smash.revolance.ui.model.bot.Bot;
import com.smash.revolance.ui.model.element.api.*;
import com.smash.revolance.ui.model.helper.BotHelper;
import com.smash.revolance.ui.model.helper.ImageHelper;
import com.smash.revolance.ui.model.helper.UrlHelper;
import com.smash.revolance.ui.model.page.IPage;
import com.smash.revolance.ui.model.sitemap.SiteMap;
import com.smash.revolance.ui.model.user.User;
import org.apache.log4j.Level;
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

    private List<Element> content;

    private BufferedImage image;

    private boolean explored;
    private boolean parsed;
    private boolean screenshotTaken;

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
        setHome( user.getHome() == null ? true : url.contentEquals( user.getHome() ) );
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
        ElementBean source = this.bean.getSource();
        if ( source == null )
        {
            return new Element();
        }
        else
        {
            return source.getInstance();
        }
    }

    @Override
    public String getUrl()
    {
        if ( !isExternal() )
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
        getUser().getLogger().log(Level.INFO, "Awaiting page: '" + getTitle() + "' to be loaded" );
        long mark = System.currentTimeMillis();

        getApplication().awaitPageLoaded( this );

        long duration = ( System.currentTimeMillis() - mark ) / 1000;
        getUser().getLogger().log(Level.INFO, "Awaiting page: '" + getTitle() + "' to be loaded [Done] [Duration: " + duration + " sec]" );
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
        if ( isOriginal() )
        {
            getSiteMap().delPage( this );
        } else
        {
            getOriginal().getVariants().remove( this );
        }

        if ( getUser().isPageElementScreenshotEnabled() )
        {
            for ( Element element : getContent() )
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

    @Override
    public List<Element> getLinks() throws Exception
    {
        return Link.filterLinks( content );
    }

    @Override
    public String getTitle()
    {
        if ( !isOriginal() )
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
        List<Element> brokenLinks = new ArrayList();

        for ( Element link : getLinks() )
        {
            if ( link.isBroken() )
            {
                brokenLinks.add( link );
            }
        }
        return brokenLinks;
    }

    @Override
    public List<Element> getContent() throws Exception
    {
        return content;
    }

    public boolean isHomePage()
    {
        return bean.isHome();
    }

    public void setBroken(boolean b)
    {
        bean.setBroken( b );
        if ( getSource() != null )
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

    @Override
    public Application getApplication() throws Exception
    {
        return getUser().getApplication();
    }

    @Override
    public BufferedImage getImage() throws Exception
    {
        if ( image == null )
        {
            String img = BotHelper.takeScreenshot( getBot() );

            if ( img != null )
            {
                // update the caption
                setImage( ImageHelper.decodeToImage( img ) );
            }
        }
        return image;
    }

    @Override
    public List<Element> getButtons() throws Exception
    {
        return Button.filterButtons( content );
    }

    public List<Page> getVariants()
    {
        if ( isOriginal() )
        {
            List<Page> variants = new ArrayList();
            for ( PageBean page : _getVariants() )
            {
                variants.add( page.getInstance() );
            }
            return variants;
        } else
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
        if ( !hash.isEmpty() )
        {
            for ( Page variant : getVariants() )
            {
                if ( UrlHelper.getHash( getUrl() ).contentEquals( hash ) )
                {
                    return variant.getBean();
                }
            }
        }
        return null;
    }

    public Page findVariantByCaption(String caption) throws Exception
    {
        for ( Page variant : getVariants() )
        {
            if ( variant.getCaption().contentEquals( caption ) )
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
     * @param source
     * @return
     * @throws Exception
     */
    public Page findVariantBySource(ElementBean source) throws Exception
    {
        if ( source == null )
        {
            return null;
        }
        else
        {
            for ( Page page : getVariants() )
            {
                if ( source.equals( page.getSource() ) )
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
        if ( isOriginal() )
        {
            _getVariants().add( page.getBean() );
        } else
        {
            getOriginal().addVariant( page );
        }
    }

    @Override
    public boolean isExternal()
    {
        return bean.isExternal();
    }

    @Override
    public boolean isLogin()
    {
        return bean.isLogin();
    }

    @Override
    public void addMetaInf(String k, String v)
    {
        bean.addMetaInf(k, v);
    }

    public void setExternal(boolean b)
    {
        bean.setExternal( b );
    }

    private List<Element> getClickableVariations() throws Exception
    {
        List<Element> elements = new ArrayList();

        elements.addAll( Element.filterClickableElementBeans( bean.getAddedVariations() ) );

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
        Page page = new Page( user, url );
        page.setTitle( title );
        user.getSiteMap().addPage( page );
        return page.getBean();
    }

    public static PageBean createPage(User user, String url, String title, String caption) throws Exception
    {
        Page page = new Page( user, url );
        page.setTitle( title );
        page.setCaption( caption );
        user.getSiteMap().addPage( page );
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
    public List<Element> getClickableContent() throws Exception
    {
        List<Element> clickableElements = new ArrayList();

        clickableElements.addAll( getLinks() );
        clickableElements.addAll( getButtons() );

        Collections.sort( clickableElements );
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
        for ( Element aButton : getButtons() )
        {
            if ( aButton.getContent().contentEquals( button ) )
            {
                return aButton;
            }
        }
        return null;
    }

    public Collection<Element> getButtons(String button) throws Exception
    {
        List<Element> buttons = new ArrayList();
        for ( Element aButton : getButtons() )
        {
            if ( aButton.getContent().contentEquals( button ) )
            {
                buttons.add( aButton );
            }
        }
        return buttons;
    }

    public Element getLink(String link) throws Exception
    {
        for ( Element aLink : getLinks() )
        {
            if ( aLink.getContent().contentEquals( link ) )
            {
                return aLink;
            }
        }
        return null;
    }

    public Collection<Element> getLinks(String link) throws Exception
    {
        List<Element> links = new ArrayList();
        for ( Element aLink : getLinks() )
        {
            if ( aLink.getContent().contentEquals( link ) )
            {
                links.add( aLink );
            }
        }
        return links;
    }

    public List<Element> getImages() throws Exception
    {
        return Image.filterImages( content );
    }

    public Element getData(String data)
    {
        for ( Element elem : Data.filterData( content ) )
        {
            if ( elem.getContent().contentEquals( data ) )
            {
                return elem;
            }
        }
        return null;
    }

    public List<Element> getDatas(String data)
    {
        List<Element> datas = new ArrayList();
        for ( Element elem : Data.filterData( content ) )
        {
            if ( elem.getContent().contentEquals( data ) )
            {
                datas.add( elem );
            }
        }
        return datas;
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
        ArrayList<ElementBean> beanElements = new ArrayList();
        for ( Element element : content )
        {
            beanElements.add( element.getBean() );
        }
        bean.setContent( beanElements );
        this.content = content;
    }

    public List<Element> getFields()
    {
        return Input.filterInputs( content );
    }

    public boolean isEmpty()
    {
        return content == null || content.isEmpty();
    }

    public void setScreenshotTaken(boolean b)
    {
        this.screenshotTaken = b;
    }
}
