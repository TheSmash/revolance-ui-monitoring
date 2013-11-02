package com.smash.revolance.ui.comparator.page;

import com.smash.revolance.ui.model.page.api.PageBean;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * User: wsmash
 * Date: 09/06/13
 * Time: 11:15
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
                getterVisibility = JsonAutoDetect.Visibility.NONE,
                isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class PageMatch implements Comparable<PageMatch>
{
    @JsonIgnore
    private double rate = 0;

    private PageBean match;
    private PageBean reference;

    public PageMatch(PageBean page)
    {
        setReference( page );
    }

    public PageMatch()
    {

    }

    public double getRate()
    {
        return rate;
    }

    public void setReference(PageBean reference)
    {
        this.reference = reference;
    }

    public PageBean getReference()
    {
        return reference;
    }

    public void setMatch(PageBean match, int rate)
    {
        this.match = match;
        this.rate = rate;
    }

    public void setMatch(PageBean match)
    {
        this.match = match;
    }

    public PageBean getMatch()
    {
        return match;
    }

    @Override
    public int compareTo(PageMatch o)
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
