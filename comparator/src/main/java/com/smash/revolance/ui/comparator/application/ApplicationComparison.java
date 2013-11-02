package com.smash.revolance.ui.comparator.application;

import com.smash.revolance.ui.comparator.page.PageComparison;
import com.smash.revolance.ui.model.page.api.PageBean;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

/**
 * User: wsmash
 * Date: 09/06/13
 * Time: 12:07
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
                getterVisibility = JsonAutoDetect.Visibility.NONE,
                isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class ApplicationComparison
{

    @JsonDeserialize(contentAs = PageComparison.class, as = ArrayList.class)
    private List<PageComparison> pageComparisons = new ArrayList<PageComparison>();

    @JsonDeserialize(contentAs = PageBean.class, as = ArrayList.class)
    private List<PageBean> added = new ArrayList<PageBean>();

    @JsonDeserialize(contentAs = PageBean.class, as = ArrayList.class)
    private List<PageBean> deleted = new ArrayList<PageBean>();

    public void addNewPage(PageBean page)
    {
        added.add( page );
    }

    public void addRemovedPage(PageBean page)
    {
        deleted.add( page );
    }

    public void addPageComparison(PageComparison pageComparison)
    {
        pageComparisons.add( pageComparison );
    }

    public List<PageComparison> getPageComparisons()
    {
        return pageComparisons;
    }

    public List<PageBean> getRemovedPages()
    {
        return deleted;
    }

    public List<PageBean> getAddedPages()
    {
        return added;
    }
}
