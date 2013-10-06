package com.smash.revolance.ui.materials.mock.webdriver;

import com.smash.revolance.ui.materials.TestConstants;
import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * User: wsmash
 * Date: 30/09/13
 * Time: 19:28
 */
@Ignore
public class PageTest extends TestConstants
{
    private static String newPageContentReference;
    private static String newPageLayoutReference;
    private static String refPageReference;
    private static String newPageUrlReference;
    private static String newPageLookReference;

    @BeforeClass
    public static void loadResources() throws IOException
    {
        refPageReference            = FileUtils.readFileToString( new File( TEST_RESOURCES + REF_PAGE_DIR, "mock.json" ) );
        newPageUrlReference         = FileUtils.readFileToString( new File( TEST_RESOURCES + NEW_PAGE_URL_DIR, "mock.json" ) );
        newPageLookReference        = FileUtils.readFileToString( new File( TEST_RESOURCES + NEW_PAGE_LOOK_DIR, "mock.json" ) );
        newPageLayoutReference      = FileUtils.readFileToString( new File( TEST_RESOURCES + NEW_PAGE_LAYOUT_DIR, "mock.json" ) );
        newPageContentReference     = FileUtils.readFileToString( new File( TEST_RESOURCES + NEW_PAGE_CONTENT_DIR , "mock.json" ) );
    }

    @Test
    public void extractDataFromRefPage() throws Exception
    {
        new WebPageDataExtractor( REF_PAGE_HOME ).extractToFile( MOCK_REF_PAGE );
        assertThat( refPageReference, is(FileUtils.readFileToString(MOCK_REF_PAGE)) );
    }

    @Test
    public void extractDataFromNewPageUrl() throws Exception
    {
        new WebPageDataExtractor( NEW_PAGE_URL_HOME ).extractToFile( MOCK_NEW_PAGE_URL );
        assertThat( newPageUrlReference, is(FileUtils.readFileToString(MOCK_REF_PAGE)) );
    }

    @Test
    public void extractDataFromNewPageLook() throws Exception
    {
        new WebPageDataExtractor( NEW_PAGE_LOOK_HOME ).extractToFile( MOCK_NEW_PAGE_LOOK );
        assertThat( newPageLookReference, is(FileUtils.readFileToString(MOCK_REF_PAGE)) );
    }

    @Test
    public void extractDataFromNewPageLayout() throws Exception
    {
        new WebPageDataExtractor( NEW_PAGE_LAYOUT_HOME ).extractToFile( MOCK_NEW_PAGE_LAYOUT );
        assertThat( newPageLayoutReference, is(FileUtils.readFileToString(MOCK_REF_PAGE)) );
    }

    @Test
    public void extractDataFromNewPageContent() throws Exception
    {
        new WebPageDataExtractor( NEW_PAGE_CONTENT_HOME ).extractToFile( MOCK_NEW_PAGE_CONTENT );
        assertThat( newPageContentReference, is(FileUtils.readFileToString(MOCK_REF_PAGE)) );
    }

}
