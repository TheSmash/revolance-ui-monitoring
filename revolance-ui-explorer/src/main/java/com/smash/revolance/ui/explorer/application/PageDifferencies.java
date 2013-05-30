package com.smash.revolance.ui.explorer.application;

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

import com.smash.revolance.ui.explorer.diff.DiffType;
import com.smash.revolance.ui.explorer.diff.PageDiffType;
import com.smash.revolance.ui.explorer.element.api.ElementBean;
import com.smash.revolance.ui.explorer.page.api.PageBean;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

/**
 * User: wsmash
 * Date: 28/04/13
 * Time: 19:13
 */
@JsonAutoDetect(fieldVisibility= JsonAutoDetect.Visibility.ANY,
                getterVisibility= JsonAutoDetect.Visibility.NONE,
                isGetterVisibility= JsonAutoDetect.Visibility.NONE)
public class PageDifferencies
{
    private String pageId    = "";
    private String refPageId = "";
    private DiffType diffType;

    @JsonDeserialize(contentAs = ElementBean.class, as = ArrayList.class)
    private List<ElementBean>  content;

    @JsonDeserialize(contentAs = PageDiffType.class, as = ArrayList.class)
    private List<PageDiffType> pageDiffTypes;

    public PageDifferencies(PageBean page, DiffType diffType)
    {
        this.pageId = page.getId();
        setDiffType( diffType );
        setContent( page.getContent() );
    }

    public PageDifferencies(PageBean page, PageBean refPage)
    {
        this.pageId = page.getId();
        this.refPageId = refPage.getId();
    }

    public void setContent(List<ElementBean> content)
    {
        this.content = content;
    }

    public void setPageDiffTypes(List<PageDiffType> pageDiffTypes)
    {
        this.pageDiffTypes = pageDiffTypes;
    }

    public void setDiffType(DiffType diffType)
    {
        this.diffType = diffType;
    }

}
