package com.smash.revolance.ui.explorer.helper;

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

import com.smash.revolance.ui.explorer.element.api.Button;
import com.smash.revolance.ui.explorer.element.api.Data;
import com.smash.revolance.ui.explorer.element.api.Element;
import com.smash.revolance.ui.explorer.element.api.Link;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.theInstance;
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

    private static String target = new File(new File("").getAbsoluteFile(), "target").getAbsolutePath();

    @Test
    public void checkUrlAreEquivalent()
    {
        String url1 = "url/";
        String url2 = "url";
        assertThat(UrlHelper.areEquivalent(url1, url1), is(true));

        assertThat(UrlHelper.areEquivalent(url1, url2), is(true));

        url1 = "url/";
        url2 = "url2";
        assertThat(UrlHelper.areEquivalent(url1, url2), is(false));
    }

    @Test
    public void checkUrlInDomain() throws Exception
    {
        // True
        assertThat( BotHelper.rightDomain( "http://google.fr", "http://google.fr/maynotexists/nothingcanbedonehere" ), is(true));

        // Not True
        assertThat(BotHelper.rightDomain("http://google.fr", "http://glagla.fr"), is(false));
    }

    @Test
    public void checkRemoveHash()
    {
        String url = "url#hash";
        assertThat(UrlHelper.getHash(url), is("hash"));
        assertThat(UrlHelper.removeHash(url), is("url"));
    }

    @Test
    public void verifyRelativePath() throws IOException
    {

        File reports = new File(target, "reports");
        reports.mkdirs();
        reports.deleteOnExit();

        File thumbnails = new File(reports, "thumbnails");
        thumbnails.mkdir();
        thumbnails.deleteOnExit();

        File thumbnail = new File(thumbnails, "thumbnail.png");
        thumbnail.createNewFile();
        thumbnail.deleteOnExit();

        File sitemap = new File(reports, "sitemap");
        sitemap.mkdir();
        sitemap.deleteOnExit();

        File sitemapFile = new File(sitemap, "sitemap.svg");
        sitemap.createNewFile();
        sitemap.deleteOnExit();


        String relPath = FileHelper.getRelativePath( sitemapFile, thumbnail );
        assertThat(relPath, is("../../thumbnails/thumbnail.png"));
    }

    @Test
    public void dataElementAdditionShouldKeepClickableContentOverData()
    {
        // Configuration
        Link aLink;

        List<Element> elements = new ArrayList<Element>(  );

        aLink = new Link(  );
        aLink.setPos( new Point( 0, 0 ) );
        aLink.setDim( new Dimension( 100, 30 ) );
        aLink.setText( "Click Here!" );

        elements.add( aLink );

        Data aData;
        aData = new Data(  );
        aData.setPos( new Point( 0, 0 ) );
        aData.setDim( new Dimension( 200, 30 ) );
        aData.setText( "Click Here!" );

        assertThat( elements.size(), is( 1 ) );

        // Action
        Data.handleAddition( elements, aData );

        // Verification
        assertThat( elements.size(), is( 2 ) );
        assertThat( elements.get( 0 ) == aLink, is( true ) );

    }

    @Test
    public void linkElementAdditionShouldKeepClickableContentOverData()
    {
        // Configuration
        List<Element> elements = new ArrayList<Element>(  );

        Data aData;
        aData = new Data(  );
        aData.setPos( new Point( 0, 0 ) );
        aData.setDim( new Dimension( 200, 30 ) );
        aData.setText( "Click Here!" );

        elements.add( aData );

        Link aLink;
        aLink = new Link(  );
        aLink.setPos( new Point( 0, 0 ) );
        aLink.setDim( new Dimension( 100, 30 ) );
        aLink.setText( "Click Here!" );

        assertThat( elements.size(), is( 1 ) );

        // Action
        Element.handleAddition( elements, aLink );

        // Verification
        assertThat( elements.size(), is( 1 ) );
        assertThat( elements.get( 0 ) == aLink, is(true) );

    }

    @Test
    public void buttonElementAdditionShouldKeepClickableContentOverData()
    {
        // Configuration
        List<Element> elements = new ArrayList<Element>(  );

        Button aButton;
        aButton = new Button(  );
        aButton.setPos( new Point( 0, 0 ) );
        aButton.setDim( new Dimension( 200, 30 ) );
        aButton.setText( "Click Here!" );

        elements.add( aButton );

        Data aData;
        aData = new Data(  );
        aData.setPos( new Point( 0, 0 ) );
        aData.setDim( new Dimension( 200, 30 ) );
        aData.setText( "Click Here!" );

        assertThat( elements.size(), is( 1 ) );

        // Action
        Element.handleAddition( elements, aData );

        // Verification
        assertThat( elements.size(), is( 1 ) );
        assertThat( elements.get( 0 ) == aButton, is(true) );

    }

    @Test
    public void dataElementAdditionShouldKeepTheSmallestPieceOfData()
    {
        // Step1

        // Configuration
        List<Element> elements = new ArrayList<Element>(  );

        Data aData;
        aData = new Data(  );
        aData.setPos( new Point( 0, 0 ) );
        aData.setDim( new Dimension( 200, 30 ) );
        aData.setText( "Click Here!" );

        elements.add( aData );

        Data aBloc = new Data(  );
        aBloc.setPos( new Point( 0, 0 ) );
        aBloc.setDim( new Dimension( 200, 300 ) );
        aBloc.setText( "Click Here!" );

        // Configuration checkup
        assertThat( elements.size(), is( 1 ) );

        // Action
        Element.handleAddition( elements, aBloc );

        // Verification
        assertThat( elements.size(), is( 1 ) );
        assertThat( elements.get( 0 ) == aData, is(true) );

        // Step2
        // Configuration
        Button aButton;
        aButton = new Button(  );
        aButton.setPos( new Point( 10, 10 ) );
        aButton.setDim( new Dimension( 80, 10 ) );
        aButton.setText( "Click Here!" );

        // Action
        Element.handleAddition( elements, aButton );

        // Verfications
        assertThat( elements.size(), is( 1 ) );
        assertThat( elements.get( 0 ) == aButton, is(true) );

        // Step3
        // Configuration
        aData = new Data(  );
        aData.setPos( new Point( 0, 0 ) );
        aData.setDim( new Dimension( 200, 30 ) );
        aData.setText( "Click Here!" );

        // Action
        Element.handleAddition( elements, aData );

        // Verifications
        assertThat( elements.size(), is( 1 ) );
        assertThat( elements.get( 0 ) == aButton, is(true) );
    }

    @Test
    public void retrieveElementIncludedInABloc()
    {
        // Configuration
        List<Element> elements = new ArrayList<Element>(  );

        Data aBloc = new Data(  );
        aBloc.setPos( new Point( 0, 0 ) );
        aBloc.setDim( new Dimension( 200, 300 ) );
        aBloc.setText( "Button\nLink" );

        Button aButton = new Button(  );
        aButton.setPos( new Point( 10, 10 ) );
        aButton.setDim( new Dimension( 80, 10 ) );
        aButton.setText( "Button" );

        Element.handleAddition( elements, aButton );

        Link aLink = new Link(  );
        aLink.setPos( new Point( 200, 10 ) );
        aLink.setDim( new Dimension( 80, 10 ) );
        aLink.setText( "Link" );

        Element.handleAddition( elements, aLink );

        // Configuration checkup
        assertThat( elements.size(), is( 2 ) );

        // Action
        List<Element> elementsInBloc = Element.getElementsIn( elements, aBloc );

        // Verfications
        assertThat( elementsInBloc.size(), is(1) );
    }


    @Test
    public void filterElementIncludedInEachOther()
    {
        // Configuration
        List<Element> elements = new ArrayList<Element>(  );

        Data aBloc = new Data(  );
        aBloc.setPos( new Point( 0, 0 ) );
        aBloc.setDim( new Dimension( 200, 300 ) );
        aBloc.setText( "Button\nLink" );

        Element.handleAddition( elements, aBloc );

        Button aButton;
        aButton = new Button(  );
        aButton.setPos( new Point( 10, 10 ) );
        aButton.setDim( new Dimension( 100, 10 ) );
        aButton.setText( "Button" );

        Element.handleAddition( elements, aButton );

        Link aLink = new Link(  );
        aLink.setPos( new Point( 10, 30 ) );
        aLink.setDim( new Dimension( 100, 10 ) );
        aLink.setText( "Link" );

        Element.handleAddition( elements, aLink );

        // Configuration checkup
        assertThat( elements.size(), is( 3 ) );

        // Action
        Element.filterElementsIncludedInEachOthers( elements, 1000, 1 );

        // Verficiations
        assertThat( elements.size(), is(2) );
        assertThat( elements.contains( aLink ), is(true) );
        assertThat( elements.contains( aButton ), is(true) );
    }

}
