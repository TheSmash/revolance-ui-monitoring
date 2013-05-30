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

import com.smash.revolance.ui.explorer.element.IElement;
import com.smash.revolance.ui.explorer.helper.BotHelper;
import com.smash.revolance.ui.explorer.page.IPage;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;


/**
 * User: wsmash
 * Date: 28/01/13
 * Time: 20:33
 */
public class Button extends Element
{
    public Button(IPage page, WebElement element)
    {
        super( page, element );
        setType( element.getAttribute( "type" ) );
        setValue( element.getAttribute( "value" ) );
    }

    public Button(ElementBean bean)
    {
        super(bean.getPage(), bean);
    }

    public static boolean isButton(WebElement element)
    {
        String tag = element.getTagName();
        String type = element.getAttribute( "type" );
        return Element.isVisible( element )
                && tag.contentEquals( "input" )
                && ( type.contentEquals( "button" ) || type.contentEquals( "submit" ) );
    }

    public static boolean containsButton(List<IElement> elements, IElement element) throws Exception
    {
        for ( IElement button : filterButtons( elements ) )
        {
            if ( button.getValue().contentEquals( element.getValue() ) )
            {
                return true;
            }
        }
        return false;
    }

    public static List<IElement> filterButtons(List<IElement> elements)
    {
        List<IElement> buttons = new ArrayList<IElement>(  );

        for( IElement element : elements )
        {
            if( element instanceof Button )
            {
                buttons.add( element );
            }
        }

        return buttons;
    }

    public static List<IElement> getButtons(IPage page) throws Exception
    {
        List<IElement> buttons = new ArrayList<IElement>();

        for(WebElement element : BotHelper.getRawButtons(page.getUser().getBot(), page))
        {
            try
            {
                if(element.isDisplayed())
                {
                    Button button = new Button( page, element );
                    String type = button.getType();
                    if(button.getArea()>0 && type.contentEquals( "submit" ) || type.contentEquals( "button" ) )
                    {
                        if(!Button.containsButton( buttons, button ))
                        {
                            buttons.add( button );
                        }
                    }
                }
            }
            catch (StaleElementReferenceException e)
            {
                System.err.println(e);
            }
        }

        return buttons;
    }

    public static IElement getButtonByValue(List<IElement> elements, String value) throws Exception
    {
        for(IElement element : filterButtons( elements ))
        {
            if(element.getValue().contentEquals( value ))
            {
                return element;
            }
        }
        throw new Exception("Unable to find button: " + value);
    }

    /**
     * For testing purposes
     *
     * @param elements
     * @param action
     * @return
     * @throws Exception
     */
    public static boolean containsButton(List<IElement> elements, String action) throws Exception
    {
        for ( IElement button : filterButtons( elements ) )
        {
            if ( button.getValue().contentEquals( action ) )
            {
                return true;
            }
        }
        return false;
    }
}
