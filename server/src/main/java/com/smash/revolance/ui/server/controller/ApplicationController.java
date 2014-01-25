package com.smash.revolance.ui.server.controller;

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


import com.smash.revolance.ui.database.IStorage;
import com.smash.revolance.ui.database.StorageException;
import com.smash.revolance.ui.explorer.ExplorationConfiguration;
import com.smash.revolance.ui.explorer.IExplorer;
import com.smash.revolance.ui.materials.JsonHelper;
import com.smash.revolance.ui.model.element.ElementNotFound;
import com.smash.revolance.ui.model.element.api.ElementBean;
import com.smash.revolance.ui.model.page.api.PageBean;
import com.smash.revolance.ui.model.sitemap.SiteMap;
import com.smash.revolance.ui.server.model.Application;
import com.smash.revolance.ui.server.model.Exploration;
import com.smash.revolance.ui.server.renderable.ApplicationDetailsRenderable;
import org.rendersnake.HtmlCanvas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by wsmash on 27/10/13.
 */
@Controller
public class ApplicationController
{
    private IStorage logs;

    @Autowired
    public void setLogs(IStorage logs)
    {
        this.logs = logs;
    }

    private IStorage applications;

    @Autowired
    public void setApplications(IStorage applications)
    {
       this.applications = applications;
    }

    IExplorer explorer;

    @Autowired
    public void setExplorer(IExplorer explorer)
    {
        this.explorer = explorer;
    }

