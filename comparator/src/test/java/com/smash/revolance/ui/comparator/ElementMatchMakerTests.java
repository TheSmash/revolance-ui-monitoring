package com.smash.revolance.ui.comparator;

/*
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * ui-monitoring-comparator
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

import com.smash.revolance.ui.comparator.element.ElementMatch;
import com.smash.revolance.ui.comparator.element.ElementMatchMaker;
import com.smash.revolance.ui.comparator.element.ElementSearchMethod;
import com.smash.revolance.ui.comparator.element.IElementMatchMaker;
import com.smash.revolance.ui.model.element.api.ElementBean;
import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * User: wsmash
 * Date: 21/09/13
 * Time: 18:18
 */
public class ElementMatchMakerTests
{
    private List<ElementBean> elements = new ArrayList<ElementBean>();
    private ElementMatch match;

    @After
    public void teardownTests()
    {
        elements.clear();
    }

    private static ElementBean buildElement(String impl, String txt, int x, int y, int w, int h, String caption)
    {
        ElementBean element = new ElementBean();

        element.setImpl( impl );
        element.setText( txt );
        element.setPos( new Point( x, y ) );
        element.setDim( new Dimension( w, h ) );
        element.setCaption( caption );

        return element;
    }

    @Test(expected = NoMatchFound.class)
    public void compareElementShouldRaiseExceptionIfNoElementsToBeMatchedAgainst() throws NoMatchFound
    {
        IElementMatchMaker matcher = new ElementMatchMaker();
        matcher.findMatch( new ArrayList<ElementBean>(), new ElementBean() );
    }

    @Test
    public void compareElementShouldFindCorrespondingElementFromText() throws NoMatchFound
    {
        ArrayList<ElementBean> elements = new ArrayList<ElementBean>();
        ElementBean matchingElement = buildElement( "Data", "Some text", 0, 0, 300, 30, "" );
        elements.add( matchingElement );

        ElementBean referenceElement = buildElement( "Data", "Some text", 0, 0, 0, 0, "" );
        IElementMatchMaker matcher = new ElementMatchMaker();
        match = matcher.getBestMatch( matcher.findMatch( elements, referenceElement, ElementSearchMethod.VALUE ) );

        assertThat( match.getMatch(), is( matchingElement ) );
        assertThat( match.getReference(), is( referenceElement ) );
    }

    @Test(expected = NoMatchFound.class)
    public void compareElementShouldRaiseExceptionWhenElementCouldNotBeFoundFromText() throws NoMatchFound
    {
        ArrayList<ElementBean> elements = new ArrayList<ElementBean>();
        ElementBean matchingElement = buildElement( "Data", "Some text", 0, 0, 300, 30, "" );
        elements.add( matchingElement );

        IElementMatchMaker matcher = new ElementMatchMaker();
        match = matcher.getBestMatch( matcher.findMatch( elements, buildElement( "Data", "Some other text", 0, 0, 0, 0, "" ), ElementSearchMethod.VALUE ) );
    }

}
