package com.smash.revolance.ui.explorer.user;

/*
        This file is part of Revolance UI Suite.

        Revolance UI Suite is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        Revolance UI Suite is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with Revolance UI Suite.  If not, see <http://www.gnu.org/licenses/>.
*/

import com.smash.revolance.ui.explorer.element.api.Element;
import com.smash.revolance.ui.explorer.element.api.ElementBean;
import com.smash.revolance.ui.explorer.helper.JsonHelper;
import com.smash.revolance.ui.explorer.application.Application;
import com.smash.revolance.ui.explorer.application.ApplicationDifferencies;
import com.smash.revolance.ui.explorer.helper.UserHelper;
import com.smash.revolance.ui.explorer.page.api.Page;
import com.smash.revolance.ui.explorer.page.api.PageBean;
import com.smash.revolance.ui.explorer.reporter.api.GraphReporter;
import com.smash.revolance.ui.explorer.sitemap.SiteMap;
import com.smash.revolance.ui.explorer.bot.Bot;
import com.smash.revolance.ui.explorer.bot.BrowserFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.service.DriverService;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * User: wsmash
 * Date: 09/01/13
 * Time: 11:38
 */
public class User
{
    private Bot       bot;
    private WebDriver browser;

    private SiteMap sitemap;

    private File baseReportFolder = new File( "." );
    private File reportFolder     = new File( "." );

    private boolean browserActive;
    private boolean explorationDone;

    private Application app;

    private DriverService driverService;


    private UserBean bean = new UserBean( this );
    private Page currentPage;


    public User()
    {
        sitemap = new SiteMap( bean );
    }

    public User(String id, String home)
    {
        this();
        setId( id );
        setHome( home );
    }

    public User(String id, String home, String login, String passwd, String newPasswd)
    {
        this( id, home );
        setLogin( login );
        setPasswd( passwd );
        setNewPasswd( newPasswd );
    }

    public void enablePageElementScreenshot(boolean b)
    {
        bean.setPageElementScreenshotEnabled( b );
    }

    public Application getApplication()
    {
        return app;
    }

    public void setApplication(Application app)
    {
        this.app = app;
        setDomain( app.getDomain() );
    }

    public Bot getBot() throws Exception
    {
        if ( bot == null )
        {
            try
            {
                bot = new Bot( this );
            }
            catch (BrowserFactory.InstanciationError instanciationError)
            {
                throw new Exception( instanciationError.getCause() );
            }
        }
        return bot;
    }

    public String getLogin()
    {
        return bean.getLogin();
    }

    public void setLogin(String login)
    {
        bean.setLogin( login );
    }

    public String getPasswd()
    {
        return bean.getPasswd();
    }

    public void setPasswd(String passwd)
    {
        bean.setPasswd( passwd );
    }

    public String getNewPasswd()
    {
        return bean.getNewPasswd();
    }

    public void setNewPasswd(String passwd)
    {
        bean.setNewPasswd( passwd );
    }

    public WebDriver getBrowser() throws Exception
    {
        if(browser == null)
        {
            browser = getBot().getBrowser();
        }
        return browser;
    }

    public SiteMap getSiteMap()
    {
        if(sitemap == null)
        {
            sitemap = new SiteMap(bean);
        }
        return sitemap;
    }

    public File getReportFolder()
    {
        if(bean.getReportFolder().isEmpty())
        {
            reportFolder = new File(getBaseReportFolder(), getId());
            if(!reportFolder.exists())
            {
                reportFolder.mkdirs();
            }
            bean.setReportFolder( reportFolder.getPath() );
        }
        return new File( bean.getReportFolder() );
    }

    public String getId()
    {
        return bean.getId();
    }

    public void setId(String id)
    {
        bean.setId( id );
    }

    public File getBaseReportFolder()
    {
        return baseReportFolder;
    }

    public void setBaseReportFolder(String folder)
    {
        baseReportFolder = new File(folder, getApplication().getId());
    }

    public void doGraphReport() throws IOException
    {
        String title = "Sitemap for user: " + getId();
        File dotfile = getSiteMap().getSitemapDotFile();
        File imgFile = getSiteMap().getSitemapImgFile();

        new GraphReporter( this ).doGraphReport(title, dotfile, imgFile);
    }


    public void enablePageScreenshot(boolean b)
    {
        bean.setPageScreenshotEnabled( b );
    }

    public boolean isPageScreenshotEnabled()
    {
        return bean.isPageScreenshotEnabled();
    }

    public void setExplorationDone(boolean explorationDone)
    {
        this.explorationDone = explorationDone;
    }

    public void setDomain(String domain)
    {
        bean.setDomain( domain );
    }

    public void stopBot() throws Exception
    {
        if (isBrowserActive())
        {
            WebDriver browser = getBrowser();
            if(browser != null)
            {
                browser.quit();
            }

            DriverService driverService = getDriverService();
            if(driverService != null)
            {
                driverService.stop();
            }

            setBrowserActive(false);
        }
    }

    public boolean isBrowserActive()
    {
        return browserActive;
    }

    public void setBrowserActive(boolean browserActive)
    {
        this.browserActive = browserActive;
    }

    public List<String> getExcludedLinks()
    {
        return bean.getExcludedLinks();
    }

    public void setExcludedLinks(List<String> excludedLinks)
    {
        this.bean.setExcludedLinks( excludedLinks );
    }

