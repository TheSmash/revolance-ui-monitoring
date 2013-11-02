package com.smash.revolance.ui.database;

import java.io.IOException;

/**
 * User: wsmash
 * Date: 08/06/13
 * Time: 08:17
 */
public class StorageException extends Exception
{
    public StorageException(String s)
    {
        super( s );
    }

    public StorageException(String s, IOException e)
    {
        super( s );
        super.initCause( e.getCause() );
    }
}
