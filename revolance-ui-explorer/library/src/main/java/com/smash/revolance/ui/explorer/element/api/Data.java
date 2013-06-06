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

import com.smash.revolance.ui.explorer.page.api.Page;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * User: wsmash
 * Date: 29/01/13
 * Time: 20:47
 */
public class Data extends Element
{

    public Data(Page page, WebElement element)
    {
        super( page, element );
        setImplementation( "Data" );
    }

    public Data()
    {
        setTag( "div" );
    }

    @Override
    public boolean equals(Element element)
    {
        return getContent().contentEquals( element.getContent() );
    }

    public static List<Element> filterData(List<Element> content)
    {
        List<Element> datas = new ArrayList<Element>(  );

        if( content != null )
        {
            for(Element element : content)
            {
                if(element instanceof Data)
                {
                    datas.add( element );
                }
            }
        }

        return datas;
    }

}
