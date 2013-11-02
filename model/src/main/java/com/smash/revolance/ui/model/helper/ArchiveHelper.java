package com.smash.revolance.ui.model.helper;

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

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * User: wsmash
 * Date: 03/05/13
 * Time: 08:15
 */
public class ArchiveHelper
{

    public static File buildArchive(File archive, File... files) throws FileNotFoundException
    {
        FileOutputStream fos = new FileOutputStream( archive );
        ZipOutputStream zos = new ZipOutputStream( fos );
        int bytesRead;
        byte[] buffer = new byte[1024];
        CRC32 crc = new CRC32();

        for ( File file : files )
        {
            if ( !file.exists() )
            {
                System.err.println( "Skipping: " + file );
                continue;
            }
            BufferedInputStream bis = null;
            try
            {
                bis = new BufferedInputStream( new FileInputStream( file ) );
                crc.reset();
                while ( ( bytesRead = bis.read( buffer ) ) != -1 )
                {
                    crc.update( buffer, 0, bytesRead );
                }

                bis.close();

                // Reset to beginning of input stream
                bis = new BufferedInputStream( new FileInputStream( file ) );
                String entryPath = FileHelper.getRelativePath( archive.getParentFile(), file );

                ZipEntry entry = new ZipEntry( entryPath );
                entry.setMethod( ZipEntry.STORED );
                entry.setCompressedSize( file.length() );
                entry.setSize( file.length() );
                entry.setCrc( crc.getValue() );
                zos.putNextEntry( entry );
                while ( ( bytesRead = bis.read( buffer ) ) != -1 )
                {
                    zos.write( buffer, 0, bytesRead );
                }
            }
            catch (FileNotFoundException e)
            {

            }
            catch (IOException e)
            {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            finally
            {
                IOUtils.closeQuietly( bis );
            }
        }
        IOUtils.closeQuietly( zos );
        return archive;
    }
}
