package com.smash.revolance.ui.model.sitemap;

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

import com.smash.revolance.ui.materials.JsonHelper;
import com.smash.revolance.ui.model.element.api.ElementBean;
import com.smash.revolance.ui.model.helper.BotHelper;
import com.smash.revolance.ui.model.helper.UrlHelper;
import com.smash.revolance.ui.model.page.IPage;
import com.smash.revolance.ui.model.page.api.Page;
import com.smash.revolance.ui.model.page.api.PageBean;
import com.smash.revolance.ui.model.user.UserBean;
import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.kohsuke.graphviz.Edge;
import org.kohsuke.graphviz.Graph;
import org.kohsuke.graphviz.Node;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * User: wsmash
 * Date: 20/02/13
 * Time: 10:43
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
                getterVisibility = JsonAutoDetect.Visibility.NONE,
                isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class SiteMap
{
    private long date = 0;

    private HashMap<String, PageBean> sitemap = null;

    @JsonIgnore
    private static final String RED   = "#FF0000";

    @JsonIgnore
    private static final String BLUE  = "#0000FF";

    @JsonIgnore
    private static final String GRAY  = "#222222";

    @JsonIgnore
    private static final String GREEN = "#00FF00";

    @JsonIgnore
    private static final String BLACK = "#000000";

    private UserBean user;

    /*
    public File getSitemapDotFile() throws IOException
    {
        String sitemapDotFileName = "sitemap.dot";
        return new File( user.getSitemapFolder(), sitemapDotFileName );
    }

    public File getSitemapImgFile() throws IOException
    {
        String sitemapImgFileName = "sitemap.svg";
        return new File( user.getSitemapFolder(), sitemapImgFileName );
    }
    */

    private SiteMap()
    {
        sitemap = new HashMap();
    }

    public SiteMap(UserBean user)
    {
        this();
        setUser( user );
    }

    private void setUser(UserBean user)
    {
        this.user = user;
    }

    /**
     * Retrieve all the original pages of the application
     *
     * @return
     */
    public Collection<PageBean> getPages()
    {
        return sitemap.values();
    }

    /**
     * Retrieve the number of pages in the sitemap
     */
    public int getPagesCount()
    {
        return getPages().size();
    }

    /**
     * Retrieve a page. Either it's an original if first time asked for this page
     * Or a variant of the original page when the url hasn't changed.
     *
     * @return
     * @throws Exception
     */
    public PageBean getPage(String url, boolean generate) throws Exception
    {
        PageBean page = findPageByUrl( url );
        if ( page == null )
        {
            if ( generate )
            {
                page = Page.createPage( getUser().getInstance(), url, getUser().getInstance().getBot().getCurrentTitle() );
            }
        }
        return page;
    }

    /**
     * Only register pages that are original and not variant
     *
     * @param page
     * @throws Exception
     */
    public void addPage(Page page)
    {
        if ( page.isOriginal() )
        {
            sitemap.put( page.getUrl(), page.getBean() );
        }
    }

    public PageBean findPageByUrl(String url)
    {
        if ( sitemap.isEmpty() )
        {
            return null;
        } else
        {
            PageBean page = getPage( url );
            if ( page != null )
            {
                if ( UrlHelper.containsHash( url ) )
                {
                    String hash = UrlHelper.getHash( url );
                    return page.getInstance().findVariantByHash( hash );
                }
            }
            return page;
        }
    }

    private PageBean getPage(String url)
    {
        PageBean page = null;
        for ( String urlRegistered : sitemap.keySet() )
        {
            if ( UrlHelper.areEquivalent(urlRegistered, url) )
            {
                return sitemap.get( urlRegistered );
            }
        }
        return page;
    }

    public List<PageBean> getBrokenPages()
    {
        List<PageBean> brokenPages = new ArrayList<PageBean>();
        for ( PageBean page : getPages() )
        {
            if ( page.isBroken() )
            {
                brokenPages.add( page );
            }
        }
        return brokenPages;
    }

    public List<String> getBrokenLinks()
    {
        List<String> brokenLinks = new ArrayList<String>();
        for ( PageBean page : getBrokenPages() )
        {
            brokenLinks.add( page.getSource().getContent() );
        }
        return brokenLinks;
    }

    public Graph convertToGraph(String title) throws Exception
    {

        Graph graph = new Graph();

        graph.attr( "label", title );
        graph.attr( "mindist", "15" );
        graph.attr( "ratio", "auto" );
        graph.attr( "overlap", "false" );
        graph.attr( "ranksep", "15" );
        graph.attr( "weight", "2" );
        graph.attr( "splines", "true" );
        graph.attr( "margin", "10" );

        List<Edge> edges = new ArrayList<Edge>();
        Map<String, Node> nodes = new HashMap<String, Node>();

        for ( PageBean page : getPages() )
        {
            Node node = createNode( page );
            graph.node( node );
            nodes.put( page.getUrl(), node );
        }

        for ( PageBean page : getPages() )
        {
            for ( ElementBean link : page.getLinks() )
            {
                Edge edge = new Edge( nodes.get( page.getUrl() ), nodes.get( link.getHref() ) );
                edge.attr( "label", link.getHref() );

                if ( !isAlreadyDefined( edge, edges ) )
                {
                    // if( Link.containsLink( page.getBrokenLinks(), link.getHref(), element.getLocation(), element.getSize()) )
                    // {
                    edge.attr( "color", RED );
                    // }
                    graph.edge( edge );
                    edges.add( edge );
                }
            }
        }

        /*

        Map<Node, Integer> countsPos = new HashMap<Node, Integer>();
        Map<Node, Integer> countsNeg = new HashMap<Node, Integer>();
        for(Edge edge : edges)
        {
            if(!countsPos.containsKey(edge.dst))
            {
                countsPos.put( edge.dst, new Integer(0) );
            }
            countsPos.put(edge.dst, countsPos.get(edge.dst) + 1);

            if(!countsNeg.containsKey(edge.src))
            {
                countsNeg.put( edge.src, new Integer(0) );
            }
            countsPos.put(edge.src, countsPos.get(edge.src) + 1);
        }

        Node nodePos = getMaxNode(countsPos);
        Node nodeNeg = getMaxNode(countsNeg);

        */

        return graph;
    }

    private Node getMaxNode(Map<Node, Integer> countsPos)
    {
        int max = 0;
        Node nodeMaxPos = null;
        boolean first = true;
        for ( Node nodePos : countsPos.keySet() )
        {
            if ( first )
            {
                max = countsPos.get( nodePos );
                nodeMaxPos = nodePos;
                first = false;
                continue;
            }

            if ( max < countsPos.get( nodePos ) )
            {
                max = countsPos.get( nodePos );
                nodeMaxPos = nodePos;
            }
        }
        return nodeMaxPos;
    }

    private boolean isAlreadyDefined(Edge edge, List<Edge> edges)
    {
        String label = edge.attr( "label" );
        for ( Edge iterator : edges )
        {
            String dstLabel = iterator.attr( "label" );
            boolean dstMatch = dstLabel.contentEquals( label );
            if ( dstMatch )
            {
                return true;
            }
        }
        return false;
    }

    private Node createNode(PageBean page) throws Exception
    {
        Node node = new Node();
        String url = page.getUrl();
        String urlString;
        if ( url == null )
            urlString = node.toString();
        else
            urlString = escape( url );

        node.id( urlString );

        node.attr( "label", page.getTitle() );
        node.attr( "fixedsize", "true" );
        node.attr( "height", String.valueOf( "4" ) );
        node.attr( "width", String.valueOf( "3" ) );
        node.attr( "shape", "box" );

        if ( !page.isBroken() )
        {
            setColor( node, BLUE );
            if ( page.isHome() )
            {
                setColor( node, GREEN );
            }
        } else
        {
            setColor( node, RED );
        }
        setImage( node, page.getCaption() );
        setUrl( node, page.getUrl() );
        return node;
    }

    private String escape(String string)
    {
        return "\"" + string + "\"";
    }

    private void setImage(Node node, String caption) throws IOException
    {
        if ( caption == null )
        {
            return;
        }

        // File root = user.getSitemapFolder();
        // String relCaption = FileHelper.getRelativePath( root, caption );

        node.attr( "image", caption );

    }

    private void setUrl(Node node, String url)
    {
        if ( url != null )
        {
            node.attr( "URL", url );
        }
    }

    private void setColor(Node node, String color)
    {
        node.attr("color", color);
    }

    public void setHomeNode(Node node)
    {
        node.attr( "shape", "box" );
        setColor( node, BLUE );
    }

    public String getDomain()
    {
        return user.getDomain();
    }

    public UserBean getUser()
    {
        return user;
    }

    public boolean isEmpty()
    {
        return sitemap.isEmpty();
    }

    public Collection<PageBean> getPagesWithVariations()
    {
        List<PageBean> pages = new ArrayList<PageBean>();
        pages.addAll( getPages() );

        for ( PageBean page : getPages() )
        {
            pages.addAll( page.getVariants() );
        }
        return pages;
    }

    public boolean hasBeenExplored(String url) throws Exception
    {
        PageBean page = findPage( url );

        if ( page != null )
        {
            if ( page.getInstance().hasBeenExplored() )
            {
                return true;
            }
            else
            {
                for ( ElementBean element : page.getClickableContent() )
                {
                    if ( !element.isBroken()
                            && !element.hasBeenClicked()
                            && element.getInstance().isClickable()
                            && BotHelper.rightDomain(getUser().getDomain(), url))
                    {
                        return false;
                    }
                }
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    public PageBean findPage(String url) throws Exception
    {
        PageBean page;

        if ( url.isEmpty() )
        {
            return null;
        }
        else if ( !UrlHelper.containsHash( url ) )
        {
            page = findPageByUrl( url );
        }
        else
        {
            url = UrlHelper.removeHash( url );
            page = findPageByUrl( url );
            if ( page != null )
            {
                page = page.getInstance().findVariantByHash( UrlHelper.getHash( url ) );
            }
        }

        return page;
    }

    public ElementBean getFirstUnexploredElement(String url) throws Exception
    {
        PageBean page = findPage( url );

        if ( page == null )
        {
            return null;
        }
        else
        {
            if ( !page.getInstance().hasBeenExplored() )
            {
                for ( ElementBean element : page.getClickableContent() )
                {
                    if ( !element.isBroken()
                            && !element.hasBeenClicked()
                            && element.getInstance().isClickable() )
                    {
                        return element;
                    }
                }
            }
            return null;
        }
    }

    public void delPage(IPage page)
    {
        if ( page.isOriginal() )
        {
            if ( sitemap.containsKey( page.getUrl() ) )
            {
                sitemap.remove( page.getUrl() );
            }
        }
    }

    public static SiteMap fromJson(File json) throws IOException
    {
        return fromJson( FileUtils.readFileToString( json ) );
    }

    public static SiteMap fromJson(String json) throws IOException
    {
        SiteMap sitemap = (SiteMap) JsonHelper.getInstance().map( SiteMap.class, json );
        UserBean user = sitemap.getUser();

        // Attach user and elements to the page
        for ( PageBean page : sitemap.getPages() )
        {
            page.setUser( user );
            for ( ElementBean element : page.getContent() )
            {
                element.setPage( page );
            }
        }

        // Attach original page reference to each page
        for ( PageBean originalPage : sitemap.getPages() )
        {
            for ( PageBean page : sitemap.getPages() )
            {
                if ( page.getOriginalPageId().contentEquals( originalPage.getId() ) )
                {
                    page.setOriginal( originalPage );
                }
            }
        }

        return sitemap;
    }

    public void setHome(String home)
    {
        this.user.setHome( home );
    }

    public PageBean findPageByInternalId(String pageId)
    {
        for ( PageBean page : getPages() )
        {
            if ( page.getId().contentEquals( pageId ) )
            {
                return page;
            }
        }
        return null;
    }

    public long getDate()
    {
        return date;
    }

    public void setDate(final long date)
    {
        this.date = date;
    }

}
