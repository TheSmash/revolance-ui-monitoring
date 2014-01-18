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

    int pagesCount = 0;

    MultipartFile file;

    private UserBean user;
    private int revision;
    private int progress;

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

    public void setPagesCount(int pagesCount)
    {
        this.pagesCount = pagesCount;
    }

    public int getPagesCount()
    {
        return this.pagesCount;
    }

    public void setRevision(int revision)
    {
        this.revision = revision;
    }

    public int getRevision()
    {
        return revision;
    }

    public void setProgress(int progress)
    {
        this.progress = progress;
    }

    public int getProgress()
    {
        return progress;
    }
}
