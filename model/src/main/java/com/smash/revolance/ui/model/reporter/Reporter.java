package com.smash.revolance.ui.model.reporter;

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

import java.io.File;

/**
 * User: wsmash
 * Date: 20/02/13
 * Time: 10:43
 */
public interface Reporter<T>
{
    void doReport(T object, File file) throws Exception;
}