    @RequestMapping(value = "/applications", method = RequestMethod.POST)
    public ModelAndView declareApplication(@Valid Exploration exploration, BindingResult result, @ModelAttribute("model") ModelMap model) throws IOException, StorageException
    {
        if ( result.hasErrors() )
        {
            return displayApplicationForm(model);
        }
        else
        {
            final ExplorationConfiguration cfg = new ExplorationConfiguration();

            File log = new File(logs.getLocation(), exploration.getTag());
            log.createNewFile();

            cfg.setLogFile(log);

            File report = new File(applications.getLocation(), exploration.getTag());
            cfg.setReportFile(report);

            cfg.setLogin(exploration.getLogin()); // This is mandatory in both cases
            if(exploration.isSecured())
            {
                cfg.setPassword(exploration.getPassword());
                cfg.setApplication(exploration.getApplicationModel().getBytes());
                cfg.setApplicationSecured(true);
                cfg.setApplicationClassName(exploration.getApplicationClassName());
            }

            cfg.setBrowserHeight(exploration.getHeight());
            cfg.setBrowserWidth(exploration.getWidth());

            Map<String, Object> settings = (Map<String, Object>) JsonHelper.getInstance().map(Map.class, new File(System.getProperty("settings", "")));
            List<Map<String, String>> browsers = (List<Map<String, String>>) settings.get("browsers");

            boolean found = false;
            for(Map<String, String> browser : browsers)
            {
                if(browser.get("name").contentEquals(exploration.getBrowserType()))
                {
                    cfg.setBrowserType(browser.get("name"));
                    cfg.setDriverPath(browser.get("driverPath"));
                    cfg.setBrowserPath(browser.get("browserPath"));
                    found = true;
                }
                if(found)
                {
                    break;
                }
            }

            cfg.setDomain(exploration.getDomain());
            cfg.setUrl(exploration.getUrl());
            cfg.setFollowButtons(exploration.isFollowButtons());
            cfg.setFollowLinks(exploration.isFollowLinks());
            cfg.setId(exploration.getTag());
            cfg.setTimeout(exploration.getTimeout());
            cfg.setPageScreenshotEnabled(true);
            cfg.setPageElementScreenshotEnabled(true);

            new Thread(new Runnable() {
                @Override
                public void run()
                {
                    try {
                        explorer.explore(cfg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            return new ModelAndView("ApplicationLogs", "applicationId", exploration.getTag());
        }
    }

    @RequestMapping(value = "/applications/declare", method = RequestMethod.GET)
    public ModelAndView displayApplicationForm(@ModelAttribute("model") ModelMap model) throws IOException
    {
        Map<String, Object> settings = (Map<String, Object>) JsonHelper.getInstance().map(Map.class, new File(System.getProperty("settings", "")));
        model.addAttribute("browsers", settings.get("browsers"));

        model.addAttribute("resolutions", settings.get("resolutions"));

        return new ModelAndView( "ApplicationForm", model );
    }

    @RequestMapping(value = "/applications/{applicationId}", method = RequestMethod.DELETE)
    public ModelAndView removeApplication(@PathVariable String applicationId) throws StorageException, IOException
    {
        applications.delete(applicationId);
        logs.delete(applicationId);
        return new ModelAndView( "ApplicationList", "applications", getApplications() );
    }

    @ResponseBody
    @RequestMapping(value = "/applications/{applicationId}/pages/{pageId}/elements/${elementId}", method = RequestMethod.GET, headers="Accept=application/json")
    public ElementBean retrievePageElement(@PathVariable String applicationId, @PathVariable String pageId, @PathVariable String elementId) throws StorageException, IOException, ElementNotFound
    {
        PageBean refPage = getSiteMap(applicationId).findPageByInternalId(pageId);
        for(ElementBean element : refPage.getContent())
        {
            if(element.getId().contentEquals(elementId))
            {
                return element;
            }
        }
        throw new ElementNotFound("Element with id: '"+elementId+"' could not be found.");
    }

    @RequestMapping(value = "/applications", method = RequestMethod.GET)
    public ModelAndView displayApplications(@ModelAttribute("model") ModelMap model) throws IOException, StorageException
    {
        model.addAttribute("applications", getApplications());
        return new ModelAndView( "ApplicationList", model );
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView displayRoot(@ModelAttribute("model") ModelMap model) throws IOException, StorageException
    {
        return displayApplications(model);
    }

    @RequestMapping(value = "/applications/{contentTag}", method = RequestMethod.GET)
    public ModelAndView displayContentDetails(@RequestParam(value="reviewId", defaultValue = "") String reviewId, @PathVariable String contentTag, @ModelAttribute("model") ModelMap model) throws StorageException, IOException
    {
        List<PageBean> pages = new ArrayList<PageBean>();
        for ( PageBean page : getSiteMap(contentTag).getPages() )
        {
            pages.add(page);
            if ( page.hasVariants() )
            {
                pages.addAll(page.getVariants());
            }
        }

        model.addAttribute( "applicationId", contentTag );
        model.addAttribute("pagesCount", pages.size());

        HtmlCanvas canvas = new HtmlCanvas();
        new ApplicationDetailsRenderable(reviewId, pages).renderOn(canvas);
        model.addAttribute( "pages", canvas.toHtml() );


        return new ModelAndView( "ApplicationDetail", model );
    }



    @RequestMapping(value = "/applications/{applicationId}/logs", method = RequestMethod.GET)
    public ModelAndView renderLogs(@PathVariable String applicationId)
    {
        return new ModelAndView( "ApplicationLogs", "applicationId", applicationId );
    }

    @ResponseBody
    @RequestMapping(value = "/applications/{applicationId}/logs", method = RequestMethod.GET, headers="Accept=application/json")
    public String updateExplorationLogs(@PathVariable String applicationId, @RequestParam(value = "fromLine", defaultValue = "0") int fromLine) throws IOException, StorageException
    {
        List<String> logs = new ArrayList();
        if(!this.logs.isKeyUsed(applicationId))
        {
            logs = Arrays.asList(new String[]{"Undefined explorationId: " + applicationId});
        }
        else
        {
            List<String> content = new ArrayList<>();
            content.addAll(Arrays.asList(this.logs.retrieve(applicationId).split("\\n")));
            if(!content.isEmpty())
            {
                while(fromLine>0 && !content.isEmpty())
                {
                    content.remove(0);
                    fromLine --;
                }

                logs.addAll(content);
            }
        }
        return JsonHelper.getInstance().map(logs);
    }

//-- Private methods

    private List<Application> getApplications() throws StorageException, IOException
    {
        List<Application> applications = new ArrayList<>();

        for ( String key : this.applications.retrieveAll().keySet() )
        {
            Application app = new Application();
            app.setTag( key );

            SiteMap sitemap = getSiteMap(key);

            app.setSitemap( sitemap );
            app.setUser( sitemap.getUser() );
            app.setPagesCount( sitemap.getPagesCount() );

            applications.add( app );
        }

        return applications;
    }

    private SiteMap getSiteMap(String applicationId) throws IOException, StorageException
    {
        return SiteMap.fromJson(applications.retrieve(applicationId));
    }


}
