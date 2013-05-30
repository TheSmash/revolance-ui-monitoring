package com.smash.revolance.ui.explorer.element.api;

/*
        This file is part of Revolance.

        Revolance is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        Revolance is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with Revolance UI Suite.  If not, see <http://www.gnu.org/licenses/>.
*/

import com.smash.revolance.ui.explorer.element.IElement;
import com.smash.revolance.ui.explorer.helper.BotHelper;
import com.smash.revolance.ui.explorer.helper.ImageHelper;
import com.smash.revolance.ui.explorer.helper.UserHelper;
import com.smash.revolance.ui.explorer.page.IPage;
import com.smash.revolance.ui.explorer.page.api.Page;
import com.smash.revolance.ui.explorer.page.api.PageBean;
import com.smash.revolance.ui.explorer.user.User;
import com.smash.revolance.ui.explorer.bot.Bot;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.smash.revolance.ui.explorer.helper.BotHelper.findMatchingElement;
import static com.smash.revolance.ui.explorer.helper.UserHelper.browseTo;
import static com.smash.revolance.ui.explorer.helper.UserHelper.hasBeenExplored;

/**
 * User: wsmash
 * Date: 26/01/13
 * Time: 23:59
 */
public abstract class Element implements IElement
{
    private ElementBean bean;
    private BufferedImage img;

    private Element()
    {
        bean = new ElementBean( this.getClass().getSimpleName() );
    }

    public Element(IPage page, WebElement element)
    {
        this();
        setInternalId( UUID.randomUUID().toString() );
        setPage( page );
        setText( element.getText() );
        setTag( element.getTagName() );
        setDim( element.getSize() );
        setPos( element.getLocation() );
        setClz( element.getAttribute( "class" ) );
        setId( element.getAttribute( "id" ) );
        setValue( element.getAttribute( "value" ) );
    }

    public Element(PageBean pageBean, ElementBean bean)
    {
        this.bean = bean;
        setPage( new Page(pageBean) );
    }

    @Override
    public void click() throws Exception
    {
        if ( isClickable() && !hasBeenClicked()
                && !hasBeenExplored( getUser(), getHref() ) )
        {
            browseTo( getUser(), getUrl() );
            try
            {
                _click( getBot() );

                setHref( getBot().getCurrentUrl() );
                setClicked( true );
            }
            catch (Exception e)
            {
                setBroken( true );
                logException( e );
            }
        }
    }

    private void _click(Bot bot) throws Exception
    {
        WebElement element = findMatchingElement( bot, this );
        if(element != null)
        {
            System.out.println("Clicking on " + toJson());
            try
            {
                String disabled = element.getAttribute( "disabled" );
                if( disabled == null )
                {
                    element.click(); // Can thow an ex
                    BotHelper.sleep( 1 );
                }
            }
            catch (UnhandledAlertException e)
            {
                UserHelper.handleAlert( getUser() );
                _click( bot );
            }
        }
    }

    private void logException(Exception e) throws IOException
    {
        System.err.println("Element could not be found:\n" + e.getMessage());
    }

    public boolean equals(IElement element) throws Exception
    {
        return getBean().equals( element.getBean() );
    }

    @Override
    public boolean isIncluded(IElement element)
    {
        return element.getBean().isIncluded( getBean() );
    }

    @Override
    public Rectangle getLocation()
    {
        return bean.getLocation();
    }

//-- Beginning of setters / getters

    public void setId(String id)
    {
        bean.setId( id );
    }

    public String getId()
    {
        return bean.getId();
    }

    public String getHref()
    {
        return getUser().getDomain() + bean.getHref();
    }

    public void setHref(String href)
    {
        if( href.startsWith( getUser().getDomain() ) )
        {
            href = href.substring( getUser().getDomain().length() );
        }
        bean.setHref( href );
    }

    public boolean hasBeenClicked()
    {
        return bean.hasBeenClicked();
    }

    public void setTag(String tag)
    {
        bean.setTag( tag );
    }

    public String getTag()
    {
        return bean.getTag();
    }

    public String getClz()
    {
        return bean.getClz();
    }

    public void setClz(String clz)
    {
        bean.setClz( clz );
    }

    public Dimension getDim()
    {
        return bean.getDim();
    }

    public void setDim(Dimension dim)
    {
        bean.setDim( dim );
    }

    public Point getPos()
    {
        return bean.getPos();
    }

    public void setPos(Point pos)
    {
        bean.setPos( pos );
    }

    public void setType(String type)
    {
        bean.setType( type );
    }

    public String getType()
    {
        return bean.getType();
    }

    public void setText(String txt)
    {
        bean.setText( txt );
    }

    public String getText()
    {
        return bean.getText();
    }

