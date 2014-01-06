package com.smash.revolance.ui.materials;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jboss.arquillian.phantom.resolver.ResolvingPhantomJSDriverService;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by wsmash on 07/12/13.
 */
public class BrowserSteps
{
    private final WebDriver browser;
    private final Map<String, String> variables = new HashMap<>();

    public WebDriver getBrowser()
    {
        return browser;
    }

    public BrowserSteps() throws RuntimeException
    {
        this.browser = instanciateWebDriver();
    }

    @Given("save $jScript as $var")
    public void saveResultInVar(@Named("jScript") final String jScript, @Named("var") final String var)
    {
        variables.put(var, (String) execJScript(jScript));
    }

    @Given("do $action on $elementId")
    @When("do $action on $elementId")
    public void doActionOnElementWithId(@Named("action") final String action, @Named("elementId") final String elementId)
    {
        WebElement element = getElementById(elementId);
        if(action.contentEquals("click"))
            element.click();
    }

    private WebElement getElementById(String elementId) {
        return this.browser.findElement(By.id(elementId));
    }

    @Given("do type $text in $elementId")
    @When("do type $text in $elementId")
    public void fillInputField(@Named("elementId") final String elementId, @Named("value") final String value)
    {
        WebElement element = getElementById(elementId);
        element.sendKeys(value);
    }

    @Given("goto $url")
    @When("goto $url")
    public BrowserSteps goTo(@Named("url") final String url)
    {
        browser.get(url);
        return this;
    }

    @Then("quit")
    public BrowserSteps quit()
    {
        browser.close();
        return this;
    }

    @Then("$script==$expected")
    public void assertResponseIs(@Named("script") final String script, @Named("expected") final String expected)
    {
        JavascriptExecutor js = (JavascriptExecutor) getBrowser();
        String actual = (String) js.executeScript( script );

        assertThat(actual , is(expected));
    }

    @Given("do exec $jScript")
    @Then("do exec $jScript")
    public Object execJScript(@Named("jScript") final String jScript)
    {
        JavascriptExecutor js = (JavascriptExecutor) getBrowser();
        return js.executeScript( jScript );
    }

    @Given("do await $sec sec for elementId")
    public void awaitForElement(@Named("sec") final int sec, @Named("elementId") final String id)
    {
        boolean found = false;
        long timestamp = System.currentTimeMillis();
        while((System.currentTimeMillis()-timestamp)<sec*1000)
        {
            try
            {
                assertResponseIs("$('" + id + "').length>0", "true");
                found = true;
                break;
            }
            catch (Exception e)
            {
                found = false;
            }
        }
        if(!found)
            fail("Unable to find element with id: " + id + " in " + sec + " sec.");
    }

    @Then("browser.url==$url")
    public void assertUrlIs(@Named("url") final String url)
    {
        assertThat(browser.getCurrentUrl(), is(url));
    }

    @Then("browser.title==$title")
    public void assertTitleIs(@Named("title") final String title)
    {
        assertThat(browser.getTitle(), is(title));
    }

    public WebDriver instanciateWebDriver() throws RuntimeException
    {
        return instanciatePhantomJsWebDriver();
    }

    public static WebDriver instanciatePhantomJsWebDriver() throws RuntimeException
    {
        DesiredCapabilities cfg = DesiredCapabilities.phantomjs();
        cfg.setJavascriptEnabled(true);

        try
        {
            // service resolving phantomjs binary automatically
            WebDriver browser = new PhantomJSDriver(ResolvingPhantomJSDriverService.createDefaultService(), cfg);
            browser.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            return browser;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

}

