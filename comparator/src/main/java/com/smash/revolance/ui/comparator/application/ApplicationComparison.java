package com.smash.revolance.ui.comparator.application;

 /*
        This file is part of Revolance UI Suite.

        Revolance UI Suite is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        Revolance UI Suite is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with Revolance UI Suite.  If not, see <http://www.gnu.org/licenses/>.
*/

import com.smash.revolance.ui.comparator.page.PageComparison;
import com.smash.revolance.ui.model.page.api.PageBean;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

 /*
        This file is part of Revolance UI Suite.

        Revolance UI Suite is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        Revolance UI Suite is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with Revolance UI Suite.  If not, see <http://www.gnu.org/licenses/>.
*/

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
