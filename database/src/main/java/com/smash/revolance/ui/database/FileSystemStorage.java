package com.smash.revolance.ui.database;

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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * User: wsmash
 * Date: 14/09/13
 * Time: 17:36
 */
public class FileSystemStorage implements IStorage
{
    File databaseFolder;

    public FileSystemStorage()
    {
        databaseFolder = new File( new File( "" ).getAbsolutePath(), "revolance/ui/storage" );
        createStorageDirectory();
    }

    public FileSystemStorage(File root)
    {
        if ( root != null )
        {
            databaseFolder = root;
            createStorageDirectory();
        }
    }

    private void createStorageDirectory()
    {
        if ( !databaseFolder.exists() )
        {
            databaseFolder.getParentFile().mkdirs();
            databaseFolder.mkdir();
        }
    }

    @Override
    public void store(String k, String o) throws StorageException
    {
        if ( isKeyValid( k ) && !isKeyUsed( k ) )
        {
            FileWriter writer = null;
            File f = new File( databaseFolder, k );
            try
            {
                f.createNewFile();
                writer = new FileWriter( f );
                writer.write( o );

                writer.close();
            }
            catch (IOException e)
            {
                throw new StorageException( "Unable to create file: " + f + " or to write data into it." );
            }
            finally
            {
                IOUtils.closeQuietly( writer );
            }
        } else
        {
            throw new StorageException( "Invalid or already used key: '" + k + "'." );
        }
    }

    @Override
    public String retrieve(String key) throws StorageException
    {
        if ( isKeyValid( key ) && isKeyUsed( key ) )
        {
            try
            {
                return FileUtils.readFileToString( new File( databaseFolder, key ), "UTF-8" );
            }
            catch (IOException e)
            {
                throw new StorageException( "Unable to retrieve stored content for key: " + key );
            }
        } else
        {
            throw new StorageException( "Invalid or unused key: " + key );
        }
    }

    @Override
    public Map<String, String> retrieveAll()
    {
        Map<String, String> filesContent = new HashMap<String, String>();

        for ( File file : databaseFolder.listFiles() )
        {
            try
            {
                filesContent.put( file.getName(), FileUtils.readFileToString( file ) );
            }
            catch (IOException e)
            {
                //TODO: log the exception but do not prevent moving on the loop
            }
        }

        return filesContent;
    }

    @Override
    public Collection<String> getKeys()
    {
        List<String> keys = new ArrayList<String>();

        Collection<File> files = FileUtils.listFiles( databaseFolder, new String[]{}, false );

        for ( File file : files )
        {
            keys.add( file.getName() );
        }

        return keys;

    }

    @Override
    public boolean isKeyUsed(String key)
    {
        return new File( databaseFolder, key ).exists();
    }

    @Override
    public boolean isKeyValid(String key)
    {
        return !key.contains( "#" ) || !key.contains( "/" ) || !key.contains( "." ) || !key.contains( "-" ) || !key.contains( "_" );
    }

    @Override
    public void clear() throws StorageException
    {
        try
        {
            FileUtils.forceDelete( databaseFolder );
            databaseFolder.mkdirs();
        }
        catch (IOException e)
        {
            throw new StorageException( "Unable to clear storage system.", e );
        }
    }

    @Override
    public String delete(String key) throws StorageException
    {
        if ( isKeyValid( key ) && isKeyUsed( key ) )
        {
            try
            {
                String content = retrieve( key );
                FileUtils.forceDelete( new File( databaseFolder, key ) );
                return content;
            }
            catch (IOException e)
            {
                throw new StorageException( e.getMessage() );
            }
        } else
        {
            throw new StorageException( "Invalid or unknown key: '" + key + "'." );
        }
    }
}
