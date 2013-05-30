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
        along with Revolance UI Suite.  If not, see <http://www.gnu.org/licenses/>.
*/

import com.smash.revolance.ui.explorer.page.IPage;
import org.openqa.selenium.WebElement;

/**
 * User: wsmash
 * Date: 02/02/13
 * Time: 08:12
 */
public class Image extends Element
{
    public Image(IPage page, WebElement element)
    {
        super( page, element );
        // setImg( element.getCssValue( "background" ) );
    }

    public static boolean isImage(WebElement element)
    {
        return Element.isVisible( element ) &&
                ( !element.getCssValue( "background-image" ).isEmpty() || element.getTagName().contentEquals( "img" ) );
    }

}
