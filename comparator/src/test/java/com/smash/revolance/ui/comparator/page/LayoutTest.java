package com.smash.revolance.ui.comparator.page;

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

        assertThat( comparison.getPageDiffType(), is( DiffType.BASE ) );
        assertThat( comparison.getPageDifferencies().contains( PageDiffType.LAYOUT ), is( true ) );

        assertThat( comparison.getPageElementComparisons().size(), is( 39 ) );

        assertThat( comparison.getBaseContent().size(), is( 39 ) );
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