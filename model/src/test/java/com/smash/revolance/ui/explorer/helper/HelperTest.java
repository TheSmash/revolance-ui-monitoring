package com.smash.revolance.ui.explorer.helper;

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

import com.smash.revolance.ui.model.helper.BotHelper;
import com.smash.revolance.ui.model.helper.FileHelper;
import com.smash.revolance.ui.model.helper.UrlHelper;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * User: wsmash
 * Date: 25/02/13
 * Time: 11:13
 */
@RunWith(JUnit4ClassRunner.class)
public class HelperTest
{

    private static String target = new File( new File( "" ).getAbsoluteFile(), "target" ).getAbsolutePath();

    @Test
    public void checkUrlAreEquivalent()
    {
        String url1 = "url/";
        String url2 = "url";
        assertThat( UrlHelper.areEquivalent( url1, url1 ), is( true ) );

        assertThat( UrlHelper.areEquivalent( url1, url2 ), is( true ) );

        url1 = "url/";
        url2 = "url2";
        assertThat( UrlHelper.areEquivalent( url1, url2 ), is( false ) );

        url1 = "http://www.google.fr";
        url2 = "https://www.google.fr";

        assertThat( UrlHelper.areEquivalent( url1, url2 ), is( true ) );
    }

    @Test
    public void checkUrlInDomain() throws Exception
    {
        // True
        assertThat( BotHelper.rightDomain( "http://google.fr", "http://google.fr/maynotexists/nothingcanbedonehere" ), is( true ) );

        // Not True
        assertThat( BotHelper.rightDomain( "http://google.fr", "http://glagla.fr" ), is( false ) );
    }

    @Test
    public void checkRemoveHash()
    {
        String url = "url#hash";
        assertThat( UrlHelper.getHash( url ), is( "hash" ) );
        assertThat( UrlHelper.removeHash( url ), is( "url" ) );
    }

    @Test
    public void verifyRelativePath() throws IOException
    {

        File reports = new File( target, "reports" );
        reports.mkdirs();
        reports.deleteOnExit();

        File thumbnails = new File( reports, "thumbnails" );
        thumbnails.mkdir();
        thumbnails.deleteOnExit();

        File thumbnail = new File( thumbnails, "thumbnail.png" );
        thumbnail.createNewFile();
        thumbnail.deleteOnExit();

        File sitemap = new File( reports, "sitemap" );
        sitemap.mkdir();
        sitemap.deleteOnExit();

        File sitemapFile = new File( sitemap, "sitemap.svg" );
        sitemap.createNewFile();
        sitemap.deleteOnExit();


        String relPath = FileHelper.getRelativePath( sitemapFile, thumbnail );
        assertThat( relPath, is( "../../thumbnails/thumbnail.png" ) );
    }

}
