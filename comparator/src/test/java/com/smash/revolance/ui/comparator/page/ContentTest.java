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
 * Date: 17/09/13
 * Time: 20:03
 */
public class ContentTest extends TestConstants
{
    private PageBean newPage;
    private PageBean refPage;

    @Before
    public void setup() throws Exception
    {
        refPage = SiteMap.fromJson( REF_PAGE_SITEMAP ).getPages().iterator().next();
        assertThat( refPage, notNullValue() );
        assertThat( refPage.getContent().size(), is( 39 ) );

        newPage = SiteMap.fromJson( NEW_PAGE_CONTENT_SITEMAP ).getPages().iterator().next();
        assertThat( newPage, notNullValue() );
        assertThat( newPage.getContent().size(), is( 31 ) );
    }

    @Test
    public void checkAllDifferencies()
    {
        PageComparator comparator = new PageComparator();
        PageComparison comparison = comparator.compare( newPage, refPage );

        assertThat( comparison.getPageDiffType(), is( DiffType.BASE ) );
        assertThat( comparison.getPageDifferencies().contains( PageDiffType.CONTENT ), is( true ) );

        assertThat( comparison.getPageElementComparisons().size(), is( 44 ) );

        assertThat( comparison.getBaseContent().size(), is( 26 ) );
        assertThat( comparison.getAddedContent().size(), is( 5 ) );
        assertThat( comparison.getDeletedContent().size(), is( 13 ) );

        // Checking the removed elements
        assertThat( ElementBean.containsData( comparison.getDeletedContent(), "Your Company" ), is( true ) );
        assertThat( ElementBean.containsData( comparison.getDeletedContent(), "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Phasellus ornare condimentum sem. Nullam a eros. Vivamus vestibulum hendrerit arcu. Integer a orci. Morbi nonummy semper est. Donec at risus sed velit porta dictum. Maecenas condimentum orci aliquam nunc. Morbi nonummy tellus in nibh. Suspendisse orci eros, dapibus at, ultrices at, egestas ac, tortor. Suspendisse fringilla est id erat. Praesent et libero." ), is( true ) );
        assertThat( ElementBean.containsData( comparison.getDeletedContent(), "Fusce et leo. Maecenas massa libero, egestas sed, ultricies tempor, laoreet eget, nisl. Nunc eleifend, orci eu aliquet consequat, metus diam suscipit felis, in adipiscing sapien nisl vitae ipsum. Nunc dui ante, vestibulum eget, viverra sed, ullamcorper quis, est. Nullam varius nunc." ), is( true ) );
        assertThat( ElementBean.containsData( comparison.getDeletedContent(), "WELCOME" ), is( true ) );
        assertThat( ElementBean.containsData( comparison.getDeletedContent(), "Nullam a eros. Vivamus vestibulum hendrerit arcu. Integer a orci. Morbi nonummy semper est." ), is( true ) );
        assertThat( ElementBean.containsData( comparison.getDeletedContent(), "Donec at risus sed velit porta dictum. Maecenas condimentum orci aliquam nunc." ), is( true ) );
        assertThat( ElementBean.containsData( comparison.getDeletedContent(), "Phone: 000-000-0000 /\n              000-000-0000" ), is( true ) );
        assertThat( ElementBean.containsData( comparison.getDeletedContent(), "Contact Info" ), is( true ) );
        assertThat( ElementBean.containsData( comparison.getDeletedContent(), "E-mail: abc@Lorem ipsum.com" ), is( true ) );
        assertThat( ElementBean.containsData( comparison.getDeletedContent(), "Fax:      000-000-0000" ), is( true ) );
        assertThat( ElementBean.containsData( comparison.getDeletedContent(), "00.00.0000" ), is( true ) );
        assertThat( ElementBean.containsData( comparison.getDeletedContent(), "00.00.0000" ), is( true ) );
        assertThat( ElementBean.containsLink( comparison.getDeletedContent(), "Email" ), is( true ) );

        // Checking the added elements
        assertThat( ElementBean.containsData( comparison.getAddedContent(), "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Phasellus ornare condimentum sem. Nullam a eros. Vivamus vestibulum hendrerit arcu. Integer a orci. Morbi nonummy semper est. Donec at risus sed velit porta dictum. Maecenas condimentum orci aliquam nunc. Morbi nonummy tellus in nibh. Suspendisse orci eros, dapibus at, ultrices at, egestas ac, tortor. Suspendisse fringilla est id erat." ), is( true ) );
        assertThat( ElementBean.containsData( comparison.getAddedContent(), "Info" ), is( true ) );
        assertThat( ElementBean.containsData( comparison.getAddedContent(), "E-mail:" ), is( true ) );
        assertThat( ElementBean.containsData( comparison.getAddedContent(), "Fax:" ), is( true ) );
        assertThat( ElementBean.containsData( comparison.getAddedContent(), "Phone:" ), is( true ) );

    }

}
