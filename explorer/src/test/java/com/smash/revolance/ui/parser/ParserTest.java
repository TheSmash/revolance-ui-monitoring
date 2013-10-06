package com.smash.revolance.ui.parser;

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

import com.smash.revolance.ui.explorer.UserExplorer;
import com.smash.revolance.ui.model.page.api.Page;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * User: wsmash
 * Date: 14/09/13
 * Time: 23:44
 */
public class ParserTest extends BaseTests
{
    private static Page page;

    @BeforeClass
    public static void setupTest() throws Exception
    {
        setUp();
        user.setDomain( REF_PAGE_HOME );
        user.setHome( REF_PAGE_HOME );

        new UserExplorer( user ).explore();

        bot = user.getBot();
        browser = bot.getBrowser();
        sitemap = user.getSiteMap();

        page = sitemap.findPage( REF_PAGE_HOME ).getInstance();

        user.doContentReport( new File( "target/sitemap.json" ) );

    }

    @After
    public void tearDown() throws Exception
    {
        user.getBrowser().quit();
    }

    @Test
    public void botShouldGrabImages() throws Exception
    {
        assertThat( page.getImages().size(), is( 3 ) );
    }

    @Test
    public void botShouldGrabData() throws Exception
    {

        assertThat( page.getData( "Your Company" ), notNullValue() );
        assertThat( page.getData( "Latest News" ), notNullValue() );
        assertThat( page.getData( "WELCOME" ), notNullValue() );
        assertThat( page.getDatas( "00.00.0000" ).size(), is( 3 ) );

        assertThat( page.getData( "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Phasellus ornare condimentum sem. Nullam a eros. Vivamus vestibulum hendrerit arcu. Integer a orci. Morbi nonummy semper est. Donec at risus sed velit porta." ), notNullValue() );
        assertThat( page.getData( "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Phasellus ornare condimentum sem." ), notNullValue() );
        assertThat( page.getData( "Nullam a eros. Vivamus vestibulum hendrerit arcu. Integer a orci. Morbi nonummy semper est." ), notNullValue() );
        assertThat( page.getData( "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Phasellus ornare condimentum sem. Nullam a eros. Vivamus vestibulum hendrerit arcu. Integer a orci. Morbi nonummy semper est. Donec at risus sed velit porta dictum. Maecenas condimentum orci aliquam nunc. Morbi nonummy tellus in nibh. Suspendisse orci eros, dapibus at, ultrices at, egestas ac, tortor. Suspendisse fringilla est id erat. Praesent et libero." ), notNullValue() );

        assertThat( page.getData( "Donec at risus sed velit porta dictum. Maecenas condimentum orci aliquam nunc." ), notNullValue() );
        assertThat( page.getData( "Contact Info" ), notNullValue() );
        assertThat( page.getData( "About us" ), notNullValue() );
        assertThat( page.getData( "00 / 00Lorem ipsum dolor sit amet, consectetuer adipiscing elit." ), notNullValue() );

        assertThat( page.getData( "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Phasellus ornare condimentum sem. Nullam a eros. Vivamus vestibulum hendrerit arcu. Integer a orci. Morbi nonummy semper est. Donec at risus sed velit porta dictum. Maecenas condimentum orci aliquam nunc. Morbi nonummy tellus in nibh. Suspendisse orci eros, dapibus at, ultrices at, egestas ac, tortor. Suspendisse fringilla est id erat. Praesent et libero. Proin nisi felis, euismod a, tempus varius, elementum vel, nisl. Fusce non magna sit amet enim suscipit feugiat." ), notNullValue() );
        assertThat( page.getData( "E-mail: abc@Lorem ipsum.com" ), notNullValue() );
        assertThat( page.getData( "Fax:      000-000-0000" ), notNullValue() );
        assertThat( page.getData( "Phone: 000-000-0000 /\\n              000-000-0000" ), notNullValue() );

        assertThat( page.getData( "Fusce et leo. Maecenas massa libero, egestas sed, ultricies tempor, laoreet eget, nisl. Nunc eleifend, orci eu aliquet consequat, metus diam suscipit felis, in adipiscing sapien nisl vitae ipsum. Nunc dui ante, vestibulum eget, viverra sed, ullamcorper quis, est. Nullam varius nunc." ), notNullValue() );

    }

    @Test
    public void botShouldGrabLinks() throws Exception
    {
        assertThat( page.getLinks( "Home" ).size(), is( 2 ) );
        assertThat( page.getLinks( "About Us" ).size(), is( 2 ) );

        assertThat( page.getLinks( "Recent articles" ), notNullValue() );
        assertThat( page.getLinks( "Email" ), notNullValue() );
        assertThat( page.getLinks( "Resources" ), notNullValue() );
        assertThat( page.getLinks( "Links" ), notNullValue() );

        assertThat( page.getLink( "Contacts" ), notNullValue() );
        assertThat( page.getLink( "Services" ), notNullValue() );
        assertThat( page.getLink( "Pricing" ), notNullValue() );

        assertThat( page.getLinks( "htmltemplates.net" ), notNullValue() );
    }

    @Test
    public void botShouldGrabButtons()
    {

    }

    @Test
    public void botShouldGrabFields()
    {
        // assertThat( page.getFields().size(), is( 2 ) );
    }

    /*
    @Test
    public void imageElementAdditionShouldKeepImageAndData()
    {
        // Configuration
        Image anImage;

        List<Element> elements = new ArrayList<Element>();

        anImage = new Image(  );
        anImage.setPos( new Point( 0, 0 ) );
        anImage.setDim( new Dimension( 100, 30 ) );

        elements.add( anImage );

        Data aData;
        aData = new Data(  );
        aData.setPos( new Point( 0, 0 ) );
        aData.setDim( new Dimension( 50, 10 ) );
        aData.setText( "Click Here!" );

        assertThat( elements.size(), is( 1 ) );

        // Action
        PageParser.handleAddition( elements, aData );

        // Verification
        assertThat( elements.size(), is( 2 ) );

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
        PageParser.handleAddition( elements, aData );

        // Verification
        assertThat( elements.size(), is( 1 ) );
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
        PageParser.handleAddition( elements, aLink );

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
        PageParser.handleAddition( elements, aData );

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
        PageParser.handleAddition( elements, aBloc );

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
        PageParser.handleAddition( elements, aButton );

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
        PageParser.handleAddition( elements, aData );

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

        PageParser.handleAddition( elements, aButton );

        Link aLink = new Link(  );
        aLink.setPos( new Point( 200, 10 ) );
        aLink.setDim( new Dimension( 80, 10 ) );
        aLink.setText( "Link" );

        PageParser.handleAddition( elements, aLink );

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

        PageParser.handleAddition( elements, aBloc );

        Button aButton;
        aButton = new Button(  );
        aButton.setPos( new Point( 10, 10 ) );
        aButton.setDim( new Dimension( 100, 10 ) );
        aButton.setText( "Button" );

        PageParser.handleAddition( elements, aButton );

        Link aLink = new Link(  );
        aLink.setPos( new Point( 10, 30 ) );
        aLink.setDim( new Dimension( 100, 10 ) );
        aLink.setText( "Link" );

        PageParser.handleAddition( elements, aLink );

        // Configuration checkup
        assertThat( elements.size(), is( 2 ) );

        // Action
        Element.filterElementsIncludedInEachOthers( elements, 1000, 1 );

        // Verficiations
        assertThat( elements.size(), is(2) );
        assertThat( elements.contains( aLink ), is(true) );
        assertThat( elements.contains( aButton ), is(true) );
    }
    */
}
