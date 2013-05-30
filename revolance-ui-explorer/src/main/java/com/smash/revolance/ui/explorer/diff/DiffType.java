package com.smash.revolance.ui.explorer.diff;

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
 * Date: 24/01/13
 * Time: 17:06
 */
public enum DiffType
{
    ADDED("ADDED"),
    DELETED("DELETED"),
    BASE("BASE");

    private final String value;

    private DiffType(String str)
    {
        this.value = str;
    }

    public String toString()
    {
        return value;
    }

    public boolean is(String value)
    {
        return toString().contentEquals( value );
    }

}
