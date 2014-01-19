package com.smash.revolance.ui.server;

/*
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * ui-monitoring-server
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

import com.smash.revolance.ui.materials.JsonHelper;
import org.jbehave.core.annotations.*;
import org.jboss.arquillian.phantom.resolver.ResolvingPhantomJSDriverService;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.service.DriverService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.fail;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.endsWith;
import static org.junit.Assert.assertThat;

/**
 * Created by wsmash on 07/12/13.
 */
public class BrowserSteps
{
    private DriverService service;
    private WebDriver browser;
    private final Map<String, String> variables = new HashMap<>();

    public WebDriver getBrowser()
    {
        return browser;
    }

    public BrowserSteps() throws RuntimeException
    {
        instanciateWebDriver(System.getProperty("BROWSER"));
        this.variables.put("URL", System.getProperty("URL"));
        this.variables.put("WEBSITE_REF", System.getProperty("WEBSITE_REF"));
        this.variables.put("WEBSITE_NEW", System.getProperty("WEBSITE_NEW"));
        this.variables.put("PAGE_REF", System.getProperty("PAGE_REF"));
        this.variables.put("PAGE_NEW_LAYOUT", System.getProperty("PAGE_NEW_LAYOUT"));
        this.variables.put("PAGE_NEW_CONTENT", System.getProperty("PAGE_NEW_CONTENT"));
        this.variables.put("PAGE_NEW_LOOK", System.getProperty("PAGE_NEW_LOOK"));
    }

    private void instanciateWebDriver(String browser) throws RuntimeException
    {
        if(browser.contentEquals("PhantomJS"))
        {
            instanciatePhantomJsWebDriver();
        }
        else if(browser.contentEquals("Firefox"))
        {
            instanciateFirefoxDriver();
        }
        else
        {
            throw new RuntimeException("Undefined browser type. Please define the system property: 'BROWSER' with a value in {'Firefox', 'PhantomJS'}");
        }
    }

