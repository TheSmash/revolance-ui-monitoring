package com.smash.revolance.ui.materials;

import com.smash.revolance.ui.model.application.Application;
import com.smash.revolance.ui.model.page.IPage;
import com.smash.revolance.ui.model.user.User;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Created by ebour on 25/01/14.
 */
public class SecuredApplication extends Application
{
    @Override
    public void handleAlert(Alert alert) throws Exception
    {
        alert.accept();
    }

    @Override
    public void login(User user, IPage page) throws Exception
    {
        WebDriver browser = user.getBrowser();

        String login = user.getLogin();
        WebElement loginField = user.getBrowser().findElement(By.id("username"));
        loginField.sendKeys(login);

        String passwd = user.getPasswd();
        WebElement passwdField = browser.findElement(By.id("password"));
        passwdField.sendKeys(passwd);
    }

    @Override
    public boolean isLoginPage(IPage page) throws Exception
    {
        WebDriver browser = page.getUser().getBrowser();
        try
        {
            browser.findElement(By.xpath("username"));
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    @Override
    public boolean isPageBroken(IPage page) throws Exception
    {
        return false;
    }

    @Override
    public boolean isAuthorized(IPage page) throws Exception
    {
        return false;
    }

    @Override
    public void awaitLoaded(IPage page) throws Exception
    {

    }
}
