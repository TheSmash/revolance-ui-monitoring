package com.smash.revolance.ui.comparator.element;

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

import com.smash.revolance.ui.comparator.NoMatchFound;
import com.smash.revolance.ui.model.element.api.ElementBean;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: wsmash
 * Date: 09/06/13
 * Time: 13:01
 */
public class ElementMatchMaker implements IElementMatchMaker
{
    @Override
    public ElementMatch getBestMatch(Collection<ElementMatch> matches) throws NoMatchFound, IllegalArgumentException
    {
        if ( matches.isEmpty() )
        {
            throw new IllegalArgumentException( "Empty collection of matches cannot be analysed." );
        }

        ElementMatch bestMatch = new ElementMatch();
        for ( ElementMatch match : matches )
        {
            if ( match.getRate() > bestMatch.getRate() )
            {
                bestMatch = match;
            }
        }

        if ( bestMatch.getMatch() == null )
        {
            throw new NoMatchFound( "Unable to find a match." );
        } else
        {
            return bestMatch;
        }
    }

    @Override
    public Collection<ElementMatch> findMatch(Collection<ElementBean> elements, ElementBean element, ElementSearchMethod... methods) throws NoMatchFound, IllegalArgumentException
    {
        if ( elements == null )
        {
            throw new IllegalArgumentException( "Null content cannot be parsed" );
        }
        if ( element == null )
        {
            throw new IllegalArgumentException( "Null element cannot be matched." );
        }
        if ( elements.isEmpty() )
        {
            throw new NoMatchFound( "Unable to find a single match. Empty element collection passed in." );
        }

        Collection<ElementBean> localElements = new ArrayList<ElementBean>();
        localElements.addAll( elements );
        if ( localElements.contains( element ) )
        {
            localElements.remove( element );
        }

        List<ElementMatch> matches = getElementMatches( element, localElements, methods );

        if ( matches.isEmpty() )
        {
            throw new NoMatchFound( "Unable to find a single match." );
        } else
        {
            return matches;
        }
    }

    private List<ElementMatch> getElementMatches(ElementBean element, Collection<ElementBean> elements, ElementSearchMethod... methods)
    {
        List<ElementMatch> results = new ArrayList<ElementMatch>();

        ElementBean match = null;
        if ( ArrayUtils.contains( methods, ElementSearchMethod.LOOK ) )
        {
            match = null;
            if ( ( match = findElementByLook( elements, element ) ) != null )
            {
                ElementMatch elementMatch = new ElementMatch( match );
                elementMatch.setMatch( element, 1 );
                results.add( elementMatch );
            }
        }

        if ( ArrayUtils.contains( methods, ElementSearchMethod.VALUE ) )
        {
            match = null;
            if ( ( match = findElementByContent( elements, element ) ) != null )
            {
                ElementMatch elementMatch = new ElementMatch( match );
                elementMatch.setMatch( element, 1 );
                results.add( elementMatch );
            }
        }

        if ( ArrayUtils.contains( methods, ElementSearchMethod.POS ) )
        {
            match = null;
            if ( ( match = findElementByLocation( elements, element ) ) != null )
            {
                ElementMatch elementMatch = new ElementMatch( match );
                elementMatch.setMatch( element, 1 );
                results.add( elementMatch );
            }
        }

        if ( ArrayUtils.contains( methods, ElementSearchMethod.TARGET )
                && ( element.getImpl().contentEquals( "Button" ) || element.getImpl().contentEquals( "Link" ) ) )
        {
            match = null;
            if ( ( match = findElementByTarget( elements, element ) ) != null )
            {
                ElementMatch elementMatch = new ElementMatch( match );
                elementMatch.setMatch( element, 1 );
                results.add( elementMatch );
            }
        }

        return results;
    }

    public static ElementBean findElementByLocation(Collection<ElementBean> elements, ElementBean element)
    {
        for ( ElementBean elem : elements )
        {
            if ( ElementComparator.posEquals( element, elem ) )
            {
                return elem;
            }
        }
        return null;
    }

    public static ElementBean findElementByTarget(Collection<ElementBean> elements, ElementBean element)
    {
        for ( ElementBean elem : elements )
        {
            if ( ElementComparator.targetEquals( element, elem ) )
            {
                return elem;
            }
        }
        return null;
    }

    public static ElementBean findElementByContent(Collection<ElementBean> elements, ElementBean element)
    {
        for ( ElementBean elem : elements )
        {
            if ( ElementComparator.valueEquals( element, elem ) )
            {
                return elem;
            }
        }
        return null;
    }

    public static ElementBean findElementByLook(Collection<ElementBean> elements, ElementBean element)
    {
        for ( ElementBean elementBean : elements )
        {
            if ( ElementComparator.lookEquals( element, elementBean ) )
            {
                return elementBean;
            }
        }
        return null;
    }
}