    private void instanciateFirefoxDriver()
    {
        DesiredCapabilities cfg = DesiredCapabilities.firefox();
        cfg.setJavascriptEnabled(true);
        browser = new FirefoxDriver(cfg);
        browser.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @AfterStories
    public void afterScenario()
    {
        if(browser!=null)
        {
            browser.close();
        }
        if(service!=null)
        {
            service.stop();
        }
    }

    @Given("do $action on $elementId")
    @When("do $action on $elementId")
    public void doActionOnElementWithId(@Named("action") final String action, @Named("elementId") final String elementId)
    {
        WebElement element = getElementById(elementId);
        if(action.contentEquals("click"))
            element.click();
    }

    private WebElement getElementById(String elementId)
    {
        return this.browser.findElement(By.id(elementId));
    }

    @Given("do type $value in $elementId")
    @When("do type $value in $elementId")
    public void fillInputField(@Named("elementId") final String elementId, @Named("value") final String value)
    {
        WebElement element = getElementById(elementId);
        element.sendKeys(substituteVarByValue(value));
    }

    @Given("do select $option as $elementId")
    @When("do select $option as $elementId")
    public void selectOptionInList(@Named("elementId") final String elementId, @Named("option") final String option)
    {
        WebElement element = getElementById(elementId);
        if(!selectOption(option, element))
        {
            fail("Unable to find option: '" + option + "' in list: '#" +elementId + "'");
        }
    }

    @Given("goto $url")
    @When("goto $url")
    @Then("goto $url")
    public BrowserSteps goTo(@Named("url") final String url) throws IOException
    {
        browser.get(substituteVarByValue(url));
        return this;
    }

    @Given("browser.url==$url")
    public void assertBrowserUrlIs(@Named("url") final String url)
    {
        String expected = substituteVarByValue(url);
        String actual = browser.getCurrentUrl();
        assertThat(actual, endsWith(expected));
    }

    @Then("$elementId==$value")
    public void assertElementContentIs(@Named("elementId") final String elementId, @Named("value") final String value)
    {
        String expected = value;
        String actual = "";
        if(elementId.contentEquals("browser.url"))
        {
            actual = browser.getCurrentUrl();
        }
        else if(elementId.contentEquals("browser.title"))
        {
            actual = browser.getTitle();
        }
        else
        {
           actual = getElementById(elementId).getText();
        }

        assertThat(actual, equalTo(expected));
    }

/*
    private void loadJQuery() throws IOException
    {
        String script = "";
        script += "var script = document.createElement('script');";
        script += "script.src = 'http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js';";
        script += "script.type = 'text/javascript';";
        script += "document.getElementsByTagName('head')[0].appendChild(script);";

        String _jScript = substituteVarByValue(script);
        JavascriptExecutor js = (JavascriptExecutor) getBrowser();
        js.executeScript( _jScript );
    }

    private String mapSToJQuery(final String script)
    {
        String _script = script;
        for(String var : variables.keySet())
        {
            if(script.contains("<"+var+">"))
            {
                _script = _script.replaceAll("<"+var+">", variables.get(var));
            }
        }
        return _script;
    }
*/
    @Then("quit")
    public BrowserSteps quit()
    {
        browser.close();
        return this;
    }

    @Then("$script==$expected")
    public void assertResponseIs(@Named("script") final String script, @Named("expected") final String expected) throws IOException
    {
        String actual = JsonHelper.getInstance().map(execJScript(substituteVarByValue(script)));

        assertThat(actual , is(expected));
    }

    @Given("do exec $jScript")
    @Then("do exec $jScript")
    public Object execJScript(@Named("jScript") final String jScript)
    {
        String _jScript = "return "+substituteVarByValue(jScript);
        JavascriptExecutor js = (JavascriptExecutor) getBrowser();
        return js.executeScript( _jScript );
    }

    @Then("await $duration $unit")
    public void await(@Named("duration") final int duration, @Named("unit") final String unit)
    {
        int _duration = duration;
        if(unit.contentEquals("s"))
        {
            _duration = _duration*1000;
        }
        if(unit.contentEquals("mn"))
        {
            _duration = duration*1000*60;
        }
        sleep(_duration);
    }

    private void sleep(int duration)
    {
        try
        {
            Thread.sleep(duration);
        }
        catch(InterruptedException e)
        {
            // Ignore gently
        }
    }

    @Given("do await $sec sec for $elementId")
    public void awaitForElement(@Named("sec") final int sec, @Named("elementId") final String elementId)
    {
        boolean found = false;
        long timestamp = System.currentTimeMillis();
        while((System.currentTimeMillis()-timestamp)<sec*1000)
        {
            try
            {
                getElementById(elementId);
                found = true;
                break;
            }
            catch (Exception e)
            {
                found = false;
            }
        }
        if(!found)
        {
            fail("Unable to find element with id: " + elementId + " in " + sec + " sec.");
        }
    }

    private String substituteVarByValue(final String text)
    {
        String _text = text;
        for(String var : variables.keySet())
        {
            while(_text.contains("<"+var+">"))
            {
                _text = _text.replace("<"+var+">", variables.get(var));
            }
        }
        return _text;
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

    public void instanciatePhantomJsWebDriver() throws RuntimeException
    {
        DesiredCapabilities cfg = DesiredCapabilities.phantomjs();
        cfg.setJavascriptEnabled(true);

        try
        {
            service = ResolvingPhantomJSDriverService.createDefaultService();
            // service resolving phantomjs binary automatically
            browser = new PhantomJSDriver(service, cfg);
            browser.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private boolean selectOption(String option, WebElement element)
    {
        List<WebElement> options = element.findElements(By.tagName("option"));

        for(WebElement _option : options)
        {
            if(_option.getText().contentEquals(option))
            {
                _option.click();
                return true;
            }
        }
        return false;
    }

}

