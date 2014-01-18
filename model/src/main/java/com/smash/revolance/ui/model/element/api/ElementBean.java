package com.smash.revolance.ui.model.element.api;

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

import com.smash.revolance.ui.materials.JsonHelper;
import com.smash.revolance.ui.model.diff.DiffCondition;
import com.smash.revolance.ui.model.diff.ElementDiffType;
import com.smash.revolance.ui.model.page.api.PageBean;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * User: wsmash
 * Date: 28/02/13
 * Time: 18:51
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
                getterVisibility = JsonAutoDetect.Visibility.NONE,
                isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class ElementBean implements Comparable<ElementBean>
{
    private String internalId = "";

    // Location & Area of the element
    private int x, y, w, h;

    // UI look of the element
    private String caption = "";
    // HTML description of the element
    private String tag     = "";
    private String id      = "";
    private String clz     = "";
    private String txt     = "";
    private String type    = "";
    private String href    = "";
    private String value   = "";
    private String target  = "";

    // What king of element is this?
    // Can be anyone of 'Link', 'Button', 'Input', 'Data' or an 'Image'
    private String impl = "";

    // The background file of the element
    private String bg = "";

    // Alternative content rendered when the 'Image' cannot be displayed
    private String alt = "";

    // bot functional data
    private boolean clicked = false;

    // When the page contains a broken link
    private boolean broken = false;

    // Clicking on an element, we should see a change (url or dynamic content udpate)
    private boolean disabled = false;

    // URLs are relative but those outside the user's domain are absolute
    private boolean external = false;

    @JsonIgnore
    private PageBean page;

    @JsonIgnore
    private Element instance;


//+ Constructors

    public ElementBean()
    {
        setInternalId( UUID.randomUUID().toString() );
    }

    public ElementBean(Element instance)
    {
        this();
        setInstance( instance );
    }

//- Constructors

//+ Getters / Setters

    public void setClicked(boolean clicked)
    {
        this.clicked = clicked;
    }

    public void setId(String id)
    {
        if ( id != null )
            this.id = id;
    }

    public String getId()
    {
        return id;
    }

    public void setHref(String href)
    {
        if ( href != null )
            this.href = href;
    }

    public String getHref()
    {
        return href;
    }

    public boolean hasBeenClicked()
    {
        return clicked;
    }

    public void setTag(String tag)
    {
        if ( tag != null )
            this.tag = tag;
    }

    public String getTag()
    {
        return tag;
    }

    public void setClz(String clz)
    {
        if ( clz != null )
            this.clz = clz;
    }

    public String getClz()
    {
        return clz;
    }

    public void setDim(Dimension dim)
    {
        if ( dim != null )
        {
            this.w = dim.getWidth();
            this.h = dim.getHeight();
        }
    }

    public Dimension getDim()
    {
        return new Dimension( w, h );
    }

    public void setPos(Point pos)
    {
        if ( pos != null )
        {
            this.x = pos.getX();
            this.y = pos.getY();
        }
    }

    public Point getPos()
    {
        return new Point( x, y );
    }

    public void setType(String type)
    {
        if ( type != null )
            this.type = type;
    }

    public String getType()
    {
        return type;
    }

    public void setText(String txt)
    {
        if ( txt != null )
            this.txt = txt;
    }

    public String getText()
    {
        return txt;
    }

    public void setValue(String value)
    {
        if ( value != null )
            this.value = value;
    }

    public String getValue()
    {
        return value;
    }

    public void setBroken(boolean broken)
    {
        this.broken = broken;
    }

    public boolean isBroken()
    {
        return broken;
    }

    public void setCaption(String caption)
    {
        if ( caption != null )
            this.caption = caption;
    }

    public String getCaption()
    {
        return caption;
    }

    public void setImpl(String impl)
    {
        if ( impl != null )
            this.impl = impl;
    }

    public String getImpl()
    {
        return impl;
    }

    public void setTarget(String target)
    {
        if ( target != null )
            this.target = target;
    }

    public String getTarget()
    {
        return target;
    }

    public void setExternal(boolean external)
    {
        this.external = external;
    }

    public boolean isExternal()
    {
        return external;
    }

    public void setDisabled(boolean disabled)
    {
        this.disabled = disabled;
    }

    public boolean isDisabled()
    {
        return disabled;
    }

    public void setPage(PageBean page)
    {
        this.page = page;
    }

    public PageBean getPage()
    {
        return page;
    }

    public void setInternalId(String internalId)
    {
        if ( internalId != null )
            this.internalId = internalId;
    }

    public String getInternalId()
    {
        return internalId;
    }

    public void setInstance(Element instance)
    {
        this.instance = instance;
    }

    public Element getInstance()
    {
        if ( instance == null )
        {
            instance = Element.buildElement( this );
        }
        return instance;
    }

    public void setBg(String bg)
    {
        if ( bg != null )
            this.bg = bg;
    }

    public String getBg()
    {
        return bg;
    }

    public void setAlt(String alt)
    {
        if ( alt != null )
            this.alt = alt;
    }

    public String getAlt()
    {
        return alt;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public double getHeight()
    {
        return h;
    }

    public double getWidth()
    {
        return w;
    }

//- Getters / Setters

// + Comparison methods

    public boolean equals(ElementBean bean) throws IOException
    {
        return equalsByAll( bean, ElementDiffType.values() );
    }

    public boolean equalsByAll(ElementBean bean, ElementDiffType... diffTypes)
    {
        return equalsBy( bean, DiffCondition.AND, diffTypes );
    }

    public boolean equalsByOne(ElementBean bean, ElementDiffType... diffTypes)
    {
        return equalsBy( bean, DiffCondition.OR, diffTypes );
    }

    private boolean equalsByState(ElementBean bean)
    {
        return isDisabled() == bean.isDisabled();
    }

    public boolean equalsByType(ElementBean bean)
    {
        return bean.getImpl().contentEquals( getImpl() );
    }

    public boolean equalsByLook(ElementBean bean)
    {
        return bean.getDim().getHeight() == getDim().getHeight() &&
                bean.getDim().getWidth() == getDim().getWidth() &&
                equalsByLocation( bean ) &&
                bean.getCaption().contentEquals( getCaption() );
    }

    private boolean equalsByLocation(ElementBean bean)
    {
        return bean.getLocation().contains( getLocation() ) && getLocation().contains( bean.getLocation() );
    }

    public boolean equalsByTarget(ElementBean bean)
    {
        return getHref().contentEquals( bean.getHref() );
    }

    public boolean equalsByContent(ElementBean bean)
    {
        return getContent().contentEquals( bean.getContent() );
    }

    public boolean equalsBy(ElementBean element, DiffCondition condition, ElementDiffType... diffTypes)
    {
        boolean b;

        if ( condition == DiffCondition.AND )
        {
            b = true;
        } else
        {
            b = false;
        }
        for ( ElementDiffType diffType : diffTypes )
        {
            switch ( diffType )
            {
                case LOOK:
                    if ( condition == DiffCondition.AND )
                    {
                        b = b && equalsByLook( element );
                    } else if ( condition == DiffCondition.OR )
                    {
                        b = b || equalsByLook( element );
                    }
                    break;

                case TYPE:
                    if ( condition == DiffCondition.AND )
                    {
                        b = b && equalsByType( element );
                    } else if ( condition == DiffCondition.OR )
                    {
                        b = b || equalsByType( element );
                    }
                    break;

                case TARGET:
                    if ( condition == DiffCondition.AND )
                    {
                        b = b && equalsByTarget( element );
                    } else if ( condition == DiffCondition.OR )
                    {
                        b = b || equalsByTarget( element );
                    }
                    break;

                case CONTENT:
                    if ( condition == DiffCondition.AND )
                    {
                        b = b && equalsByContent( element );
                    } else if ( condition == DiffCondition.OR )
                    {
                        b = b || equalsByContent( element );
                    }
                    break;

                case LOCATION:
                    if ( condition == DiffCondition.AND )
                    {
                        b = b && equalsByLocation( element );
                    } else if ( condition == DiffCondition.OR )
                    {
                        b = b || equalsByLocation( element );
                    }
                    break;

            }
        }
        return b;
    }

    public List<ElementDiffType> getDiff(ElementBean refElement)
    {
        List<ElementDiffType> differencies = new ArrayList();

        for ( ElementDiffType diffType : ElementDiffType.values() )
        {
            if ( !equalsByOne( refElement, diffType ) )
            {
                differencies.add( diffType );
            }
        }

        return differencies;
    }

// - Comparison methods

//+ Convenience methods

    /**
     * Detect if the area of this element includes the area of the element in parameter
     *
     * @param element
     * @return true if this element includes the element in parameter or false otherwise.
     */
    public boolean isIncluded(ElementBean element)
    {
        return element.getContent().contains( getContent() )
                && element.getLocation().contains( getLocation() );
    }


    /**
     * Compute a rectangle object for this object location
     *
     * @return a rectangle sized and located at this element
     */
    public Rectangle getLocation()
    {
        return new Rectangle( getPos().getX(), getPos().getY(), getDim().getWidth(), getDim().getHeight() );
    }

    /**
     * Sort the elements in a collection so that the topleft element is before the top right and before the bottom left.
     *
     * @param element
     * @return -1 is this element is to be placed before the element in parameter or 1 otherwise
     */
    @Override
    public int compareTo(ElementBean element)
    {
        if ( isLeft( element ) && isAbove( element ) )
        {
            return -1;
        }
        else if ( isLeft( element ) && isBelow( element ) )
        {
            return 1;
        }
        else if ( isRight( element ) && isAbove( element ) )
        {
            return -1;
        }
        else if ( isRight( element ) && isBelow( element ) )
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }

    public boolean isLeft(ElementBean element)
    {
        return isLeft( this, element );
    }

    public static boolean isLeft(ElementBean e1, ElementBean e2)
    {
        return e1.getCenter().getX() < e2.getCenter().getX();
    }

    public boolean isRight(ElementBean element)
    {
        return !isLeft( element );
    }

    public boolean isAbove(ElementBean element)
    {
        return isAbove( this, element );
    }

    public static boolean isAbove(ElementBean e1, ElementBean e2)
    {
        return e1.getCenter().getY() < e2.getCenter().getY();
    }

    public boolean isBelow(ElementBean element)
    {
        return !isAbove( element );
    }

    /**
     * Compute the pixel area of this element.
     *
     * @return getWidth() * getHeight()
     */
    public int getArea()
    {
        return getDim().getWidth() * getDim().getHeight();
    }

    /**
     * Keep only the datas contained in the content.
     *
     * @param content
     * @return a list of the datas in the content
     */
    public static List<ElementBean> filterDatas(List<ElementBean> content)
    {
        return filterByImpl( content, "Data" );
    }

    /**
     * Keep only the links contained in the content.
     *
     * @param content
     * @return a list of the links in the content
     */
    public static List<ElementBean> filterLinks(List<ElementBean> content)
    {
        return filterByImpl( content, "Link" );
    }

    /**
     * Keep only the buttons contained in the content.
     *
     * @param content
     * @return a list of the buttons in the content
     */
    public static List<ElementBean> filterButtons(List<ElementBean> content)
    {
        return filterByImpl( content, "Button" );
    }

    /**
     * Keep only the images contained in the content.
     *
     * @param content
     * @return a list of the images in the content
     */
    public static List<ElementBean> filterImages(List<ElementBean> content)
    {
        return filterByImpl( content, "Image" );
    }

    public static boolean containsLink(List<ElementBean> elements, String link)
    {
        for ( ElementBean element : filterLinks( elements ) )
        {
            if ( element.getContent().contentEquals( link ) )
            {
                return true;
            }
        }
        return false;
    }

    public static ElementBean getLink(List<ElementBean> elements, String link)
    {
        for ( ElementBean element : filterLinks( elements ) )
        {
            if ( element.getContent().contentEquals( link ) )
            {
                return element;
            }
        }
        throw new NoSuchElementException( "Unable to find any link matching: " + link );
    }


    public static boolean containsData(List<ElementBean> elements, String content)
    {
        for ( ElementBean element : filterDatas( elements ) )
        {
            if ( element.getText().contentEquals( content ) )
            {
                return true;
            }
        }
        return false;
    }

    public static ElementBean getData(List<ElementBean> elements, String data)
    {
        for ( ElementBean element : filterDatas( elements ) )
        {
            if ( element.getContent().contentEquals( data ) )
            {
                return element;
            }
        }
        throw new NoSuchElementException( "Unable to find any data matching: " + data );
    }


    public static boolean containsImage(List<ElementBean> elements, String caption)
    {
        for ( ElementBean element : filterImages( elements ) )
        {
            if ( element.getCaption().contentEquals( caption ) )
            {
                return true;
            }
        }
        return false;
    }

    /**
     * For convenience, retrieve the center of this element
     *
     * @return the center Point of this element
     */
    public Point getCenter()
    {
        Rectangle area = getLocation();
        return new Point( (int) area.getCenterX(), (int) area.getCenterY() );
    }

    /**
     * For convenience, retrieve the user visible value of this element
     *
     * @return the text visible in the browser for this element
     */
    public String getContent()
    {
        return getText().isEmpty() ? getValue() : getText();
    }

    public String toJson() throws IOException
    {
        return JsonHelper.getInstance().map( this );
    }

    public String toString()
    {
        return "[" + getImpl() + "] " + getContent() + " -> " + getHref();
    }

    public static boolean isIncluded(ElementBean element, Rectangle rectangle)
    {
        return element.getLocation().contains( rectangle );
    }

//- Convenience methods

//+ Private methods

    /**
     * Filter the content given an implementation. .
     *
     * @param content
     * @param implementation
     *         The implementation can be: 'Link' or 'Button'
     * @return a list of the content matching the implementation criteria
     */
    public static List<ElementBean> filterByImpl(List<ElementBean> content, String implementation)
    {
        List<ElementBean> elements = new ArrayList<ElementBean>();

        for ( ElementBean element : content )
        {
            if ( element.getImpl().contentEquals( implementation ) )
            {
                elements.add( element );
            }
        }

        return elements;
    }


//- Private methods

}
