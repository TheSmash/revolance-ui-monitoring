package com.smash.revolance.ui.model.element.api;

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

import com.smash.revolance.ui.model.helper.BotHelper;
import com.smash.revolance.ui.model.page.api.Page;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * User: wsmash
 * Date: 02/02/13
 * Time: 08:12
 */
public class Image extends Element
{
    public Image()
    {
        setTag( "img" );
    }

    public Image(Page page, WebElement element)
    {
        super( page, element );
        setText( "" );
        setImplementation( "Image" );
        setBackground( getBg( element.getTagName(), element ) );
        setAlt( element.getAttribute( "alt" ) );
    }

    public static List<Element> getImages(Page page) throws Exception
    {
        List<Element> images = new ArrayList<Element>();

        for ( WebElement element : BotHelper.getRawImages( page.getUser().getBot(), page ) )
        {
            try
            {
                if ( element.isDisplayed()
                        && Element.getImplementation( element ).getClass().getName().contentEquals( Image.class.getName() ) )
                {
                    Image image = new Image( page, element );
                    if ( image.getArea() > 0 )
                    {
                        if ( !Image.containsImage( images, image ) )
                        {
                            images.add( image );
                        }
                    }
                }
            }
            catch (StaleElementReferenceException e)
            {
                System.err.println( e );
            }
        }

        return images;
    }

    private static boolean containsImage(List<Element> images, Image image)
    {
        for ( Element element : images )
        {
            if ( element.getLocation().contains( image.getLocation() ) )
            {
                return true;
            }
        }
        return false;
    }


}
