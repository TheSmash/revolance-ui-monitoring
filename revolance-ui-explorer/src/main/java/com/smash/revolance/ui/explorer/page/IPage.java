package com.smash.revolance.ui.explorer.page;

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

import com.smash.revolance.ui.explorer.application.Application;

import com.smash.revolance.ui.explorer.element.IElement;
import com.smash.revolance.ui.explorer.element.api.ElementBean;
import com.smash.revolance.ui.explorer.page.api.Page;
import com.smash.revolance.ui.explorer.page.api.PageBean;
import com.smash.revolance.ui.explorer.sitemap.SiteMap;
import com.smash.revolance.ui.explorer.user.User;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * User: wsmash
 * Date: 30/11/12
 * Time: 17:04
 */
public interface IPage
{

    IElement getSource();

    String getUrl();

    void explore() throws Exception;

    boolean hasBeenBrowsed();

    List<IElement> getLinks() throws Exception;

    String getTitle();

    boolean isBroken();

    boolean hasBrokenLinks() throws Exception;

    List<IElement> getBrokenLinks() throws Exception;

    boolean isHomePage();

    void setBroken(boolean b);

    void setTitle(String title);

    void setBrowsed(boolean b);

    User getUser();

    SiteMap getUserSitemap();

    List<IElement> getContent() throws Exception;

    List<IElement> getClickableContent() throws Exception;

    Application getApplication() throws Exception;

    List<IElement> getButtons() throws Exception;

    boolean isOriginal();

    Page getOriginal();

    SiteMap getSiteMap();

    void setAuthorized(boolean b);

    String takeScreenShot() throws Exception;

    BufferedImage getImage() throws Exception;

    PageBean getVariant(ElementBean source, boolean generate) throws Exception;

    PageBean getVariant(IElement clickable, String hash, boolean generate) throws Exception;

    Collection<PageBean> getVariants();

    void setUrl(String currentUrl);

    void setId(String id);

    void delete() throws Exception;

    PageBean findVariantByHash(String hash);

    PageBean findVariantByCaption(String caption) throws Exception;

    PageBean findVariantBySource(ElementBean source) throws Exception;

    void setSource(IElement source);

    PageBean getBean();

    void setCaption(String img) throws IOException;

    void setImage(BufferedImage img) throws Exception;

    String getCaption();

    boolean hasBeenExplored();

    boolean hasBeenParsed();

    void setParsed(boolean b);

    void setExplored(boolean b);

    void addVariant(PageBean variant);
}
