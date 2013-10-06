package com.smash.revolance.ui.materials.mock.webdriver;

import com.smash.revolance.ui.materials.JsonHelper;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * User: wsmash
 * Date: 03/10/13
 * Time: 20:02
 */
public class WebPageDataExtractor
{
    private String url;

    public WebPageDataExtractor(String url)
    {
        this.url = url;
    }

    public void extractToFile(File file) throws Exception
    {
        WebDriver browser = new FirefoxDriver();
        browser.navigate().to( url );

        List<WebElement> elements = browser.findElements( By.xpath( "//body//*" ) );
        String caption = takeScreenshot( browser );
        int w = getWidth( browser );
        int h = getHeight( browser );

        MockedWebPage page = new MockedWebPage();

        page.setUrl( browser.getCurrentUrl() );
        page.setTitle( browser.getTitle() );

        page.setH( h );
        page.setW( w );

        page.setCaption( caption );

        List<MockedWebElement> content = new ArrayList<MockedWebElement>();
        for ( WebElement element : elements )
        {

            MockedWebElement mockedElement = new MockedWebElement( UUID.randomUUID().toString() );

            Dimension dim = element.getSize();
            mockedElement.setW( dim.getWidth() );
            mockedElement.setH( dim.getHeight() );

            if ( dim.getHeight() * dim.getWidth() != 0 )
            {
                Point location = element.getLocation();
                mockedElement.setX( location.getX() );
                mockedElement.setY( location.getY() );

                mockedElement.setTag( element.getTagName() );
                mockedElement.setText( element.getText() );

                mockedElement.setClazz( element.getAttribute( "class" ) );
                mockedElement.setId( element.getAttribute( "id" ) );
                mockedElement.setHref( element.getAttribute( "href" ) );
                mockedElement.setType( element.getAttribute( "type" ) );
                mockedElement.setBg( element.getCssValue( "background-image" ) );
                mockedElement.setDisabled( element.getCssValue( ":disabled" ).contentEquals( "true" ) ? true : false );
                mockedElement.setDisplayed( element.isDisplayed() );

                content.add( mockedElement );
            }
        }

        page.setContent( content );

        browser.quit();

        if ( content.isEmpty() )
        {
            throw new Exception( "Content page is empty for url: " + url );
        } else
        {
            String data = JsonHelper.getInstance().map( page );
            FileUtils.writeStringToFile( file, data );
        }
    }

    private String takeScreenshot(WebDriver browser)
    {
        try
        {
            return ( (TakesScreenshot) browser ).getScreenshotAs( OutputType.BASE64 );
        }
        catch (Exception e)
        {
            sleep( 1 );
            return takeScreenshot( browser );
        }
    }

    private static void sleep(long duration)
    {
        int count = 0;
        while ( count < duration )
        {
            try
            {
                Thread.sleep( 1000 );
                count++;
            }
            catch (InterruptedException e)
            {
                // Ignore gently !
            }
        }
    }

    private int getHeight(WebDriver driver) throws Exception
    {
        Object o = runJS( driver, "var D = document; return Math.max(D.body.scrollHeight, D.documentElement.scrollHeight,D.body.offsetHeight, D.documentElement.offsetHeight,D.body.clientHeight, D.documentElement.clientHeight);" );
        return Integer.parseInt( String.valueOf( (Long) o ) );
    }

    private int getWidth(WebDriver driver) throws Exception
    {
        Object o = runJS( driver, "var D = document; return Math.max(D.body.scrollWidth, D.documentElement.scrollWidth,D.body.offsetWidth, D.documentElement.offsetWidth,D.body.clientWidth, D.documentElement.clientWidth);" );
        return Integer.parseInt( String.valueOf( (Long) o ) );
    }

    public Object runJS(WebDriver driver, String script) throws Exception
    {
        try
        {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            return js.executeScript( script );
        }
        catch (Exception e)
        {
            return null;
        }
    }

}
