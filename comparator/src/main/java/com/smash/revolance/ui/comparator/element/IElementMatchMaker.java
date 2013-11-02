package com.smash.revolance.ui.comparator.element;

import com.smash.revolance.ui.comparator.NoMatchFound;
import com.smash.revolance.ui.model.element.api.ElementBean;

import java.util.Collection;

/**
 * User: wsmash
 * Date: 09/06/13
 * Time: 13:01
 */
public interface IElementMatchMaker
{
    ElementMatch getBestMatch(Collection<ElementMatch> matches) throws NoMatchFound, IllegalArgumentException;

    Collection<ElementMatch> findMatch(Collection<ElementBean> content, ElementBean element, ElementSearchMethod... methods) throws NoMatchFound, IllegalArgumentException;
}
