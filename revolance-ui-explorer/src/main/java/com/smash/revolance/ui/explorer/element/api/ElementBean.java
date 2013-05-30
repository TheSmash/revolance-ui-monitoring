package com.smash.revolance.ui.explorer.element.api;

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

import com.smash.revolance.ui.explorer.helper.JsonHelper;
import com.smash.revolance.ui.explorer.diff.DiffCondition;
import com.smash.revolance.ui.explorer.diff.ElementDiffType;
import com.smash.revolance.ui.explorer.element.IElement;
import com.smash.revolance.ui.explorer.page.api.PageBean;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * User: wsmash
 * Date: 28/02/13
 * Time: 18:51
 */
@JsonAutoDetect(fieldVisibility= JsonAutoDetect.Visibility.ANY,
                getterVisibility= JsonAutoDetect.Visibility.NONE,
                isGetterVisibility= JsonAutoDetect.Visibility.NONE)
public class ElementBean implements Comparable<ElementBean>
{
    private int x, y, w, h;

    private String tag   = "";
    private String id    = "";
    private String clz   = "";
    private String txt   = "";
    private String type  = "";
    private String href  = "";
    private String value = "";

    private String impl    = "";
    private String caption = "";

    private boolean clicked = false;
    private boolean broken  = false;

    private String internalId = "";

    private boolean disabled = true;

    @JsonIgnore
    private PageBean page;

    @JsonIgnore
    private IElement instance;

    public ElementBean()
    {

    }

    public ElementBean(IElement instance)
    {
        setInstance( instance );
    }

