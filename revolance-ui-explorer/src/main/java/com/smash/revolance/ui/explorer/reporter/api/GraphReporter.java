package com.smash.revolance.ui.explorer.reporter.api;

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

import com.smash.revolance.ui.explorer.sitemap.SiteMap;
import com.smash.revolance.ui.explorer.user.User;
import org.kohsuke.graphviz.Graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Arrays;

/**
 * User: wsmash
 * Date: 20/02/13
 * Time: 10:47
 */
public class GraphReporter
{
    private final User user;

    public GraphReporter(User user)
    {
        this.user = user;
    }

    public SiteMap getSiteMap()
    {
        return user.getSiteMap();
    }

    public void doGraphReport(String graphTitle, File dotFile, File imgFile)
    {
        try
        {
            Graph graph = getSiteMap().convertToGraph(graphTitle);
            if(dotFile.exists())
            {
                dotFile.delete();
            }
            dotFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(dotFile);
            graph.writeTo(fos);

            if(imgFile.exists())
            {
                imgFile.delete();
            }
            imgFile.createNewFile();


            String rendering = "svg";
            String[] commands = new String[]{"twopi", "-o" + imgFile.toString(), "-T" + rendering};
            graph.generateTo(Arrays.asList(commands), imgFile);
        }
        catch(FileNotFoundException e)
        {
            // Should not happen since created in the above
        }
        catch (InterruptedException e)
        {
            System.err.println( e );
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
