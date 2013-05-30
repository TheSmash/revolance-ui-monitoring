package com.smash.revolance.ui.explorer.element;

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

import com.smash.revolance.ui.explorer.element.api.ElementBean;
import com.smash.revolance.ui.explorer.page.IPage;
import com.smash.revolance.ui.explorer.user.User;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * User: wsmash
 * Date: 29/01/13
 * Time: 19:14
 */
public interface IElement extends Comparable<IElement>
{
    User getUser();

    IPage getPage();

    boolean equals(IElement element) throws Exception;

    boolean isIncluded(IElement element);

    void click() throws Exception;

    String getText();

    String getHref();

    String getTag();

    int getArea();

    Dimension getDim();

    Point getPos();

    void takeScreenShot() throws Exception;

    boolean hasBeenClicked();

    void delete() throws Exception;

    String getValue();

    String getId();

    boolean isClickable();

    String toJson() throws IOException;

    Rectangle getLocation();

    ElementBean getBean();

    void setDisabled(boolean b);

    void setClicked(boolean b);

    String getCaption();

    BufferedImage getImage() throws Exception;
}