    public ElementBean(String impl)
    {
        setImpl( impl );
    }


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
        if(tag != null)
            this.tag = tag;
    }

    public void setClz(String clz)
    {
        if(clz != null)
            this.clz = clz;
    }

    public String getClz()
    {
        return clz;
    }

    public void setDim(Dimension dim)
    {
        this.w = dim.getWidth();
        this.h = dim.getHeight();
    }

    public Dimension getDim()
    {
        return new Dimension( w, h );
    }

    public void setPos(Point pos)
    {
        this.x = pos.getX();
        this.y = pos.getY();
    }

    public Point getPos()
    {
        return new Point(x, y);
    }

    public String getTag()
    {
        return tag;
    }

    public void setType(String type)
    {
        if(type != null)
            this.type = type;
    }

    public String getType()
    {
        return type;
    }

    public String getText()
    {
        return txt;
    }

    public void setText(String txt)
    {
        this.txt = txt;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        if(value != null)
            this.value = value;
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
        this.caption = caption;
    }

    public String getCaption()
    {
        return caption;
    }

    public String toJson() throws IOException
    {
        return JsonHelper.getInstance().map( this );
    }

    public String getImpl()
    {
        return impl;
    }

    public void setImpl(String impl)
    {
        this.impl = impl;
    }

    public PageBean getPage()
    {
        return page;
    }

    public void setPage(PageBean page)
    {
        this.page = page;
    }

    public IElement getInstance()
    {
        return instance;
    }

    public void setInstance(IElement instance)
    {
        this.instance = instance;
    }

    public boolean isIncluded(ElementBean element)
    {
        return element.getText().contains( getText() )
                && element.getLocation().contains( getLocation() );
    }

    public Rectangle getLocation()
    {
        return new Rectangle( getPos().getX(), getPos().getY(), getDim().getWidth(), getDim().getHeight() );
    }

    @Override
    public int compareTo(ElementBean element)
    {
        Point elementCenter = element.getCenter();
        if ( elementCenter.getY() > getCenter().getY() )
        {
            return -1;
        }
        else
        {
            if( element.getPos().getX() < this.getPos().getX() )
            {
                return -1;
            }
            else
            {
                return 1;
            }
        }
    }

    public int getArea()
    {
        return getDim().getWidth() * getDim().getHeight();
    }

    public static List<ElementBean> filterLinks(List<ElementBean> elements)
    {
        return filterByImpl( elements, "Link" );
    }

    private static List<ElementBean> filterByImpl(List<ElementBean> content, String impl)
    {
        List<ElementBean> elements = new ArrayList<ElementBean>(  );

        for( ElementBean element : content )
        {
            if( element.getImpl().contentEquals( impl ) )
            {
                elements.add( element );
            }
        }

        return elements;
    }

    public static List<ElementBean> filterButtons(List<ElementBean> content)
    {
        return filterByImpl( content, "Button" );
    }

    public static boolean containsLink(List<ElementBean> elements, Link link)
    {
        for ( ElementBean element : filterLinks( elements ) )
        {
            if ( element.getText().contentEquals( link.getText() )
                    && element.getHref().contentEquals( link.getHref() )
                    && Element.isIncluded( element, link.getLocation() ))
            {
                return true;
            }
        }
        return false;
    }

    public static boolean containsLink(List<ElementBean> elements, String link)
    {
        for ( ElementBean element : filterLinks( elements ) )
        {
            if ( element.getText().contentEquals( link ) )
            {
                return true;
            }
        }
        return false;
    }

    public boolean isDisabled()
    {
        return disabled;
    }

    public void setDisabled(boolean disabled)
    {
        this.disabled = disabled;
    }

    public boolean equals(ElementBean bean) throws IOException
    {
        return equalsByAll( bean, ElementDiffType.values() );
    }

    public boolean equalsByAll(ElementBean bean, ElementDiffType... diffTypes)
    {
        return equalsBy( bean, DiffCondition.AND, diffTypes  );
    }

    public boolean equalsByOne(ElementBean bean, ElementDiffType... diffTypes)
    {
        return equalsBy( bean, DiffCondition.OR, diffTypes  );
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
        return getRelativeHref().contentEquals( bean.getRelativeHref() );
    }

    public boolean equalsByContent(ElementBean bean)
    {
        return getContent().contentEquals( bean.getContent() );
    }

    public String toString()
    {
        return getValue();
    }

    public String getContent()
    {
        return getText().isEmpty()?getValue():getText();
    }

    public String getRelativeHref()
    {
        return StringUtils.remove( getHref(), getPage().getUser().getDomain() );
    }

    public boolean equalsBy(ElementBean element, DiffCondition condition, ElementDiffType... diffTypes)
    {
        boolean b;

        if(condition == DiffCondition.AND)
        {
            b = true;
        }
        else
        {
            b = false;
        }
        for(ElementDiffType diffType : diffTypes)
        {
            switch ( diffType )
            {
                case LOOK:
                    if(condition == DiffCondition.AND)
                    {
                        b = b && equalsByLook( element );
                    }
                    else if(condition == DiffCondition.OR)
                    {
                        b = b || equalsByLook( element );
                    }
                    break;

                case TYPE:
                    if(condition == DiffCondition.AND)
                    {
                        b = b && equalsByType( element );
                    }
                    else if(condition == DiffCondition.OR)
                    {
                        b = b || equalsByType( element );
                    }
                    break;

                case TARGET:
                    if(condition == DiffCondition.AND)
                    {
                        b = b && equalsByTarget( element );
                    }
                    else if (condition == DiffCondition.OR)
                    {
                        b = b || equalsByTarget( element );
                    }
                    break;

                case CONTENT:
                    if(condition == DiffCondition.AND)
                    {
                        b = b && equalsByContent( element );
                    }
                    else if (condition == DiffCondition.OR)
                    {
                        b = b || equalsByContent( element );
                    }
                    break;

                case LOCATION:
                    if(condition == DiffCondition.AND)
                    {
                        b = b && equalsByLocation( element );
                    }
                    else if (condition == DiffCondition.OR)
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
        List<ElementDiffType> differencies = new ArrayList<ElementDiffType>();

        for(ElementDiffType diffType : ElementDiffType.values())
        {
            if(!equalsByOne( refElement, diffType ))
            {
                differencies.add( diffType );
            }
        }

        return differencies;
    }

    public void setInternalId(String internalId)
    {
        this.internalId = internalId;
    }

    public String getInternalId()
    {
        return internalId;
    }


    public boolean isClickable()
    {
        if ( getImpl().contentEquals( "Image" ) || getImpl().contentEquals( "Data" ))
        {
            return false;
        }
        else if ( getImpl().contentEquals( "Link" ) )
        {
            return !getPage().getInstance().getUser().getExcludedLinks().contains( getText() );
        }
        else if ( getImpl().contentEquals( "Button" ) )
        {
            return !getPage().getInstance().getUser().getExcludedButtons().contains( getText().isEmpty()?getValue():getText() );
        }
        else
        {
            return true;
        }
    }

    public Point getCenter()
    {
        return new Point( getPos().getY() + getDim().getHeight() / 2, getPos().getX() + getDim().getWidth() / 2 );
    }
}
