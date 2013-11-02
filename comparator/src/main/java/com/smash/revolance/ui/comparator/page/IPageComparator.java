package com.smash.revolance.ui.comparator.page;

import com.smash.revolance.ui.model.page.api.PageBean;

/**
 * User: wsmash
 * Date: 09/06/13
 * Time: 10:53
 */
public interface IPageComparator
{
    PageComparison compare(PageBean page, PageBean reference);
}
