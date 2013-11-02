package com.smash.revolance.ui.database;

import java.io.File;
import java.util.Collection;
import java.util.Map;

/**
 * User: wsmash
 * Date: 08/06/13
 * Time: 08:13
 */
public interface IStorage
{
    void store(String key, String object) throws StorageException;

    String retrieve(String key) throws StorageException;

    String delete(String key) throws StorageException;

    Map<String, String> retrieveAll();

    Collection<String> getKeys();

    boolean isKeyUsed(String key);

    boolean isKeyValid(String key);

    void clear() throws StorageException;

    File getLocation();
}
