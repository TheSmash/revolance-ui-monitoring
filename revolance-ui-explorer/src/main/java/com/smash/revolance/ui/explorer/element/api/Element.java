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

import com.smash.revolance.ui.explorer.helper.BotHelper;
import com.smash.revolance.ui.explorer.helper.ImageHelper;
import com.smash.revolance.ui.explorer.helper.UserHelper;
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

import static com.smash.revolance.ui.explorer.helper.BotHelper.findMatchingElement;
import static com.smash.revolance.ui.explorer.helper.UserHelper.browseTo;

/**
 * User: wsmash
 * Date: 26/01/13
 * Time: 23:59
 */
public class Element implements Comparable<Element>
{
    private ElementBean bean = new ElementBean( this );
    private BufferedImage img;

    public Element(Page page, WebElement element)
    {
        setPage( page );
        setText( element.getText().trim() );
        setTag( element.getTagName() );
        setDim( element.getSize() );
        setPos( element.getLocation() );
        setClz( element.getAttribute( "class" ) );
        setId( element.getAttribute( "id" ) );
    }

    public Element( )
    {

    }

    public void setAlt(String alt)
    {
        this.bean.setAlt( alt );
    }

    public void setBackground(String img)
    {
        bean.setBg( img );
    }

    public static String getBg(WebElement element)
    {
        String bg = "";
        if ( !element.getTagName().contentEquals( "img" ) )
        {
            bg = element.getCssValue( "background-image" );
            if ( bg.contentEquals( "none" ) )
            {
                bg = element.getCssValue( "background-url" );
            }
        }
        else
        {
            bg = element.getAttribute( "src" );
        }
        if ( bg.contentEquals( "none" ) )
        {
            bg = "";
        }
        if( !bg.isEmpty() )
        {
            //TODO: use standard image format instead of css/uri
            if(!bg.contains( "url" )
                    && !bg.contains( "http" )
                    && !bg.contains( ImageHelper.BASE64_IMAGE_PNG ) )
            {
                bg = "";
            }
        }
        return bg;
    }

    public static Class<? extends Element> getImplementation(WebElement element)
    {
        String tag = element.getTagName();
        if( tag == null )
        {
            tag = "";
        }
        String href = element.getAttribute( "href" );
        if( href == null )
        {
            href = "";
        }
        String type = element.getAttribute( "type" );
        if( type == null )
        {
            type = "";
        }
        String txt = element.getText();
        if( txt == null )
        {
            txt = "";
        }
        if ( tag.contentEquals( "a" ) && !href.isEmpty() )
        {
            return Link.class;
        }
        else if ( ( tag.contentEquals( "input" )
                        && (type.contentEquals( "submit" )
                            || type.contentEquals( "button" ) ) )
                    || ( tag.contentEquals( "button" ) ) )
        {
            return Button.class;
        }
        else if( tag.contentEquals( "input" )
                && (!type.contentEquals( "submit" ) && !type.contentEquals( "button" )) )
        {
            return Input.class;
        }
        else if ( !getBg( element ).isEmpty() && txt.isEmpty() )
        {
            return Image.class;
        }
        else if( !txt.isEmpty() )
        {
            return Data.class;
        }
        else
        {
            return null;
        }
    }

    public Element(PageBean pageBean, ElementBean bean)
    {
        this.bean = bean;
        setPage( new Page( pageBean ) );
    }

    protected void setImplementation(String implementation)
    {
        this.bean.setImpl( implementation );
    }

    public boolean click() throws Exception
    {
        if ( isClickable()  )
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

        return hasBeenClicked();
    }

