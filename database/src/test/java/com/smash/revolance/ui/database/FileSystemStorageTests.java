package com.smash.revolance.ui.database;

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
    private static File target = new File( new File( "" ).getAbsoluteFile(), "target" );
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
