package com.smash.revolance.ui.model.page.api;

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

import com.smash.revolance.ui.model.diff.DiffType;
import com.smash.revolance.ui.model.diff.ElementDiffType;
import com.smash.revolance.ui.model.diff.PageDiffType;
import com.smash.revolance.ui.model.element.ElementNotFound;
import com.smash.revolance.ui.model.element.api.ElementBean;
import com.smash.revolance.ui.model.user.UserBean;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.io.IOException;
import java.util.*;

/**
 * User: wsmash
 * Date: 21/04/13
 * Time: 09:33
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
                getterVisibility = JsonAutoDetect.Visibility.NONE,
                isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class PageBean
{
    private String id    = "";
    private String title = "";
    private String url   = "";

    @JsonDeserialize(contentAs = ElementBean.class, as = ArrayList.class)
    private List<ElementBean> content = new ArrayList<ElementBean>();

    private String caption = "";

    private int w, h;

    private String originalPageId;

    @JsonIgnore
    private PageBean original;

    @JsonDeserialize(contentAs = PageBean.class, as = ArrayList.class)
    private List<PageBean> variants = new ArrayList<PageBean>();
    private ElementBean source;

    private boolean broken;
    private boolean browsed;
    private boolean homePage;
    private boolean external;
    private boolean authorized = true;

    @JsonIgnore
    private UserBean user;

    @JsonIgnore
    private Page instance;

    private Map<DiffType, List<ElementBean>> variations;
    private String                           scrollY;
    private String                           scrollX;

    public PageBean()
    {

    }

    public PageBean(Page instance)
    {
        this();
        setInstance( instance );
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }

    public void setLinks(ArrayList<ElementBean> links)
    {
        this.content.addAll( links );
        Collections.sort( content );
    }

    public List<ElementBean> getLinks()
    {
        return ElementBean.filterLinks( getContent() );
    }

    public void setButtons(ArrayList<ElementBean> buttons)
    {
        this.content.addAll( buttons );
        Collections.sort( content );
    }

    public List<ElementBean> getButtons()
    {
        return ElementBean.filterButtons( getContent() );
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public void setImages(List<ElementBean> images)
    {
        this.content.addAll( images );
        Collections.sort( content );
    }

    public List<ElementBean> getImages()
    {
        return ElementBean.filterImages( getContent() );
    }

    public String getUrl()
    {
        return url;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getCaption()
    {
        return caption;
    }

    public void setCaption(String caption)
    {
        this.caption = caption;
    }

    public boolean isOriginal()
    {
        return this == original;
    }

    public ElementBean getSource()
    {
        return source;
    }

    public void setSource(ElementBean source)
    {
        this.source = source;
    }

    public PageBean getOriginal()
    {
        return original;
    }

    public void setAuthorized(boolean authorized)
    {
        this.authorized = authorized;
    }

    public boolean isAuthorized()
    {
        return authorized;
    }

    public List<PageBean> getVariants()
    {
        return variants;
    }

    public void setVariants(ArrayList<PageBean> variants)
    {
        this.variants = variants;
    }

    public void setInstance(Page instance)
    {
        this.instance = instance;
    }

    public Page getInstance()
    {
        return instance;
    }

    public void setOriginal(PageBean original)
    {
        this.original = original;
        this.originalPageId = original.getId();
    }

    public String getOriginalPageId()
    {
        return originalPageId;
    }

    public boolean isBroken()
    {
        return broken;
    }

    public boolean hasBeenBrowsed()
    {
        return browsed;
    }

    public void setBrowsed(boolean browsed)
    {
        this.browsed = browsed;
    }

    public boolean isHome()
    {
        return homePage;
    }

    public void setHome(boolean b)
    {
        this.homePage = b;
    }

    public void setUser(UserBean user)
    {
        this.user = user;
    }

    public UserBean getUser()
    {
        return user;
    }

    public void setHeight(int height)
    {
        this.h = height;
    }

    public void setWidth(int width)
    {
        this.w = width;
    }

    public String toJSon() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString( this );
    }

    /**
     * Handling variation of a page with dynamic content poping up vs original content
     *
     * @return
     * @throws Exception
     */
    public boolean hasVariations() throws Exception
    {
        return ( getAddedVariations().size() > 0 || getRemovedVariations().size() > 0 ) && !getCaption().contentEquals( getOriginal().getCaption() );
    }

    /**
     * Handling variation of a page with dynamic content poping up vs original content
     *
     * @return
     * @throws Exception
     */
    public List<ElementBean> getAddedVariations() throws Exception
    {
        return getVariations().get( DiffType.ADDED );
    }

    /**
     * Handling variation of a page with dynamic content poping up vs original content
     *
     * @return
     * @throws Exception
     */
    public List<ElementBean> getRemovedVariations() throws Exception
    {
        return getVariations().get( DiffType.DELETED );
    }

    /**
     * Handling variation of a page with dynamic content poping up vs original content
     *
     * @return
     * @throws Exception
     */
    public List<ElementBean> getBaseVariations() throws Exception
    {
        return getVariations().get( DiffType.BASE );
    }

    public Map<DiffType, List<ElementBean>> getVariations() throws Exception
    {
        if ( variations == null )
        {
            variations = this.getDiff( getOriginal() );
        }
        return variations;
    }

    public ArrayList<ElementBean> getRemovedElements(List<ElementBean> elements) throws Exception
    {
        ArrayList<ElementBean> removedElements = new ArrayList<ElementBean>();
        for ( ElementBean refElement : elements )
        {
            if ( !this.contains( refElement ) )
            {
                removedElements.add( refElement );
            }
        }

        markElements( removedElements, DiffType.DELETED );
        markElements( removedElements, ElementDiffType.values() );

        filterElementsIncludedInEachOthers( removedElements );

        Collections.sort( removedElements );
        return removedElements;
    }

    public static void markElements(List<ElementBean> elements, ElementDiffType... diff) throws IOException
    {
        for ( ElementBean element : elements )
        {
            markElement( element, diff );
        }
    }

    public static void markElements(List<ElementBean> elements, DiffType type)
    {
        for ( ElementBean element : elements )
        {
            // element.setDiffType( type );
        }
    }

    public ArrayList<ElementBean> getAddedElements(List<ElementBean> elements) throws Exception
    {
        ArrayList<ElementBean> addedElements = new ArrayList<ElementBean>();
        for ( ElementBean element : getContent() )
        {
            boolean added = true;
            for ( ElementBean elem : elements )
            {
                if ( elem.equals( element ) )
                {
                    added = false;
                    break;
                }
            }
            if ( added )
            {
                addedElements.add( element );
            }
        }


        filterElementsIncludedInEachOthers( addedElements );
        markElements( addedElements, DiffType.ADDED );
        markElements( addedElements, ElementDiffType.values() );

        Collections.sort( addedElements );
        return addedElements;
    }

    public ArrayList<ElementBean> getBaseElements(List<ElementBean> elements) throws Exception
    {
        ArrayList<ElementBean> baseElements = new ArrayList<ElementBean>();

        for ( ElementBean refElement : elements )
        {
            if ( this.contains( refElement ) )
            {
                baseElements.add( refElement );
            }
        }

        filterElementsIncludedInEachOthers( baseElements );
        markElements( baseElements, DiffType.BASE );

        Collections.sort( baseElements );
        return baseElements;
    }

    public boolean contains(ElementBean element) throws Exception
    {
        for ( ElementBean refElement : getContent() )
        {
            if ( refElement.equals( element ) )
            {
                return true;
            }
        }
        return false;
    }

    public Map<DiffType, List<ElementBean>> getDiff(List<ElementBean> elements) throws Exception
    {
        Map<DiffType, List<ElementBean>> differencies = new HashMap<DiffType, List<ElementBean>>();

        ArrayList<ElementBean> baseElements = getBaseElements( elements );
        ArrayList<ElementBean> removedElements = getRemovedElements( elements );
        ArrayList<ElementBean> addedElements = getAddedElements( elements );

        ArrayList<ElementBean> updatedElements = updatedElements( addedElements, removedElements );

        baseElements.addAll( updatedElements );
        addedElements.removeAll( updatedElements );
        removedElements.removeAll( updatedElements );

        filterElementsIncludedInEachOthers( baseElements );
        Collections.sort( baseElements );
        differencies.put( DiffType.BASE, baseElements );

        filterElementsIncludedInEachOthers( removedElements );
        Collections.sort( removedElements );
        differencies.put( DiffType.DELETED, removedElements );

        filterElementsIncludedInEachOthers( addedElements );
        Collections.sort( addedElements );
        differencies.put( DiffType.ADDED, addedElements );

        return differencies;
    }

    private ArrayList<ElementBean> updatedElements(List<ElementBean> addedElements, List<ElementBean> removedElements)
    {
        ArrayList<ElementBean> baseElements = new ArrayList<ElementBean>();

        int removedIdx = 0;
        int addedIdx = 0;
        boolean equalityFound = false;
        for ( ElementBean element : addedElements )
        {
            for ( ElementBean refElement : removedElements )
            {
                if ( refElement.equalsByAll( element, ElementDiffType.values() ) )
                {
                    equalityFound = true;
                    break;
                }
                removedIdx++;
            }
            if ( equalityFound )
            {
                break;
            }
            addedIdx++;
        }

        if ( equalityFound )
        {
            ElementBean addedElement = addedElements.remove( addedIdx );
            ElementBean removedElement = removedElements.remove( removedIdx );

            List<ElementDiffType> diff = addedElement.getDiff( removedElement );
            markElement( addedElement, diff.toArray( new ElementDiffType[diff.size()] ) );
            baseElements.add( addedElement );

            // Check for other elements remaining to be matched
            baseElements.addAll( updatedElements( addedElements, removedElements ) );
        }

        return baseElements;
    }

    public static void markElement(ElementBean element, ElementDiffType... diffTypes)
    {
        // element.setElementDiffTypes( Arrays.asList( diffTypes ) );
    }

    public Map<DiffType, List<ElementBean>> getDiff(PageBean page) throws Exception
    {
        if ( page == null || this == page )
        {
            return new HashMap<DiffType, List<ElementBean>>();
        } else
        {
            return getDiff( getContent() );
        }
    }

    private void filterElementsIncludedInEachOthers(List<ElementBean> elements)
    {
        if ( elements.size() > 2 )
        {
            List<ElementBean> filteredElements = new ArrayList<ElementBean>();
            ElementBean bigElement;
            while ( ( bigElement = getBiggestArea( elements ) ) != null )
            {
                elements.remove( bigElement );
                List<ElementBean> included = new ArrayList<ElementBean>();
                for ( ElementBean anElement : elements )
                {
                    if ( anElement.isIncluded( bigElement ) )
                    {
                        included.add( anElement );
                    }
                }

                elements.removeAll( included );
                filteredElements.add( bigElement );
            }

            elements.clear();
            elements.addAll( filteredElements );
        }
    }

    private ElementBean getBiggestArea(List<ElementBean> elements)
    {
        if ( elements == null )
        {
            return null;
        }
        if ( elements.isEmpty() )
        {
            return null;
        }
        int maxArea = 0;
        ElementBean pageElement = null;
        for ( ElementBean element : elements )
        {
            if ( element.getArea() > maxArea )
            {
                pageElement = element;
                maxArea = element.getArea();
            }
        }
        return pageElement;
    }

    public List<ElementBean> getContent()
    {
        return content;
    }

    public List<ElementBean> getOriginalContent()
    {
        return getOriginal().getContent();
    }

    public void setUserBean(UserBean user)
    {
        this.user = user;
    }

    public boolean equals(PageBean page) throws IOException
    {
        return equalsBy( page, PageDiffType.values() );
    }

    public boolean equalsBy(PageBean page, PageDiffType... diffTypes)
    {
        boolean b = true;
        for ( PageDiffType diffType : diffTypes )
        {
            switch ( diffType )
            {
                case LAYOUT:
                    b = b && equalsByLayout( page );
                    break;

                case LOOK:
                    b = b && equalsByLook( page );
                    break;

                case URL:
                    b = b && equalsByUrl( page );
                    break;

                case CONTENT:
                    b = b && equalsByContent( page );
                    break;
            }
        }
        return b;
    }

    public boolean equalsByContent(PageBean page)
    {
        return contentEquals( page.getContentAsValue(), getContentAsValue(), false );
    }

    private boolean contentEquals(List<String> content, List<String> refContent, boolean ordered)
    {
        if ( content.size() != refContent.size() )
        {
            return false;
        } else
        {
            if ( ordered )
            {
                int refContentIdx = 0;
                for ( String value : refContent )
                {
                    if ( !content.get( refContentIdx ).contentEquals( value ) )
                    {
                        return false;
                    }
                    refContentIdx++;
                }
                return true;
            } else
            {
                for ( String value : refContent )
                {
                    if ( !content.contains( value ) )
                    {
                        return false;
                    }
                }
                return true;
            }
        }
    }

    public boolean equalsByUrl(PageBean page)
    {
        return getRelativeUrl().contentEquals( page.getRelativeUrl() );
    }

    public boolean equalsByLook(PageBean page)
    {
        return caption.contentEquals( page.getCaption() );
    }

    public List<String> getContentAsValue()
    {
        Collections.sort( getContent() );

        List<String> contentValue = new ArrayList<String>();

        for ( ElementBean element : getContent() )
        {
            String value = element.getContent();
            if ( !contentValue.contains( value ) )
            {
                contentValue.add( value );
            }
        }

        return contentValue;
    }

    public String getRelativeUrl()
    {
        return StringUtils.remove( getUrl(), getUser().getDomain() );
    }

    public boolean equalsByLayout(PageBean page)
    {
        return contentEquals( getContentAsValue(), page.getContentAsValue(), true );
    }

    public List<ElementBean> getBrokenLinks()
    {
        List<ElementBean> brokenLinks = new ArrayList<ElementBean>();

        for ( ElementBean link : getLinks() )
        {
            if ( link.isBroken() )
            {
                brokenLinks.add( link );
            }
        }
        return brokenLinks;
    }

    public boolean hasBrokenLinks()
    {
        return !getBrokenLinks().isEmpty();
    }

    public void clearContent()
    {
        content.clear();
    }

    public void setContent(List<ElementBean> content)
    {
        this.content = content;
    }

    public ElementBean contains(String txt, String impl, boolean active) throws ElementNotFound
    {
        for ( ElementBean element : content )
        {
            if ( element.getValue().contentEquals( txt ) && element.getImpl().contentEquals( impl ) && element.isDisabled() != active )
            {
                return element;
            }
        }
        throw new ElementNotFound( "Unable to find element in page." );
    }

    public void setBroken(boolean broken)
    {
        this.broken = broken;
    }

    public boolean isExternal()
    {
        return external;
    }

    public void setExternal(boolean external)
    {
        this.external = external;
    }

    public int getWidth()
    {
        return w;
    }

    public int getHeight()
    {
        return h;
    }

    public List<ElementBean> getClickableContent()
    {
        List<ElementBean> clickables = new ArrayList<ElementBean>();

        String impl = "";
        for ( ElementBean element : getContent() )
        {
            impl = element.getImpl();
            if ( impl.contentEquals( "Link" )
                    || impl.contains( "Button" ) )
            {
                clickables.add( element );
            }
        }

        return clickables;
    }

    public void setScrollY(String scrollY)
    {
        this.scrollY = scrollY;
    }

    public String getScrollY()
    {
        return scrollY;
    }

    public void setScrollX(String scrollX)
    {
        this.scrollX = scrollX;
    }

    public String getScrollX()
    {
        return scrollX;
    }

    public boolean hasVariants()
    {
        return variants.isEmpty();
    }
}
