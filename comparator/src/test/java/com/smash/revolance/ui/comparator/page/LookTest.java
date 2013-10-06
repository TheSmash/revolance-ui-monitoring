package com.smash.revolance.ui.comparator.page;

import com.smash.revolance.ui.materials.TestConstants;
import com.smash.revolance.ui.model.diff.DiffType;
import com.smash.revolance.ui.model.diff.PageDiffType;
import com.smash.revolance.ui.model.element.api.ElementBean;
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
 * Time: 22:38
 */
public class LookTest extends TestConstants
{
    private PageBean newPage;
    private PageBean refPage;

    @Before
    public void setup() throws Exception
    {
        refPage = SiteMap.fromJson( REF_PAGE_SITEMAP ).getPages().iterator().next();
        assertThat( refPage, notNullValue() );
        assertThat( refPage.getContent().size(), is( 39 ) );

        newPage = SiteMap.fromJson( NEW_PAGE_LOOK_SITEMAP ).getPages().iterator().next();
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

        assertThat( comparison.getLookChanges().size(), is( 10 ) );

        assertThat( ElementBean.filterImages( comparison.getLookChanges() ).size(), is( 2 ) );

        assertThat( ElementBean.containsData( comparison.getLookChanges(), "Your Company" ), is( true ) );
        assertThat( ElementBean.containsLink( comparison.getLookChanges(), "Home" ), is( true ) );
        assertThat( ElementBean.containsLink( comparison.getLookChanges(), "About Us" ), is( true ) );
        assertThat( ElementBean.containsLink( comparison.getLookChanges(), "Recent articles" ), is( true ) );
        assertThat( ElementBean.containsLink( comparison.getLookChanges(), "Email" ), is( true ) );
        assertThat( ElementBean.containsLink( comparison.getLookChanges(), "Resources" ), is( true ) );
        assertThat( ElementBean.containsLink( comparison.getLookChanges(), "Links" ), is( true ) );
        assertThat( ElementBean.containsData( comparison.getLookChanges(), "WELCOME" ), is( true ) );
    }
}