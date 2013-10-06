package com.smash.revolance.ui.model.element.api;

import com.smash.revolance.ui.model.page.api.Page;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * User: wsmash
 * Date: 01/06/13
 * Time: 04:21
 */
public class Input extends Element
{
    public Input(Page page, WebElement element)
    {
        super( page, element );
        setText( element.getText().trim() );
        setImplementation( "Input" );
        setType( element.getAttribute( "type" ) );
    }

    public static List<Element> filterInputs(List<Element> elements)
    {
        List<Element> fields = new ArrayList<Element>();

        if ( elements != null )
        {
            for ( Element element : elements )
            {
                if ( element instanceof Input )
                {
                    fields.add( element );
                }
            }
        }

        return fields;
    }
}
