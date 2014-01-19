package com.smash.revolance.ui.database;

/*
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * ui-monitoring-database
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

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * User: wsmash
 * Date: 14/09/13
 * Time: 17:36
 */
public class FileSystemStorageTests
{
    private static String dbRoot = "db";

    private static IStorage storage;

    @BeforeClass
    public static void beforeTests() throws StorageException
    {
        storage = new FileSystemStorage( dbRoot );
        storage.clear();

        assertThat( storage.getKeys().isEmpty(), is( true ) );
    }

    @Test
    public void storageShouldStoreDataInFileSystem() throws StorageException, IOException
    {
        storage.store( "key", "data" );
        String content = FileUtils.readFileToString( new File( dbRoot, "key" ) );

        assertThat( content, is( "data" ) );
    }

    @Test
    public void storageShouldHandleFileAddedDirectlyInTheRootFolder() throws IOException, StorageException
    {
        File file = new File( dbRoot, "key" );

        // Create the file since it does not exists yet!
        FileUtils.writeStringToFile( file, "data" );

        String content = storage.retrieve( "key" );

        assertThat( content, is( "data" ) );
    }

    @Test(expected = StorageException.class)
    public void storageShouldHandleDeletion() throws IOException, StorageException
    {
        storage.store( "key", "data" );
        assertThat( storage.isKeyUsed( "key" ), is( true ) );

        storage.delete( "key" );
        assertThat( storage.isKeyUsed( "key" ), is( false ) );

        storage.retrieve( "key" );
    }

}
