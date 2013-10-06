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

/**
 * User: wsmash
 * Date: 09/06/13
 * Time: 20:52
 */
public interface ISettings
{
    int getDecoratorWidth();

    void setDecoratorWidth(int width);

    int getDecoratorHeight();

    void setDecoratorHeight(int height);
}
