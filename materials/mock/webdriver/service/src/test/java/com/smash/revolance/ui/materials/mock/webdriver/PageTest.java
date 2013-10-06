package com.smash.revolance.ui.materials.mock.webdriver;

import com.smash.revolance.ui.materials.TestConstants;
import com.smash.revolance.ui.materials.mock.webdriver.driver.MockedWebDriver;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * User: wsmash
 * Date: 30/09/13
 * Time: 19:28
 */
public class PageTest extends TestConstants
{
    private static MockedWebDriver browser;

    @BeforeClass
    public static void setUpTests() throws MalformedURLException
    {
        System.setProperty( "webdriver.remote.server", String.valueOf( 9000 ) );
        browser = new MockedWebDriver( 9000 );

        browser.navigate().to( REF_PAGE_HOME );
    }

    @Test
    public void pageShouldHandleHtml() throws IOException
    {
        List<WebElement> elements = browser.findElements( By.xpath( "//body//*" ) );
        assertThat( elements.size(), is( 85 ) );

        WebElement element = elements.get( 0 );

        Point location = element.getLocation();
        assertThat( location.getX(), is( 255 ) );
        assertThat( location.getY(), is( 35 ) );

        Dimension dimension = element.getSize();
        assertThat( dimension.getWidth(), is( 879 ) );
        assertThat( dimension.getHeight(), is( 918 ) );

        String tag = element.getTagName();
        assertThat( tag, is( "div" ) );

        String txt = element.getText();
        assertThat( txt, is( nullValue() ) );

        String cssClass = element.getAttribute( "class" );
        assertThat( cssClass, is( "page" ) );
    }
}