    private void _click(Bot bot) throws Exception
    {
        WebElement element = findMatchingElement( bot, this );
        if ( element != null )
        {
            System.out.println( "Clicking on " + toJson() );
            try
            {
                String disabled = element.getAttribute( "disabled" );
                if ( disabled == null )
                {
                    element.click(); // Can thow an ex

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

    public boolean isIncluded(Element element)
    {
        return element.getBean().isIncluded( getBean() );
    }

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
        if(!bean.isExternal())
        {
            // The url is relative to the domain
            return getUser().getDomain() + bean.getHref();
        }
        else
        {
            return bean.getHref();
        }
    }

    public void setHref(String href)
    {
        if ( href.startsWith( getUser().getDomain() ) )
        {
            // The url is relative to the domain
            href = href.substring( getUser().getDomain().length() );
            bean.setHref( href );
        }
        else
        {
            // The url is absolute
            bean.setExternal( true );
            bean.setHref( href );
        }
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

    public Page getPage()
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
    public int compareTo(Element element)
    {
        return bean.compareTo( element.getBean() );
    }

    public void setPage(Page page)
    {
        bean.setPage( page.getBean() );
    }

    public boolean isClickable()
    {
        if ( bean.getImpl().contentEquals( "Link" ) )
        {
            if( !getUser().wantsToFollowLinks() )
            {
                return false;
            }
            else
            {
                return !getUser().getExcludedLinks().contains( getContent() );
            }
        }
        else if ( bean.getImpl().contentEquals( "Button" ) )
        {
            if( !getUser().wantsToFollowButtons() )
            {
                return false;
            }
            else
            {
                return !getUser().getExcludedButtons().contains( getContent() );
            }
        }
        else
        {
            return false;
        }
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

    public static boolean isIncluded(Element element, Rectangle rectangle)
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

    public static List<Element> filterClickableElements(List<ElementBean> elements)
    {
        List<Element> filteredElements = new ArrayList<Element>(  );

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
            String txt = element.getText();
            String value = element.getAttribute( "value" );
            if( txt.contentEquals( text ) || value.contentEquals( txt ))
            {
                return element;
            }
        }
        throw new Exception( "Unable to find element with text/value matching '" + text + "'." );
    }

    public static ElementBean filterElementByText(List<ElementBean> elements, String text) throws Exception
    {
        for( ElementBean element : elements )
        {
            if( element.getContent().contentEquals( text ) )
            {
                return element;
            }
        }
        return null;
    }

    public static List<ElementBean> filterElementsByText(List<ElementBean> elements, String... textList) throws Exception
    {
        List<ElementBean> matchingElements = new ArrayList<ElementBean>(  );

        for( ElementBean element : elements )
        {
            for(String txt : textList)
            {
                if( element.getContent().contentEquals( txt ) )
                {
                    matchingElements.add( element );
                    break;
                }
            }
        }

        return matchingElements;
    }

    public static List<ElementBean> filterElementsByImpls(List<ElementBean> elements, String... implementations)
    {
        List<ElementBean> matchingElements = new ArrayList<ElementBean>(  );

        for( ElementBean element : elements )
        {
            for(String implementation : implementations)
            {
                if( element.getImpl().contentEquals( implementation ) )
                {
                    matchingElements.add( element );
                    break;
                }
            }
        }

        return matchingElements;
    }

    public static List<ElementBean> filterElementsByLocations(List<ElementBean> elements, Rectangle... locations)
    {
        List<ElementBean> matchingElements = new ArrayList<ElementBean>(  );

        for( ElementBean element : elements )
        {
            for(Rectangle location : locations)
            {
                if( element.getLocation().contains( location ) )
                {
                    matchingElements.add( element );
                    break;
                }
            }
        }

        return matchingElements;
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

    public void setTarget(String target)
    {
        this.bean.setTarget( target );
    }

    public String getTarget()
    {
        return bean.getTarget();
    }

    public String getContent()
    {
        return bean.getContent();
    }

    public static Element buildElement(ElementBean elementBean)
    {
        Element element = null;
        if( elementBean.getImpl().contentEquals( "Link" ) )
        {
            element = new Link( elementBean );
        }
        else if( elementBean.getImpl().contentEquals( "Button" ) )
        {
            element = new Button( elementBean );
        }
        return element;
    }

    public boolean equals(Element element) throws IOException
    {
        return getBean().equals( element.getBean() );
    }

    public static List<Element> getElements(Page page) throws Exception
    {
        List<Element> elements = new ArrayList<Element>(  );

        List<WebElement> webElements = BotHelper.getRawElements( page.getUser().getBot(), page );
        int idx = 0;
        int elementCount = webElements.size();
        for(WebElement element : webElements)
        {
            idx ++;
            try
            {
                if(element.isDisplayed())
                {
                    Class<? extends Element> elemImpl = Element.getImplementation( element );
                    if(elemImpl != null)
                    {
                        Element elem = elemImpl.getConstructor( Page.class, WebElement.class ).newInstance( page, element );

                        if( elem.getArea()>0 )
                        {
                            Element.handleAddition( elements, elem );
                        }
                    }
                }
            }
            catch (StaleElementReferenceException e)
            {
                System.err.println(e);
            }
            System.out.print("\rRetrieving page element ( " + idx + "/" + elementCount + " )");
        }
        System.out.println("\nRetrieving page elements [Done]");
        return elements;
    }

    public static void handleAddition(List<Element> elements, Element elem)
    {
        List<Element> toBeRemoved = new ArrayList<Element>(  );

        if( elem instanceof Button
                || elem instanceof Link
                || elem instanceof Data )
        {
            for(Element element : elements )
            {
                if( element instanceof Data )
                {
                    if( element.isIncluded( elem )
                            && elem.getContent().contentEquals( element.getContent() ) )
                    {
                        // Replace the matched Data element by the Link or Button
                        toBeRemoved.add( element );
                    }
                }
            }

            elements.removeAll( toBeRemoved );
        }
        elements.add( elem );
    }

    private static boolean containsElement(List<Element> elements, Element elem)
    {
        for(Element element : elements)
        {
            if(elem.isIncluded( element )
                    && element.getClass().toString().contentEquals( elem.getClass().toString() ))
            {
                return true;
            }
        }
        return false;
    }

    public static List<Element> filterImages(List<Element> elements)
    {
        List<Element> images = new ArrayList<Element>(  );

        for( Element element : elements )
        {
            if( element instanceof Image )
            {
                images.add( element );
            }
        }

        return images;
    }


    public String getBackground()
    {
        return bean.getBg();
    }

    public Point getCenter()
    {
        return bean.getCenter();
    }

    public boolean isDisabled()
    {
        return bean.isDisabled();
    }

}