    public IPage getPage()
    {
        return bean.getPage().getInstance();
    }

    public int getArea()
    {
        return bean.getArea();
    }

    public void setValue(String value)
    {
        bean.setValue( value );
    }

    public String getValue()
    {
        return bean.getValue();
    }

    public boolean isBroken()
    {
        return bean.isBroken();
    }

    public void setBroken(boolean b)
    {
        bean.setBroken( b );
    }

//-- Enf of basic setters / getters

    // Caption feature

    public BufferedImage getImage() throws Exception
    {
        if( img == null )
        {
            img = ImageHelper.decodeToImage( getCaption() );
        }
        return img;
    }

    public String getCaption()
    {
        return bean.getCaption();
    }

    @Override
    public void takeScreenShot() throws Exception
    {
        if ( getArea() > 0 )
        {
            if ( getPage().getUser().isPageScreenshotEnabled() && getPage().getUser().isPageElementScreenshotEnabled() )
            {

                int x = getPos().getX();
                int y = getPos().getY();

                int w = getDim().getWidth();
                int h = getDim().getHeight();

                BufferedImage pageImg = getPage().getImage();
                BufferedImage img = ImageHelper.cropImage( pageImg, x, y, w, h, getBot().getXScale(), getBot().getYScale() );
                bean.setCaption( ImageHelper.encodeToString( img ) );

                // Remove the element caption from the page caption
                pageImg = ImageHelper.eraseRect( pageImg, x, y, w, h, getBot().getXScale(), getBot().getYScale() );
                getPage().setCaption( ImageHelper.encodeToString( pageImg ) );
            }
        }
    }

    @Override
    public int compareTo(IElement element)
    {
        return bean.compareTo( element.getBean() );
    }

    public void setPage(IPage page)
    {
        bean.setPage( page.getBean() );
    }

    public boolean isClickable()
    {
        if( bean.getImpl().contentEquals( "Link" ) )
        {
            if( !getUser().isFollowLinksEnabled() )
            {
                return false;
            }
        }
        else if( bean.getImpl().contentEquals( "Button" ) )
        {
            if( !getUser().isFollowButtonsEnabled() )
            {
                return false;
            }
        }
        return bean.isClickable() ;
    }

    public static boolean isVisible(WebElement element)
    {
        return element.isDisplayed();
    }

    public String toJson() throws IOException
    {
        return bean.toJson();
    }

    // For convenience only

    public User getUser()
    {
        return getPage().getUser();
    }

    public Bot getBot() throws Exception
    {
        return getUser().getBot();
    }

    public WebDriver getBrowser() throws Exception
    {
        return getBot().getBrowser();
    }

    public String getUrl()
    {
        return getPage().getUrl();
    }

    public void delete() throws Exception
    {
        // nothing to be done
    }

    public String toString()
    {
        return bean.toString();
    }

    public static boolean isIncluded(ElementBean element, Rectangle rectangle)
    {
        return element.getLocation().contains( rectangle );
    }

    public static boolean isIncluded(IElement element, Rectangle rectangle)
    {
        return isIncluded( element.getBean(), rectangle );
    }

    public static boolean isIncluded(WebElement element, Rectangle rectangle)
    {
        Point topleft = element.getLocation();
        Dimension dim = element.getSize();
        Rectangle rectangleRef = new Rectangle( topleft.getX(), topleft.getY(), dim.getWidth(), dim.getHeight());

        return rectangleRef.contains( rectangle );
    }

    public static List<IElement> filterClickableElements(List<ElementBean> elements)
    {
        List<IElement> filteredElements = new ArrayList<IElement>(  );

        for(ElementBean element : elements)
        {
            if( element.getInstance().isClickable()  )
            {
                filteredElements.add( element.getInstance() );
            }
        }

        return filteredElements;
    }

    public static WebElement filterElementByText(List<WebElement> elements, String text) throws Exception
    {
        for(WebElement element : elements)
        {
            if(element.getText().contentEquals( text ) )
            {
                return element;
            }
        }
        throw new Exception( "Unable to find element with text '" + text + "'." );
    }

    public static WebElement filterElementByLocation(List<WebElement> elements, Rectangle rectangle) throws Exception
    {
        for(WebElement element : elements)
        {
            if( Element.isIncluded( element, rectangle ) )
            {
                return element;
            }
        }
        throw new Exception( "Unable to find element at location " + rectangle.toString() + "." );
    }

    public void setClicked(boolean clicked)
    {
        bean.setClicked( clicked );
    }

    public ElementBean getBean()
    {
        return bean;
    }

    public void setDisabled(boolean b)
    {
        bean.setDisabled( b );
    }


    public void setInternalId(String internalId)
    {
        this.bean.setInternalId( internalId );
    }

}