    public List<String> getExcludedButtons()
    {
        return bean.getExcludedButtons();
    }

    public void setExcludedButtons(List<String> excludedButtons)
    {
        this.bean.setExcludedButtons( excludedButtons );
    }


    public Collection<String> getBrokenLinks()
    {
        List<String> links = new ArrayList<String>();
        for( String url : getSiteMap().getBrokenLinks())
        {
            links.add( url );
        }
        return links;
    }

    public Collection<PageBean> getBrokenPages()
    {
        return getSiteMap().getBrokenPages();
    }


    public ApplicationDifferencies getDiff(User user) throws Exception
    {
        ApplicationDifferencies differencies = new ApplicationDifferencies(this, user);
        return differencies;
    }

    public void setDriverService(DriverService service)
    {
        this.driverService = service;
    }

    public DriverService getDriverService()
    {
        return driverService;
    }

    public WebDriver setBrowser(WebDriver browser)
    {
        WebDriver oldBrowser = this.browser;
        this.browser = browser;
        return oldBrowser;
    }

    public int getBrowserHeight()
    {
        return bean.getBrowserHeight();
    }

    public void setBrowserHeight(int height)
    {
        this.bean.setBrowserHeight( height );
    }

    public int getBrowserWidth()
    {
        return bean.getBrowserWidth();
    }

    public void setBrowserWidth(int width)
    {
        this.bean.setBrowserWidth( width );
    }

    public File doContentReport() throws Exception
    {
        File report = new File(getReportFolder(), "sitemap.json");
        JsonHelper.getInstance().map( report, getSiteMap() );
        System.out.println("Report has been generated: " + report.getAbsolutePath());
        return report;
    }


    public String getBrowserBinary()
    {
        return bean.getBrowserBinary();
    }

    public void setBrowserBinary(String binary)
    {
        this.bean.setBrowserBinary( binary );
    }

    public UserBean getBean()
    {
        return bean;
    }

    public String getDomain()
    {
        return bean.getDomain();
    }

    public String getHome()
    {
        return bean.getHome();
    }

    public void setHome(String home)
    {
        bean.setHome( home );
    }

    public boolean isPageElementScreenshotEnabled()
    {
        return bean.isPageElementScreenshotEnabled();
    }

    public boolean isExplorationDone()
    {
        return explorationDone;
    }

    public void setExploreVariantsEnabled(boolean b)
    {
        this.bean.setExploreVariants( b );
    }

    public boolean wantsToExploreVariants()
    {
        return bean.isExploreVariantsEnabled();
    }

    public String getBrowserType()
    {
        return bean.getBrowserType();
    }

    public void setBrowserType(String type)
    {
        this.bean.setBrowserType( type );
    }

    public void setFollowButtons(boolean followButtons)
    {
        this.bean.setFollowButtonsEnabled( followButtons );
    }

    public boolean wantsToFollowButtons()
    {
        return bean.isFollowButtonsEnabled();
    }

    public void setFollowLinks(boolean followLinks)
    {
        this.bean.setFollowLinksEnabled( followLinks );
    }

    public boolean wantsToFollowLinks()
    {
        return bean.isFollowLinksEnabled();
    }

    public void setDriverPath(String path)
    {
        bean.setDriverPath( path );
    }

    public String getDriverPath()
    {
        return bean.getDriverPath();
    }

    public void setPageElementScreenshotEnabled(boolean b)
    {
        this.bean.setPageElementScreenshotEnabled( b );
    }

    public void setPageScreenshotEnabled(boolean b)
    {
        this.bean.setPageScreenshotEnabled( b );
    }

    public String getApplicationId()
    {
        return this.app.getId();
    }

    public void addExcludedButtons( String button )
    {
        if( button != null && !button.isEmpty())
        {
            getExcludedButtons().add( button );
        }
    }

    public void addExcludedLink(String link)
    {
        if( link != null && !link.isEmpty())
        {
            getExcludedLinks().add( link );
        }
    }

    public boolean wantsGreedyExploration()
    {
        return true;
    }

    public User goTo(Page page)
    {
        UserHelper.browseTo( page );
        setCurrentPage( page );
        return this;
    }

    public Page getCurrentPage()
    {
        return currentPage;
    }

    public User awaitLoaded() throws Exception
    {
        return awaitLoaded( getCurrentPage() );
    }

    public void parse( ) throws Exception
    {
        parse( getCurrentPage() );
    }

    public User awaitLoaded(Page page) throws Exception
    {
        page.awaitLoaded( );
        return this;
    }

    public void parse(Page page) throws Exception
    {
        page.parse( );
    }

    public void setCurrentPage(Page currentPage)
    {
        this.currentPage = currentPage;
    }

    public boolean canSee(String url, String impl, String txt) throws Exception
    {
        return !Element.filterElementsByText( filterContentByImpl( url, impl ), txt ).isEmpty();
    }

    public boolean cannotSee(String url, String impl, String txt) throws Exception
    {
        return !canSee( url, impl, txt );
    }

    public List<ElementBean> filterContentByImpl(String url, String... impl) throws Exception
    {
        PageBean page = getSiteMap().findPage( url );
        if( page != null )
        {
            return Element.filterElementsByImpls( page.getContent(), impl );
        }
        else
        {
            return new ArrayList<ElementBean>(  );
        }
    }

}
