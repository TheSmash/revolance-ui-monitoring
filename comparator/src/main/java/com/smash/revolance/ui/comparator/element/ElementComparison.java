package com.smash.revolance.ui.comparator.element;

import com.smash.revolance.ui.model.diff.DiffType;
import com.smash.revolance.ui.model.element.api.ElementBean;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.util.ArrayList;
import java.util.Collection;

/**
 * User: wsmash
 * Date: 09/06/13
 * Time: 11:01
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
                getterVisibility = JsonAutoDetect.Visibility.NONE,
                isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class ElementComparison
{
    private ElementMatch match = new ElementMatch();

    private DiffType diffType;

    @JsonDeserialize(contentAs = ElementDifferency.class, as = ArrayList.class)
    private Collection<ElementDifferency> elementDifferencies = new ArrayList<ElementDifferency>();

    public ElementComparison(ElementMatch match)
    {
        this.match = match;
    }

    public ElementComparison()
    {

    }

    public void setDiffType(DiffType diffType)
    {
        this.diffType = diffType;
    }

    public DiffType getDiffType()
    {
        return diffType;
    }

    public void setElementDifferencies(Collection<ElementDifferency> elementDifferencies)
    {
        this.elementDifferencies = elementDifferencies;
    }

    public Collection<ElementDifferency> getElementDifferencies()
    {
        return elementDifferencies;
    }

    public ElementBean getReference()
    {
        return match.getReference();
    }

    public ElementBean getMatch()
    {
        return match.getMatch();
    }

    public void setMatch(ElementMatch match)
    {
        this.match = match;
    }

    public String toString()
    {
        return String.valueOf( diffType ) + "[" + ( diffType == DiffType.ADDED ? match.getMatch().getText() : ( diffType == DiffType.DELETED ? getReference().getText() : "" ) ) + "]";
    }
}
