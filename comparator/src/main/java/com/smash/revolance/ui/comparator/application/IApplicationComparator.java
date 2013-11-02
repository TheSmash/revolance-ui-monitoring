package com.smash.revolance.ui.comparator.application;

import com.smash.revolance.ui.model.sitemap.SiteMap;

/**
 * User: wsmash
 * Date: 09/06/13
 * Time: 12:06
 */
public interface IApplicationComparator
{
    ApplicationComparison compare(SiteMap reference, SiteMap sitemap) throws IllegalArgumentException;
}
