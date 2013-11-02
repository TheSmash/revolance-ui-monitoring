package com.smash.revolance.ui.materials.mock.webdriver;

/*
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Revolance-UI-Materials-Mock-Webdriver-Commons
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

import org.apache.commons.lang3.StringEscapeUtils;
import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * User: wsmash
 * Date: 03/10/13
 * Time: 20:34
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
                getterVisibility = JsonAutoDetect.Visibility.NONE,
                isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class MockedWebElement
{
    private String internalId = "";

    private int x, y, w, h;

    private String tag   = "";
    private String text  = "";
    private String clazz = "";
    private String id    = "";
    private String href  = "";
    private String type  = "";
    private String bg    = "none";

    private boolean disabled = false;
    private boolean displayed;

    public MockedWebElement()
    {

    }

    public MockedWebElement(String internalId)
    {
        setInternalId( internalId );
    }

    public String getInternalId()
    {
        return internalId;
    }

    public String getTag()
    {
        return tag;
    }

    public void setTag(String tag)
    {
        this.tag = tag;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = StringEscapeUtils.escapeJava( text );
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getW()
    {
        return w;
    }

    public void setW(int w)
    {
        this.w = w;
    }

    public int getH()
    {
        return h;
    }

    public void setH(int h)
    {
        this.h = h;
    }

    public String getClazz()
    {
        return clazz;
    }

    public void setClazz(String clazz)
    {
        this.clazz = clazz;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getHref()
    {
        return href;
    }

    public void setHref(String href)
    {
        if ( href != null )
            this.href = href;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        if ( type != null )
            this.type = type;
    }

    public String getBg()
    {
        return StringEscapeUtils.escapeJava( bg );
    }

    public void setBg(String bg)
    {
        if ( bg != null && !bg.contentEquals( "none" ) )
            this.bg = bg;
    }

    public boolean isDisabled()
    {
        return disabled;
    }

    public void setDisabled(boolean disabled)
    {
        this.disabled = disabled;
    }

    public void setInternalId(String internalId)
    {
        this.internalId = internalId;
    }

    public void setDisplayed(boolean displayed)
    {
        this.displayed = displayed;
    }

    public boolean isDisplayed()
    {
        return displayed;
    }
}
