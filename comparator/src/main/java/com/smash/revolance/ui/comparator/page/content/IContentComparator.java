package com.smash.revolance.ui.comparator.page.content;

import com.smash.revolance.ui.comparator.element.ElementComparison;
import com.smash.revolance.ui.model.element.api.ElementBean;

import java.util.Collection;

/**
 * User: wsmash
 * Date: 09/06/13
 * Time: 12:59
 */
public interface IContentComparator
{
    Collection<ElementComparison> compare(Collection<ElementBean> reference, Collection<ElementBean> content);
}
