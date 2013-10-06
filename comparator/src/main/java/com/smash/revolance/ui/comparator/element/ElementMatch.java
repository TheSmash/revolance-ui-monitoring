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

import com.smash.revolance.ui.model.element.api.ElementBean;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * User: wsmash
 * Date: 09/06/13
 * Time: 12:48
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
                getterVisibility = JsonAutoDetect.Visibility.NONE,
                isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class ElementMatch implements Comparable<ElementMatch>
{
    @JsonIgnore
    private double rate = 0;

    private ElementBean match;
    private ElementBean reference;

    public ElementMatch(ElementBean element)
    {
        setReference( element );
    }

    public ElementMatch()
    {

    }

    public ElementBean getReference()
    {
        return reference;
    }

    public ElementMatch setReference(ElementBean reference)
    {
        this.reference = reference;
        return this;
    }

    public ElementBean getMatch()
    {
        return match;
    }

    public ElementMatch setMatch(ElementBean match)
    {
        this.match = match;
        return this;
    }

    public ElementMatch setMatch(ElementBean match, double rate)
    {
        setMatch( match );
        this.rate = rate;
        return this;
    }

    public double getRate()
    {
        return rate;
    }

    @Override
    public int compareTo(ElementMatch o)
    {
        if ( getRate() > o.getRate() )
        {
            return -1;
        } else
        {
            return 1;
        }
    }
}
