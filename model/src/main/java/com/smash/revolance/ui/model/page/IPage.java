package com.smash.revolance.ui.model.page;

/*
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Revolance-UI-Model
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2012 - 2013 RevoLance
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

import com.smash.revolance.ui.model.application.Application;
import com.smash.revolance.ui.model.element.api.Element;
import com.smash.revolance.ui.model.page.api.Page;
import com.smash.revolance.ui.model.sitemap.SiteMap;
import com.smash.revolance.ui.model.user.User;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * User: wsmash
 * Date: 30/11/12
 * Time: 17:04
 */
public interface IPage
{

    User getUser();

    String getId();

    String getUrl();

    String getTitle();

    Element getSource();

    boolean hasBeenExplored();

    boolean hasBeenParsed();

    List<Element> getLinks() throws Exception;

    boolean isBroken();

    List<Element> getBrokenLinks() throws Exception;

    List<Element> getContent() throws Exception;

    List<Element> getClickableContent() throws Exception;

    Application getApplication() throws Exception;

    List<Element> getButtons() throws Exception;

    boolean isOriginal();

    Page getOriginal();

    SiteMap getSiteMap();

    BufferedImage getImage() throws Exception;

    List<Page> getVariants();

    void delete() throws Exception;

    String getCaption();

    boolean isExternal();

    boolean isLogin();

    void addMetaInf(String k, String v);
}
