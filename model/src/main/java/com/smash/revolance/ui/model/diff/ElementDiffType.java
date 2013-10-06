package com.smash.revolance.ui.model.diff;

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
 * Date: 23/04/13
 * Time: 22:59
 */
public enum ElementDiffType
{
    LOOK( "LOOK" ),
    CONTENT( "CONTENT" ),
    TYPE( "TYPE" ),
    LOCATION( "LOCATION" ),
    TARGET( "TARGET" ),
    STATE( "STATE" );

    private final String value;

    private ElementDiffType(String str)
    {
        this.value = str;
    }

    public String toString()
    {
        return value;
    }
}
