package com.smash.revolance.ui.server.model;

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

import com.smash.revolance.ui.model.sitemap.SiteMap;
import com.smash.revolance.ui.model.user.UserBean;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: wsmash
 * Date: 08/06/13
 * Time: 11:16
 */
public class Application
{
    @NotEmpty
    String date;

    @NotEmpty
    String tag;

    SiteMap sitemap;

    MultipartFile file;
    private UserBean user;

    public Application()
    {
        date = new SimpleDateFormat().format( new Date() );
    }

    public MultipartFile getFile()
    {
        return file;
    }

    public void setFile(MultipartFile file)
    {
        this.file = file;
    }

    public void setTag(String tag)
    {
        this.tag = tag;
    }

    public void setSitemap(SiteMap sitemap)
    {
        this.sitemap = sitemap;
    }

    public String getTag()
    {
        return tag;
    }

    public String getDate()
    {
        return date;
    }

    public String getContent() throws IOException
    {
        return new String( getFile().getBytes() );
    }

    public void setUser(UserBean user)
    {
        this.user = user;
    }

    public UserBean getUser()
    {
        return user;
    }
}
