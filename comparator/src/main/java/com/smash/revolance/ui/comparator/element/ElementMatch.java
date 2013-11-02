package com.smash.revolance.ui.comparator.element;

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
