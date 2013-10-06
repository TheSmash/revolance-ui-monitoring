package com.smash.revolance.ui.server.model;

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

import org.hibernate.validator.constraints.Range;
import org.springframework.stereotype.Service;

/**
 * User: wsmash
 * Date: 08/06/13
 * Time: 18:27
 */
@Service
public class Settings implements ISettings
{
    @Range(min = 100, max = 500)
    int decoratorWidth = 300;

    @Range(min = 100, max = 500)
    int decoratorHeight = 400;

    public Settings()
    {
    }

    @Override
    public int getDecoratorWidth()
    {
        return decoratorWidth;
    }

    @Override
    public void setDecoratorWidth(int width)
    {
        this.decoratorWidth = width;
    }

    @Override
    public int getDecoratorHeight()
    {
        return decoratorHeight;
    }

    @Override
    public void setDecoratorHeight(int height)
    {
        this.decoratorHeight = height;
    }
}
