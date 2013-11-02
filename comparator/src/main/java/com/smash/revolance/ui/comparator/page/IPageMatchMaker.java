package com.smash.revolance.ui.comparator.page;

import com.smash.revolance.ui.comparator.NoMatchFound;
import com.smash.revolance.ui.model.page.api.PageBean;

import java.util.Collection;

/**
 * User: wsmash
 * Date: 09/06/13
 * Time: 11:07
 */
public interface IPageMatchMaker
{
    PageMatch getBestMatch(Collection<PageMatch> matches) throws NoMatchFound, IllegalArgumentException;

    Collection<PageMatch> findMatch(Collection<PageBean> pages, PageBean page, PageSearchMethod... methods) throws NoMatchFound, IllegalArgumentException;
}
