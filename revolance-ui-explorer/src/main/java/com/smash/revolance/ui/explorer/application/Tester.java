package com.smash.revolance.ui.explorer.application;

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

import com.smash.revolance.ui.explorer.user.User;

/**
 * User: wsmash
 * Date: 28/05/13
 * Time: 00:16
 */
public class Tester implements Runnable
{
    private final User user;

    public Tester(User user)
    {
        this.user = user;
    }

    @Override
    public void run()
    {
        try
        {
            user.explore();
            user.doSitemapReport();
        }
        catch (Exception e)
        {
            System.err.println("Error during exploration. Cause: " + e.getCause());
        }
    }
}
