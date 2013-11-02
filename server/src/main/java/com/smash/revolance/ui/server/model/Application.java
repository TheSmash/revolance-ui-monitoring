package com.smash.revolance.ui.server.model;

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
