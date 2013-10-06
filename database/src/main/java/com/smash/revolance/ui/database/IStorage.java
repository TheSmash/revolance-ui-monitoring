package com.smash.revolance.ui.database;

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

import java.util.Collection;
import java.util.Map;

/**
 * User: wsmash
 * Date: 08/06/13
 * Time: 08:13
 */
public interface IStorage
{
    void store(String key, String object) throws StorageException;

    String retrieve(String key) throws StorageException;

    String delete(String key) throws StorageException;

    Map<String, String> retrieveAll();

    Collection<String> getKeys();

    boolean isKeyUsed(String key);

    boolean isKeyValid(String key);

    void clear() throws StorageException;
}
