package com.smash.revolance.ui.comparator.element;


import com.smash.revolance.ui.model.element.api.ElementBean;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: wsmash
 * Date: 09/06/13
 * Time: 13:31
 */
public class ElementComparator implements IElementComparator
{
    @Override
    public Collection<ElementDifferency> compare(ElementBean reference, ElementBean element) throws InvalidParameterException
    {
        List<ElementDifferency> differencies = new ArrayList<ElementDifferency>();
        if ( reference == null )
        {
            throw new InvalidParameterException( "Null reference element passed in." );
        }

        if ( !valueEquals( reference, element ) )
        {
            differencies.add( ElementDifferency.VALUE );
        }

        if ( !lookEquals( reference, element ) )
        {
            differencies.add( ElementDifferency.LOOK );
        }

        if ( !posEquals( reference, element ) )
        {
            differencies.add( ElementDifferency.POS );
        }

        if ( !targetEquals( reference, element ) )
        {
            differencies.add( ElementDifferency.TARGET );
        }

        if ( !implEquals( reference, element ) )
        {
            differencies.add( ElementDifferency.IMPL );
        }

        return differencies;
    }

    public static boolean implEquals(ElementBean reference, ElementBean element)
    {
        return reference.getImpl().contentEquals( element.getImpl() );
    }

    public static boolean targetEquals(ElementBean reference, ElementBean element)
    {
        return reference.getTarget().contentEquals( element.getTarget() );
    }

    public static boolean posEquals(ElementBean reference, ElementBean element)
    {
        return reference.getLocation().getX() == element.getX()
                && reference.getLocation().getY() == element.getY()
                && reference.getLocation().getWidth() == element.getWidth()
                && reference.getLocation().getHeight() == element.getHeight();
    }

    public static boolean lookEquals(ElementBean reference, ElementBean element)
    {
        return reference.getCaption().contentEquals( element.getCaption() );
    }

    public static boolean valueEquals(ElementBean reference, ElementBean element)
    {
        return reference.getContent().contentEquals( element.getContent() );
    }


}
