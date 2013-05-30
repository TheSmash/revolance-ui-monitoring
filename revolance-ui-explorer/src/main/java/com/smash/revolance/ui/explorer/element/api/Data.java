package com.smash.revolance.ui.explorer.element.api;

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
        along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
*/

import com.smash.revolance.ui.explorer.element.IElement;
import com.smash.revolance.ui.explorer.page.IPage;
import org.openqa.selenium.WebElement;

/**
 * User: wsmash
 * Date: 29/01/13
 * Time: 20:47
 */
public class Data extends Element
{

    public Data(IPage page, WebElement element)
    {
        super( page, element );
    }

    @Override
    public boolean equals(IElement element)
    {
        return getText().contentEquals( element.getText() );
    }

}
