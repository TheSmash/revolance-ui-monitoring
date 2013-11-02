package com.smash.revolance.ui.comparator.element;

import com.smash.revolance.ui.model.element.api.ElementBean;

import java.security.InvalidParameterException;
import java.util.Collection;

/**
 * User: wsmash
 * Date: 09/06/13
 * Time: 13:29
 */
public interface IElementComparator
{
    Collection<ElementDifferency> compare(ElementBean reference, ElementBean element) throws InvalidParameterException;
}
