package com.smash.revolance.ui.explorer.reporter.api;

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

import com.smash.revolance.ui.explorer.application.ApplicationDifferencies;
import com.smash.revolance.ui.explorer.reporter.Reporter;
import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * User: wsmash
 * Date: 20/02/13
 * Time: 10:46
 */
public class RegressionReporter implements Reporter<ApplicationDifferencies>
{
    public void doReport( ApplicationDifferencies differencies,  File file ) throws Exception
    {
        FileUtils.writeStringToFile(file, differencies.toJSon());
    }
}
