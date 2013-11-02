package com.smash.revolance.ui.model.helper;

/*
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * For the Free Code & Passion they provided on Revolance Ui-Model,
 *   RevoLance thanks the following contributors:
 * 
 *                     ~ Smash Williams
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

import com.smash.revolance.ui.model.bot.BrowserFactory;

/**
 * User: wsmash
 * Date: 19/10/13
 * Time: 12:08
 */
public enum BrowserType
{
    Chrome, Firefox, IE, MockedWebDriver;

    public static BrowserType fromString(String browserType) throws BrowserFactory.InstanciationError
    {
        for ( BrowserType browser : BrowserType.values() )
        {
            if ( String.valueOf( browser ).toLowerCase().contentEquals( browserType.trim().toLowerCase() ) )
            {
                return browser;
            }
        }
        throw new BrowserFactory.InstanciationError( "Undefined browser type: '" + browserType + "'." );
    }
}
