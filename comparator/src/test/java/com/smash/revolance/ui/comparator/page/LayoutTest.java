package com.smash.revolance.ui.comparator.page;

/*
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * ui-monitoring-comparator
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2012 - 2014 RevoLance
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

import com.smash.revolance.ui.materials.TestConstants;
import com.smash.revolance.ui.model.diff.DiffType;
import com.smash.revolance.ui.model.diff.PageDiffType;
import com.smash.revolance.ui.model.page.api.PageBean;
import com.smash.revolance.ui.model.sitemap.SiteMap;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * User: wsmash
 * Date: 27/09/13
 * Time: 19:22
 */
public class LayoutTest extends TestConstants
{
    private PageBean newPage;
    private PageBean refPage;

    @Before
    public void setup() throws Exception
    {
        refPage = SiteMap.fromJson( REF_PAGE_SITEMAP ).getPages().iterator().next();
        assertThat( refPage, notNullValue() );
        assertThat( refPage.getContent().size(), is( 39 ) );

        newPage = SiteMap.fromJson( NEW_PAGE_LAYOUT_SITEMAP ).getPages().iterator().next();
        assertThat( newPage, notNullValue() );
        assertThat( newPage.getContent().size(), is( 39 ) );
    }

    @Test
    public void checkAllDifferencies()
    {
        PageComparator comparator = new PageComparator();
        PageComparison comparison = comparator.compare( newPage, refPage );

        assertThat( comparison.getPageDiffType(), is( DiffType.CHANGED ) );
        assertThat( comparison.getPageDifferencies().contains( PageDiffType.LAYOUT ), is( true ) );

        assertThat( comparison.getPageElementComparisons().size(), is( 39 ) );

        assertThat( comparison.getChangedContent().size(), is( 32 ) );
        assertThat( comparison.getAddedContent().size(), is( 0 ) );
        assertThat( comparison.getDeletedContent().size(), is( 0 ) );
        assertThat( comparison.getMovedContent().size(), is( 30 ) );

        // Checking the layout changes
        //assertThat( ElementBean.getData( comparison.getMovedContent(), "Your Company" ).isLeft( ElementBean.getData( refPage.getContent(), "Your Company" ) ), is(true));
        //assertThat( ElementBean.getData( comparison.getMovedContent(), "Your Company" ).isAbove( ElementBean.getData( refPage.getContent(), "Your Company" ) ), is(true));

    }

/*
    public static boolean checkElementPosition(List<ElementBean> e1s, List<ElementBean> e2s, String elem, String impl, LayoutChange... changes)
    {
        ElementBean e1 = ElementBean.getElement(e1s, impl, elem);
        ElementBean e2 = ElementBean.getElement(e2s, impl, elem);
        return checkPosition( e1, e2, changes);
    }
*/

}